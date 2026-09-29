package chon.group.game.gateway;

import java.util.ArrayList;
import java.util.List;

import chon.group.game.core.agent.Agent;
import chon.group.game.core.agent.Direction;
import chon.group.game.core.environment.Environment;
import chon.group.game.core.environment.Level;
import chon.group.game.core.weapon.Shot;
import chon.group.game.joystick.GameCommand;
import chon.group.game.joystick.client.ExternalJoystick;
import chon.group.game.sound.Sound;
import chon.group.game.sound.SoundEvent;

/**
 * Binds one external client connection to exactly one controllable agent.
 *
 * <p>
 * Slot {@code 0} always maps to the protagonist, whose movement keeps being
 * driven by the game's normal input pipeline. Bot controllers keep the stable
 * ID of the nearest agent selected when the client connects.
 * </p>
 */
public class ExternalAgentController {

    private final int slot;
    private final ExternalJoystick joystick;
    private volatile String assignedAgentId;
    private final GameActionQueue actionQueue = new GameActionQueue();
    private volatile boolean closed;
    private Agent boundAgent;
    private boolean bindingResolved;
    private boolean gameOverNotified;

    public ExternalAgentController(int slot, ExternalJoystick joystick, String assignedAgentId) {
        this.slot = slot;
        this.joystick = joystick;
        this.assignedAgentId = assignedAgentId;
    }

    public int getSlot() {
        return slot;
    }

    public boolean isProtagonist() {
        return slot == 0;
    }

    public ExternalJoystick getJoystick() {
        return joystick;
    }

    public String getAssignedAgentId() {
        return assignedAgentId;
    }

    public void assignAgent(String assignedAgentId) {
        if (!bindingResolved && this.assignedAgentId == null) {
            this.assignedAgentId = assignedAgentId;
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
     * Applies the current joystick state to the bound agent. Bots only; the
     * protagonist keeps being handled by the normal game/joystick pipeline.
     * Must only be called from the game thread.
     */
    public boolean update(Environment environment) {
        if (isProtagonist()) {
            if (environment.getProtagonist() != null
                    && environment.getProtagonist().isDead()) {
                if (!gameOverNotified) {
                    gameOverNotified = true;
                    return true;
                }
                return false;
            }
            gameOverNotified = false;
            return false;
        }

        if (bindingResolved && boundAgent == null) {
            return false;
        }

        Level level = environment.getCurrentLevel();
        if (!bindingResolved) {
            boundAgent = level == null ? null : level.getAgents().stream()
                .filter(agent -> agent.getId().equals(assignedAgentId))
                .findFirst()
                .orElse(null);
            if (boundAgent == null) {
                return false;
            }
            bindingResolved = true;
        }

        if (boundAgent.isDead()) {
            closed = true;
            return true;
        }

        boundAgent.setExternallyControlled(true);
        applyMovement(boundAgent);
        clampToLevelBounds(boundAgent, level);
        applyAttack(boundAgent, level, environment);
        joystick.endFrame();
        return false;
    }

    /** Releases the bound agent back to AI control. Must only be called from the game thread. */
    public void release() {
        if (boundAgent != null) {
            boundAgent.setExternallyControlled(false);
            boundAgent = null;
        }
        bindingResolved = true;
    }

    private void applyMovement(Agent agent) {
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

    private void applyAttack(Agent agent, Level level, Environment environment) {
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
}
