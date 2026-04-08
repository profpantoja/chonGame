package chon.group.game.core.weapon;

import java.util.List;

import chon.group.game.core.agent.Direction;
import chon.group.game.core.agent.Entity;
import chon.group.game.messaging.Message;
import chon.group.game.sound.Sound;

public abstract class Shot extends Entity {

    private boolean destructible = false;
    private int damage;
    /** Time when this shot was created. */
    private int creationPosX;
    /** Duration in milliseconds that the shot/hit is effectible. */
    private int range = 15;

    public Shot(
            int posX,
            int posY,
            int width,
            int height,
            int speed,
            int health,
            Direction direction,
            boolean flipped,
            int damage,
            int range) {
        super(posX, posY, width, height, 0, speed, health, direction, flipped, false);
        this.damage = damage;
        this.creationPosX = posX;
        this.range = range;
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

    public long getCreationPosX() {
        return creationPosX;
    }

    public int getRange() {
        return range;
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

    /**
     * Checks whether this shot has exceeded its lifetime.
     *
     * <p>
     * If the lifetime is less than or equal to zero, the shot is treated as
     * non-expirable and this method always returns {@code false}.
     * </p>
     *
     * @return {@code true} if the shot has expired; {@code false} otherwise
     */
    public boolean hasExpired() {
        if (this.range <= 0) {
            return false;
        }
        return Math.abs(this.getPosX() - this.creationPosX) >= this.range;
    }

}