package chon.group.game.domain.agent;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import chon.group.game.core.Entity;
import chon.group.game.domain.environment.Environment;
import chon.group.game.messaging.Message;

/**
 * Represents an agent in the game, with properties such as position, size,
 * speed, and image.
 * The agent can move in specific directions and chase a target.
 */
public class Agent extends Entity {

    /* The time of the last hit taken. */
    private long lastHitTime = 0;
    
    /* Flag to control the invulnerability status of the agent. */
    private boolean invulnerable = false;

    /* Invulnerability (in milliseconds) */
    private final long INVULNERABILITY_COOLDOWN = 1500;

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
     * Gets if the agent is invulnerable.
     *
     * @return if the agent is invulnerable
     */
    public boolean isInvulnerable() {
        return invulnerable;
    }

    /**
     * Sets the agent invulnerable status.
     *
     * @param invulnerable the new invulnerable status
     */
    public void setInvulnerable(boolean invulnerable) {
        this.invulnerable = invulnerable;
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
     * Makes the agent take damage.
     * If health reaches 0, the game must end.
     *
     * @param damage the amount of damage to be applied
     */
    @Override
    public void takeDamage(int damage, List<Message> messages) {
        this.invulnerable = this.updateInvulnerability();
        if (!this.invulnerable) {
            super.takeDamage(damage, messages); 
            this.lastHitTime = System.currentTimeMillis();
        }
    }

    public void checkCollision(Collision collision) {
        if (getPosX() < collision.getX() + collision.getWidth() &&
            getPosX() + getWidth() > collision.getX() &&
            getPosY() < collision.getY() + collision.getHeight() &&
            getPosY() + getHeight() > collision.getY()) {

            // Detecção de colisão
            float dx = (getPosX() + getWidth() / 2.0f) - (collision.getX() + collision.getWidth() / 2.0f);
            float dy = (getPosY() + getHeight() / 2.0f) - (collision.getY() + collision.getHeight() / 2.0f);

            float halfWidths = (getWidth() / 2.0f) + (collision.getWidth() / 2.0f);
            float halfHeights = (getHeight() / 2.0f) + (collision.getHeight() / 2.0f);

            float overlapX = halfWidths - Math.abs(dx);
            float overlapY = halfHeights - Math.abs(dy);

            // 1. Impedir passagem se não for passável
            if (!collision.isPassable()) {
                int fix = (int)Math.ceil(Math.min(overlapX, overlapY));
                if (overlapX < overlapY) {
                    if (dx > 0) {
                        setPosX(getPosX() + fix);
                    } else {
                        setPosX(getPosX() - fix);
                    }
                } else {
                    if (dy > 0) {
                        setPosY(getPosY() + fix);
                    } else {
                        setPosY(getPosY() - fix);
                    }
                }
            }

            // 2. Aplicar dano
            if (collision.getDamage() > 0) {
                takeDamage(collision.getDamage(), new ArrayList<Message>());
            }

            // 3. Destruir ao contato
            if (collision.isContactDestroy()) {
                collision.setDestroy(true);
            }
        }
    }

    /**
     * Method to update the invulnerable status.
     *
     * @return if the agent is still invulnerable
     */
    private boolean updateInvulnerability() {
        if (System.currentTimeMillis() - lastHitTime >= INVULNERABILITY_COOLDOWN) {
            return false;
        }
        return true;
    }

}