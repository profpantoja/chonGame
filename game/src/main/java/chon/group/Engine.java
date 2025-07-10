package chon.group;

import java.util.ArrayList;
import java.util.List;

import chon.group.game.core.agent.Agent;
import chon.group.game.core.agent.GameObject;
import chon.group.game.core.weapon.Weapon;
import chon.group.game.domain.environment.Environment;
import chon.group.game.domain.weapon.Cannon;
import chon.group.game.domain.weapon.Lancer;
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
            Environment environment = new Environment(0, 0, 1200, 700, "/images/environment/Hogwarts.png");
            Agent HarryPotter = new Agent(400, 390, 90, 65, 5, 1000, "/images/agents/HarryPotter.png", false);
            Agent Voldemort = new Agent(920, 440, 90, 65, 2, 3, "/images/agents/valdemortpng-removebg-preview.png", true);
            environment.setProtagonist(HarryPotter);
            environment.getAgents().add(Voldemort);
            Weapon cannon = new Cannon(400, 390, 0, 0, 3, 0, "", false);
            Weapon lancer = new Lancer(400, 390, 0, 0, 3, 0, "", false);

            HarryPotter.setWeapon(lancer);

            environment.setProtagonist(HarryPotter);
            environment.getAgents().add(Voldemort);
            environment.setPauseImage("/images/environment/pause.png");
            environment.setGameOverImage("/images/environment/gameover.png");
            

List<GameObject> objects = new ArrayList<>();
objects.add(new GameObject(200, 200, 32, 32, "/images/agents/coin.png", true, false));
objects.add(new GameObject(400, 300, 32, 32, "/images/agents/coin.png",true, false));
objects.add(new GameObject(600, 500, 32, 32, "/images/agents/coin.png",true, false));
objects.add(new GameObject(800, 250, 32, 32, "/images/agents/coin.png",true, false));
objects.add(new GameObject(1000, 400, 32, 32, "/images/agents/coin.png",true, false));
objects.add(new GameObject(300, 600, 32, 32, "/images/agents/coin.png",true, false));
objects.add(new GameObject(700, 150, 32, 32, "/images/agents/coin.png",true, false));
environment.setObjects(objects);



            /* Set up the graphical canvas */ 
            Canvas canvas = new Canvas(environment.getWidth(), environment.getHeight());
            GraphicsContext gc = canvas.getGraphicsContext2D();
            EnvironmentDrawer mediator = new JavaFxMediator(environment, gc);
            /* Set up the scene and stage */ 
            StackPane root = new StackPane();
            Scene scene = new Scene(root, environment.getWidth(), environment.getHeight());
            theStage.setTitle("Harry Potter Game");
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
                    environment.detectCollision();
                    
                    /* Branching the Game Loop */
                    /* If the agent died in the last loop */
                    if (environment.getProtagonist().isDead()) {
                        /* Still prints ongoing messages (e.g., last hit taken) */
                        environment.updateMessages();
                        mediator.drawBackground();
                        

                        mediator.drawAgents();
                        
                        mediator.drawMessages();
                        mediator.drawObjects();
                        /* Rendering the Game Over Screen */
                        mediator.drawGameOver();
                    } else {
                        if (isPaused) {
                            mediator.drawBackground();
                            environment.detectCollision();

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
                                    
                            /* Render the game environment and agents */

                            environment.updateMessages();
                            mediator.drawBackground();
                            mediator.drawAgents();
                            mediator.drawMessages();
                            mediator.drawObjects();
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