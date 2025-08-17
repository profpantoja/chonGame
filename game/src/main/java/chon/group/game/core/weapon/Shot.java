package chon.group.game.core.weapon;

import java.util.List;

import chon.group.game.core.agent.Entity;
import chon.group.game.core.animation.AnimationSpritesheet;
import chon.group.game.core.animation.AnimationSystem;
import chon.group.game.messaging.Message;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

public abstract class Shot extends Entity {

    private boolean destructible = false;
    private String direction;
    private int damage;

    public Shot(int posX, int posY, int height, int width, int speed, int health, String pathImage, boolean flipped,
            int damage, String direction) {
        super(posX, posY, height, width, speed, health, pathImage, flipped, false);
        this.damage = damage;
        this.direction = direction;
    }

    public Shot(int posX, int posY, int height, int width, int speed, int health, String pathImage, boolean flipped,
                int damage, String direction, AnimationSpritesheet animationSpritesheet) {
        super(posX, posY, height, width, speed, health, pathImage, flipped, false);
        this.damage = damage;
        this.direction = direction;
        if (animationSpritesheet != null) {
            setAnimationSystem(new AnimationSystem(animationSpritesheet));
        }
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
}