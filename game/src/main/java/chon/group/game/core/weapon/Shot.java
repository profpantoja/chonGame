package chon.group.game.core.weapon;

import java.util.List;
import chon.group.game.core.agent.Entity;
import chon.group.game.messaging.Message;
import java.util.ArrayList;

public abstract class Shot extends Entity {

    private String direction;
    private int damage;
    private String tag;

    public Shot(int posX, int posY, int height, int width, int speed, int health, String pathImage, boolean flipped, int damage, String direction) {
        super(posX, posY, height, width, speed, health, pathImage, flipped, false);
        this.damage = damage;
        this.direction = direction;
        this.tag = "UNDEFINED";
    }

    public void update(double deltaTime) {
        this.updateAnimation(deltaTime);
        this.move(new ArrayList<>(List.of(this.getDirection())));
    }

    public boolean shouldBeRemoved() {
        if (currentAnimation != null && !currentAnimation.isLooping() && currentAnimation.isFinished()) {
            return true;
        }
        return false;
    }

    public String getTag() { return tag; }
    public void setTag(String tag) { this.tag = tag; }
    public String getDirection() { return direction; }
    public void setDirection(String direction) { this.direction = direction; }
    public int getDamage() { return damage; }
    public void setDamage(int damage) { this.damage = damage; }

    @Override
    public void takeDamage(int damage, List<Message> messages) { }
}