package chon.group.game.core.agent;

import java.util.List;

import chon.group.game.core.weapon.Shot;
import chon.group.game.core.weapon.Weapon;
import chon.group.game.messaging.Message;

/**
 * Represents an agent in the game, with properties such as position, size,
 * speed, and image.
 * The agent can move in specific directions and chase a target.
 */
public class Agent extends Entity {

    /** The time of the last hit taken. */
    private long lastHitTime = 0;

    /** Flag to control the invulnerability status of the agent. */
    private boolean invulnerable = false;

    /* Invulnerability (in milliseconds) */
    private final long INVULNERABILITY_COOLDOWN = 1000;

    /** The Agent's Weapon */
    private Weapon weapon;

    /** The initial agent's energy */
    private double energy;

    /** The maximum agent's energy. */
    private final double fullEnergy;

    /** The agent's energy recovery factor */
    private final double recoveryFactor;
    
    /** The energy cost required for the agent to perform an action */
    private double energyCost;

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
        this.energy = 1.0;
        this.fullEnergy = 1.0;
        this.recoveryFactor = 0.0002;
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
        this.energy = 1.0;
        this.fullEnergy = 1.0;
        this.recoveryFactor = 0.0002;
        this.energyCost = 0.01;
    }

    /**
     * Gets the last hit taken.
     */
    public long getLastHitTime() {
        return lastHitTime;
    }

    /**
     * Sets the last hit taken.
     *
     * @param lastHitTime the new image
     */
    public void setLastHitTime(long lastHitTime) {
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
     * Gets the current energy level (0.0 to 1.0).
     *
     * @return current energy level
     */
    public double getEnergy() {
        return energy;
    }

    public void setEnergy(double energy) {
        this.energy = energy;
    }

    /**
     * Gets the maximum energy capacity.
     *
     * @return maximum energy
     */
    public double getFullEnergy() {
        return fullEnergy;
    }

    public double getRecoveryFactor() {
        return recoveryFactor;
    }

    /**
     * Gets the energy cost of performing an action.
     *
     * @return the energy cost
     */
    public double getEnergyCost() {
        return energyCost;
    }

    /**
     * Sets the energy cost required to perform an action.
     *
     * @param energyCost the new energy cost
     */
    public void setEnergyCost(double energyCost) {
        this.energyCost = energyCost;
    }

    /**
     * Consumes a specified amount of energy.
     *
     * @param amount the amount of energy to consume
     */
    public void consumeEnergy(double amount) {
        this.energy = Math.max(0, this.energy - amount);
    }

    /**
     * Recovers a specified amount of energy.
     *
     * @param amount the amount of energy to recover
     */
    public void recoverEnergy() {
        this.energy = Math.min(this.fullEnergy, (this.energy + this.recoveryFactor));
    }

    /**
     * Checks if energy is empty.
     *
     * @return true if energy is 0 or less
     */
    public boolean isEnergyEmpty() {
        return this.energy <= 0;
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

    public Shot useWeapon() {
        String direction = this.isFlipped() ? "LEFT" : "RIGHT";
        if (this.energy >= this.getWeapon().getEnergyCost()) {
            this.consumeEnergy(this.getWeapon().getEnergyCost());
            return this.weapon.fire(this.getPosX(), this.getPosY(), direction);
        } else
            return null;
    }

    @Override
    /**
     
    Makes the agent sprint based on the movement commands provided, but only if there is enough energy in the energy bar.
    @param movements a list of movement directions ("RIGHT", "LEFT", "UP",
    "DOWN") and "SHIFT" to sprint
    */
    public void sprint(List<String> movements) {
        if (movements.contains("SHIFT") && this.energy >= this.energyCost) {
            if (movements.contains("RIGHT")) {
                setPosX(posX += speed * 2);
                this.consumeEnergy(this.energyCost);
            } else if (movements.contains("LEFT")) {
                setPosX(posX -= speed * 2);
                this.consumeEnergy(this.energyCost);
            } else if (movements.contains("UP")) {
                setPosY(posY -= speed * 2);
                this.consumeEnergy(this.energyCost);
            } else if (movements.contains("DOWN")) {
                setPosY(posY += speed * 2);
                this.consumeEnergy(this.energyCost);
            }
        }
    }

}