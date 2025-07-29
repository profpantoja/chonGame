package chon.group.game.core.weapon;

import java.util.List;

import chon.group.game.core.agent.Agent;
import chon.group.game.core.agent.Entity;
import chon.group.game.messaging.Message;


public class Slash extends Entity {

    private boolean destructible = false;
    private String direction;
    private int damage;
    private int lifeSpan = 90; 
    private int ticksAlive = 0;
    private Agent owner;

    public boolean shouldRemove() {
        return ticksAlive++ >= lifeSpan;
    }
    
    public Slash(int posX, int posY, int height, int width, int speed, int health, String pathImage, boolean flipped,
            int damage, String direction, Agent owner) {
        super(posX, posY, height, width, speed, health, pathImage, flipped, false);
        this.damage = damage;
        this.direction = direction;
        this.owner = owner;
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
    
    public Agent getOwner() {
        return owner;
    }

    public void setOwner(Agent owner) {
        this.owner = owner;
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
}