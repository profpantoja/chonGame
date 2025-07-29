package chon.group.game.core.weapon;

import chon.group.game.core.agent.Entity;

public abstract class Weapon extends Entity {

    private double energyCost;
    private final long cooldown;
    private long lastFireTime;

    public Weapon(int posX, int posY, int height, int width, int speed, int health, double energyCost, String pathImage, boolean flipped, long cooldown) {
        super(posX, posY, height, width, speed, health, pathImage, flipped, false);
        this.energyCost = energyCost;
        this.cooldown = cooldown;
        this.lastFireTime = 0;
    }

    public double getEnergyCost() {
        return energyCost;
    }

    protected abstract Shot createShot(int posX, int posY, String direction);

    public Shot fire(int posX, int posY, String direction) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastFireTime > cooldown) {
            lastFireTime = currentTime;
            return this.createShot(posX, posY, direction);
        }
        return null;
    }
}