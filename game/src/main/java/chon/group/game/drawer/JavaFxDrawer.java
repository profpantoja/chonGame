package chon.group.game.drawer;

import javafx.scene.canvas.GraphicsContext;
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
        this.gc.fillRect(posX , posY - barY, width, barHeight + (borderThickness * 2));

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
     * Displays a status panel showing the protagonist's coordinates.
     *
     * @param posX The x-coordinate of the protagonist.
     * @param posY The y-coordinate of the protagonist.
     */
    public void drawStatusPanel(int posX, int posY, int camX) {
        Font theFont = Font.font("Verdana", FontWeight.BOLD, 14);
        this.gc.setFont(theFont);
        this.gc.setFill(Color.BLACK);
        this.gc.fillText("X: " + posX, (posX - camX) + 5, posY - 55);
        this.gc.fillText("Y: " + posY, (posX - camX) + 5, posY - 40);
        this.gc.fillText("CamX: " + camX, (posX - camX) + 5, posY - 25);
    }

    /**
     * Displays a status panel showing the protagonist's coordinates.
     *
     * @param worldX The x-coordinate in the world.
     * @param worldY The y-coordinate in the world.
     * @param posX The x-coordinate of the protagonist.
     * @param posY The y-coordinate of the protagonist.
     */
    public void drawStatusPanelSideScrolling(int worldX, int worldY, int posX, int posY) {
        Font theFont = Font.font("Verdana", FontWeight.BOLD, 14);
        this.gc.setFont(theFont);
        this.gc.setFill(Color.BLACK);
        this.gc.fillText("X: " + worldX, posX + 10, posY - 40);
        this.gc.fillText("Y: " + worldY, posX + 10, posY - 25);
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
        gc.fillText(title, getCanvasWidth() / 2, menuY + 60);

        gc.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
        for (int i = 0; i < options.length; i++) {
            gc.setFill((i == selectedIndex) ? Color.YELLOW : Color.WHITE);
            double optionY = menuY + 120 + (i * 50);
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


}