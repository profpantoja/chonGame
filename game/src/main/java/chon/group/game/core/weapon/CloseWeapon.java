package chon.group.game.core.weapon;

import chon.group.game.core.agent.Entity;

public abstract class CloseWeapon extends Entity {
//Create the close combat weapon template for it to be used by other weapons
    
    private double energyCost;
    public CloseWeapon(int posX, int posY, int height, int width, int speed, int health, String pathImage, boolean flipped) {
        super(posX, posY, height, width, speed, health, pathImage, flipped);
        this.energyCost = energyCost;
    
    }
    protected abstract Slash createSlash(int posX, int posY, String direction);
    
    
    public double getEnergyCost() {
        return energyCost;
    }

    public void setEnergyCost(double energyCost) {
        this.energyCost = energyCost;
    }



    public Slash slash(int posX, int posY, String direction) {
        return createSlash(posX, posY, direction);
    }

}
