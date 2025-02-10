package chon.group.game.drawer;

import java.util.List;

import chon.group.game.domain.item.FallingItem;
import javafx.scene.canvas.GraphicsContext;

import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.application.Platform;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

public class JavaFxDrawer {

    /** The graphics context used to render the environment. */
    private final GraphicsContext gc;
    private final EnvironmentDrawer mediator;

    /**
     * Button to restart the game.
     */
    private Button restartButton;

    /**
     * Button to exit the game.
     */
    private Button exitButton;

    /**
     * Container for game control buttons.
     */
    private VBox buttonContainer;

    /**
     * Constructor to initialize the JavaFx Drawer.
     *
     * @param gc the GraphicsContext instance
     */
    public JavaFxDrawer(GraphicsContext gc, EnvironmentDrawer mediator) {
        this.gc = gc;
        this.mediator = mediator;

        // Inicializa os botões
        this.restartButton = new Button("Jogar Novamente");
        this.exitButton = new Button("Sair");

        // Estilo dos botões - apenas fonte
        String buttonStyle = "-fx-background-color: transparent;\n" +
                "-fx-text-fill: white;\n" +
                "-fx-font-family: 'Verdana';\n" +
                "-fx-font-size: 18px;";

        restartButton.setStyle(buttonStyle);
        exitButton.setStyle(buttonStyle);

        // Remove todos os efeitos hover
        restartButton.setOnMouseEntered(null);
        restartButton.setOnMouseExited(null);
        exitButton.setOnMouseEntered(null);
        exitButton.setOnMouseExited(null);

        // Container para os botões com espaçamento vertical
        this.buttonContainer = new VBox(20); // 20px de espaçamento entre botões
        this.buttonContainer.getChildren().addAll(restartButton, exitButton);

        // Configura ação do botão sair
        exitButton.setOnAction(e -> Platform.exit());
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

    // metodos novos adicionados a partir daqui
    /**
     * Draws falling items on the screen.
     * 
     * @param items List of falling items to be rendered
     */
    public void drawFallingItems(List<FallingItem> items) {
        for (FallingItem item : items) {
            this.gc.drawImage(item.getCachedImage(),
                    item.getPosX(),
                    item.getPosY(),
                    item.getWidth(),
                    item.getHeight());
        }
    }

    /**
     * Draws the game over screen with final score and control buttons.
     *
     * @param width  The width of the screen
     * @param height The height of the screen
     * @param score  The final score to display
     */
    public void drawGameOverScreen(int width, int height, int score) {
        String gameOverImagePath = "/images/environment/gameover.png";

        try {
            // Carrega e desenha a imagem de game over
            Image gameOverImage = new Image(getClass().getResource(gameOverImagePath).toExternalForm());
            this.gc.drawImage(gameOverImage, 0, 0, width, height);

            // Carrega a fonte personalizada
            Font customFont = Font.loadFont(
                    getClass().getResourceAsStream("/fonts/rittswoodProfile.ttf"),
                    30);

            // Configurações do score
            String scoreText = "" + score;
            double scoreX = 600; // posição X fixa
            double scoreY = height - 300; // posição Y fixa

            // Configurações da sombra
            int shadowOffset = 4; // offset da sombra
            Color shadowColor = Color.rgb(0, 0, 0, 0.6); // sombra mais escura e mais opaca

            // Desenha múltiplas camadas de sombra para criar profundidade
            this.gc.setFont(Font.font(customFont.getFamily(), 120));

            // Camada 1 da sombra (mais distante)
            this.gc.setFill(Color.rgb(0, 0, 0, 0.2));
            this.gc.fillText(scoreText, scoreX + shadowOffset * 2, scoreY + shadowOffset * 2);

            // Camada 2 da sombra (média)
            this.gc.setFill(Color.rgb(0, 0, 0, 0.4));
            this.gc.fillText(scoreText, scoreX + shadowOffset * 1.5, scoreY + shadowOffset * 1.5);

            // Camada 3 da sombra (próxima)
            this.gc.setFill(shadowColor);
            this.gc.fillText(scoreText, scoreX + shadowOffset, scoreY + shadowOffset);

            // Desenha o texto principal
            this.gc.setFill(Color.WHITE);
            this.gc.fillText(scoreText, scoreX, scoreY);

            // Posiciona os botões
            restartButton.setTranslateX(292);
            restartButton.setTranslateY(444);

            exitButton.setTranslateX(595);
            exitButton.setTranslateY(385);

        } catch (Exception e) {
            // Fallback para o layout anterior caso a imagem não seja encontrada
            System.out.println("Erro ao carregar imagem de game over: " + e.getMessage());

            this.gc.setFill(new Color(0, 0, 0, 0.7));
            this.gc.fillRect(0, 0, width, height);

            this.gc.setFill(Color.RED);
            this.gc.setFont(Font.font("Verdana", FontWeight.BOLD, 50));
            String gameOverText = "GAME OVER";
            double textWidth = this.gc.getFont().getSize() * gameOverText.length() * 0.5;
            this.gc.fillText(gameOverText, (width - textWidth) / 2, height / 2 - 50);

            this.gc.setFill(Color.WHITE);
            this.gc.setFont(Font.font("Verdana", FontWeight.NORMAL, 30));
            String scoreText = "Final Score: " + score;
            double scoreWidth = this.gc.getFont().getSize() * scoreText.length() * 0.4;
            this.gc.fillText(scoreText, (width - scoreWidth) / 2, height / 2 + 20);

            buttonContainer.setTranslateX((width - 150) / 2);
            buttonContainer.setTranslateY(height / 2 + 50);
        }
    }

    /**
     * Gets the restart button instance.
     *
     * @return The restart button
     */
    public Button getRestartButton() {
        return restartButton;
    }

    /**
     * Gets the exit button instance.
     *
     * @return The exit button
     */
    public Button getExitButton() {
        return exitButton;
    }

    /**
     * Retrieves the VBox container that holds the game control buttons.
     * 
     * @return The VBox containing the control buttons
     */
    public VBox getButtonContainer() {
        return buttonContainer;
    }

    /**
     * Draws the score panel with current score.
     *
     * @param scoreImage The image to use as score panel background
     * @param score      The current score to display
     */
    public void drawScorePanel(Image scoreImage, int score) {
        // Define valores padrão para o painel de score
        int panelX = 1058;
        int panelY = 120;
        int panelWidth = 221;
        int panelHeight = 67;

        // Desenha o fundo do score
        this.gc.drawImage(scoreImage, panelX, panelY, panelWidth, panelHeight);

        try {
            // Carrega a fonte personalizada
            Font customFont = Font.loadFont(
                    getClass().getResourceAsStream("/fonts/rittswoodProfile.ttf"),
                    30);

            // Posição do texto do score
            int scoreTextX = panelX + 197;
            int scoreTextY = panelY + 45;

            // Aplica a transformação para simular itálico
            this.gc.save();
            this.gc.transform(1, 0, -0.2, 1, 0, 0);

            String scoreText = String.valueOf(score);

            // Desenha a sombra
            this.gc.setFill(Color.rgb(0, 0, 0, 0.5)); // Cor preta semi-transparente para sombra
            this.gc.setFont(Font.font(customFont.getFamily(), 30));
            this.gc.fillText(scoreText, scoreTextX + 2, scoreTextY + 2); // Offset da sombra

            // Desenha o texto principal
            this.gc.setFill(Color.WHITE);
            this.gc.setFont(Font.font(customFont.getFamily(), 30));
            this.gc.fillText(scoreText, scoreTextX, scoreTextY);

            this.gc.restore();
        } catch (Exception e) {
            System.out.println("Erro ao carregar fonte personalizada: " + e.getMessage());
            this.gc.setFont(Font.font("Verdana", FontPosture.ITALIC, 30));
        }
    }
}