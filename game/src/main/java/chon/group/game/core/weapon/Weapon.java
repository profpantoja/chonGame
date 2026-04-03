package chon.group.game.core.weapon;

import chon.group.game.core.agent.Direction;
import chon.group.game.core.agent.Entity;

public abstract class Weapon extends Entity {

    private int offsetPosY = 0;
    private double energyCost;
    private Shot typeShot;
    /** The time of the last attack made. */
    private long lastAttackTime = 0;
    /* Attack cooldown (in milliseconds) */
    private long cooldown = 0;

    public Weapon(
            int posX,
            int posY,
            int width,
            int height,
            int speed,
            int health,
            double energyCost,
            boolean flipped) {
        super(posX, posY, width, height, 0, speed, health, Direction.IDLE, flipped, false);
        this.energyCost = energyCost;
    }

    public Weapon(
            int offsetPosY,
            int speed,
            int health,
            int width,
            int height,
            double energyCost,
            boolean flipped,
            long cooldown) {
        super(0, 0, width, height, 0, speed, health, Direction.IDLE, flipped, false);
        this.offsetPosY = offsetPosY;
        this.energyCost = energyCost;
        this.cooldown = cooldown;
    }

    public int getOffsetPosY() {
        return offsetPosY;
    }

    public void setOffsetPosY(int offsetPosY) {
        this.offsetPosY = offsetPosY;
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

    protected abstract Shot createShot(
            int posX,
            int posY,
            int offsetPosY,
            Direction direction);

    public Shot fire(int posX, int posY, int entityWidth, Direction direction) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - this.lastAttackTime >= this.cooldown) {
            this.lastAttackTime = currentTime;
            return createShot(
                    posX,
                    posY,
                    entityWidth,
                    direction);
        }
        return null;
    }

}
