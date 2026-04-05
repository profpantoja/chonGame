package chon.group.game.animation;

import java.util.List;

import chon.group.game.core.agent.Agent;
import chon.group.game.core.agent.Entity;
import chon.group.game.core.environment.Level;

public class Animator {

    /**
     * Updates one specific agent.
     */
    public void animate(Entity entity) {
        if (!entity.getAnimationState().isBlocked())
            entity.getAnimationState().setCurrentAnimation(this.selectAnimation(entity));
        /*
         * It gets the elapsed time and compares with the frame duration of the current
         * animation.
         */
        if (entity.isVisible())
            entity.getAnimationState().stepAnimation();
    }

    public void animateEntities(List<? extends Entity> entities) {
        for (Entity entity : entities) {
            this.animate(entity);
        }
    }

    public void animateLevel(Level level, Agent protagonist) {
        /* It animates the protagonist. */
        this.animate(protagonist);
        /* It animates all agents. */
        this.animateEntities(level.getAgents());
        /* It animates all objects. */
        this.animateEntities(level.getObjects());
        /* It animates all shots. */
        this.animateEntities(level.getShots());
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
