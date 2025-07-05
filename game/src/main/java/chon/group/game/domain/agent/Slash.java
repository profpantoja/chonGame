package chon.group.game.domain.agent;

import java.util.List;

import chon.group.game.core.Entity;
import chon.group.game.domain.environment.Collision;
import chon.group.game.messaging.Message;

public class Slash extends Entity {

    private boolean destructible = false;
    private String direction;
    private int damage;
    private int lifeSpan = 10; 
    private int ticksAlive = 0;

    public boolean shouldRemove() {
        return ticksAlive++ >= lifeSpan;
    }
    
    public Slash(int posX, int posY, int height, int width, int speed, int health, String pathImage, boolean flipped,
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
    public void takeDamage(int damage, List<Message> messages) {
        if (destructible) {
            /* Decrease health. */
            this.setHealth(this.getHealth() - damage);
            messages.add(new Message(
                    String.valueOf(damage),
                    this.getPosX(),
                    this.getPosY(),
                    25));
            /* After taking the damage, the health must not be negative. */
            if (this.getHealth() < 0)
                this.setHealth(0);
        }
    }

     public void checkCollision(Collision collision) {
        if (getPosX() < collision.getX() + collision.getWidth() &&
            getPosX() + getWidth() > collision.getX() &&
            getPosY() < collision.getY() + collision.getHeight() &&
            getPosY() + getHeight() > collision.getY()) {

            if (collision.isProjectileDestroy()) {
                collision.setDestroy(true);
            }
        }
    }
    

}