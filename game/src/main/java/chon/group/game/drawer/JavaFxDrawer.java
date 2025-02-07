package chon.group.game.drawer;

import java.util.List;

import chon.group.game.domain.item.FallingItem;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class JavaFxDrawer {

    /** The graphics context used to render the environment. */
    private final GraphicsContext gc;
    private final EnvironmentDrawer mediator;

    /**
     * Constructor to initialize the JavaFx Drawer.
     *
     * @param gc the GraphicsContext instance
     */
    public JavaFxDrawer(GraphicsContext gc, EnvironmentDrawer mediator) {
        this.gc = gc;
        this.mediator = mediator;
    }

    /**
     * Clears the canvas area, removing previously drawn elements.
     */
    public void clearScreen(int width, int height) {
        this.gc.clearRect(0, 0, width, height);
    }

    /**
     * Renders the environment's background on the graphics context.
     */
    public void drawImage(Image image, int posX, int posY, int width, int height) {
        this.gc.drawImage(image, posX, posY, width, height);
    }

    /**
     * Renders the Protagonist's Life Bar.
     */
    public void drawLifeBar(int health, int fullHealth, int width, int posX, int posY, Color color) {
        /* The border's thickness. */
        int borderThickness = 2;
        /* The bar's height. */
        int barHeight = 5;
        /* The life span proportion calculated based on actual and maximum health. */
        int lifeSpan = Math.round(
                (float) ((health * 100 / fullHealth)
                        * width) / 100);
        /* Int points before the agent's y position. The initial bar's position. */
        int barY = 15;
        /* The outside background of the health bar. */
        this.gc.setFill(Color.BLACK);
        /* The height is a little bit bigger to give a border experience. */
        this.gc.fillRect(posX,
                posY - barY,
                width,
                barHeight + (borderThickness * 2));
        /**
         * The inside of the health bar. It is the effective life of the agent.
         * The border height plus the thickness multiplied by two (beggining and end at
         * X).
         */
        this.gc.setFill(color);
        /**
         * The initial position considering the border from both X and Y points.
         * The life span less the border thickness multiplied by two (beggining and end
         * at Y).
         */
        this.gc.fillRect(posX + borderThickness,
                posY - (barY - borderThickness),
                (lifeSpan - (borderThickness * 2)),
                barHeight);
    }

    /**
     * Displays a status panel showing the protagonist's coordinates.
     *
     * @param agent the protagonist whose information will be displayed
     */
    public void drawStatusPanel(int posX, int posY) {
        Font theFont = Font.font("Verdana", FontWeight.BOLD, 14);
        this.gc.setFont(theFont);
        this.gc.setFill(Color.BLACK);
        this.gc.fillText("X: " + posX, posX + 10, posY - 40);
        this.gc.fillText("Y: " + posY, posX + 10, posY - 25);
    }

    /**
     * Renders the Game Paused Screen.
     */
    public void drawPauseScreen(Image image, int imageWidth, int imageHeight, int width, int height) {
        if (image != null && this.gc != null) {
            double centerX = (width - imageWidth) / 2;
            double centerY = (height - imageHeight) / 2;
            /* Draw image on the center of screen */
            this.gc.drawImage(image, centerX, centerY);
        }
    }

    // Adiciona os itens que caem do céu
    public void drawFallingItems(List<FallingItem> items) {
        for (FallingItem item : items) {
            this.gc.drawImage(item.getCachedImage(),
                    item.getPosX(),
                    item.getPosY(),
                    item.getWidth(),
                    item.getHeight());
        }
    }

    public void drawScore(int score) {
        this.gc.setFill(Color.WHITE);
        this.gc.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
        this.gc.fillText("Score: " + score, 10, 30);
    }

}
