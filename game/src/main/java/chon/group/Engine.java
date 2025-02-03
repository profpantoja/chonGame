package chon.group;

import java.util.ArrayList;

import chon.group.game.domain.agent.Agent;
import chon.group.game.domain.environment.Environment;
import chon.group.game.drawer.EnvironmentDrawer;
import chon.group.game.drawer.JavaFxMediator;
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

    /* If the game is paused or not. */
    private boolean isPaused = false;
    private boolean isGameOver = false;

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
            /* Initialize the game environment and agents */
            Environment environment = new Environment(0, 0, 1280, 780, "/images/environment/environmentSG.png");
            Agent saeByeok = new Agent(400, 390, 150, 80, 6, 50, "/images/agents/saeByeok.png", false);
            Agent triangleGuard = new Agent(920, 440, 130, 70, 2, 3, "/images/agents/triangleGuard.png", true);
            environment.setProtagonist(saeByeok);
            environment.getAgents().add(triangleGuard);
            environment.setPauseImage("/images/environment/pauseSG.png");
            environment.setGameOverImage("/images/environment/gameoverSG.png");
            environment.setDefaultCollectibleProperties("/images/environment/cookieSG.png", 45, 80, 3, 3000);

            /* Set up the graphical canvas */
            Canvas canvas = new Canvas(environment.getWidth(), environment.getHeight());
            GraphicsContext gc = canvas.getGraphicsContext2D();
            EnvironmentDrawer mediator = new JavaFxMediator(environment, gc);

            /* Set up the scene and stage */
            StackPane root = new StackPane();
            Scene scene = new Scene(root, environment.getWidth(), environment.getHeight());
            theStage.setTitle("Chon: The Learning Game");
            theStage.setScene(scene);

            root.getChildren().add(canvas);
            theStage.show();

            /* Handle keyboard input */
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

            /* Start the game loop */
            new AnimationTimer() {

                /**
                 * The game loop, called on each frame.
                 *
                 * @param now the timestamp of the current frame in nanoseconds.
                 */
                @Override
                public void handle(long arg0) {
                    mediator.clearEnvironment();

                    /* Branching the Game Loop */
                    if (isGameOver) {
                        mediator.drawBackground();
                        mediator.drawAgents();
                        /* Rendering the Pause Screen */
                        mediator.drawGameOverScreen();

                        return;
                    }
                    /* Branching the Game Loop */
                    if (isPaused) {
                        mediator.drawBackground();
                        mediator.drawAgents();
                        /* Rendering the Pause Screen */
                        mediator.drawPauseScreen();
                    } else {
                        /* ChonBota Only Moves if the Player Press Something */
                        /* Update the protagonist's movements if input exists */
                        if (!input.isEmpty()) {
                            /* ChonBota's Movements */
                            environment.getProtagonist().move(input);
                            environment.checkBorders();
                        }

                        /* ChonBot's Automatic Movements */
                        /* Update the other agents' movements */
                        environment.getAgents().get(0).chase(environment.getProtagonist().getPosX(),
                                environment.getProtagonist().getPosY());

                        // Verifica se o protagonista morreu
                        if (environment.getProtagonist().isDead()) {
                            isGameOver = true;
                            environment.setGameOver(true);
                        }
                        /* Render the game environment and agents */
                        environment.detectCollision();
                        mediator.drawBackground();
                        mediator.drawAgents();
                        environment.updateCollectibles();
                    }
                }

            }.start();
            theStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}