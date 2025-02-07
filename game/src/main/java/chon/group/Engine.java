package chon.group;

import java.util.ArrayList;

import chon.group.game.domain.agent.Agent;
import chon.group.game.domain.collectibles.PointsItem;
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
            Environment environment = new Environment(0, 0, 1366, 780, "/images/environment/environmentSG2.png");
            Agent saeByeok = new Agent(670, 390, 150, 80, 10, 30, "/images/agents/saeByeok.png", false);
            Agent triangleGuard = new Agent(1350, 390, 130, 70, 4, 3, "/images/agents/triangleGuard.png", true);
            Agent circleGuard = new Agent(0, 390, 130, 60, 3, 2, "/images/agents/circleGuard.png", true);

            environment.setProtagonist(saeByeok);
            environment.getAgents().add(triangleGuard);
            environment.getAgents().add(circleGuard);
            environment.setPauseImage("/images/environment/sgpause.png");
            environment.setGameOverImage("/images/environment/gameoverSG.png");
            environment.setVictoryImage("/images/environment/victorySG2.png");

            PointsItem cookie = new PointsItem(0, 0, 45, 80, "/images/environment/cookieSG.png", 10);
            PointsItem cookie2 = new PointsItem(0, 0, 45, 80, "/images/environment/cookieSG2.png", 5);
            PointsItem cookie3 = new PointsItem(0, 0, 45, 80, "/images/environment/cookieSG3.png", 2);
            PointsItem cookie4 = new PointsItem(0, 0, 45, 80, "/images/environment/cookieSG4.png", 1);
            environment.setPiSpawnInterval(2000);
            environment.getModels().add(cookie);
            environment.getModels().add(cookie2);
            environment.getModels().add(cookie3);
            environment.getModels().add(cookie4);

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

                    /* If protagonist dies Game Over screen appears */
                    if (saeByeok.isDead()) {
                        mediator.drawBackground();
                        mediator.drawAgents();
                        mediator.drawCollectibles();
                        mediator.drawScore();
                        /* Rendering the Game Over Screen */
                        mediator.drawGameOverScreen();

                        return;
                    }

                    /* If protagonist scores above 200 Victory Screen appears */
                    if (saeByeok.getScore() >= 100) {
                        mediator.drawBackground();
                        mediator.drawAgents();
                        mediator.drawCollectibles();
                        mediator.drawScore();
                        /* Rendering the Game Over Screen */
                        mediator.drawVictoryScreen();

                        return;
                    }

                    /* Branching the Game Loop */
                    if (isPaused) {
                        mediator.drawBackground();
                        mediator.drawAgents();
                        mediator.drawCollectibles();
                        mediator.drawScore();
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
                        environment.getAgents().get(1).chase(environment.getProtagonist().getPosX(),
                                environment.getProtagonist().getPosY());

                        /* Render the game environment and agents */
                        environment.detectCollision();
                        environment.updateCollectible();
                        mediator.drawBackground();
                        mediator.drawAgents();
                        mediator.drawCollectibles();
                        mediator.drawScore();
                    }
                }

            }.start();
            theStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}