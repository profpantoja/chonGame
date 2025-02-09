package chon.group.game.drawer;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;

/**
 * A utility class for drawing elements on a JavaFX canvas.
 */
public class JavaFxDrawer {
    private final GraphicsContext gc;

    @SuppressWarnings("unused")
    private final EnvironmentDrawer mediator;

    /**
     * Constructs a JavaFxDrawer with the specified graphics context and environment drawer.
     *
     * @param gc       The graphics context used for drawing.
     * @param mediator The environment drawer that manages the environment rendering.
     */
    public JavaFxDrawer(GraphicsContext gc, EnvironmentDrawer mediator) {
        this.gc = gc;
        this.mediator = mediator;
    }

    /**
     * Clears the screen by removing all drawn elements.
     *
     * @param width  The width of the canvas.
     * @param height The height of the canvas.
     */
    public void clearScreen(double width, double height) {
        gc.clearRect(0, 0, width, height);
    }

    /**
     * Draws a shape or image pattern on the canvas.
     *
     * @param fill   The fill color or image pattern.
     * @param x      The x-coordinate of the top-left corner.
     * @param y      The y-coordinate of the top-left corner.
     * @param width  The width of the shape.
     * @param height The height of the shape.
     */
    public void drawImage(Paint fill, double x, double y, double width, double height) {
        if (fill instanceof Color) {
            gc.setFill((Color) fill);
            gc.fillRect(x, y, width, height);
        } else if (fill instanceof ImagePattern) {
            ImagePattern imagePattern = (ImagePattern) fill;
            gc.drawImage(imagePattern.getImage(), x, y, width, height);
        }
    }

    /**
     * Draws an image on the canvas.
     *
     * @param image  The image to be drawn.
     * @param x      The x-coordinate of the top-left corner.
     * @param y      The y-coordinate of the top-left corner.
     * @param width  The width of the image.
     * @param height The height of the image.
     */
    public void drawImage(Image image, double x, double y, double width, double height) {
        gc.drawImage(image, x, y, width, height);
    }

    /**
     * Draws a life bar representing the player's health.
     *
     * @param health      The current health of the player.
     * @param fullHealth  The maximum health of the player.
     * @param width       The total width of the life bar.
     * @param x           The x-coordinate of the life bar.
     * @param y           The y-coordinate of the life bar.
     * @param color       The color of the life bar.
     */
    public void drawLifeBar(double health, double fullHealth, double width, double x, double y, Color color) {
        double barWidth = (health / fullHealth) * width;
        gc.setFill(color);
        gc.fillRect(x, y - 10, barWidth, 5);
    }

    /**
     * Draws the pause screen with a centered pause image.
     *
     * @param pauseImage   The image to display when the game is paused.
     * @param imageWidth   The width of the pause image.
     * @param imageHeight  The height of the pause image.
     * @param canvasWidth  The width of the canvas.
     * @param canvasHeight The height of the canvas.
     */
    public void drawPauseScreen(Image pauseImage, int imageWidth, int imageHeight, double canvasWidth, double canvasHeight) {
        double x = (canvasWidth - imageWidth) / 2;
        double y = (canvasHeight - imageHeight) / 2;
        gc.drawImage(pauseImage, x, y);
    }
}
