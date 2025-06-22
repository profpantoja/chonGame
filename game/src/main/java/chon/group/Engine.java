package chon.group;

import java.util.ArrayList;

import chon.group.game.domain.agent.Agent;
import chon.group.game.domain.agent.Cannon;
import chon.group.game.domain.agent.Fireball;
import chon.group.game.domain.agent.Weapon;
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
    long shotNow;
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
            Agent chonBota = new Agent(400, 390, 96, 96, 3, 1000, "/images/agents/chonBota.png", false);
            Weapon cannon = new Cannon(400, 390, 0, 0, 3, 0, "", false,64);
            Weapon fireball = new Fireball(400, 390, 0, 0, 3, 0, "", false,75);
            chonBota.setWeapon(fireball);
            chonBota.setPathImageHit("/images/agents/Link_Damage.png");

            Agent chonBot = new Agent(920, 440, 90, 65, 1, 500, "/images/agents/chonBot.png", true);
            environment.setProtagonist(chonBota);
            environment.getAgents().add(chonBot);
            environment.setPauseImage("/images/environment/pause.png");
            environment.setGameOverImage("/images/environment/gameover.png");

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
                    /* If the agent died in the last loop */
                    if (environment.getProtagonist().isDead()) {
                        /* Still prints ongoing messages (e.g., last hit taken) */
                        environment.updateMessages();
                        environment.updateShots();
                        mediator.drawBackground();
                        mediator.drawAgents();
                        mediator.drawShots();
                        mediator.drawMessages();
                        /* Rendering the Game Over Screen */
                        mediator.drawGameOver();
                    } else {
                        if (isPaused) {
                            mediator.drawBackground();
                            mediator.drawAgents();
                            mediator.drawMessages();
                            mediator.drawShots();
                            /* Rendering the Pause Screen */
                            mediator.drawPauseScreen();
                        } else {
                            /* ChonBota Only Moves if the Player Press Something */
                            /* Update the protagonist's movements if input exists */
                            if (!input.isEmpty()) {
                                /* ChonBota Shoots Somebody Who Outdrew You */
                                if((input.contains("RIGHT") ||
                                input.contains("LEFT") ||
                                input.contains("DOWN") ||
                                input.contains("UP")) && 
                                environment.getProtagonist().getCurrentSpritesheet() != "/images/agents/Link_Running.png" &&
                                (System.currentTimeMillis() - environment.getProtagonist().getlastHitTime()) >= environment.getProtagonist().getInvulnerabilityCooldown()){
                                    environment.getProtagonist().setWidth(96);
                                    environment.getProtagonist().setAnimation("/images/agents/Link_Running.png", 6, 75);
                                    System.out.println("ChonBota is running.");
                                }
                                
                                if (input.contains("SPACE")) {
                                    shotNow = System.currentTimeMillis();
                                    environment.getProtagonist().setWidth(180);
                                    environment.getProtagonist().setAnimation("/images/agents/Link_Attack.png", 3, 120);
                                    System.out.println("ChonBota is attacking.");
                                    input.remove("SPACE");
                                    String direction;
                                    int widthAnimation = environment.getProtagonist().getPosX();
                                    if (chonBota.isFlipped())
                                        direction = "LEFT";
                                    else{
                                        widthAnimation += environment.getProtagonist().getWidth() - environment.getProtagonist().getWeapon().getShotWidth();
                                        direction = "RIGHT";                  
                                    }
                                    environment.getShots().add(chonBota.getWeapon().fire(widthAnimation,
                                            chonBota.getPosY(),
                                            direction));
                                }
                                /* ChonBota's Movements */
                                environment.getProtagonist().move(input);
                                environment.checkBorders();
                            }
                            else {
                                if (environment.getProtagonist().getCurrentSpritesheet() != "/images/agents/Link_Standing.png" && 
                                    (System.currentTimeMillis() - shotNow) >= 360 &&
                                    (System.currentTimeMillis() - environment.getProtagonist().getlastHitTime()) >= environment.getProtagonist().getInvulnerabilityCooldown()){
                                    environment.getProtagonist().setWidth(64);
                                    environment.getProtagonist().setAnimation("/images/agents/Link_Standing.png", 4, 150);
                                    System.out.println("ChonBota is standing still.");
                                }
                            }
                            environment.getProtagonist().updateAnimation();
                            /* ChonBot's Automatic Movements */
                            /* Update the other agents' movements */
                            for (Agent agent : environment.getAgents()) {
                                agent.chase(environment.getProtagonist().getPosX(),
                                        environment.getProtagonist().getPosY());
                            }
                            /* Render the game environment and agents */
                            environment.detectCollision();
                            environment.updateShots();
                            environment.updateMessages();
                            mediator.drawBackground();
                            mediator.drawAgents();
                            mediator.drawShots();
                            mediator.drawMessages();
                        }
                    }
                }
            }.start();
            theStage.show();

        } catch (

        Exception e) {
            e.printStackTrace();
        }
    }
}