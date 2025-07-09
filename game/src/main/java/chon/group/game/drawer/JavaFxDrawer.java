package chon.group.game.drawer;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * The {@code JavaFxDrawer} class is responsible for rendering various elements
 * of the game environment using JavaFX. It provides methods to draw images,
 * life bars, status panels, and the pause screen.
 */
public class JavaFxDrawer {

    /** The graphics context used to render the environment. */
    private final GraphicsContext gc;
    /** The mediator instance, if necessary. */
    @SuppressWarnings("unused")
    private final EnvironmentDrawer mediator;

    /**
     * Constructor to initialize the JavaFxDrawer.
     *
     * @param gc       The GraphicsContext instance used for rendering.
     * @param mediator The mediator that manages the environment.
     */
    public JavaFxDrawer(GraphicsContext gc, EnvironmentDrawer mediator) {
        this.gc = gc;
        this.mediator = mediator;
    }

    /**
     * Clears the canvas area, removing previously drawn elements.
     *
     * @param width  The width of the area to clear.
     * @param height The height of the area to clear.
     */
    public void clearScreen(int width, int height) {
        this.gc.clearRect(0, 0, width, height);
    }

    /**
     * Renders an image at the specified position and dimensions.
     *
     * @param image  The image to be drawn.
     * @param posX   The x-coordinate position.
     * @param posY   The y-coordinate position.
     * @param width  The width of the image.
     * @param height The height of the image.
     */
    public void drawImage(Image image, int posX, int posY, int width, int height) {
        this.gc.drawImage(image, posX, posY, width, height);
    }

    /**
     * Renders the protagonist's life bar.
     *
     * @param health     The current health value.
     * @param fullHealth The maximum health value.
     * @param width      The width of the life bar.
     * @param posX       The x-coordinate position.
     * @param posY       The y-coordinate position.
     * @param color      The color of the life bar.
     */
    public void drawLifeBar(int health, int fullHealth, int width, int posX, int posY, Color color) {
        int borderThickness = 2;
        int barHeight = 5;
        int lifeSpan = Math.round((float) ((health * 100 / fullHealth) * width) / 100);
        int barY = 15;

        this.gc.setFill(Color.BLACK);
        this.gc.fillRect(posX, posY - barY, width, barHeight + (borderThickness * 2));

        this.gc.setFill(color);
        this.gc.fillRect(posX + borderThickness,
                posY - (barY - borderThickness),
                (lifeSpan - (borderThickness * 2)),
                barHeight);
    }

    /**
     * Displays a status panel showing the protagonist's coordinates.
     *
     * @param posX The x-coordinate of the protagonist.
     * @param posY The y-coordinate of the protagonist.
     */
    public void drawStatusPanel(int posX, int posY) {
        Font theFont = Font.font("Verdana", FontWeight.BOLD, 14);
        this.gc.setFont(theFont);
        this.gc.setFill(Color.BLACK);
        this.gc.fillText("X: " + posX, posX + 10, posY - 40);
        this.gc.fillText("Y: " + posY, posX + 10, posY - 25);
    }

    /**
     * Renders the pause screen, centering the pause image within the environment.
     *
     * @param image       The image representing the pause screen.
     * @param imageWidth  The width of the pause image.
     * @param imageHeight The height of the pause image.
     * @param width       The total width of the environment.
     * @param height      The total height of the environment.
     */
    public void drawScreen(Image image, int imageWidth, int imageHeight, int width, int height) {
        if (image != null && this.gc != null) {
            double centerX = (width - imageWidth) / 2;
            double centerY = (height - imageHeight) / 2;
            this.gc.drawImage(image, centerX, centerY);
        }
    }

}
