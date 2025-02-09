package chon.group.game.domain.agent;

import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

/**
 * Represents an agent in the Air Hockey game, including paddles and a puck.
 */
public class Agent {
    private Circle paddle1;
    private Circle paddle2;
    private Circle puck;

    /**
     * Creates an Agent with paddles and a puck of specified radii.
     * 
     * @param paddleRadius The radius of the paddles.
     * @param puckRadius   The radius of the puck.
     */
    public Agent(double paddleRadius, double puckRadius) {
        paddle1 = new Circle(paddleRadius, Color.BLUE);
        paddle2 = new Circle(paddleRadius, Color.RED);
        puck = new Circle(puckRadius, Color.BLACK);
    }

    /**
     * Gets the first paddle.
     * 
     * @return The first paddle (Circle object).
     */
    public Circle getPaddle1() {
        return paddle1;
    }

    /**
     * Gets the second paddle.
     * 
     * @return The second paddle (Circle object).
     */
    public Circle getPaddle2() {
        return paddle2;
    }

    /**
     * Gets the puck.
     * 
     * @return The puck (Circle object).
     */
    public Circle getPuck() {
        return puck;
    }

    /**
     * Sets an image as the texture for the first paddle.
     * 
     * @param imagePattern The image pattern to be applied.
     */
    public void setPaddle1Image(ImagePattern imagePattern) {
        paddle1.setFill(imagePattern);
    }

    /**
     * Sets an image as the texture for the second paddle.
     * 
     * @param imagePattern The image pattern to be applied.
     */
    public void setPaddle2Image(ImagePattern imagePattern) {
        paddle2.setFill(imagePattern);
    }

    /**
     * Sets an image as the texture for the puck.
     * 
     * @param imagePattern The image pattern to be applied.
     */
    public void setPuckImage(ImagePattern imagePattern) {
        puck.setFill(imagePattern);
    }

    /**
     * Resets the puck position to the specified coordinates.
     * 
     * @param x The x-coordinate.
     * @param y The y-coordinate.
     */
    public void resetPuckPosition(double x, double y) {
        puck.setTranslateX(x);
        puck.setTranslateY(y);
    }
}