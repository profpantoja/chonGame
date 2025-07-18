package chon.group.game;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;

/**
 * The {@code GamePanel} class is responsible for rendering the UI panel that shows
 * the player's health, energy, score, and collectible statistics.
 * It uses JavaFX to draw custom UI elements with visual effects.
 */
public class GamePanel {

    private final GraphicsContext gc;
    private final Image heartIcon;
    private final Image coinIcon;
    private final Image scoreIcon;
    private final Image energyIcon;
    private final Font pixelFont;

    /**
     * Constructs a new GamePanel with the specified graphics context and font.
     *
     * @param gc         The {@code GraphicsContext} used for rendering.
     * @param pixelFont  The pixel-style {@code Font} used for UI text.
     */
    public GamePanel(GraphicsContext gc, Font pixelFont) {
        this.gc = gc;
        this.pixelFont = pixelFont;
        this.heartIcon = loadImage("/images/environment/heart.png");
        this.energyIcon = loadImage("/images/environment/energy.png"); 
        this.coinIcon = loadImage("/images/agents/coin.png");
        this.scoreIcon = loadImage("/images/environment/score.png");
    }

    /**
     * Loads an image from the specified path in the resource directory.
     *
     * @param path the relative path to the image resource
     * @return the loaded {@code Image}
     * @throws RuntimeException if the image is not found
     */
    private Image loadImage(String path) {
        var url = getClass().getResource(path);
        if (url == null) {
            throw new RuntimeException("Image not found: " + path);
        }
        return new Image(url.toExternalForm());
    }

    /**
     * Draws the status panel showing health, energy, score, and collectibles.
     *
     * @param life       current life value
     * @param maxLife    maximum life value
     * @param collected  number of collected items
     * @param total      total collectible items
     * @param score      current score
     * @param energy     current energy value
     * @param maxEnergy  maximum energy value
     */
    public void drawPanel(int life, int maxLife, int collected, int total, int score, double energy, double maxEnergy) {
        drawGlassPanel();
        drawBarsAndIcons(life, maxLife, collected, total, score, energy, maxEnergy);
    }

    /**
     * Draws a semi-transparent glass-style background panel with lighting effects.
     */
    private void drawGlassPanel() {
        gc.setFill(Color.rgb(0, 0, 0, 0.4));
        gc.fillRoundRect(10, 10, 240, 110, 15, 15);

        gc.setStroke(Color.rgb(150, 255, 150, 0.3));
        gc.setLineWidth(1.5);
        gc.strokeRoundRect(10, 10, 240, 110, 15, 15);

        InnerShadow innerShadow = new InnerShadow();
        innerShadow.setColor(Color.rgb(0, 100, 0, 0.4));
        innerShadow.setBlurType(BlurType.GAUSSIAN);
        innerShadow.setRadius(10);
        gc.setEffect(innerShadow);
        gc.fillRoundRect(10, 10, 240, 110, 15, 15);
        gc.setEffect(null);

        DropShadow glow = new DropShadow();
        glow.setColor(Color.rgb(100, 255, 100, 0.2));
        glow.setBlurType(BlurType.GAUSSIAN);
        glow.setRadius(15);
        gc.setEffect(glow);
        gc.strokeRoundRect(10, 10, 240, 110, 15, 15);
        gc.setEffect(null);
    }

    /**
     * Draws health bar, energy bar, collectible count, and score values with icons.
     */
    private void drawBarsAndIcons(int life, int maxLife, int collected, int total, int score, double energy, double maxEnergy) {
        gc.setFont(pixelFont);
        gc.setFill(Color.WHITE);

        int barHeight = 14;
        int iconSize = 24;

        int lifeY = 25;
        gc.drawImage(heartIcon, 30, lifeY + (barHeight - iconSize) / 2, iconSize, iconSize);
        drawLifeBar(life, maxLife, 65, lifeY, 150, barHeight);

        int energyY = 50;
        gc.drawImage(energyIcon, 30, energyY + (barHeight - iconSize) / 2, iconSize, iconSize);
        drawEnergyBar(energy, maxEnergy, 65, energyY, 150, barHeight);

        int itemsY = 90;
        int bigIconSize = 26;

        gc.drawImage(coinIcon, 30, itemsY - (bigIconSize / 2), bigIconSize, bigIconSize);
        drawTextWithBorder(collected + "/" + total, 70, itemsY + 8, Color.WHITE, Color.BLACK);

        gc.setStroke(Color.rgb(100, 255, 100, 0.3));
        gc.setLineWidth(1.2);
        gc.strokeLine(130, itemsY - 8, 130, itemsY + 18);

        gc.drawImage(scoreIcon, 150, itemsY - (bigIconSize / 2), bigIconSize, bigIconSize);
        drawTextWithBorder(String.valueOf(score), 190, itemsY + 8, Color.WHITE, Color.BLACK);
    }

    /**
     * Draws text with a stroke (outline) for better readability.
     *
     * @param text   the text to display
     * @param x      the x-coordinate
     * @param y      the y-coordinate
     * @param fill   the fill color
     * @param stroke the stroke color
     */
    private void drawTextWithBorder(String text, double x, double y, Color fill, Color stroke) {
        gc.setFont(pixelFont);
        gc.setStroke(stroke);
        gc.setLineWidth(2);
        gc.strokeText(text, x, y);
        gc.setFill(fill);
        gc.fillText(text, x, y);
    }

    /**
     * Draws the life (health) bar.
     *
     * @param current current life value
     * @param max     maximum life
     * @param x       x position
     * @param y       y position
     * @param width   bar width
     * @param height  bar height
     */
    private void drawLifeBar(int current, int max, double x, double y, double width, double height) {
        double filled = ((double) current / max) * width;

        gc.setFill(Color.rgb(20, 0, 0, 0.7));
        gc.fillRoundRect(x, y, width, height, 8, 8);

        gc.setFill(Color.rgb(255, 40, 40, 0.9));
        gc.fillRoundRect(x, y, filled, height, 8, 8);

        if (filled > 0) {
            gc.setFill(Color.rgb(255, 120, 120, 0.4));
            gc.fillRect(x, y, filled, height / 2);
        }

        gc.setStroke(Color.rgb(255, 80, 80, 0.5));
        gc.setLineWidth(1.2);
        gc.strokeRoundRect(x, y, width, height, 8, 8);
    }

    /**
     * Draws the energy bar with animated glow effect.
     *
     * @param current current energy value
     * @param max     maximum energy
     * @param x       x position
     * @param y       y position
     * @param width   bar width
     * @param height  bar height
     */
    private void drawEnergyBar(double current, double max, double x, double y, double width, double height) {
        double filled = (current / max) * width;

        gc.setFill(Color.rgb(0, 10, 30, 0.7));
        gc.fillRoundRect(x, y, width, height, 8, 8);

        gc.setFill(Color.rgb(50, 180, 255, 0.9));
        gc.fillRoundRect(x, y, filled, height, 8, 8);

        if (filled > 0) {
            gc.setFill(Color.rgb(120, 220, 255, 0.3));
            for (int i = 0; i < filled; i += 15) {
                gc.fillRect(x + i, y, 8, height);
            }
        }

        gc.setStroke(Color.rgb(100, 220, 255, 0.5));
        gc.setLineWidth(1.2);
        gc.strokeRoundRect(x, y, width, height, 8, 8);
    }
}
