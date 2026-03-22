package chon.group.game.animation;

import chon.group.game.core.agent.Entity;

public class Animator {

    /**
     * Updates one specific agent.
     */
    public void update(Entity entity) {
        if (!entity.getAnimationState().isBlocked())
            entity.getAnimationState().setCurrentAnimation(this.selectAnimation(entity));
        /*
         * It gets the elapsed time and compares with the frame duration of the current
         * animation.
         */
        if (entity.getAnimationState().tick() >= entity.getAnimationState().getCurrentAnimation().getDuration()) {
            int frameIndex = entity.getAnimationState().nextFrame();
            entity.getAnimationState().setCurrentFrameIndex(frameIndex);
        }
    }

    private Animation selectAnimation(Entity entity) {
        AnimationSet animationSet = entity.getAnimationSet();
        AnimationType type;
        switch (entity.getStatus()) {
            case WALK:
                type = AnimationType.WALK;
                break;
            case TERMINATE:
                type = AnimationType.TERMINATE;
                break;
            case ATTACK:
                type = AnimationType.ATTACK;
                break;
            case DAMAGE:
                type = AnimationType.DAMAGE;
                break;
            case IDLE:
            default:
                type = AnimationType.IDLE;
                break;
        }
        Animation animation = animationSet.get(type);
        return animation != null ? animation : animationSet.get(AnimationType.IDLE);
    }
}
