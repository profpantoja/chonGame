package chon.group.game.core.weapon;

import chon.group.game.core.agent.Entity;

public abstract class Weapon extends Entity {

    public Weapon(int posX, int posY, int height, int width, int speed, int health, String pathImage, boolean flipped) {
        super(posX, posY, height, width, speed, health, pathImage, flipped);
    }

    protected abstract Shot createShot(int posX, int posY, String direction);

    public Shot fire(int posX, int posY, String direction) {
        return this.createShot(posX, posY, direction);
    }

}
