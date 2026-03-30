package chon.group.game.core.weapon;

import java.util.List;

import chon.group.game.core.agent.Direction;
import chon.group.game.core.agent.Entity;
import chon.group.game.messaging.Message;
import chon.group.game.sound.Sound;

public abstract class Shot extends Entity {

    private boolean destructible = false;
    private int damage;

    public Shot(
            int posX,
            int posY,
            int height,
            int width,
            int speed,
            int health,
            Direction direction,
            String pathImage,
            boolean flipped,
            int damage) {
        super(posX, posY, height, width, speed, health, direction, flipped, false);
        this.damage = damage;
    }

    public boolean isDestructible() {
        return destructible;
    }

    public void setDestructible(boolean destructible) {
        this.destructible = destructible;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    @Override
    public void takeDamage(int damage, List<Message> messages, List<Sound> sounds) {
        if (this.destructible) {
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