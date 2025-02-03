package chon.group;

import java.util.ArrayList;

import chon.group.game.domain.agent.Agent;
import chon.group.game.domain.environment.Environment;
import chon.group.game.Drawer.EnvironmentDrawer;
import chon.group.game.Drawer.JavaFxDrawer;
import chon.group.game.Drawer.JavaFxMediator;
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
 * game initialization, rendering, and main game loop using
 * {@link javafx.animation.AnimationTimer}.
 * </p>
 * 
 * <h2>Responsibilities</h2>
 * <ul>
 * <li>Set up the game environment, agents, and graphical components.</li>
 * <li>Handle keyboard input for controlling the protagonist agent.</li>
 * <li>Execute the game loop for updating and rendering the game state.</li>
 * </ul>
 */
public class Engine extends Application {

    private boolean isPaused = false;
    private boolean gameOver = false;
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
            Environment environment = new Environment(0, 0, 1280, 780, "/images/environment/backgroundGPT.png");
            Agent jerry = new Agent(400, 390, 70, 85, 4, 100, "/images/agents/jerrySprite.png", false);
            Agent tom = new Agent(920, 440, 130, 96, 2, 3, "/images/agents/tom.png", true);
            environment.setProtagonist(jerry);
            environment.getAgents().add(tom);
            environment.setPauseImage("/images/environment/pause.png");
            environment.setGameOverImage("/images/environment/gameOver1.png");

            // Set up the graphical canvas
            Canvas canvas = new Canvas(environment.getWidth(), environment.getHeight());
            GraphicsContext gc = canvas.getGraphicsContext2D();
            EnvironmentDrawer mediator = new JavaFxMediator(environment, gc);
            environment.setGc(gc);

            // Set up the scene and stage
            StackPane root = new StackPane();
            Scene scene = new Scene(root, environment.getWidth(), environment.getHeight());
            theStage.setTitle("Chon: The Learning Game");
            theStage.setScene(scene);

            root.getChildren().add(canvas);
            theStage.show();

            // Handle keyboard input
            ArrayList<String> input = new ArrayList<String>();
            scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
                public void handle(KeyEvent e) {
                    String code = e.getCode().toString();
                    input.clear();

                    System.out.println("Pressed: " + code);

                    if (code.equals("P")) {
                        isPaused = !isPaused;
                    }

                    if (!isPaused && !input.contains(code)) {
                        input.add(code);
                    }

                }
            });

            scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
                public void handle(KeyEvent e) {
                    String code = e.getCode().toString();
                    System.out.println("Released: " + code);
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
                public void handle(long arg0) {

                    // Render gameOver image
                    if (gameOver) {
                        mediator.drawBackground();
                        mediator.drawAgents();
                        mediator.drawGameOverScreen();

                        return;
                    }

                    if (isPaused) {
                        // Render pause image
                        mediator.drawBackground();
                        mediator.drawAgents();
                        mediator.drawPauseScreen();
                    } else {
                        /* jerry Only Moves if the Player Press Something */
                        // Update the protagonist's movements if input exists
                        if (!input.isEmpty()) {
                            /* jerry's Movements */
                            environment.getProtagonist().move(input);
                            environment.checkBorders();
                        }

                        /* tom's Automatic Movements */
                        // Update the other agents' movements
                        environment.getAgents().get(0).chase(environment.getProtagonist().getPosX(),
                                environment.getProtagonist().getPosY());

                        // Verifica se o protagonista morreu
                        if (environment.getProtagonist().isDead()) {
                            gameOver = true;
                            environment.setGameOver(true);
                        }

                        // Render the game environment and agents
                        environment.detectCollision();
                        mediator.drawBackground();
                        mediator.drawAgents();
                    }
                }

            }.start();
            theStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}