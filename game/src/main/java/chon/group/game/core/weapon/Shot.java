package chon.group.game.core.weapon;

import chon.group.game.core.agent.Entity;

public abstract class Shot extends Entity {

    private boolean destructible = false;
    private String direction;
    private int damage;

    public Shot(int posX, int posY, int height, int width, int speed, int health, String pathImage, boolean flipped,
            int damage, String direction) {
        super(posX, posY, height, width, speed, health, pathImage, flipped);
        this.damage = damage;
        this.direction = direction;
    }

    public boolean isDestructible() {
        return destructible;
    }

    public void setDestructible(boolean destructible) {
        this.destructible = destructible;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    @Override
    public void takeDamage(int damage) {
        if (destructible) {
            /* Decrease health. */
            this.setHealth(this.getHealth() - damage);
            /* After taking the damage, the health must not be negative. */
            if (this.getHealth() < 0)
                this.setHealth(0);
        }
    }
}