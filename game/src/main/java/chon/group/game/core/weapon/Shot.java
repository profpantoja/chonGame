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
    private long creationTime;
    /** Duration in milliseconds that the shot/hit is effectible. */
    private long lifetime = 0;

    public Shot(
            int posX,
            int posY,
            int height,
            int width,
            int speed,
            int health,
            Direction direction,
            boolean flipped,
            int damage) {
        super(posX, posY, height, width, speed, health, direction, flipped, false);
        this.damage = damage;
        this.creationTime = System.currentTimeMillis();
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

    public long getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(long creationTime) {
        this.creationTime = creationTime;
    }

    public long getLifetime() {
        return lifetime;
    }

    public void setLifetime(long lifetime) {
        this.lifetime = lifetime;
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
        if (this.lifetime <= 0) {
            return false;
        }
        long currentTime = System.currentTimeMillis();
        long age = currentTime - this.creationTime;
        return age > this.lifetime;
    }

}