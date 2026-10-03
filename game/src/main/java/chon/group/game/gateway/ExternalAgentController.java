package chon.group.game.gateway;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import chon.group.game.core.agent.Agent;
import chon.group.game.core.agent.AgentControlOwner;
import chon.group.game.core.agent.Direction;
import chon.group.game.core.agent.Team;
import chon.group.game.core.agent.TeamType;
import chon.group.game.core.environment.Environment;
import chon.group.game.core.environment.Level;
import chon.group.game.core.weapon.Shot;
import chon.group.game.joystick.GameCommand;
import chon.group.game.joystick.client.ExternalJoystick;
import chon.group.game.sound.Sound;
import chon.group.game.sound.SoundEvent;

/**
 * Binds one external MAS client connection to one controllable team.
 *
 * <p>
 * Actions identify the concrete team member they affect through its stable
 * runtime entity ID.
 * </p>
 */
public class ExternalAgentController {

    private final int slot;
    private final Map<String, ExternalJoystick> joysticks = new HashMap<>();
    private final Map<String, Agent> boundAgents = new LinkedHashMap<>();
    private final Map<String, Agent> knownTeamAgents = new LinkedHashMap<>();
    private final Map<String, String> slotAssignments = new LinkedHashMap<>();
    private final List<ControllerEvent> pendingEvents = new ArrayList<>();
    private final GameActionQueue actionQueue = new GameActionQueue();
    private volatile List<String> requestedSlots = List.of();
    private volatile boolean closed;
    private boolean waitingNotified;
    private volatile String teamId;
    private volatile String requestedTeamId;
    private Level assignedLevel;
    private boolean assignmentsInitialized;

    public ExternalAgentController(int slot) {
        this.slot = slot;
    }

    public int getSlot() {
        return slot;
    }

    public String getTeamId() {
        return teamId;
    }

    public boolean isAssigned() {
        return teamId != null;
    }

    public String getRequestedTeamId() {
        return requestedTeamId;
    }

    public synchronized void requestTeam(String teamId, List<String> slotIds) {
        if (this.teamId == null && this.requestedTeamId == null && teamId != null && !teamId.isBlank()) {
            this.requestedTeamId = teamId;
            this.requestedSlots = slotIds == null
                    ? List.of()
                    : slotIds.stream().filter(slot -> slot != null && !slot.isBlank()).distinct().toList();
        }
    }

    public List<String> getRequestedSlots() {
        return requestedSlots;
    }

    public boolean hasExplicitSlots() {
        return !requestedSlots.isEmpty();
    }

    public synchronized boolean markWaitingNotified() {
        if (waitingNotified) {
            return false;
        }
        waitingNotified = true;
        return true;
    }

    public void assignTeam(String teamId) {
        if (this.teamId == null) {
            this.teamId = teamId;
        }
    }

    public GameActionQueue getActionQueue() {
        return actionQueue;
    }

    public void close() {
        closed = true;
    }

    public boolean isClosed() {
        return closed;
    }

    /**
    * Applies the current joystick state to every member addressed by this
    * controller's queued MAS actions.
     * Must only be called from the game thread.
     */
    public boolean update(Environment environment, boolean hybridMode, boolean playableState) {
        if (!isAssigned()) {
            return false;
        }
        Agent protagonist = environment.getProtagonist();
        if (hybridMode && protagonist != null) {
            protagonist.setControlOwner(AgentControlOwner.LOCAL);
        }
        List<Agent> roster = collectTeamMembers(environment, hybridMode);
        if (!assignmentsInitialized || assignedLevel != environment.getCurrentLevel()) {
            initializeAssignments(roster, environment.getCurrentLevel());
        } else {
            reassignDeadMembers(roster, protagonist, hybridMode);
        }
        bindAssignedMembers(roster, protagonist, hybridMode);
        if (playableState) {
            applyQueuedActions();
        } else {
            actionQueue.clear();
        }
        Level level = environment.getCurrentLevel();
        for (Agent agent : boundAgents.values()) {
            if (agent.isDead()) {
                continue;
            }
            agent.setControlOwner(AgentControlOwner.MAS);
            if (!playableState) {
                continue;
            }
            ExternalJoystick joystick = joysticks.get(agent.getId());
            applyMovement(agent, joystick);
            clampToLevelBounds(agent, level);
            applyAttack(agent, level, environment, joystick);
            joystick.endFrame();
        }
        return false;
    }

    /** Releases controlled members according to team policy; call only from the game thread. */
    public void release() {
        for (Agent agent : knownTeamAgents.values()) {
            if (agent.getControlOwner() == AgentControlOwner.LOCAL) {
                continue;
            }
            AgentControlOwner fallback = agent.getTeam() != null
                    && agent.getTeam().getType() == TeamType.ALLY
                            ? AgentControlOwner.DORMANT : AgentControlOwner.AI;
            agent.setControlOwner(fallback);
        }
        boundAgents.clear();
        joysticks.clear();
        knownTeamAgents.clear();
        slotAssignments.clear();
        actionQueue.clear();
    }

    private List<Agent> collectTeamMembers(Environment environment, boolean hybridMode) {
        Map<String, Agent> members = new LinkedHashMap<>();
        Agent protagonist = environment.getProtagonist();
        if (!hybridMode) {
            addIfTeamMember(members, protagonist);
        }
        Level level = environment.getCurrentLevel();
        if (level != null) {
            level.getAgents().forEach(agent -> {
                if (!hybridMode || agent != protagonist) {
                    addIfTeamMember(members, agent);
                }
            });
        }
        return new ArrayList<>(members.values());
    }

    private void initializeAssignments(List<Agent> roster, Level level) {
        slotAssignments.clear();
        knownTeamAgents.clear();
        if (roster.stream().noneMatch(agent -> !agent.isDead())) {
            assignmentsInitialized = false;
            assignedLevel = level;
            return;
        }
        List<String> slots = requestedSlots;
        for (String slotId : slots) {
            Agent member = findFreeMember(roster);
            if (member == null) {
                slotAssignments.put(slotId, null);
                pendingEvents.add(new ControllerEvent("agent_dormant", slotId, null, null,
                        "NO_FREE_AGENT"));
            } else {
                slotAssignments.put(slotId, member.getId());
                pendingEvents.add(new ControllerEvent("agent_assigned", slotId, null,
                        member.getId(), null));
            }
        }
        assignmentsInitialized = true;
        assignedLevel = level;
    }

    private void reassignDeadMembers(List<Agent> roster, Agent protagonist, boolean hybridMode) {
        Map<String, Agent> membersById = new LinkedHashMap<>();
        roster.forEach(agent -> membersById.put(agent.getId(), agent));
        for (Map.Entry<String, String> assignment : new ArrayList<>(slotAssignments.entrySet())) {
            String slotId = assignment.getKey();
            String currentAgentId = assignment.getValue();
            Agent currentAgent = currentAgentId == null ? null : membersById.get(currentAgentId);
            boolean reclaimedByLocalPlayer = hybridMode && protagonist != null
                    && protagonist.getId().equals(currentAgentId);
            if (currentAgentId != null && !reclaimedByLocalPlayer
                    && currentAgent != null && !currentAgent.isDead()) {
                continue;
            }
            if (currentAgentId != null) {
                pendingEvents.add(new ControllerEvent(
                        reclaimedByLocalPlayer ? "agent_released" : "agent_dead",
                        slotId,
                        currentAgentId,
                        null,
                        reclaimedByLocalPlayer ? "LOCAL_PLAYER_CONTROL" : null));
            }
            slotAssignments.put(slotId, null);
            Agent replacement = findFreeMember(roster);
            if (replacement == null) {
                if (currentAgentId != null) {
                    pendingEvents.add(new ControllerEvent("agent_dormant", slotId,
                            currentAgentId, null, "NO_FREE_AGENT"));
                }
            } else {
                slotAssignments.put(slotId, replacement.getId());
                pendingEvents.add(new ControllerEvent("agent_reassigned", slotId,
                        currentAgentId, replacement.getId(), null));
            }
        }
    }

    private Agent findFreeMember(List<Agent> roster) {
        for (Agent member : roster) {
            if (!member.isDead() && !slotAssignments.containsValue(member.getId())) {
                return member;
            }
        }
        return null;
    }

    private void bindAssignedMembers(List<Agent> roster, Agent protagonist, boolean hybridMode) {
        Map<String, Agent> rosterById = new LinkedHashMap<>();
        roster.forEach(agent -> {
            rosterById.put(agent.getId(), agent);
            knownTeamAgents.put(agent.getId(), agent);
        });
        Map<String, Agent> members = new LinkedHashMap<>();
        for (String agentId : slotAssignments.values()) {
            Agent agent = agentId == null ? null : rosterById.get(agentId);
            if (agent != null && !agent.isDead()) {
                members.put(agentId, agent);
            }
        }
        for (Map.Entry<String, Agent> previous : boundAgents.entrySet()) {
            if (!members.containsKey(previous.getKey())) {
                Agent agent = previous.getValue();
                if (hybridMode && agent == protagonist) {
                    agent.setControlOwner(AgentControlOwner.LOCAL);
                } else if (!agent.isDead()) {
                    agent.setControlOwner(AgentControlOwner.AI);
                }
            }
        }
        boundAgents.clear();
        boundAgents.putAll(members);
        for (Agent agent : roster) {
            if (hybridMode && agent == protagonist) {
                agent.setControlOwner(AgentControlOwner.LOCAL);
            } else if (!members.containsKey(agent.getId())) {
                agent.setControlOwner(AgentControlOwner.AI);
            }
        }
        members.values().forEach(agent -> agent.setControlOwner(AgentControlOwner.MAS));
        members.keySet().forEach(id -> joysticks.computeIfAbsent(id, ignored -> new ExternalJoystick()));
        joysticks.keySet().retainAll(members.keySet());
    }

    private void addIfTeamMember(Map<String, Agent> members, Agent agent) {
        if (agent != null && agent.getTeam() != null && teamId.equals(agent.getTeam().getId())) {
            members.put(agent.getId(), agent);
        }
    }

    public void discardPendingActions() {
        actionQueue.clear();
    }

    public List<ControllerEvent> drainEvents() {
        List<ControllerEvent> events = List.copyOf(pendingEvents);
        pendingEvents.clear();
        return events;
    }

    private void applyQueuedActions() {
        GameAction action;
        while ((action = actionQueue.poll()) != null) {
            String slotId = action.slotId();
            String assignedAgentId;
            if (hasExplicitSlots()) {
                assignedAgentId = slotAssignments.get(slotId);
                if (assignedAgentId == null || !assignedAgentId.equals(action.agentId())) {
                    continue;
                }
            } else {
                String currentAgentId = action.agentId();
                assignedAgentId = currentAgentId;
                Agent candidate = knownTeamAgents.get(currentAgentId);
                if (candidate == null || candidate.isDead()
                        || candidate.getControlOwner() == AgentControlOwner.LOCAL) {
                    continue;
                }
                slotId = slotAssignments.entrySet().stream()
                    .filter(entry -> currentAgentId.equals(entry.getValue()))
                        .map(Map.Entry::getKey)
                        .findFirst().orElse(null);
                if (slotId == null) {
                    if (slotAssignments.containsValue(assignedAgentId)) {
                        continue;
                    }
                    slotId = assignedAgentId;
                    slotAssignments.put(slotId, assignedAgentId);
                    pendingEvents.add(new ControllerEvent("agent_assigned", slotId, null,
                            assignedAgentId, null));
                    boundAgents.put(assignedAgentId, candidate);
                    joysticks.computeIfAbsent(assignedAgentId, ignored -> new ExternalJoystick());
                    candidate.setControlOwner(AgentControlOwner.MAS);
                }
            }
            Agent agent = boundAgents.get(assignedAgentId);
            if (agent == null || agent.getTeam() == null) {
                continue;
            }
            ExternalJoystick joystick = joysticks.get(agent.getId());
            applyAction(agent.getTeam(), joystick, action);
        }
    }

    private void applyAction(Team team, ExternalJoystick joystick, GameAction action) {
        String actionName = action.name().toUpperCase();
        if ("MOVE".equals(actionName)) {
            applyMovementCommand(team, joystick, action.direction());
        } else if ("ATTACK".equals(actionName) && team.allows(GameCommand.ATTACK)) {
            joystick.press(GameCommand.ATTACK);
        }
    }

    private void applyMovementCommand(Team team, ExternalJoystick joystick, String direction) {
        if (direction == null) {
            return;
        }
        try {
            GameCommand command = GameCommand.valueOf(direction.toUpperCase());
            if (!team.allows(command)) {
                return;
            }
            joystick.hold(command);
            releaseOtherDirections(joystick, command);
        } catch (IllegalArgumentException exception) {
            // Invalid actions are ignored by the external joystick.
        }
    }

    private void applyMovement(Agent agent, ExternalJoystick joystick) {
        List<Direction> directions = new ArrayList<>();
        if (joystick.isHeld(GameCommand.RIGHT)) {
            directions.add(Direction.RIGHT);
        }
        if (joystick.isHeld(GameCommand.LEFT)) {
            directions.add(Direction.LEFT);
        }
        if (joystick.isHeld(GameCommand.DOWN)) {
            directions.add(Direction.DOWN);
        }
        if (joystick.isHeld(GameCommand.UP)) {
            directions.add(Direction.UP);
        }

        if (directions.isEmpty()) {
            agent.idle();
        } else {
            agent.move(directions);
        }
    }

    private void clampToLevelBounds(Agent agent, Level level) {
        if (level == null) {
            return;
        }

        if (agent.getPosX() < 0) {
            agent.setPosX(0);
        }
        if (agent.getPosX() + agent.getWidth() > level.getWidth()) {
            agent.setPosX(level.getWidth() - agent.getWidth());
        }
        if (agent.getPosY() < level.getTopY()) {
            agent.setPosY(level.getTopY());
        }
        if (agent.getPosY() + agent.getHeight() > level.getBottomY()) {
            agent.setPosY(level.getBottomY() - agent.getHeight());
        }
    }

    private void applyAttack(Agent agent, Level level, Environment environment, ExternalJoystick joystick) {
        if (level == null || !joystick.inPress(GameCommand.ATTACK)) {
            return;
        }
        Shot shot = agent.useWeapon();
        if (shot == null) {
            return;
        }
        level.getShots().add(shot);
        Sound sound = agent.getSoundSet().get(SoundEvent.ATTACK);
        if (sound != null) {
            environment.getSounds().add(sound);
        }
    }

        public record ControllerEvent(
            String type,
            String slotId,
            String previousAgentId,
            String agentId,
            String reason) {
        }

        private void releaseOtherDirections(ExternalJoystick joystick, GameCommand activeCommand) {
        for (GameCommand direction : new GameCommand[] {
                GameCommand.UP, GameCommand.DOWN, GameCommand.LEFT, GameCommand.RIGHT }) {
            if (direction != activeCommand) {
                joystick.release(direction);
            }
        }
    }
}
