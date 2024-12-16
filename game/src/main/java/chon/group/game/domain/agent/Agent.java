package chon.group.game.domain.agent;

import java.util.List;

import chon.group.game.domain.environment.Environment;
import chon.group.game.domain.movement.MovementImpl;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * Represents an agent in the game, with properties such as position, size, speed, and image.
 * The agent can move in specific directions and chase a target.
*/
public class Agent implements MovementImpl {

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

    /** Key mapping for moving up. */
    private String moveUp;

    /** Key mapping for moving down. */
    private String moveDown;

    /** Key mapping for moving left. */
    private String moveLeft;

    /** Key mapping for moving right. */
    private String moveRight;

    /** Key mapping to finish the game. */
    private String finish;

    /**
     * Constructor to initialize the agent properties.
     *
     * @param posX the agent's initial X (horizontal) position
     * @param posY the agent's initial Y (vertical) position
     * @param height the agent's height
     * @param width the agent's width
     * @param speed the agent's speed
     * @param pathImage the path to the agent's image
     */
    public Agent(int posX, int posY, int height, int width, int speed, String pathImage) {
        this.posX = posX;
        this.posY = posY;
        this.height = height;
        this.width = width;
        this.speed = speed;
        this.image = new Image(getClass().getResource(pathImage).toExternalForm());
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
     * Gets the agent image.
     *
     * @return the agent image
     */
    public Image getImage() {
        return image;
    }

    /**
     * Sets the agent image.
     *
     * @param image the new image
     */
    public void setImage(Image image) {
        this.image = image;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUp() {
        return moveUp;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDown() {
        return moveDown;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getLeft() {
        return moveLeft;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getRight() {
        return moveRight;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFinish() {
        return finish;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setUp(String up) {
        this.moveUp = up;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDown(String down) {
        this.moveDown = down;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setLeft(String left) {
        this.moveLeft = left;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setRight(String right) {
        this.moveRight = right;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setFinish(String finish) {
        this.finish = finish;
    }

    /**
     * Moves the agent based on the movement commands provided.
     *
     * @param movements a list of movement directions ("RIGHT", "LEFT", "UP", "DOWN")
     */
    public void move(List<String> movements, Agent agent, Environment environment, Stage theStage) {
        if (movements.contains(getRight())) {
            if ((posX + width) + speed < (environment.getWidth()))
                setPosX(posX += speed);
            else
                setPosX(environment.getWidth() - width);
        } else if (movements.contains(getLeft())) {
            if (posX - speed > 0)
                setPosX(posX -= speed);
            else
                posX = 0;
        } else if (movements.contains(getUp())) {
            if (posY - speed > 20)
                setPosY(posY -= speed);
            else
                posY = 20;
        } else if (movements.contains(getDown())) {
            if ((posY + height) + speed < environment.getHeight() - 45)
                setPosY(posY += speed);
            else
                setPosY((environment.getHeight() - height) - 45);
        } else if (movements.contains(getFinish())) {
            theStage.close();
        }
    }

    /**
     * Makes the agent chase a target based on its coordinates.
     *
     * @param targetX the target's X (horizontal) position
     * @param targetY the target's Y (vertical) position
     */
    /*
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
    */
}
