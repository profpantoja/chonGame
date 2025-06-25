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
    private ArrayList<Environment> environments = new ArrayList<>();
    private int currentLevelIndex = 0;
    private Environment currentEnvironment;
    private EnvironmentDrawer mediator;
        

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
            
            /* Initialize the game levels and agents */
            
            //Level 1
            Environment level1 = new Environment(0, 0, 4096, 768, "/images/environment/castle.png");
            Agent chonBota = new Agent(100, 390, 90, 65, 5, 1, "/images/agents/chonBota.png", false);
            Weapon fireball = new Fireball(400, 390, 0, 0, 3, 0, "", false);
            chonBota.setWeapon(fireball);

            Agent chonBot = new Agent(920, 440, 90, 65, 1, 1, "/images/agents/chonBot.png", true);
            level1.setProtagonist(chonBota);
            level1.getAgents().add(chonBot);
            level1.setGameOverImage("/images/environment/gameover.png");
            level1.setWinImage("/images/environment/gameover.png");

            //Level 2
            Environment level2 = new Environment(0, 0, 4096, 768, "/images/environment/mountain.png");
            level2.setProtagonist(chonBota); // mesmo personagem
            level2.setGameOverImage("/images/environment/gameover.png");
            level2.setWinImage("/images/environment/gameover.png");
            Agent chonBoss = new Agent(920, 440,    100, 75, 1, 1, "/images/agents/chonBot.png", true);

            level2.getAgents().add(chonBoss);

            environments.add(level1);
            environments.add(level2);

            currentEnvironment = environments.get(currentLevelIndex);


            /* Initialize the game environment and agents */
            Environment environment = new Environment(0, 0,4096,768, "/images/environment/castle.png");

            Weapon cannon = new Cannon(400, 390, 0, 0, 5, 0, "", false);
            

            
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
            mediator = new JavaFxMediator(currentEnvironment, gc);

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

                    if (currentEnvironment.getProtagonist().isDead()) {
                        /* Game Over Screen */
                        currentEnvironment.updateMessages();
                        currentEnvironment.updateShots();
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
                            currentEnvironment.getProtagonist().move(input);
                            
                            /* Update the camera position based on the protagonist's position */ 
                            double cameraTargetX = currentEnvironment.getProtagonist().getPosX() - (windowWidth / 1.4);
                            if (cameraTargetX < 0) 
                                cameraTargetX = 0;
                            double maxCameraX = currentEnvironment.getWidth() - windowWidth;
                            if (cameraTargetX > maxCameraX) 
                                cameraTargetX = maxCameraX;
                            currentEnvironment.setCameraX(cameraTargetX);

                            /* Handle Shooting */
                            if (input.contains("SPACE")) {
                                input.remove("SPACE"); // Evita tiros contínuos
                                String direction = chonBota.isFlipped() ? "LEFT" : "RIGHT";
                                currentEnvironment.getShots().add(chonBota.getWeapon().fire(chonBota.getPosX(), chonBota.getPosY(), direction));
                            }
                            currentEnvironment.checkBorders();
                        }
                        
                        /* Handle AI Movement */
                        for (Agent agent : currentEnvironment.getAgents()) {
                            agent.chase(currentEnvironment.getProtagonist().getPosX(), currentEnvironment.getProtagonist().getPosY());
                        }

                        /* Update Game State */
                        currentEnvironment.detectCollision();
                        currentEnvironment.updateShots();
                        currentEnvironment.updateMessages();

                        /* --- Check for Game State Transitions (Win or Level Change) --- */

                        if (currentEnvironment.levelChanger()) {
                            currentLevelIndex++;

                            if (currentLevelIndex >= environments.size()) {
                                win = true;
                            } else {
                                currentEnvironment = environments.get(currentLevelIndex);
                                currentEnvironment.getProtagonist().setPosX(100);
                                currentEnvironment.getProtagonist().setPosY(390);
                                currentEnvironment.setCameraX(0);
                                mediator = new JavaFxMediator(currentEnvironment, gc); 
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