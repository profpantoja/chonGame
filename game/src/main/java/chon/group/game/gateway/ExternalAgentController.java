package chon.group.game.gateway;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import chon.group.game.core.agent.Agent;
import chon.group.game.core.agent.Direction;
import chon.group.game.core.agent.Team;
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
    private final Map<String, Agent> boundAgents = new HashMap<>();
    private final GameActionQueue actionQueue = new GameActionQueue();
    private volatile boolean closed;
    private volatile String teamId;
    private volatile String requestedTeamId;

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

    public void requestTeam(String teamId) {
        if (this.teamId == null && this.requestedTeamId == null && teamId != null && !teamId.isBlank()) {
            this.requestedTeamId = teamId;
        }
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
    public boolean update(Environment environment) {
        if (!isAssigned()) {
            return false;
        }
        bindTeamMembers(environment);
        applyQueuedActions();
        Level level = environment.getCurrentLevel();
        for (Agent agent : boundAgents.values()) {
            if (agent.isDead()) {
                continue;
            }
            agent.setExternallyControlled(true);
            ExternalJoystick joystick = joysticks.get(agent.getId());
            applyMovement(agent, joystick);
            clampToLevelBounds(agent, level);
            applyAttack(agent, level, environment, joystick);
            joystick.endFrame();
        }
        return false;
    }

    /** Releases controlled team members back to AI control. Must only be called from the game thread. */
    public void release() {
        for (Agent agent : boundAgents.values()) {
            agent.setExternallyControlled(false);
        }
        boundAgents.clear();
        joysticks.clear();
    }

    private void bindTeamMembers(Environment environment) {
        Map<String, Agent> members = new HashMap<>();
        addIfTeamMember(members, environment.getProtagonist());
        Level level = environment.getCurrentLevel();
        if (level != null) {
            level.getAgents().forEach(agent -> addIfTeamMember(members, agent));
        }
        boundAgents.clear();
        boundAgents.putAll(members);
        members.keySet().forEach(id -> joysticks.computeIfAbsent(id, ignored -> new ExternalJoystick()));
        joysticks.keySet().retainAll(members.keySet());
    }

    private void addIfTeamMember(Map<String, Agent> members, Agent agent) {
        if (agent != null && agent.getTeam() != null && teamId.equals(agent.getTeam().getId())) {
            members.put(agent.getId(), agent);
        }
    }

    private void applyQueuedActions() {
        GameAction action;
        while ((action = actionQueue.poll()) != null) {
            Agent agent = boundAgents.get(action.agentId());
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

    private void releaseOtherDirections(ExternalJoystick joystick, GameCommand activeCommand) {
        for (GameCommand direction : new GameCommand[] {
                GameCommand.UP, GameCommand.DOWN, GameCommand.LEFT, GameCommand.RIGHT }) {
            if (direction != activeCommand) {
                joystick.release(direction);
            }
        }
    }
}
