package chon.group.game.domain.agent;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.SnapshotParameters;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

/**
 * Represents an agent in the game, with properties such as position, size,
 * speed, and image.
 * The agent can move in specific directions and chase a target.
 */
public class Agent {

    /** X position (horizontal) of the agent. */
    private int posX;

    /** Y (vertical) position of the agent. */
    private int posY;

    /** Height of the agent. */
    private int height;

    /** Width of the agent. */
    private int width;

    /** Agent speed. */
    private int speed;

    /** Image representing the agent. */
    private Image image;

    /** Indicates if the agent is facing left. */
    private boolean flipped = false;

    /** The initial agent's health. */
    private int health;

    /** The maximum agent's health. */
    private int fullHealth;

    /**
     * Constructor to initialize the agent properties.
     *
     * @param posX      the agent's initial X (horizontal) position
     * @param posY      the agent's initial Y (vertical) position
     * @param height    the agent's height
     * @param width     the agent's width
     * @param speed     the agent's speed
     * @param health    the agent's health
     * @param pathImage the path to the agent's image
     */
    public Agent(int posX, int posY, int height, int width, int speed, int health, String pathImage) {
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
     * Constructor to initialize the agent properties including its direction.
     *
     * @param posX      the agent's initial X (horizontal) position
     * @param posY      the agent's initial Y (vertical) position
     * @param height    the agent's height
     * @param width     the agent's width
     * @param speed     the agent's speed
     * @param health    the agent's health
     * @param pathImage the path to the agent's image
     * @param flipped   the agent's direction (RIGHT=0 or LEFT=1)
     */
    public Agent(int posX, int posY, int height, int width, int speed, int health, String pathImage, boolean flipped) {
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
     * Gets the X (horizontal) position of the agent.
     *
     * @return the X (horizontal) position of the agent
     */
    public int getPosX() {
        return posX;
    }

    /**
     * Sets the agent's X (horizontal) position.
     *
     * @param posX the new X (horizontal) position
     */
    public void setPosX(int posX) {
        this.posX = posX;
    }

    /**
     * Gets the Y (vertical) position of the agent.
     *
     * @return the Y (vertical) position of the agent
     */
    public int getPosY() {
        return posY;
    }

    /**
     * Sets the Y (vertical) position of the agent.
     *
     * @param posY the new Y (vertical) position
     */
    public void setPosY(int posY) {
        this.posY = posY;
    }

    /**
     * Gets the height of the agent.
     *
     * @return the height of the agent
     */
    public int getHeight() {
        return height;
    }

    /**
     * Sets the height of the agent.
     *
     * @param height the new height
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * Gets the width of the agent.
     *
     * @return the width of the agent
     */
    public int getWidth() {
        return width;
    }

    /**
     * Sets the width of the agent.
     *
     * @param width the new width
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * Gets the agent's speed.
     *
     * @return the agent's speed
     */
    public int getSpeed() {
        return speed;
    }

    /**
     * Sets the speed of the agent.
     *
     * @param speed the new speed
     */
    public void setSpeed(int speed) {
        this.speed = speed;
    }

    /**
     * Gets the agent's health.
     *
     * @return the agent's health
     */
    public int getHealth() {
        return health;
    }

    /**
     * Sets the health of the agent.
     *
     * @param health the new health
     */
    public void setHealth(int health) {
        this.health = health;
    }

    /**
     * Gets the agent's maximum health.
     *
     * @return the agent's maximum health
     */
    public int getFullHealth() {
        return fullHealth;
    }

    /**
     * Sets the maximum health of the agent.
     *
     * @param fullHealth the new maximum health
     */
    public void setFullHealth(int fullHealth) {
        this.fullHealth = fullHealth;
    }

    /**
     * Gets the agent image.
     *
     * @return the agent image
     */
    public Image getImage() {
        return image;
    }

    /**
     * Gets the agent flipped status.
     *
     * @param image the new image
     */
    public void setImage(Image image) {
        this.image = image;
    }

    public boolean isFlipped() {
        return flipped;
    }

    /**
     * Sets the agent flipped status.
     *
     * @param image the new image
     */
    public void setFlipped(boolean flipped) {
        this.flipped = flipped;
    }

    /**
     * Flips the Image horizontally.
     */
    private void flipImage() {
        ImageView flippedImage = new ImageView(image);
        flippedImage.setScaleX(-1);
        SnapshotParameters params = new SnapshotParameters();
        params.setFill(Color.TRANSPARENT);
        this.flipped = !this.flipped;
        this.image = flippedImage.snapshot(params, null);
    }

    /**
     * Moves the agent based on the movement commands provided.
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
     * Makes the agent chase a target based on its coordinates.
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

}
