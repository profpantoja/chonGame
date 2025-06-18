package chon.group.game.domain.agent;

import java.util.List;

import chon.group.game.core.Entity;
import chon.group.game.domain.environment.StatusProtagonist;
import chon.group.game.messaging.Message;

/**
 * Represents an agent in the game, with properties such as position, size,
 * speed, and image.
 * The agent can move in specific directions and chase a target.
 */
public class Agent extends Entity {

    /* The time of the last hit taken. */
    private long lastHitTime = 0;

    /* Invulnerability (in milliseconds) */
    private final long INVULNERABILITY_COOLDOWN = 3000;

    /* The agent's status */
    private StatusProtagonist status = StatusProtagonist.ALIVE;

    /* The Agent's Weapon */
    private Weapon weapon;

    /**
     * Constructor to initialize the agent properties.
     *
     * @param posX      the agent's initial X (horizontal) position
     * @param posY      the agent's initial Y (vertical) position
     * @param height    the agent's height
     * @param width     the agent's width
     * @param speed     the agent's speed
     * @param health    the agent's health
     * @param pathImage the path to the agent's image
     */
    public Agent(int posX, int posY, int height, int width, int speed, int health, String pathImage) {
        super(posX, posY, height, width, speed, health, pathImage);
    }

    /**
     * Constructor to initialize the agent properties including its direction.
     *
     * @param posX      the agent's initial X (horizontal) position
     * @param posY      the agent's initial Y (vertical) position
     * @param height    the agent's height
     * @param width     the agent's width
     * @param speed     the agent's speed
     * @param health    the agent's health
     * @param pathImage the path to the agent's image
     * @param flipped   the agent's direction (RIGHT=0 or LEFT=1)
     */
    public Agent(int posX, int posY, int height, int width, int speed, int health, String pathImage, boolean flipped) {
        super(posX, posY, height, width, speed, health, pathImage, flipped);
    }

    /**
     * Gets the last hit taken.
     */
    public long getlastHitTime() {
        return lastHitTime;
    }

    /**
     * Sets the last hit taken.
     *
     * @param lastHitTime the new image
     */
    public void setlastHitTime(long lastHitTime) {
        this.lastHitTime = lastHitTime;
    }

    /**
     * Gets invulnerable cooldown time.
     *
     * @return the time is milliseconds
     */
    public long getInvulnerabilityCooldown() {
        return INVULNERABILITY_COOLDOWN;
    }

    /**
     * Gets the agent's weapon.
     *
     * @return its weapon.
     */
    public Weapon getWeapon() {
        return weapon;
    }

    /**
     * Sets the agent new weapon.
     *
     * @param weapon the new weapon
     */
    public void setWeapon(Weapon weapon) {
        this.weapon = weapon;
    }

    /**
     * Gets if the agent is dead.
     *
     * @return if the agent is dead
     */
    public boolean isDead() {
        return (this.getHealth() <= 0);
    }

    /**
     * Sets the agent's status.
     *
     * @param status the new status
     */
    public StatusProtagonist getStatus() {
        return status;
    }
    
    /**
     * Makes the agent take damage.
     * If health reaches 0, the game must end.
     *
     * @param damage the amount of damage to be applied
     */
    @Override
    public void takeDamage(int damage, List<Message> messages) {
        if (status == StatusProtagonist.INVULNERABLE || status == StatusProtagonist.DEAD) {
            return;
        }
        super.takeDamage(damage, messages);
        if (this.getHealth() <= 0) {
            status = StatusProtagonist.DEAD;
        } else {
            status = StatusProtagonist.INVULNERABLE;
            lastHitTime = System.currentTimeMillis();
        }
    }
    
    public void update() {
        if (status == StatusProtagonist.INVULNERABLE) {
            long elapsedTime = System.currentTimeMillis() - lastHitTime;
            if (elapsedTime > INVULNERABILITY_COOLDOWN) {
                status = StatusProtagonist.ALIVE; 
            }
        }
    }
    
}