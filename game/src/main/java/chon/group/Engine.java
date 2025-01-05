package chon.group;

import java.util.ArrayList;

import chon.group.game.domain.agent.Agent;
import chon.group.game.domain.environment.Environment;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;

/**
 * The {@code Engine} class represents the main entry point of the application 
 * and serves as the game engine for "Chon: The Learning Game."
 * <p>
 * This class extends {@link javafx.application.Application} and manages the 
 * game initialization, rendering, and main game loop using {@link javafx.animation.AnimationTimer}.
 * </p>
 * 
 * <h2>Responsibilities</h2>
 * <ul>
 *   <li>Set up the game environment, agents, and graphical components.</li>
 *   <li>Handle keyboard input for controlling the protagonist agent.</li>
 *   <li>Execute the game loop for updating and rendering the game state.</li>
 * </ul>
 */
public class Engine extends Application {

    private long invulnerableTimeEnd = 0; // Tempo final da invulnerabilidade
    private boolean isInvulnerable = false; // Flag para invulnerabilidade

    /**
     * Main entry point of the application.
     *
     * @param args command-line arguments passed to the application.
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Starts the JavaFX application and initializes the game environment, agents,
     * and graphical components.
     * <p>
     * This method sets up the game scene, handles input events, and starts the
     * game loop using {@link AnimationTimer}.
     * </p>
     *
     * @param theStage the primary stage for the application.
     */
    @Override
    public void start(Stage theStage) {
        try {
            // Initialize the game environment and agents
            Environment environment = new Environment(0, 0, 1280, 780, "/images/environment/castle.png");
            Agent chonBota = new Agent(400, 390, 90, 65, 6, "/images/agents/chonBota.png", 3); // Protagonista
            Agent chonBot = new Agent(920, 440, 90, 65, 2, "/images/agents/chonBot.png", 3); // Inimigo

            environment.setProtagonist(chonBota);
            environment.getAgents().add(chonBot);

            // Set up the graphical canvas
            Canvas canvas = new Canvas(environment.getWidth(), environment.getHeight());
            GraphicsContext gc = canvas.getGraphicsContext2D();
            environment.setGraphicsContext(gc);

            // Set up the scene and stage
            StackPane root = new StackPane();
            Scene scene = new Scene(root, environment.getWidth(), environment.getHeight());
            theStage.setTitle("Chon: The Learning Game");
            theStage.setScene(scene);

            root.getChildren().add(canvas);
            theStage.show();

            // Handle keyboard input
            ArrayList<String> input = new ArrayList<>();
            scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent e) {
                    String code = e.getCode().toString();
                    if (!input.contains(code)) {
                        input.add(code);
                    }
                }
            });

            scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent e) {
                    String code = e.getCode().toString();
                    input.remove(code);
                }
            });

            // Start the game loop
            new AnimationTimer() {

                /**
                 * The game loop, called on each frame.
                 *
                 * @param now the timestamp of the current frame in nanoseconds.
                 */
                @Override
                public void handle(long now) {
                    // Clear the canvas
                    gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

                    // verify if the character are invunerable
                    if (isInvulnerable && now > invulnerableTimeEnd) {
                        isInvulnerable = false; // Fim da invulnerabilidade
                    }

                    // Continuously update the protagonist's movements based on input
                    if (!input.isEmpty()) {
                        environment.getProtagonist().move(input); // Continuar o movimento
                        environment.checkBorders();
                    }

                    // ChonBot's automatic movements
                    environment.getAgents().get(0).chase(
                        environment.getProtagonist().getPosX(),
                        environment.getProtagonist().getPosY()
                    );

                    // Detect collisions and handle health reduction
                    if (!isInvulnerable) {
                        environment.detectCollision(); // Detecta colisões apenas quando não estiver invulnerável
                    }

                    // Render the environment and agents
                    environment.render();

                    // Check if the protagonist is dead
                    if (environment.getProtagonist().isDead()) {
                        
                        environment.getProtagonist().setPosX(-100);  // Move the protagonist out of the screen
                        environment.getProtagonist().setPosY(-100);
                        stop(); // Stop the game loop if the protagonist dies
                        System.out.println("Game Over!");
                    }
                }
            }.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
