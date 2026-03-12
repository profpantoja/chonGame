package chon.group.game.animation;

import chon.group.game.core.agent.Entity;
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
    public void update(Entity entity) {
        if (entity.getStatus().equals(EntityStatus.IDLE)) {
            entity.getAnimationState().setCurrentAnimation(entity.getAnimationSet().get(AnimationType.IDLE));
        } else {
            entity.getAnimationState().setCurrentAnimation(entity.getAnimationSet().get(AnimationType.IDLE));
        }
        if (entity.getStatus().equals(EntityStatus.WALK)) {
            Animation animation = entity.getAnimationSet().get(AnimationType.WALK);
            if (!animation.equals(null)) {
                entity.getAnimationState().setCurrentFrameIndex(0);
                entity.getAnimationState().setCurrentAnimation(animation);
            }
        }
        if (entity.getAnimationState().tick() >= entity.getAnimationState().getCurrentAnimation().getDuration()) {
            int frameIndex = entity.getAnimationState().nextFrame();
            entity.getAnimationState().setCurrentFrameIndex(frameIndex);
        }
    }
}
