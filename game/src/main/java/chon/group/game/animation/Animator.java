package chon.group.game.animation;

import chon.group.game.core.agent.Agent;
import chon.group.game.core.agent.EntityStatus;
import chon.group.game.core.environment.Level;

public class Animator {

    /**
     * Updates animations for all agents in the level.
     */
    public void update(Level level, long deltaTime) {

    }

    /**
     * Updates one specific agent.
     */
    public void update(Agent agent, long deltaTime) {
        if (agent.getStatus().equals(EntityStatus.IDLE)) {
            agent.getAnimationState().setCurrentAnimation(agent.getAnimationSet().get(AnimationType.IDLE));
            agent.getAnimationState().setCurrentFrameIndex(this.nextFrame(agent));
        }
    }

    /**
     * Gets the next current frame.
     */
    public int nextFrame(Agent agent) {
        int index = agent.getAnimationState().getCurrentFrameIndex();
        int size = agent.getAnimationState().getCurrentAnimation().getFrames().size();
        return (index + 1) % size;
    }

}
