package chon.group.game.core.agent;

import java.util.ArrayList;
import java.util.List;

import chon.group.game.animation.AnimationSet;
import chon.group.game.animation.AnimationState;
import chon.group.game.messaging.Message;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

public abstract class Entity {

    /** X position (horizontal) of the entity. */
    protected int posX;

    /** Y (vertical) position of the entity. */
    protected int posY;

    /** Height of the entity. */
    protected int height;

    /** Width of the entity. */
    protected int width;

    /** entity speed. */
    protected int speed;

    /** Image representing the entity. */
    protected Image image;

    /** Indicates if the entity is facing left. */
    private boolean flipped = false;

    /** The initial entity's health. */
    private int health;

    /** The maximum entity's health. */
    private int fullHealth;

    /** The Entity's movement direction. */
    private Direction direction = Direction.IDLE;

    /** The Entity's status. */
    private EntityStatus status = EntityStatus.IDLE;

    /** Holds all the Frames for each available movement. */
    private AnimationSet animationSet = new AnimationSet();

    /** Holds the current Animation State of the entity. */
    private AnimationState animationState = new AnimationState();

    /** Indicates if the existing bars of life or energy are visible or not. */
    private boolean visibleBars = false;

    /**
     * Constructor to initialize the entity properties.
     *
     * @param posX      the entity's initial X (horizontal) position
     * @param posY      the entity's initial Y (vertical) position
     * @param height    the entity's height
     * @param width     the entity's width
     * @param speed     the entity's speed
     * @param health    the entity's health
     * @param pathImage the path to the entity's image
     */
    public Entity(int posX, int posY, int height, int width, int speed, int health, Direction direction,
            String pathImage, boolean flipped,
            boolean visibleBars) {
        /*
         * Every entity needs at least 1 point of health. Otherwise, it will be
         * considered destroyed and will be removed by the engine.
         */
        if (health <= 0)
            health = 1;
        this.posX = posX;
        this.posY = posY;
        this.height = height;
        this.width = width;
        this.speed = speed;
        this.health = health;
        this.fullHealth = health;
        this.direction = direction;
        this.image = new Image(getClass().getResource(pathImage).toExternalForm());
        this.flipped = flipped;
        this.visibleBars = visibleBars;
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
        return height;
    }

    /**
     * Sets the height of the entity.
     *
     * @param height the new height
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * Gets the width of the entity.
     *
     * @return the width of the entity
     */
    public int getWidth() {
        return width;
    }

    /**
     * Sets the width of the entity.
     *
     * @param width the new width
     */
    public void setWidth(int width) {
        this.width = width;
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
     * Gets the entity image.
     *
     * @return the entity image
     */
    public Image getImage() {
        return image;
    }

    /**
     * Gets the entity flipped status.
     *
     * @param image the new image
     */
    public void setImage(Image image) {
        this.image = image;
    }

    /**
     * Gets if the entity is flipped.
     *
     * @return if the entity is flipped
     */
    public boolean isFlipped() {
        return flipped;
    }

    /**
     * Sets the entity flipped status.
     *
     * @param flipped the new flipped status
     */
    public void setFlipped(boolean flipped) {
        this.flipped = flipped;
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

    /**
     * Flips the Image horizontally.
     */
    public void flipImage() {
        ImageView flippedImage = new ImageView(image);
        flippedImage.setScaleX(-1);
        SnapshotParameters params = new SnapshotParameters();
        params.setFill(Color.TRANSPARENT);
        this.flipped = !this.flipped;
        this.image = flippedImage.snapshot(params, null);
    }

    /**
     * Moves the entity based on the movement commands provided.
     *
     * @param movements a list of movement directions ("RIGHT", "LEFT", "UP",
     *                  "DOWN")
     */
    public void move(List<Direction> movements) {
        if (movements.contains(Direction.RIGHT)) {
            if (flipped)
                this.flipImage();
            if (movements.contains(Direction.RIGHT) && movements.contains(Direction.UP)) {
                setPosY(posY -= speed);
                setPosX(posX += speed);
            } else if (movements.contains(Direction.RIGHT) && movements.contains(Direction.DOWN)) {
                setPosY(posY += speed);
                setPosX(posX += speed);
            } else {
                setPosX(posX += speed);
                setDirection(Direction.RIGHT);
            }
        } else if (movements.contains(Direction.LEFT)) {
            if (!flipped)
                this.flipImage();
            if (movements.contains(Direction.LEFT) && movements.contains(Direction.UP)) {
                setPosY(posY -= speed);
                setPosX(posX -= speed);
            } else if (movements.contains(Direction.LEFT) && movements.contains(Direction.DOWN)) {
                setPosY(posY += speed);
                setPosX(posX -= speed);
            } else {
                setPosX(posX -= speed);
                setDirection(Direction.LEFT);
            }
        } else if (movements.contains(Direction.UP)) {
            setPosY(posY -= speed);
            setDirection(Direction.UP);
        } else if (movements.contains(Direction.DOWN)) {
            setPosY(posY += speed);
            setDirection(Direction.DOWN);
        }
    }

    /**
     * Makes the entity chase a target based on its coordinates.
     *
     * @param targetX the target's X (horizontal) position
     * @param targetY the target's Y (vertical) position
     */
    public void chase(int targetX, int targetY) {
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
    }

    /**
     * Makes the Entity take damage.
     *
     * @param damage the amount of damage to be applied
     */
    public void takeDamage(int damage, List<Message> messages) {
        if (this.getHealth() > 0) {
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

    public boolean isTerminated() {
        return (this.getHealth() <= 0);
    }

}
