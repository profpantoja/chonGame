package chon.group.game.core.environment.behavior;

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
        environment.getMessenger().update();
        updateCamera(environment);
        recoverEnergy(environment);
    }

    // public abstract void updateWorld(Environment environment);
    public void updateWorld(Environment environment) {
        updateObjects(environment);
        updateShots(environment);
        detectCollision(environment);
    };

    /**
     * Updates all collectible objects in the environment.
     * Makes them follow the protagonist and handles collection.
     */
    public void updateObjects(Environment environment) {
        Level level = environment.getCurrentLevel();
        Agent protagonist = environment.getProtagonist();
        Iterator<Object> iterator = level.getObjects().iterator();
        while (iterator.hasNext()) {
            Object object = iterator.next();
            if (!object.isTerminated()) {
                object.idle();
                if (!object.isCollected() && object.isCollectible()) {
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
                /* If it was collected then it is removed. */
                else if (object.isCollected() && object.isCollectible()) {
                    iterator.remove();
                }
            }
        }
    }

    /**
     * Updates the state of all shots in the environment.
     * Handles movement, boundary removal, and collision with agents or protagonist.
     */
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
            // Shots can expire based on their range.
            if (shot.hasExpired()) {
                itShot.remove();
                continue;
            }
            /* if the shot's position went off the level width, it is removed. */
            if (shot.getPosX() > level.getWidth() || shot.getPosX() + shot.getWidth() < 0) {
                itShot.remove();
                continue;
            }
            // Indicates whether the shot was removed.
            boolean removed = false;
            /* If the remaining shots hit (in)destructible and no collectible objects. */
            for (Object object : level.getObjects()) {
                /*
                 * The shot hit an object if it is not terminated. The shot may pass by
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
                    object.takeDamage(shot.getDamage(), environment.getMessenger().getMessages(),
                            environment.getSounds());
                    /* Then the shot is removed. */
                    itShot.remove();
                    removed = true;
                    break;
                }
                /*
                 * If the object is indestructible, it is necessary to verify if it collectible
                 * or not. If it is collectible, the shot must go on. Otherwise it should be
                 * removed.
                 */
                if (!object.isCollectible()) {
                    itShot.remove();
                    removed = true;
                    break;
                }
                /*
                 * The shot must goes on. So, it leaves the object iterator and searches other
                 * conditions.
                 */
                break;
            }
            /* If this shot was removed, then move to the next shot. */
            if (removed) {
                continue;
            }
            /* The same as before but now with all other agents. */
            for (Agent agent : level.getAgents()) {
                // The shot passes by dead agents.
                if (agent.isDead()) {
                    continue;
                }
                // If it hits an agent.
                if (intersect(agent, shot)) {
                    agent.takeDamage(shot.getDamage(), environment.getMessenger().getMessages(),
                            environment.getSounds());
                    itShot.remove();
                    removed = true;
                    break;
                }
            }
            /* If this shot was removed, then move to the next shot. */
            if (removed) {
                continue;
            }
            /**
             * If any shot intersected the protagonist, the damage is taken, the message
             * system is informed, and the shot is removed.
             */
            if (intersect(protagonist, shot)) {
                protagonist.takeDamage(shot.getDamage(), environment.getMessenger().getMessages(),
                        environment.getSounds());
                itShot.remove();
                continue;
            }
            /* The remaining shots move. */
            shot.move(List.of(shot.getDirection()));
        }
    }

    /**
     * Detects collisions between the protagonist and agents.
     * Applies damage if a collision is detected.
     */
    public void detectCollision(Environment environment) {
        Level level = environment.getCurrentLevel();
        Agent protagonist = environment.getProtagonist();
        /**
         * It detects if other agents have collided with the protagonist. It also
         * verifies if any agents has collided with non-collectible obstacles.
         */
        for (Agent agent : level.getAgents()) {
            /* It verifies a live agents vs. protagonist. */
            if (!agent.isDead())
                if (protagonist != null && intersect(protagonist, agent)) {
                    int damage = 900;
                    protagonist.takeDamage(damage, environment.getMessenger().getMessages(), environment.getSounds());
                }
            /*
             * It verifies agents vs. obstacles. The collision can only happens with non
             * collectible objects.
             */
            for (Object object : level.getObjects()) {
                if (!object.isCollectible() && !object.isDestroyed()) {
                    this.onCollision(agent, object);
                }
            }
        }
        /**
         * It verifies if the protagonist has collided with non-collectible obstacles.
         * The collision can only happens with non
         * collectible objects.
         */
        for (Object object : level.getObjects()) {
            if (!object.isCollectible() && !object.isDestroyed()) {
                this.onCollision(protagonist, object);
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
    private boolean intersect(Entity a, Entity b) {
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
    public void onCollision(Agent agent, Object object) {
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

    /**
     * Updates the camera based on the protagonist’s current position.
     */
    public void updateCamera(Environment environment) {
        if (environment.getCamera() != null) {
            environment.getCamera().update();
        }
    }

    public void recoverEnergy(Environment environment) {
        environment.getProtagonist().recoverEnergy();
    }

}
