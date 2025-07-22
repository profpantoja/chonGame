package chon.group.game.core.weapon;

import chon.group.game.core.agent.Entity;

public abstract class Weapon extends Entity {

    private double energyCost;

    public Weapon(int posX, int posY, int height, int width, int speed, int health, double energyCost, String pathImage, boolean flipped) {
        super(posX, posY, height, width, speed, health, pathImage, flipped, false);
        this.energyCost = energyCost;
    }

    public double getEnergyCost() {
        return energyCost;
    }

    public void setEnergyCost(double energyCost) {
        this.energyCost = energyCost;
    }

    protected abstract Shot createShot(int posX, int posY, String direction);

    public Shot fire(int posX, int posY, String direction) {
        return this.createShot(posX, posY, direction);
    }

}
