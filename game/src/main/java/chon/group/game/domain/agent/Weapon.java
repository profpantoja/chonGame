package chon.group.game.domain.agent;

import chon.group.game.core.Entity;

public abstract class Weapon extends Entity {

    public Weapon(double posX, double posY, int height, int width, int speed, int health, String pathImage, boolean flipped) {
        super(posX, posY, height, width, speed, health, pathImage, flipped);
    }

    protected abstract Shot createShot(double posX, double posY, String direction);

    public Shot fire(double posX, double posY, String direction) {
        return this.createShot(posX, posY, direction);
    }

}
