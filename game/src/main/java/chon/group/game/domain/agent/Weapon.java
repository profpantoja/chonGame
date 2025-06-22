package chon.group.game.domain.agent;

import chon.group.game.core.Entity;

public abstract class Weapon extends Entity {

    private int shotWidth;

    public int getShotWidth() {
        return shotWidth;
    }

    public void setShotWidth(int shotWidth) {
        this.shotWidth = shotWidth;
    }

    public Weapon(int posX, int posY, int height, int width, int speed, int health, String pathImage, boolean flipped, int shotWidth) {
        super(posX, posY, height, width, speed, health, pathImage, flipped);
        this.shotWidth = shotWidth;
    }

    protected abstract Shot createShot(int posX, int posY, String direction);

    public Shot fire(int posX, int posY, String direction) {
        return this.createShot(posX, posY, direction);
    }

}
