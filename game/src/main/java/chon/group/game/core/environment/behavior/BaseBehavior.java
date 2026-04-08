package chon.group.game.core.environment.behavior;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import chon.group.game.core.agent.Agent;
import chon.group.game.core.agent.Entity;
import chon.group.game.core.agent.Object;
import chon.group.game.core.environment.Environment;
import chon.group.game.core.environment.Level;
import chon.group.game.core.weapon.Shot;
import chon.group.game.sound.SoundEvent;

//public abstract class BaseBehavior implements EnvironmentBehavior {
public class BaseBehavior implements EnvironmentBehavior {
    @Override
    public final void update(Environment environment) {
        updateWorld(environment);
        /* It updates the messenger system. */
        environment.getMessenger().update();
        /* It updates the camera system. */
        updateCamera(environment);
        /* It recovers the agent energy. */
        recoverEnergy(environment);
    }

    protected void updateWorld(Environment environment) {
        /*
         * Update the agents based on their movements.
         * It also removes agents that can be removed.
         */
        this.updateAgents(environment);
        /* Checks the border based on the level behavior (physics). */
        this.checkBorders(environment);
        /* Updates the state of objects. */
        this.updateObjects(environment);
        /* Updates the state of all shots. */
        this.updateShots(environment);
        /* Updates the collision system. */
        this.detectCollision(environment);
    }

    protected void updateAgents(Environment environment) {
        Agent protagonist = environment.getProtagonist();
        Iterator<Agent> itAgent = environment.getCurrentLevel().getAgents().iterator();
        /* ChonBot's Automatic Movements */
        /* Update the other agents' movements */
        while (itAgent.hasNext()) {
            Agent agent = itAgent.next();
            if (agent.canRemove()) {
                itAgent.remove();
                continue;
            }
            /* Every agent chases the protagonist. */
            agent.chase(protagonist.getPosX(),
                    protagonist.getPosY());
        }
    }

    /**
     * Updates all collectible objects in the environment.
     * Makes them follow the protagonist and handles collection.
     */

    protected void updateObjects(Environment environment) {
        Level level = environment.getCurrentLevel();
        Agent protagonist = environment.getProtagonist();
        Iterator<Object> iterator = level.getObjects().iterator();
        while (iterator.hasNext()) {
            Object object = iterator.next();
            /*
             * If the object has been destroyed and its animation cycle is over, it is
             * removed. Besides, if it was collected then it is removed.
             */
            if ((object.isDestroyed() && object.canRemove())
                    || object.isCollected()) {
                iterator.remove();
                continue;
            }
            /*
             * The object has been destroyed but its animation cycle is still on. If the
             * !object.isCollectible() is inside the next condition, the hit object won't
             * return to idle. It will stay in the last frame from the hit animation. It can
             * give a sensation of almost broken.
             */
            if (object.isTerminated()) {
                continue;
            }
            /* Since it was not hit or destroyed, the object idles. */
            object.idle();
            if (!object.isCollectible()) {
                continue;
            }
            /*
             * Since the object is collectible and collected, it will chase the protagonist.
             */
            object.follow(protagonist);
            double collectRadius = 20;
            double dx = object.getPosX() - protagonist.getPosX();
            double dy = object.getPosY() - protagonist.getPosY();
            double squaredDistance = dx * dx + dy * dy;
            /**
             * If the radius between the agent and the object is less than 20 pxls then it
             * is collected.
             */
            if (squaredDistance < collectRadius * collectRadius) {
                environment.getSounds().add(object.getSoundSet().get(SoundEvent.COLLECT));
                object.onCollect();
                environment.setCollectedCount(environment.getCollectedCount() + 1);
                environment.setScore(environment.getScore() + 10);
            }
        }

    }

    /**
     * Updates the state of all shots in the environment.
     * Handles movement, boundary removal, and collision with agents or protagonist.
     */
    @Override
    public void updateShots(Environment environment) {
        Level level = environment.getCurrentLevel();
        Agent protagonist = environment.getProtagonist();
        // It gets the list of shots.
        Iterator<Shot> itShot = level.getShots().iterator();
        /*
         * While there is a next available shot. The position of each conditional block
         * defines the collision priority.
         */
        while (itShot.hasNext()) {
            Shot shot = itShot.next();
            /* Shots can expire based on their range. */
            if (shot.hasExpired()) {
                itShot.remove();
                continue;
            }
            /*
             * if the shot's position went off the level width (left or right), it is
             * removed.
             */
            if (this.isShotOutOfBounds(shot, level)) {
                itShot.remove();
                continue;
            }
            if (this.checkShotObjectCollision(shot, environment, level, itShot))
                continue;
            if (this.checkShotAgentCollision(shot, environment, level, itShot))
                continue;
            if (this.checkShotProtagonistCollision(shot, environment, protagonist, itShot))
                continue;
            /* The remaining shots move. */
            shot.move(List.of(shot.getDirection()));
        }
    }

    protected boolean isShotOutOfBounds(Shot shot, Level level) {
        return shot.getPosX() > level.getWidth()
                || shot.getPosX() + shot.getWidth() < 0;
    }

    protected boolean checkShotObjectCollision(
            Shot shot,
            Environment environment,
            Level level,
            Iterator<Shot> itShot) {
        /* If the remaining shots hit(in)destructible and no collectible objects. */
        for (Object object : level.getObjects()) {
            /*
             * The shot hit an object if it is terminated. If doen't, the shot may pass by
             * terminated objects.
             */
            if (object.isTerminated()) {
                continue;
            }
            /* If there is no intersection, the shot must go on. */
            if (!intersect(object, shot)) {
                continue;
            }
            /* If the object is destructible, it must take some damage. */
            if (object.isDestructible()) {
                this.applyDamage(object, shot.getDamage(), environment);
                /* Then the shot is removed. */
                itShot.remove();
                return true;
            }
            /*
             * If the object is indestructible, it is necessary to verify if it collectible
             * or not. If it is collectible, the shot must go on. Otherwise it should be
             * removed.
             */
            if (!object.isCollectible()) {
                itShot.remove();
                return true;
            }
            /*
             * The shot must goes on. So, it leaves the object iterator and searches other
             * conditions.
             */
            return false;
        }
        return false;
        /*
         * The shot must go on. So, it leaves the object iterator and searches other
         * conditions.
         */
    }

    protected boolean checkShotAgentCollision(
            Shot shot,
            Environment environment,
            Level level,
            Iterator<Shot> itShot) {
        /* The same as before but now with all other agents. */
        for (Agent agent : level.getAgents()) {
            // The shot passes by dead agents.
            if (agent.isDead()) {
                continue;
            }
            // If it hits an agent.
            if (intersect(agent, shot)) {
                this.applyDamage(agent, shot.getDamage(), environment);
                itShot.remove();
                /* If this shot was removed, then move to the next shot. */
                return true;
            }
        }
        /*
         * The shot must go on and search other
         * conditions.
         */
        return false;
    }

    protected boolean checkShotProtagonistCollision(
            Shot shot,
            Environment environment,
            Agent protagonist,
            Iterator<Shot> itShot) {
        /**
         * If any shot intersected the protagonist, the damage is taken, the message
         * system is informed, and the shot is removed.
         */
        if (intersect(protagonist, shot)) {
            this.applyDamage(protagonist, shot.getDamage(), environment);
            itShot.remove();
            return true;
        }
        return false;
    }

    /**
     * Detects collisions between the protagonist and agents.
     * Applies damage if a collision is detected.
     */
    protected void detectCollision(Environment environment) {
        Level level = environment.getCurrentLevel();
        Agent protagonist = environment.getProtagonist();

        List<Agent> movingAgents = new ArrayList<>();
        movingAgents.add(protagonist);
        movingAgents.addAll(level.getAgents());

        /**
         * It detects if other agents have collided with the protagonist. It also
         * verifies if any agents has collided with non-collectible obstacles.
         */
        for (Agent agent : movingAgents) {
            /* It verifies a live agents vs. protagonist. */
            if (!agent.isDead())
                if (protagonist != null
                        && protagonist != agent
                        && intersect(protagonist, agent)) {
                    this.applyDamage(protagonist, 900, environment);
                }
            /*
             * It verifies agents vs. obstacles. It verifies if the protagonist has collided
             * with non-collectible obstacles. The collision can only happens with non
             * collectible objects.
             */
            for (Object object : level.getObjects()) {
                if (object.blocksMovement()) {
                    this.onCollision(agent, object);
                }
            }
        }
    }

    /**
     * Checks if two entities intersect (collide).
     *
     * @param a the first entity
     * @param b the second entity
     * @return true if their areas overlap, false otherwise
     */
    protected boolean intersect(Entity a, Entity b) {
        return a.getPosX() < b.getPosX() + b.getHitbox().getWidth() &&
                a.getPosX() + a.getHitbox().getWidth() > b.getPosX() &&
                a.getPosY() < b.getPosY() + b.getHitbox().getHeight() &&
                a.getPosY() + a.getHitbox().getHeight() > b.getPosY();
    }

    /**
     * Ensures the protagonist stays within the boundaries of the environment.
     */
    public void checkBorders(Environment environment) {
        Level level = environment.getCurrentLevel();
        Agent protagonist = environment.getProtagonist();
        int bottomY = level.getBottomY();
        int topY = level.getTopY();
        if (protagonist.getPosX() < 0)
            protagonist.setPosX(0);
        if ((protagonist.getPosX() + protagonist.getWidth()) > level.getWidth())
            protagonist.setPosX(level.getWidth() - protagonist.getWidth());
        /* It ensures an internal pre-defined boundary in Y. */
        if (protagonist.getPosY() < topY)
            protagonist.setPosY(topY);
        if (protagonist.getPosY() + protagonist.getHeight() > bottomY)
            protagonist.setPosY(bottomY - protagonist.getHeight());
    }

    /**
     * Checks if an agent and an object intersect (collide) based on a direction. If
     * so, it adjusts its position.
     *
     * @param a the agent
     * @param b the object
     *
     */
    protected void onCollision(Agent agent, Object object) {
        if (!intersect(agent, object)) {
            return;
        }
        int agentLeft = agent.getPosX();
        int agentRight = agent.getPosX() + agent.getHitbox().getWidth();
        int agentTop = agent.getPosY();
        int agentBottom = agent.getPosY() + agent.getHitbox().getHeight();

        int objectLeft = object.getPosX();
        int objectRight = object.getPosX() + object.getHitbox().getWidth();
        int objectTop = object.getPosY();
        int objectBottom = object.getPosY() + object.getHitbox().getHeight();

        int overlapLeft = agentRight - objectLeft;
        int overlapRight = objectRight - agentLeft;
        int overlapTop = agentBottom - objectTop;
        int overlapBottom = objectBottom - agentTop;

        int minOverlapX = Math.min(overlapLeft, overlapRight);
        int minOverlapY = Math.min(overlapTop, overlapBottom);

        if (minOverlapX < minOverlapY) {
            if (overlapLeft < overlapRight) {
                agent.setPosX(objectLeft - agent.getHitbox().getWidth() - 1);
            } else {
                agent.setPosX(objectRight + 1);
            }
        } else {
            if (overlapTop < overlapBottom) {
                agent.setPosY(objectTop - agent.getHitbox().getHeight() - 1);
            } else {
                agent.setPosY(objectBottom + 1);
            }
        }
    }

    protected void applyDamage(Entity target, int damage, Environment environment) {
        target.takeDamage(
                damage,
                environment.getMessenger().getMessages(),
                environment.getSounds());
    }

    /**
     * Updates the camera based on the protagonist’s current position.
     */
    protected void updateCamera(Environment environment) {
        if (environment.getCamera() != null) {
            environment.getCamera().update();
        }
    }

    protected void recoverEnergy(Environment environment) {
        environment.getProtagonist().recoverEnergy();
    }

}
