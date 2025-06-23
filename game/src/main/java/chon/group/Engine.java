package chon.group;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Iterator;
import java.util.List;

import chon.group.game.domain.agent.Agent;
import chon.group.game.domain.agent.Cannon;
import chon.group.game.domain.agent.Collision;
import chon.group.game.domain.agent.Fireball;

import chon.group.game.domain.agent.Agent;
import chon.group.game.domain.agent.Shot;
import chon.group.game.domain.environment.Collision;
import chon.group.game.domain.agent.Weapon;
import chon.group.game.domain.environment.Environment;
import chon.group.game.domain.environment.GameStatus;
import chon.group.game.domain.environment.MainMenu;
import chon.group.game.domain.environment.MenuPause;
import chon.group.game.domain.environment.Setup;
import chon.group.game.drawer.JavaFxDrawer;
import chon.group.game.drawer.JavaFxMediator;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
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
 * * <h2>Responsibilities</h2>
 * <ul>
 * <li>Set up the game environment, agents, and graphical components.</li>
 * <li>Handle keyboard input for controlling the protagonist agent.</li>
 * <li>Execute the game loop for updating and rendering the game state.</li>
 * </ul>
 */
public class Engine extends Application {

    private GameStatus gameStatus = GameStatus.MAIN_MENU;
    private Environment environment;
    private JavaFxMediator mediator;
    private MainMenu mainMenu;
    private MenuPause menuPause;
    private GraphicsContext graphicsContext; 
    private final List<String> gameInput = new ArrayList<>();

    // --- Window Dimensions ---
    private static final double WINDOW_WIDTH = 1280;
    private static final double WINDOW_HEIGHT = 768;
    private int currentRoom = 1;
    

    /**
     * Main entry point of the application.
     *
     * @param args command-line arguments passed to the application.
     */

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage theStage) {
        // Setup Canvas and Graphics
        theStage.setTitle("Chon: The Learning Game");
        Canvas canvas = new Canvas(WINDOW_WIDTH, WINDOW_HEIGHT);
        this.graphicsContext = canvas.getGraphicsContext2D(); 
        this.environment = Setup.createEnvironment();
        this.mediator = new JavaFxMediator(environment, this.graphicsContext); 
        JavaFxDrawer drawer = new JavaFxDrawer(this.graphicsContext, null); 

        // Initialize Menus
        Image menuBackground = Setup.loadImage("/images/environment/menu_background_new.png");
        this.mainMenu = new MainMenu(drawer, menuBackground);
        this.menuPause = new MenuPause(drawer, environment.getPauseImage());        

        // Setup Menu Actions
        mainMenu.setOnStartGame(() -> {
            resetGame(); // Garante que o jogo reinicie do zero
            gameStatus = GameStatus.RUNNING;
        });
        mainMenu.setOnExit(() -> 
        Platform.exit());

        menuPause.setOnResume(() -> 
        gameStatus = GameStatus.RUNNING);
        
        menuPause.setOnExitToMenu(() -> {
            mainMenu.reset();
            gameStatus = GameStatus.MAIN_MENU;
        });

        //Setup Scene and Input
        StackPane root = new StackPane(canvas);
        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        theStage.setScene(scene);
        
        setupInputHandlers(scene);

        //Start Game Loop
        new GameLoop().start();
        
        theStage.show();
    }

    /**
     * Defines the input handlers for the game, based on the current game status.
     */
    private void setupInputHandlers(Scene scene) {
        scene.setOnKeyPressed((KeyEvent e) -> {
            String key = e.getCode().toString();
            System.out.println("Key pressed: " + key); 
            switch (gameStatus) {
                case MAIN_MENU:
                    mainMenu.handleInput(e.getCode());
                    break;
                case PAUSED:
                    menuPause.handleInput(e.getCode());
                    break;
                case RUNNING:
                    if (e.getCode().toString().equals("P")) {
                        gameStatus = GameStatus.PAUSED;
                        menuPause.reset(); 
                    } else if (!gameInput.contains(key)) {
                        gameInput.add(key);
                    }
                    break;
                default:
                    // key Enter return to the main menu in game over or victory state
                    if (e.getCode().toString().equals("ENTER")) {
                        gameStatus = GameStatus.MAIN_MENU;
                        mainMenu.reset();
                    }
                    break;
            }
        });

        scene.setOnKeyReleased((KeyEvent e) -> {
            if (gameStatus == GameStatus.RUNNING) {
                gameInput.remove(e.getCode().toString());
            }
        });
    }

    /**
     * Resets the game state to its initial configuration.
     */
    private void resetGame() {
        this.environment = Setup.createEnvironment();
        // Recria o mediator para apontar para o novo ambiente, usando o gc guardado na classe.
        this.mediator = new JavaFxMediator(this.environment, this.graphicsContext);
        gameInput.clear();
    }


    /**
     * The main game loop 
     */
    private class GameLoop extends AnimationTimer {
        
        public void handle(long currentNanoTime) {
            graphicsContext.clearRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);

            // game status switch to handle different game states
            switch (gameStatus) {
                case MAIN_MENU:
                    mainMenu.draw();
                    break;
                case RUNNING:
                    updateGameLogic();
                    renderGameWorld();
                    break;
                case PAUSED:
                    renderGameWorld(); // draw the game world static in background
                    menuPause.draw(); // draw the pause menu
                    break;
                case GAME_OVER:
                    renderGameWorld(); 
                    mediator.drawGameOver();
                    break;
                case VICTORY:
                    renderGameWorld(); 
                    mediator.drawWinScreen();
                    break;
            }
        }
    }
    
    private void updateGameLogic() {
        
        verifyGameStatus();

        // Protagonist movement and actions
        if (!gameInput.isEmpty()) {
            environment.getProtagonist().move(gameInput);
            updateCameraPosition();
            if (gameInput.contains("SPACE")) {
                String direction = environment.getProtagonist().isFlipped() ? "LEFT" : "RIGHT";
                Agent protagonist = environment.getProtagonist();
                environment.getShots().add(protagonist.getWeapon().fire(protagonist.getPosX(), protagonist.getPosY(), direction));
                gameInput.remove("SPACE"); // Evita múltiplos tiros com uma pressionada
            }
            environment.checkBorders();
        }
        
        // method chase for each agent
        environment.getAgents().forEach(agent -> 
            agent.chase(environment.getProtagonist().getPosX(), environment.getProtagonist().getPosY()));

        // update all agents and their shots, and check if have collisions 
        checkCollisions();
        environment.detectCollision();
        environment.updateShots();
        environment.updateMessages();
    }

    private void verifyGameStatus() {
    
    if (environment.getProtagonist().isDead()) gameStatus = GameStatus.GAME_OVER;
        
    else if (currentRoom == 2 && environment.getAgents().isEmpty()) gameStatus = GameStatus.VICTORY;
        
    else if (currentRoom == 1 && environment.getAgents().isEmpty() && environment.getProtagonist().getPosX() >= (0.9 * environment.getWidth())) {
        System.out.println("Avançando para a sala do chefe...");
        Agent boss = new Agent(920, 440, 90, 65, 1, 500, "/images/agents/chonBot.png", true);
        environment.loadNextRoom("/images/environment/mountain.png", boss);
        currentRoom = 2;
    }
}

    /**
     * Draw the game world elements on the screen.
     */
    private void renderGameWorld() {
        mediator.drawBackgroundSideScrolling();
        mediator.drawAgentsSideScrolling();
        mediator.drawShotsSideScrolling();
        mediator.drawMessagesSideScrolling();
        //mediator.drawCollisions();
    }

    /**
     * The camera follows the protagonist, but only moves when the protagonist is between 15% and 80% of the screen width.
     */
    private void updateCameraPosition() {
        double protagonistX = environment.getProtagonist().getPosX();
        double cameraX = environment.getCameraX();
        double protagonistSpeed = environment.getProtagonist().getSpeed();

        double rightBoundary = cameraX + WINDOW_WIDTH * 0.80; // 80% right of the screen
        double leftBoundary = cameraX + WINDOW_WIDTH * 0.15; // 15% left of the screen

        if (protagonistX > rightBoundary) {
            double newCameraX = cameraX + protagonistSpeed;
            double maxCameraX = environment.getWidth() - WINDOW_WIDTH;
            environment.setCameraX(Math.min(newCameraX, maxCameraX));
        } else if (protagonistX < leftBoundary) {
            double newCameraX = cameraX - protagonistSpeed;
            environment.setCameraX(Math.max(newCameraX, 0));
        }
    }

    private void verifyGameStatus() {
        if (environment.getProtagonist().isDead()) gameStatus = GameStatus.GAME_OVER;
        else if (currentRoom == 2 && environment.getAgents().isEmpty()) gameStatus = GameStatus.VICTORY;

        else if (currentRoom == 1 && environment.getAgents().isEmpty() && environment.getProtagonist().getPosX() >= (0.9 * environment.getWidth())) {
            System.out.println("Avançando para a sala do chefe...");
            Agent boss = new Agent(920, 440, 90, 65, 1, 500, "/images/agents/chonBot.png", true);
            environment.loadNextRoom("/images/environment/mountain.png", boss);
            currentRoom = 2;
        }
    }

    public void checkCollisions(){
         Iterator<Collision> it = environment.getCollisions().iterator();
            while (it.hasNext()) {
                Collision collision = it.next();

                Iterator<Shot> itShot = environment.getShots().iterator();
                while (itShot.hasNext()) {
                    Shot tempShot = itShot.next();
                    tempShot.checkCollision(collision);
                    if (collision.isDestroy()) {
                        itShot.remove();
                        break;
                    }    
                }
                if (collision.isDestroy()) {
                    it.remove(); 
                }
                
                for (Agent agent : environment.getAgents()) {
                    agent.checkCollision(collision, environment);
                    if (collision.isDestroy()) break;
                }
                if (collision.isDestroy()) {
                    it.remove(); 
                }

                environment.getProtagonist().checkCollision(collision, environment);
                if (collision.isDestroy()) {
                    it.remove(); 
                }
            }
    }
}
