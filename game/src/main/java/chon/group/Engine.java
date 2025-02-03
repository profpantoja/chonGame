package chon.group;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import chon.group.game.domain.agent.Agent;
import chon.group.game.domain.environment.Environment;
import chon.group.game.drawer.EnvironmentDrawer;
import chon.group.game.drawer.JavaFxMediator;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

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

    private Set<String> inputKeys = new HashSet<>();
    /* If the game is paused or not. */
    private boolean isPaused = false;

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
            Environment environment = new Environment(0, 0, 1280, 780, "/images/environment/castle.png");
            Agent chonBota = new Agent(400, 390, 90, 65, 3, 1000, "/images/agents/chonBota.png", false);
            Agent chonBot = new Agent(920, 440, 90, 65, 1, 3, "/images/agents/chonBot.png", true);
            environment.setProtagonist(chonBota);
            environment.getAgents().add(chonBot);
            environment.setPauseImage("/images/environment/pause.png");

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
                    inputKeys.add(code); // 🔥 Armazena a tecla pressionada

                    System.out.println("Pressed: " + code);

                    if (code.equals("P")) {
                        isPaused = !isPaused;
                    }

                    if (code.equals("SPACE") && environment.getProtagonist().canShoot) {
                        environment.getProtagonist().shoot();
                        environment.getProtagonist().canShoot = false;
                    }
                }
            });


            scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
                public void handle(KeyEvent e) {
                    String code = e.getCode().toString();
                    inputKeys.remove(code); // 🔥 Remove a tecla solta
            
                    System.out.println("Released: " + code);
            
                    if (code.equals("SPACE")) {
                        environment.getProtagonist().canShoot = true;
                    }
                }
            });            

            /* Start the game loop */ 
            new AnimationTimer() {
                GraphicsContext gc = canvas.getGraphicsContext2D();
                /**
                 * The game loop, called on each frame.
                 *
                 * @param now the timestamp of the current frame in nanoseconds.
                 */
                @Override
                public void handle(long arg0) {
                    mediator.clearEnvironment();

                    if (isPaused) {
                        mediator.drawBackground();
                        mediator.drawAgents();
                        mediator.drawPauseScreen();
                    } else {
                        // 🔥 Movimento do personagem INDEPENDENTE
                        environment.getProtagonist().move(new ArrayList<>(inputKeys));
                        environment.checkBorders();

                        // 🔥 Atualização dos tiros INDEPENDENTE
                        environment.getProtagonist().updateShot();

                        // 🔥 Movimentação dos inimigos INDEPENDENTE
                        environment.getAgents().get(0).chase(environment.getProtagonist().getPosX(),
                                environment.getProtagonist().getPosY());

                        // 🔥 Renderiza os elementos
                        environment.detectCollision();
                        mediator.drawBackground();
                        mediator.drawAgents();
                        environment.getProtagonist().renderShot(gc);
                        for (Agent agent : environment.getAgents()) {
                            agent.renderShot(gc);
                        }
                    }
                }


            }.start();
            theStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}