package chon.group.game.core.agent;

import java.util.ArrayList;
import java.util.List;

import chon.group.game.animation.AnimationSet;
import chon.group.game.animation.AnimationState;
import chon.group.game.messaging.Message;
import chon.group.game.sound.Sound;
import chon.group.game.sound.SoundEvent;
import chon.group.game.sound.SoundSet;

public abstract class Entity {

    /** X position (horizontal) of the entity. */
    private int posX;

    /** Y (vertical) position of the entity. */
    private int posY;

    /** Anchor Width of the entity. */
    private int widthOffset;

    /** entity speed. */
    private int speed;

    /** The initial entity's health. */
    private int health;

    /** The maximum entity's health. */
    private int fullHealth;

    /** The Entity's movement direction. */
    private Direction direction = Direction.IDLE;

    /** The Entity's status. */
    private EntityStatus status = EntityStatus.IDLE;

    /** Indicates if the existing bars of life or energy are visible or not. */
    private boolean visibleBars = false;

    /** The hitbox for collision mechanics. */
    private Hitbox hitbox;

    /** Holds all the Frames for each available movement. */
    private AnimationSet animationSet = new AnimationSet();

    /** Holds the current Animation State of the entity. */
    private AnimationState animationState = new AnimationState();

    /** Holds all the sound for each available event. */
    private SoundSet soundSet = new SoundSet();

    /**
     * Constructor to initialize the entity properties.
     *
     * @param posX      the entity's initial X (horizontal) position
     * @param posY      the entity's initial Y (vertical) position
     * @param speed     the entity's speed
     * @param health    the entity's health
     * @param pathImage the path to the entity's image
     */
    public Entity(int posX, int posY, int width, int height, int speed, int health, Direction direction,
            boolean flipped,
            boolean visibleBars) {
        /*
         * Every entity needs at least 1 point of health. Otherwise, it will be
         * considered destroyed and will be removed by the engine.
         */
        if (health <= 0)
            health = 1;
        this.posX = posX;
        this.posY = posY;
        this.widthOffset = width;
        this.speed = speed;
        this.health = health;
        this.fullHealth = health;
        this.direction = direction;
        this.getAnimationState().setFlipped(flipped);
        this.visibleBars = visibleBars;
        this.hitbox = new Hitbox(width, (int) (height * 0.4));
    }

    /**
     * Gets the X (horizontal) position of the entity.
     *
     * @return the X (horizontal) position of the entity
     */
    public int getPosX() {
        return posX;
    }

    /**
     * Sets the entity's X (horizontal) position.
     *
     * @param posX the new X (horizontal) position
     */
    public void setPosX(int posX) {
        this.posX = posX;
    }

    /**
     * Gets the Y (vertical) position of the entity.
     *
     * @return the Y (vertical) position of the entity
     */
    public int getPosY() {
        return posY;
    }

    /**
     * Sets the Y (vertical) position of the entity.
     *
     * @param posY the new Y (vertical) position
     */
    public void setPosY(int posY) {
        this.posY = posY;
    }

    /**
     * Gets the height of the entity.
     *
     * @return the height of the entity
     */
    public int getHeight() {
        if (this.isVisible())
            return this.getAnimationState()
                    .getCurrentAnimation()
                    .getFrames()
                    .get(this.getAnimationState().getCurrentFrameIndex())
                    .getHeight();
        return this.getHitbox().getHeight();
    }

    /**
     * Gets the width of the entity.
     *
     * @return the width of the entity
     */
    public int getWidth() {
        if (this.isVisible())
            return this.getAnimationState()
                    .getCurrentAnimation()
                    .getFrames()
                    .get(this.getAnimationState().getCurrentFrameIndex())
                    .getWidth();
        else
            return this.getHitbox().getWidth();
    }

    /**
     * Gets the width anchor (offset) of the entity.
     *
     * @return the width anchor (offset) of the entity
     */
    public int getWidthOffset() {
        return this.widthOffset;
    }

    /**
     * Gets the entity's speed.
     *
     * @return the entity's speed
     */
    public int getSpeed() {
        return speed;
    }

    /**
     * Sets the speed of the entity.
     *
     * @param speed the new speed
     */
    public void setSpeed(int speed) {
        this.speed = speed;
    }

    /**
     * Gets the entity's health.
     *
     * @return the entity's health
     */
    public int getHealth() {
        return health;
    }

    /**
     * Sets the health of the entity.
     *
     * @param health the new health
     */
    public void setHealth(int health) {
        this.health = health;
    }

    /**
     * Gets the entity's maximum health.
     *
     * @return the entity's maximum health
     */
    public int getFullHealth() {
        return fullHealth;
    }

    /**
     * Sets the maximum health of the entity.
     *
     * @param fullHealth the new maximum health
     */
    public void setFullHealth(int fullHealth) {
        this.fullHealth = fullHealth;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public EntityStatus getStatus() {
        return status;
    }

    public void setStatus(EntityStatus status) {
        this.status = status;
    }

    public boolean isVisibleBars() {
        return visibleBars;
    }

    public void setVisibleBars(boolean visible) {
        this.visibleBars = visible;
    }

    public Hitbox getHitbox() {
        return hitbox;
    }

    public void setHitbox(Hitbox hitbox) {
        this.hitbox = hitbox;
    }

    public AnimationSet getAnimationSet() {
        return animationSet;
    }

    public void setAnimationSet(AnimationSet animationSet) {
        this.animationSet = animationSet;
    }

    public AnimationState getAnimationState() {
        return animationState;
    }

    public void setAnimationState(AnimationState animationState) {
        this.animationState = animationState;
    }

    public SoundSet getSoundSet() {
        return soundSet;
    }

    public void setSoundSet(SoundSet soundSet) {
        this.soundSet = soundSet;
    }

    /**
     * Moves the entity based on the movement commands provided.
     *
     * @param movements a list of movement directions ("RIGHT", "LEFT", "UP",
     *                  "DOWN")
     */
    public void move(List<Direction> movements) {
        /* Removes contradictory movements: RIGHT+LEFT and UP+DOWN. */
        if (movements.contains(Direction.RIGHT) && movements.contains(Direction.LEFT)) {
            movements.remove(Direction.RIGHT);
            movements.remove(Direction.LEFT);
        }
        if (movements.contains(Direction.UP) && movements.contains(Direction.DOWN)) {
            movements.remove(Direction.DOWN);
        }
        /* It verifies if the agent intends to move to the right. */
        if (movements.contains(Direction.RIGHT)) {
            if (this.animationState.isFlipped()) {
                this.animationState.setFlipped(false);
            }
            /* It verifies if it is an up-diagonal movement. */
            if (movements.contains(Direction.RIGHT) && movements.contains(Direction.UP)) {
                setPosY(posY -= speed);
                setPosX(posX += speed);
            }
            /* It verifies if it is a down-diagonal movement. */
            else if (movements.contains(Direction.RIGHT) && movements.contains(Direction.DOWN)) {
                setPosY(posY += speed);
                setPosX(posX += speed);
            }
            /* Otherwise, it only has to move to the right. */
            else {
                setPosX(posX += speed);
                setDirection(Direction.RIGHT);
            }
        }
        /* It verifies if the agent intends to move to the left. */
        if (movements.contains(Direction.LEFT)) {
            if (!this.animationState.isFlipped()) {
                this.animationState.setFlipped(true);
            }
            /* It verifies if it is an up-diagonal movement. */
            if (movements.contains(Direction.LEFT) && movements.contains(Direction.UP)) {
                setPosY(posY -= speed);
                setPosX(posX -= speed);
            }
            /* It verifies if it is a down-diagonal movement. */
            else if (movements.contains(Direction.LEFT) && movements.contains(Direction.DOWN)) {
                setPosY(posY += speed);
                setPosX(posX -= speed);
            }
            /* Otherwise, it only has to move to the right. */
            else {
                setPosX(posX -= speed);
                setDirection(Direction.LEFT);
            }
        }
        /* It verifies if the agent intends to move up. */
        else if (movements.contains(Direction.UP)) {
            setPosY(posY -= speed);
            setDirection(Direction.UP);
        }
        /* It verifies if the agent intends to move down. */
        else if (movements.contains(Direction.DOWN)) {
            setPosY(posY += speed);
            setDirection(Direction.DOWN);
        }
        this.status = EntityStatus.WALK;
    }

    /**
     * Makes the entity chase a target based on its coordinates.
     *
     * @param targetX the target's X (horizontal) position
     * @param targetY the target's Y (vertical) position
     */
    public void chase(int targetX, int targetY) {
        if (!this.isTerminated()) {
            if (targetX > this.posX) {
                this.move(new ArrayList<Direction>(List.of(Direction.RIGHT)));
            } else if (targetX < this.posX) {
                this.move(new ArrayList<Direction>(List.of(Direction.LEFT)));
            }
            if (targetY > this.posY) {
                this.move(new ArrayList<Direction>(List.of(Direction.DOWN)));
            } else if (targetY < this.posY) {
                this.move(new ArrayList<Direction>(List.of(Direction.UP)));
            }
            this.status = EntityStatus.WALK;
        }
    }

    /**
     * Makes the Entity take damage.
     *
     * @param damage the amount of damage to be applied
     */
    public void takeDamage(int damage, List<Message> messages, List<Sound> sounds) {
        if (this.getHealth() > 0) {
            /* Decrease health. */
            this.setHealth(this.getHealth() - damage);
            messages.add(new Message(
                    String.valueOf(damage),
                    this.getPosX(),
                    this.getPosY(),
                    25));
            this.setStatus(EntityStatus.DAMAGE);
            Sound sound = this.getSoundSet().get(SoundEvent.DAMAGE);
            if (sound != null)
                sounds.add(sound);
            /* After taking the damage, the health must not be negative. */
            if (this.getHealth() <= 0) {
                this.setHealth(0);
                this.terminate();
                sound = this.getSoundSet().get(SoundEvent.TERMINATE);
                if (sound != null)
                    sounds.add(sound);
            }
        }
    }

    private void terminate() {
        this.status = EntityStatus.TERMINATE;
        this.animationState.setCurrentFrameIndex(0);
    }

    public boolean isTerminated() {
        return (this.getHealth() <= 0);
    }

    public boolean canRemove() {
        return (this.getHealth() <= 0 && this.animationState.isFinished());
    }

    public void idle() {
        this.status = EntityStatus.IDLE;
    }

    /**
     * Gets the width of the entity.
     *
     * @return the width of the entity
     */
    public int getFlippedWidth() {
        int width = this.getAnimationState()
                .getCurrentAnimation()
                .getFrames()
                .get(this.getAnimationState()
                        .getCurrentFrameIndex())
                .getWidth();
        if (this.animationState.isFlipped())
            return -width;
        return width;
    }

    /**
     * Gets the X (horizontal) position of the entity.
     *
     * @return the X (horizontal) position of the entity
     */
    public int getFlippedPosX() {
        if (this.animationState.isFlipped()) {
            return posX + this.widthOffset;
        }
        return posX;
    }

    public boolean isVisible() {
        if (animationState == null ||
                animationState.getCurrentAnimation() == null ||
                animationState.getCurrentAnimation().getFrames() == null)
            return false;
        return true;
    }

}
