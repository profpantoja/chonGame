package chon.group.game.domain.agent;

import java.util.List;

import chon.group.game.core.Entity;
import chon.group.game.messaging.Message;

public class Shot extends Entity {

    private boolean destructible = false;
    private int damage;

    public Shot(int posX, int posY, int height, int width, int speed, int health, String pathImage, boolean flipped,
            int damage) {
        super(posX, posY, height, width, speed, health, pathImage, flipped);
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
}
