package chon.group.game.animation;

import chon.group.game.core.agent.Entity;
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
        if (!entity.getAnimationState().isBlocked())
            switch (entity.getStatus()) {
                /*
                 * It verifies if the entity's status is IDLE. If so, it gets the correct
                 * Animation Set.
                 */
                case IDLE:
                    entity.getAnimationState().setCurrentAnimation(entity.getAnimationSet().get(AnimationType.IDLE));
                    break;
                /*
                 * It verifies if the entity's status is WALKING. If so, it gets the correct
                 * Animation Set.
                 */
                case WALK:
                    Animation animation = entity.getAnimationSet().get(AnimationType.WALK);
                    if (animation != null) {
                        entity.getAnimationState().setCurrentAnimation(animation);
                    } else {
                        entity.getAnimationState()
                                .setCurrentAnimation(entity.getAnimationSet().get(AnimationType.IDLE));
                    }
                    break;
                /*
                 * It verifies if the entity's status is TERMINATE. If so, it gets the correct
                 * Animation Set.
                 */
                case TERMINATE:
                    animation = entity.getAnimationSet().get(AnimationType.TERMINATE);
                    if (animation != null)
                        entity.getAnimationState().setCurrentAnimation(animation);
                    break;
                case ATTACK:
                    animation = entity.getAnimationSet().get(AnimationType.ATTACK);
                    if (animation != null) {
                        entity.getAnimationState().setCurrentAnimation(animation);
                    }
                    break;
                case DAMAGE:
                    animation = entity.getAnimationSet().get(AnimationType.DAMAGE);
                    if (animation != null) {
                        entity.getAnimationState().setCurrentAnimation(animation);
                    }
                    break;
                default:
                    entity.getAnimationState().setCurrentAnimation(entity.getAnimationSet().get(AnimationType.IDLE));
                    break;
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
