package chon.group.game.drawer.client;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

/**
 * The {@code JavaFxDrawer} class is responsible for rendering various elements
 * of the game environment using JavaFX. It provides methods to draw images,
 * life bars, status panels, and the pause screen.
 */
public class JavaFxDrawer extends Drawer {

    /** The graphics context used to render the environment. */
    private final GraphicsContext gc;
    private final Font theFont = Font.font("Verdana", FontWeight.BOLD, 14);

    /**
     * Constructor to initialize the JavaFxDrawer.
     *
     * @param gc       The GraphicsContext instance used for rendering.
     * @param mediator The mediator that manages the environment.
     */
    public JavaFxDrawer(GraphicsContext gc) {
        this.gc = gc;
    }

    @Override
    public void clearScreen(int width, int height) {
        this.gc.clearRect(0, 0, width, height);
    }

    @Override
    public void drawImage(Image image, int posX, int posY, int width, int height) {
        this.gc.drawImage(image, posX, posY, width, height);
    }

    @Override
    public void drawLifeBar(int health, int fullHealth, int width, int posX, int posY, Color color) {
        int borderThickness = 2;
        int barHeight = 5;
        int lifeSpan = Math.round((float) ((health * 100 / fullHealth) * width) / 100);
        int barY = 19;

        this.gc.setFill(Color.BLACK);
        this.gc.fillRect(posX, posY - barY, width, barHeight + (borderThickness * 2));

        this.gc.setFill(color);
        this.gc.fillRect(posX + borderThickness,
                posY - (barY - borderThickness),
                (lifeSpan - (borderThickness * 2)),
                barHeight);
    }

    @Override
    public void drawEnergyBar(double energy, double fullEnergy, int width, int posX, int posY, Color color) {
        int borderThickness = 2;
        int barHeight = 5;
        int energyWidth = (int) ((energy / fullEnergy) * width);
        int barY = 12; // Posicionado abaixo da barra de vida

        // Fundo da barra
        this.gc.setFill(Color.BLACK);
        this.gc.fillRect(posX, posY - barY, width, barHeight + (borderThickness * 2));

        // Barra de energia (com gradiente de cor)
        Color energyColor = Color.color(
                color.getRed(),
                color.getGreen(),
                color.getBlue(),
                0.7 // Slightly transparent
        );
        this.gc.setFill(energyColor);
        this.gc.fillRect(posX + borderThickness,
                posY - (barY - borderThickness),
                (energyWidth - (borderThickness * 2)),
                barHeight);
    }

    @Override
    public void drawPanel(int life, int maxLife, int collected, int total, int score, double energy, double maxEnergy,
            Font pixelFont, Image lifeIcon, Image energyIcon, Image itemIcon, Image scoreIcon, int panelWidth,
            int panelHeight) {
        drawGlassPanel(panelWidth, panelHeight, 10, 10);
        drawBarsAndIcons(life, maxLife, collected, total, score, energy, maxEnergy, pixelFont, lifeIcon, energyIcon,
                itemIcon, scoreIcon);
    }

    @Override
    public void drawDebugPanel(int panelWidth, int panelHeight, double camX, int messages, int shots) {
        int span = 20;
        drawGlassPanel(panelWidth, panelHeight, 10, 130);
        this.gc.fillText("CamX: " + camX, 20, 155);
        this.gc.fillText("Messages: " + messages, 20, 155 + span);
        this.gc.fillText("Shots: " + shots, 20, 155 + (2 * span));
        drawTextWithBorder("CamX: " + camX, 20, 155, Color.WHITE, Color.BLACK, null);
        drawTextWithBorder("Messages: " + messages, 20, 155 + span, Color.WHITE,
                Color.BLACK, null);
        drawTextWithBorder("Shots: " + shots, 20, 155 + (2 * span), Color.WHITE,
                Color.BLACK, null);
    }

    /**
     * Draws a semi-transparent glass-style background panel with lighting effects.
     */
    private void drawGlassPanel(int panelWidth, int panelHeight, int posX, int posY) {
        gc.setFill(Color.rgb(0, 0, 0, 0.65));
        gc.fillRoundRect(posX, posY, panelWidth, panelHeight, 15, 15);
        gc.setStroke(Color.rgb(150, 255, 150, 0.15));
        gc.setLineWidth(3.0);
        gc.strokeRoundRect(posX, posY, panelWidth, panelHeight, 15, 15);
    }

    /**
     * Draws health bar, energy bar, collectible count, and score values with icons.
     */
    private void drawBarsAndIcons(int life, int maxLife, int collected, int total, int score, double energy,
            double maxEnergy, Font pixelFont, Image lifeIcon, Image energyIcon, Image itemIcon, Image scoreIcon) {
        gc.setFont(pixelFont);
        gc.setFill(Color.WHITE);

        int barHeight = 14;
        int iconSize = 24;

        int lifeY = 25;
        gc.drawImage(lifeIcon, 30, lifeY + (barHeight - iconSize) / 2, iconSize, iconSize);
        drawLifeBar(life, maxLife, 65, lifeY, 150, barHeight);

        int energyY = 50;
        gc.drawImage(energyIcon, 30, energyY + (barHeight - iconSize) / 2, iconSize, iconSize);
        drawEnergyBar(energy, maxEnergy, 65, energyY, 150, barHeight);

        int itemsY = 90;
        int bigIconSize = 26;

        gc.drawImage(itemIcon, 30, itemsY - (bigIconSize / 2), bigIconSize, bigIconSize);
        drawTextWithBorder(collected + "/" + total, 70, itemsY + 8, Color.WHITE, Color.BLACK, null);

        gc.setStroke(Color.rgb(100, 255, 100, 0.3));
        gc.setLineWidth(1.2);
        gc.strokeLine(130, itemsY - 8, 130, itemsY + 18);

        gc.drawImage(scoreIcon, 150, itemsY - (bigIconSize / 2), bigIconSize, bigIconSize);
        drawTextWithBorder(String.valueOf(score), 190, itemsY + 8, Color.WHITE, Color.BLACK, null);
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
    private void drawTextWithBorder(String text, double x, double y, Color fill, Color stroke, Font pixelFont) {
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

    @Override
    public void drawEntityPanel(int posX, int posY, int camX, int height, String direction, String entityStatus,
            boolean blocked, boolean finished) {
        int span = 15;
        int barSpan = 10;
        int glassWidth = 85;
        this.gc.setFont(theFont);

        /* Panel above the Entity. */
        drawGlassPanel(glassWidth,
                35,
                (posX - camX) - 5,
                posY - (barSpan + (3 * span)));

        /* Info above the Entity. */
        this.gc.setFill(Color.WHITE);
        this.gc.fillText("X: " + posX, (posX - camX), posY - (barSpan + (2 * span)));
        this.gc.fillText("Y: " + posY, (posX - camX), posY - (barSpan + (span)));
        // drawTextWithBorder("X: " + posX, (posX - camX), posY - (barSpan + (2 *
        // span)), Color.WHITE, Color.BLACK, null);
        // drawTextWithBorder("Y: " + posY, (posX - camX), posY - (barSpan + (span)),
        // Color.WHITE, Color.BLACK, null);

        if (entityStatus.equals("TERMINATE"))
            glassWidth = 135;
        else
            glassWidth = 110;
        /* Panel bellow the Entity. */
        drawGlassPanel(glassWidth,
                65,
                (posX - camX) - 5,
                posY + height + 5);

        /* Info bellow the Entity. */
        this.gc.setFill(Color.WHITE);
        this.gc.fillText("St.: " + entityStatus, (posX - camX), posY + height + span + 5);
        this.gc.fillText("Dir.: " + direction, (posX - camX), posY + height + (2 * span) + 5);
        this.gc.fillText("Block.: " + blocked, (posX - camX), posY + height + (3 * span) + 5);
        this.gc.fillText("Finish.: " + finished, (posX - camX), posY + height + (4 * span) + 5);
    }

    public void drawScreen(Image image, int imageWidth, int imageHeight, int width, int height) {
        if (image != null && this.gc != null) {
            double centerX = (width - imageWidth) / 2;
            double centerY = (height - imageHeight) / 2;
            this.gc.drawImage(image, centerX, centerY);
        }
    }

    @Override
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

        gc.setFill(fillColor);
        gc.fillText(
                String.valueOf(message),
                posX,
                posY);

        gc.setGlobalAlpha(1.0);
    }

    @Override
    public void drawHighlight(double x, double y, double width, double height) {
        gc.setStroke(Color.YELLOW);
        gc.setLineWidth(3);
        gc.strokeRect(x, y, width, height);
    }

    @Override
    public void drawMenu(
            String title,
            int selectedIndex,
            double percentage,
            double width,
            double span,
            double screenWidth,
            double levelHeight,
            String[] options) {
        /* Sets the middle of the screen. */
        double posX = screenWidth / 2;
        /* Title configuration. */
        Font titleFont = Font.font("Verdana", FontWeight.BOLD, 28);
        double titleLineSpacing = span * 0.8;
        double spaceAfterTitle = span * 0.35;
        double maxTextWidth = width - span;
        String[] titleLines = wrapText(title, titleFont, maxTextWidth);
        /*
         * Calculates the panel height considering:
         *
         * - one top span before the title;
         * - wrapped title height;
         * - a reduced space between title and options;
         * - all menu options;
         * - one bottom span after the last option.
         */
        double titleHeight = titleLines.length * titleLineSpacing;
        double optionsHeight = options.length * span;
        double panelHeight = span + titleHeight + spaceAfterTitle + optionsHeight - (span / 2);
        /* Sets the Y position considering a percentage of the level height. */
        double centerY = levelHeight * percentage;
        double panelY = centerY - (centerY + panelHeight - levelHeight + (span / 2));
        double posY = panelY;
        this.drawGlassPanel(
                (int) width,
                (int) panelHeight,
                (int) (posX - (width / 2)),
                (int) panelY);
        /* It prints the Menu title considering its font, style, and color. */
        gc.setFont(titleFont);
        gc.setFill(Color.LIGHTGRAY);
        gc.setTextAlign(TextAlignment.CENTER);
        /* Top spacing before the title. */
        posY += span;
        /* Draw wrapped title lines. */
        for (String line : titleLines) {
            gc.fillText(line, posX, posY);
            posY += titleLineSpacing;
        }
        gc.setTextAlign(TextAlignment.CENTER);
        /* Reduced spacing between title and options. */
        double optionsStartY = posY + spaceAfterTitle;
        /* It sets the font for printing the Menu options. */
        gc.setFont(Font.font("Verdana", FontWeight.BOLD, 22));
        /* It prints each option considering a span between each one. */
        for (int i = 0; i < options.length; i++) {
            double optionY = optionsStartY + (i * span);
            /* It makes the selected option Yellow. */
            gc.setFill(i == selectedIndex ? Color.YELLOW : Color.WHITE);
            gc.fillText(options[i], posX, optionY);
        }
        /*
         * It resets the text alignment. It can causes bugs in other panels if isn't
         * reset.
         */
        gc.setTextAlign(TextAlignment.LEFT);
    }

    private String[] wrapText(String text, Font font, double maxWidth) {
        List<String> lines = new ArrayList<>();
        StringBuilder currentLine = new StringBuilder();

        for (String word : text.split(" ")) {
            String testLine = currentLine.length() == 0
                    ? word
                    : currentLine + " " + word;

            Text textNode = new Text(testLine);
            textNode.setFont(font);
            double textWidth = textNode.getLayoutBounds().getWidth();

            if (textWidth > maxWidth) {
                if (currentLine.length() > 0) {
                    lines.add(currentLine.toString());
                }
                currentLine = new StringBuilder(word);
            } else {
                currentLine = new StringBuilder(testLine);
            }
        }

        if (currentLine.length() > 0) {
            lines.add(currentLine.toString());
        }

        return lines.toArray(new String[0]);
    }

}