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
    public void update(Agent agent, long elapsedTime) {
        if (agent.getStatus().equals(EntityStatus.IDLE)) {
            agent.getAnimationState().setCurrentAnimation(agent.getAnimationSet().get(AnimationType.IDLE));
            if (agent.getAnimationState().tick() >= agent.getAnimationState().getCurrentAnimation().getDuration()) {
                int frameIndex = agent.getAnimationState().nextFrame();
                agent.getAnimationState().setCurrentFrameIndex(frameIndex);
                agent.setImage(agent.getAnimationState().getCurrentAnimation().getFrames().get(frameIndex));
            }
        }
    }

}
