package chon.group.game.drawer;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

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
     * Returns the width of the canvas used for rendering.
     *
     * @return The width of the canvas.
     */
    public double getCanvasWidth() {
        return this.gc.getCanvas().getWidth();
    }

    /**
     * Returns the height of the canvas used for rendering.
     *
     * @return The height of the canvas.
     */
    public double getCanvasHeight() {
        return this.gc.getCanvas().getHeight();
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
        int barY = 19;

        this.gc.setFill(Color.BLACK);
        this.gc.fillRect(posX, posY - barY, width, barHeight + (borderThickness * 2));

        this.gc.setFill(color);
        this.gc.fillRect(posX + borderThickness,
                posY - (barY - borderThickness),
                (lifeSpan - (borderThickness * 2)),
                barHeight);
    }

    /**
     * Renders the agent's energy bar.
     *
     * @param energy     The current energy value (0.0 to 1.0).
     * @param fullEnergy The maximum energy value (typically 1.0).
     * @param width      The width of the energy bar.
     * @param posX       The x-coordinate position.
     * @param posY       The y-coordinate position.
     * @param color      The base color of the energy bar.
     */
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

    /**
     * Draws the status panel showing health, energy, score, and collectibles.
     *
     * @param life      current life value
     * @param maxLife   maximum life value
     * @param collected number of collected items
     * @param total     total collectible items
     * @param score     current score
     * @param energy    current energy value
     * @param maxEnergy maximum energy value
     */
    public void drawPanel(int life, int maxLife, int collected, int total, int score, double energy, double maxEnergy,
            Font pixelFont, Image lifeIcon, Image energyIcon, Image itemIcon, Image scoreIcon, int panelWidth,
            int panelHeight) {
        drawGlassPanel(panelWidth, panelHeight);
        drawBarsAndIcons(life, maxLife, collected, total, score, energy, maxEnergy, pixelFont, lifeIcon, energyIcon,
                itemIcon, scoreIcon);
    }

    /**
     * Draws a semi-transparent glass-style background panel with lighting effects.
     */
    private void drawGlassPanel(int panelWidth, int panelHeight) {
        gc.setFill(Color.rgb(0, 0, 0, 0.4));
        gc.fillRoundRect(10, 10, panelWidth, panelHeight, 15, 15);

        gc.setStroke(Color.rgb(150, 255, 150, 0.3));
        gc.setLineWidth(1.5);
        gc.strokeRoundRect(10, 10, panelWidth, panelHeight, 15, 15);

        InnerShadow innerShadow = new InnerShadow();
        innerShadow.setColor(Color.rgb(0, 100, 0, 0.4));
        innerShadow.setBlurType(BlurType.GAUSSIAN);
        innerShadow.setRadius(10);
        gc.setEffect(innerShadow);
        gc.fillRoundRect(10, 10, panelWidth, panelHeight, 15, 15);
        gc.setEffect(null);

        DropShadow glow = new DropShadow();
        glow.setColor(Color.rgb(100, 255, 100, 0.2));
        glow.setBlurType(BlurType.GAUSSIAN);
        glow.setRadius(15);
        gc.setEffect(glow);
        gc.strokeRoundRect(10, 10, panelWidth, panelHeight, 15, 15);
        gc.setEffect(null);
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

    /**
     * Displays a status panel showing the protagonist's coordinates.
     *
     * @param posX The x-coordinate of the protagonist.
     * @param posY The y-coordinate of the protagonist.
     */
    public void drawDebugPanel(int posX, int posY, int camX) {
        Font theFont = Font.font("Verdana", FontWeight.BOLD, 14);
        this.gc.setFont(theFont);
        this.gc.setFill(Color.BLACK);
        this.gc.fillText("X: " + posX, (posX - camX) + 5, posY - 55);
        this.gc.fillText("Y: " + posY, (posX - camX) + 5, posY - 40);
        this.gc.fillText("CamX: " + camX, (posX - camX) + 5, posY - 25);
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

        gc.setFill(fillColor);
        gc.fillText(
                String.valueOf(message),
                posX,
                posY);

        gc.setGlobalAlpha(1.0);
    }

    /**
     * Desenha um menu genérico e estilizado, com a opção de um fundo de imagem customizado.
     *
     * @param title O título a ser exibido no topo do menu.
     * @param selectedIndex O índice da opção atualmente selecionada (para destaque).
     * @param backgroundImageUrl A URL para uma imagem de fundo para o menu. Se for nula ou inválida, usa a cor padrão.
     * @param options As opções de texto a serem listadas no menu.
     */

     public void drawMenuPause(String title, int selectedIndex, Image backgroundImage, String... options) {
        double menuWidth = 350;
        double menuHeight = 150 + (options.length * 50); 
        
        double menuX = (getCanvasWidth() - menuWidth) / 2;
        double menuY = (getCanvasHeight() - menuHeight) / 2;

        gc.setFill(Color.rgb(0, 0, 0, 0.7));
        gc.fillRect(0, 0, getCanvasWidth(), getCanvasHeight());
        
        // draw the background image if available, otherwise use a solid color
        if (backgroundImage != null && !backgroundImage.isError()) {
            gc.drawImage(backgroundImage, menuX, menuY, menuWidth, menuHeight);
        } else {
            gc.setFill(Color.rgb(40, 40, 60, 0.9));
            gc.fillRect(menuX, menuY, menuWidth, menuHeight); 
        }

        gc.setStroke(Color.rgb(150, 150, 180));
        gc.setLineWidth(2);
        gc.strokeRect(menuX, menuY, menuWidth, menuHeight); 
        gc.setFont(Font.font("Verdana", FontWeight.BOLD, 32));
        gc.setFill(Color.WHITE);
        gc.setTextAlign(TextAlignment.CENTER);
        gc.fillText(title, getCanvasWidth() / 2, menuY + 330);

        gc.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
        for (int i = 0; i < options.length; i++) {
            gc.setFill((i == selectedIndex) ? Color.YELLOW : Color.WHITE);
            double optionY = menuY + 380 + (i * 50);
            gc.fillText(options[i], getCanvasWidth() / 2, optionY);
        }
        
        gc.setTextAlign(TextAlignment.LEFT);
    }

    /**
     * Desenha o menu principal do jogo, com um fundo que preenche a tela inteira.
     *
     * @param backgroundImage A imagem para preencher toda a tela. Se for nula, usa um fundo preto.
     * @param title O título do menu central.
     * @param selectedIndex O índice da opção atualmente selecionada.
     * @param options As opções de texto para o menu central.
     */
    public void drawMainMenu(Image backgroundImage, String title, int selectedIndex, String... options) {
    // 1. Fundo
    if (backgroundImage != null && !backgroundImage.isError()) {
        gc.drawImage(backgroundImage, 0, 0, getCanvasWidth(), getCanvasHeight());
    } else {
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, getCanvasWidth(), getCanvasHeight());
    }

    
    double centerX = getCanvasWidth() / 2;

    // 2. Título "ENGINE" no topo
    gc.setFont(Font.font("Verdana", FontWeight.BOLD, 28));
    gc.setFill(Color.LIGHTGRAY);
    gc.setTextAlign(TextAlignment.CENTER);
    gc.fillText(title, centerX, getCanvasHeight() * 0.75); // pode ajustar essa altura conforme sua imagem

    // 3. Opções do menu abaixo da palavra "ENGINE"
    gc.setFont(Font.font("Verdana", FontWeight.BOLD, 22));
    double firstOptionY = getCanvasHeight() * 0.82;

    for (int i = 0; i < options.length; i++) {
        double optionY = firstOptionY + (i * 45); // espaço entre as opções
        gc.setFill((i == selectedIndex) ? Color.YELLOW : Color.WHITE);
        gc.fillText(options[i], centerX, optionY);
    }

    // Reset alinhamento
    gc.setTextAlign(TextAlignment.LEFT);
}

   public void drawSelectMenu(Image backgroundImage, String title, int selectedIndex, String... options) {
    // 1. Fundo
    if (backgroundImage != null && !backgroundImage.isError()) {
        gc.drawImage(backgroundImage, 0, 0, getCanvasWidth(), getCanvasHeight());
    } else {
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, getCanvasWidth(), getCanvasHeight());
    }

    
    double centerX = getCanvasWidth() / 2;

    // 2. Título "ENGINE" no topo
    gc.setFont(Font.font("Verdana", FontWeight.BOLD, 28));
    gc.setFill(Color.LIGHTGRAY);
    gc.setTextAlign(TextAlignment.CENTER);
    gc.fillText(title, centerX, getCanvasHeight() * 0.75); // pode ajustar essa altura conforme sua imagem

    // 3. Opções do menu abaixo da palavra "ENGINE"
    gc.setFont(Font.font("Verdana", FontWeight.BOLD, 22));
    double firstOptionY = getCanvasHeight() * 0.82;

    for (int i = 0; i < options.length; i++) {
        double optionY = firstOptionY + (i * 45); // espaço entre as opções
        gc.setFill((i == selectedIndex) ? Color.YELLOW : Color.WHITE);
        gc.fillText(options[i], centerX, optionY);
    }

    // Reset alinhamento
    gc.setTextAlign(TextAlignment.LEFT);
}


}