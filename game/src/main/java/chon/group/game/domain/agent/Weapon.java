package chon.group.game.domain.agent;

import chon.group.game.core.Entity;
import chon.group.game.domain.environment.KeyboardKey;

public abstract class Weapon extends Entity {

    public Weapon(int posX, int posY, int height, int width, int speed, int health, String pathImage, boolean flipped) {
        super(posX, posY, height, width, speed, health, pathImage, flipped);
    }

    protected abstract Shot createShot(int posX, int posY, KeyboardKey direction);

    public Shot fire(int posX, int posY, KeyboardKey direction) {
        return this.createShot(posX, posY, direction);
    }

}
