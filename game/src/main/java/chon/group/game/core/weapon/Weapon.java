package chon.group.game.core.weapon;

import chon.group.game.core.agent.Direction;
import chon.group.game.core.agent.Entity;

public abstract class Weapon extends Entity {

    private double energyCost;
    private Shot typeShot;
    /** The time of the last attack made. */
    private long lastAttackTime = 0;
    /* Attack cooldown (in milliseconds) */
    private long cooldown = 150;

    public Weapon(
            int posX,
            int posY,
            int height,
            int width,
            int speed,
            int health,
            double energyCost,
            boolean flipped) {
        super(posX, posY, height, width, speed, health, Direction.IDLE, flipped, false);
        this.energyCost = energyCost;
    }

    public Weapon(
            int posX,
            int posY,
            int height,
            int width,
            int speed,
            int health,
            double energyCost,
            boolean flipped,
            long cooldown) {
        super(posX, posY, height, width, speed, health, Direction.IDLE, flipped, false);
        this.energyCost = energyCost;
        this.cooldown = cooldown;
    }

    public double getEnergyCost() {
        return energyCost;
    }

    public void setEnergyCost(double energyCost) {
        this.energyCost = energyCost;
    }

    public Shot getTypeShot() {
        return typeShot;
    }

    public void setTypeShot(Shot typeShot) {
        this.typeShot = typeShot;
    }

    public long getLastAttackTime() {
        return lastAttackTime;
    }

    public void setLastAttackTime(long lastAttackTime) {
        this.lastAttackTime = lastAttackTime;
    }

    public long getCooldown() {
        return cooldown;
    }

    public void setCooldown(long cooldown) {
        this.cooldown = cooldown;
    }

    protected abstract Shot createShot(int posX, int posY, Direction direction);

    public Shot fire(int posX, int posY, Direction direction) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - this.lastAttackTime >= this.cooldown) {
            this.lastAttackTime = currentTime;
            return createShot(posX, posY, direction);
        }
        return null;
    }

}
