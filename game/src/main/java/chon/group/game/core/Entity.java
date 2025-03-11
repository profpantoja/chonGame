package chon.group.game.core;

import java.util.ArrayList;
import java.util.List;

import chon.group.game.messaging.Message;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

public abstract class Entity {

    /** X position (horizontal) of the entity. */
    private int posX;

    /** Y (vertical) position of the entity. */
    private int posY;

    /** Height of the entity. */
    private int height;

    /** Width of the entity. */
    private int width;

    /** entity speed. */
    private int speed;

    /** Image representing the entity. */
    private Image image;

    /** Indicates if the entity is facing left. */
    private boolean flipped = false;

    /** The initial entity's health. */
    private int health;

    /** The maximum entity's health. */
    private int fullHealth;

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
    public Entity(int posX, int posY, int height, int width, int speed, int health, String pathImage) {
        this.posX = posX;
        this.posY = posY;
        this.height = height;
        this.width = width;
        this.speed = speed;
        this.health = health;
        this.fullHealth = health;
        this.image = new Image(getClass().getResource(pathImage).toExternalForm());
    }

    /**
     * Constructor to initialize the entity properties including its direction.
     *
     * @param posX      the entity's initial X (horizontal) position
     * @param posY      the entity's initial Y (vertical) position
     * @param height    the entity's height
     * @param width     the entity's width
     * @param speed     the entity's speed
     * @param health    the entity's health
     * @param pathImage the path to the entity's image
     * @param flipped   the entity's direction (RIGHT=0 or LEFT=1)
     */
    public Entity(int posX, int posY, int height, int width, int speed, int health, String pathImage, boolean flipped) {
        this.posX = posX;
        this.posY = posY;
        this.height = height;
        this.width = width;
        this.speed = speed;
        this.health = health;
        this.fullHealth = health;
        this.image = new Image(getClass().getResource(pathImage).toExternalForm());
        this.flipped = flipped;
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
    public void move(List<String> movements) {
        if (movements.contains("RIGHT")) {
            if (flipped)
                this.flipImage();
            setPosX(posX += speed);
        } else if (movements.contains("LEFT")) {
            if (!flipped)
                this.flipImage();
            setPosX(posX -= speed);
        } else if (movements.contains("UP")) {
            setPosY(posY -= speed);
        } else if (movements.contains("DOWN")) {
            setPosY(posY += speed);
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
            this.move(new ArrayList<String>(List.of("RIGHT")));
        } else if (targetX < this.posX) {
            this.move(new ArrayList<String>(List.of("LEFT")));
        }
        if (targetY > this.posY) {
            this.move(new ArrayList<String>(List.of("DOWN")));
        } else if (targetY < this.posY) {
            this.move(new ArrayList<String>(List.of("UP")));
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

}
