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
   public void drawStatusPanel(int worldX, int worldY, int camX, int screenX, int screenY) {
        Font font = Font.font("Verdana", FontWeight.BOLD, 14);
        gc.setFont(font);
        gc.setFill(Color.BLACK);
        gc.fillText("X: " + worldX, screenX + 5, screenY - 55);
        gc.fillText("Y: " + worldY, screenX + 5, screenY - 40);
        gc.fillText("CamX: " + camX, screenX + 5, screenY - 25);
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

    /**
     * Draws damage numbers that appear when agents take damage.
     * The numbers float upward and fade out over time.
     * 
     * @param fontSize    The font size to be printed.
     * @param opacity     The opacity value from 0 to 1.
     * @param borderColor The border color.
     * @param fillColor   The inside color.
     * @param message     The message to be printed.
     * @param posX        The x-coordinate of the protagonist.
     * @param posY        The y-coordinate of the protagonist.
     */
    public void drawMessages(int fontSize, double opacity, Color borderColor, Color fillColor, String message,
            double posX, double posY) {
        Font damageFont = Font.font("Verdana", FontWeight.BOLD, fontSize);
        gc.setFont(damageFont);

        gc.setGlobalAlpha(opacity);

        gc.setFill(borderColor);
        double offset = 1.5;
        gc.fillText(
                String.valueOf(message),
                posX - offset,
                posY);
        gc.fillText(
                String.valueOf(message),
                posX + offset,
                posY);
        gc.fillText(
                String.valueOf(message),
                posX,
                posY - offset);
        gc.fillText(
                String.valueOf(message),
                posX,
                posY + offset);

        // gc.setFill(Color.rgb(255, 30, 30));
        gc.setFill(fillColor);
        gc.fillText(
                String.valueOf(message),
                posX,
                posY);

        gc.setGlobalAlpha(1.0);
    }
}
