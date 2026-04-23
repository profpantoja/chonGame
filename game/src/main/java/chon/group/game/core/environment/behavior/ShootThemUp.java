package chon.group.game.core.environment.behavior;

import chon.group.game.core.agent.Agent;
import chon.group.game.core.agent.Entity;
import chon.group.game.core.agent.Object;
import chon.group.game.core.environment.Environment;

public class ShootThemUp extends BaseBehavior {

    /**
     * Checks if two entities intersect (collide).
     * 
     * In Shoot'em Up games, the intersection considers the full image size.
     * It disconsiders the Hitbox's ratio.
     * 
     * @param a the first entity
     * @param b the second entity
     * @return true if their areas overlap, false otherwise
     */
    protected boolean intersect(Entity a, Entity b) {
        /* It is necessary to define a way of fixing the Hitbox size for all images. */
        return a.getPosX() < b.getPosX() + b.getHitbox().getWidth() &&
                a.getPosX() + a.getHitbox().getWidth() > b.getPosX() &&
                a.getPosY() < b.getPosY() + b.getHitbox().getBaseHeight() &&
                a.getPosY() + a.getHitbox().getBaseHeight() > b.getPosY();
    }

    /**
     * Checks if an agent and an object intersect (collide) based on a direction.
     * 
     * In Shoot'em Up games, when the agent collides if an object, it gets hurt and
     * the object is destroyed.
     *
     * @param a the agent
     * @param b the object
     *
     */
    @Override
    protected void onCollision(Agent agent, Object object, Environment environment) {
        if (!this.intersect(agent, object)) {
            return;
        }
        this.applyDamage(agent, 1000, environment);
        object.destroy(environment.getMessenger().getMessages(), environment.getSounds());
    }

    protected void recoverEnergy(Environment environment) {
        environment.getProtagonist().recoverEnergy();
    }

}
