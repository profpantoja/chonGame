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
        /*
         * It verifies if the entity's status is IDLE. If so, it gets the correct
         * Animation Set.
         */
        if (entity.getStatus().equals(EntityStatus.IDLE)) {
            entity.getAnimationState().setCurrentAnimation(entity.getAnimationSet().get(AnimationType.IDLE));
        }
        /*
         * It verifies if the entity's status is WALKING. If so, it gets the correct
         * Animation Set.
         */
        if (entity.getStatus().equals(EntityStatus.WALK)) {
            Animation animation = entity.getAnimationSet().get(AnimationType.WALK);
            if (animation != null) {
                entity.getAnimationState().setCurrentFrameIndex(0);
                entity.getAnimationState().setCurrentAnimation(animation);
            } else {
                /* This is here because there are Status not implemented yet. */
                entity.getAnimationState().setCurrentAnimation(entity.getAnimationSet().get(AnimationType.IDLE));
            }
        }
        /*
         * It verifies if the entity's status is TERMINATE. If so, it gets the correct
         * Animation Set.
         */
        if (entity.getStatus().equals(EntityStatus.TERMINATE)) {
            Animation animation = entity.getAnimationSet().get(AnimationType.TERMINATE);
            if (animation != null) {
                //entity.getAnimationState().setCurrentFrameIndex(0);
                entity.getAnimationState().setCurrentAnimation(animation);
            } else {
                /* This is here because there are Status not implemented yet. */
                entity.getAnimationState().setCurrentAnimation(entity.getAnimationSet().get(AnimationType.IDLE));
            }
        }
        /*
         * It gets the elapsed time and compares with the frame duration of the current
         * animation.
         */
        if (entity.getAnimationState().tick() >= entity.getAnimationState().getCurrentAnimation().getDuration()) {
            int frameIndex = entity.getAnimationState().nextFrame();
            entity.getAnimationState().setCurrentFrameIndex(frameIndex);
        }
    }
}
