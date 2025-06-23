package chon.group.game.domain.agent;

import java.util.List;

import chon.group.game.core.Entity;
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
    private final long INVULNERABILITY_COOLDOWN = 3000;

    /* The Agent's Weapon */
    private Weapon weapon;


    /*Flag to control if the protaginist its jumping */
    private boolean isJumping = false;

    /* Velocidade inicial do pulo (negativo = sobe) */
    private int jumpVelocity = -12;

    private int currentVelocityY = 0;

    private final int gravity = 2;

    private final int groundY = 513;

    private boolean isProtagonist;


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

    public Agent(int posX, int posY, int height, int width, int speed, int health, String pathImage, boolean flipped, boolean isProtagonist) {
    super(posX, posY, height, width, speed, health, pathImage, flipped);
    this.isProtagonist = isProtagonist;
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

    /**
     * Método move sobreescrito da classe Entity
     */
@Override
public void move(List<String> movements) {
    int speed = this.getSpeed();

    // Movimento lateral
    if (movements.contains("LEFT")) {
        if (!this.isFlipped()) this.flipImage();
        this.setPosX(this.getPosX() - speed);
    }

    if (movements.contains("RIGHT")) {
        if (this.isFlipped()) this.flipImage();
        this.setPosX(this.getPosX() + speed);
    }

    /* PULO: apenas se for protagonista */
    if (isProtagonist) {
        if (movements.contains("UP") && !isJumping) {
            isJumping = true;
            currentVelocityY = jumpVelocity;
        }

        if (isJumping) {
            this.setPosY(this.getPosY() + currentVelocityY);
            currentVelocityY += gravity;

            /* Se está tocando no chão */
            if (this.getPosY() >= groundY) {
                this.setPosY(groundY);
                isJumping = false;
                currentVelocityY = 0;
            }
        } else {
            // Fixa a Chonbota no chão, caso não esteja pulando
            this.setPosY(groundY);
        }
    } else {
        // Chonbot não pula, está sempre no chão
    }
}






}