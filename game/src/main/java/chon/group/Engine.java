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
 * * <h2>Responsibilities</h2>
 * <ul>
 * <li>Set up the game environment, agents, and graphical components.</li>
 * <li>Handle keyboard input for controlling the protagonist agent.</li>
 * <li>Execute the game loop for updating and rendering the game state.</li>
 * </ul>
 */
public class Engine extends Application {

    /* If the game is paused or not. */
    private boolean isPaused = false;
    private boolean win = false;
    private int currentRoom = 1;
    

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

            
            double windowWidth = 1280;
            double windowHeight = 768;
            
            /* Initialize the game environment and agents */
            Environment environment = new Environment(0, 0,4096,768, "/images/environment/castle.png");

            Agent chonBota = new Agent(100, 390, 90, 65, 5, 1000, "/images/agents/chonBota.png", false);
            Weapon cannon = new Cannon(400, 390, 0, 0, 5, 0, "", false);
            Weapon fireball = new Fireball(400, 390, 0, 0, 3, 0, "", false);
            chonBota.setWeapon(fireball);
            
            Agent chonBot = new Agent(920, 440, 90, 65, 1, 500, "/images/agents/chonBot.png", true);
            environment.setProtagonist(chonBota);
            environment.getAgents().add(chonBot);
            
            // necessita de uma UI de background menu para usar a método abaixo
            // enquanto não houver setMainMenuImage, o background será o padrão
            // environment.setPauseImage("/images/environment/pause.png");

             // necessita de um background usar a método abaixo
             // enquanto não houver setMainMenuImage, o background será o padrão
            // environment.setMainMenuImage("/images/environment/pause.png");
            
            environment.setGameOverImage("/images/environment/gameover.png");
            environment.setWinImage("/images/environment/gameover.png");
            
            /* Set up the graphical canvas */
            Canvas canvas = new Canvas(windowWidth, windowHeight);
            GraphicsContext gc = canvas.getGraphicsContext2D();
            EnvironmentDrawer mediator = new JavaFxMediator(environment, gc);
            
            /* Set up the scene and stage */
            StackPane root = new StackPane();
            Scene scene = new Scene(root, windowWidth, windowHeight);
            theStage.setTitle("Chon: The Learning Game");
            theStage.setScene(scene);
            
            //mediator.drawMainMenu();
            
            root.getChildren().add(canvas);
            theStage.show();
            
            /* Handle keyboard input */
            ArrayList<String> input = new ArrayList<String>();
            scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
                public void handle(KeyEvent e) {
                    String code = e.getCode().toString();
                    
                    // Não limpa mais o input aqui para permitir múltiplas teclas
                    // input.clear(); 
                    
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
                    mediator.clearEnvironmentSideScrolling();
                    
                    /* --- Branching the Game Loop --- */

                    if (environment.getProtagonist().isDead()) {
                        /* Game Over Screen */
                        environment.updateMessages();
                        environment.updateShots();
                        mediator.drawBackgroundSideScrolling();
                        mediator.drawAgentsSideScrolling();
                        mediator.drawShotsSideScrolling();
                        mediator.drawMessagesSideScrolling();
                        mediator.drawGameOver();

                    } else if (win) {
                        /* Win Screen */
                        mediator.drawBackgroundSideScrolling();
                        mediator.drawAgentsSideScrolling();
                        mediator.drawMessagesSideScrolling();
                        mediator.drawShotsSideScrolling();
                        mediator.drawWinScreen();

                    } else if (isPaused) {
                        /* Pause Screen */
                        mediator.drawBackgroundSideScrolling();
                        mediator.drawAgentsSideScrolling();
                        mediator.drawMessagesSideScrolling();
                        mediator.drawShotsSideScrolling();
                        mediator.drawPauseScreen();

                    } else {
                        /* --- Active Gameplay Loop --- */

                        /* Handle Player Movement and Actions */
                        if (!input.isEmpty()) {
                            environment.getProtagonist().move(input);
                            
                            /* Update the camera position based on the protagonist's position */ 
                            double cameraTargetX = environment.getProtagonist().getPosX() - (windowWidth / 1.4);
                            if (cameraTargetX < 0) 
                                cameraTargetX = 0;
                            double maxCameraX = environment.getWidth() - windowWidth;
                            if (cameraTargetX > maxCameraX) 
                                cameraTargetX = maxCameraX;
                            environment.setCameraX(cameraTargetX);

                            /* Handle Shooting */
                            if (input.contains("SPACE")) {
                                input.remove("SPACE"); // Evita tiros contínuos
                                String direction = chonBota.isFlipped() ? "LEFT" : "RIGHT";
                                environment.getShots().add(chonBota.getWeapon().fire(chonBota.getPosX(), chonBota.getPosY(), direction));
                            }
                            environment.checkBorders();
                        }
                        
                        /* Handle AI Movement */
                        for (Agent agent : environment.getAgents()) {
                            agent.chase(environment.getProtagonist().getPosX(), environment.getProtagonist().getPosY());
                        }

                        /* Update Game State */
                        environment.detectCollision();
                        environment.updateShots();
                        environment.updateMessages();

                        /* --- Check for Game State Transitions (Win or Room Change) --- */

                        if (currentRoom == 2 && environment.getAgents().isEmpty()) {
                            win = true;
                        
                        } else if (currentRoom == 1) {
                            // O jogador precisa estar no final da sala E todos os inimigos devem ter sido derrotados.
                            if (environment.getAgents().isEmpty() && environment.getProtagonist().getPosX() >= (0.9 * environment.getWidth())) {
                                System.out.println("All enemies are dead. Proceeding to the next room.");
                                Agent newChonBot = new Agent(920, 440, 90, 65, 1, 500, "/images/agents/chonBot.png", true);
                                environment.loadNextRoom("/images/environment/mountain.png", newChonBot);
                                currentRoom = 2;
                            }
                        }

                        /* Render all elements */
                        mediator.drawBackgroundSideScrolling();
                        mediator.drawAgentsSideScrolling();
                        mediator.drawShotsSideScrolling();
                        mediator.drawMessagesSideScrolling();                              
                    }
                }
            }.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}