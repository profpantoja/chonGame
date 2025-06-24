package chon.group.game.domain.agent;

import chon.group.game.core.Entity;

public abstract class CloseWeapon extends Entity {
//Create the close combat weapon template for it to be used by other weapons
    public CloseWeapon(int posX, int posY, int height, int width, int speed, int health, String pathImage, boolean flipped) {
        super(posX, posY, height, width, speed, health, pathImage, flipped);
    }
    protected abstract Slash createSlash(int posX, int posY, String direction);
    
    public Slash slash(int posX, int posY, String direction) {
        return createSlash(posX, posY, direction);
    }

}
