package chon.group;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import chon.group.game.domain.agent.Agent;
import chon.group.game.domain.agent.Shot;
import chon.group.game.domain.environment.Collision;
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
    long shotNow;
    private boolean canSlash = true;
    private boolean drawHitboxes = true;


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

    menuPause.setOnResume(() -> {
    gameStatus = GameStatus.RUNNING;
    environment.getProtagonist().setCheckMenu(false);
    environment.getProtagonist().setlastHitTime(System.currentTimeMillis());
    environment.getAgents().forEach(agent -> {
        agent.setCheckMenu(false);
        agent.setlastHitTime(System.currentTimeMillis());
    });
});

    menuPause.setOnExitToMenu(() -> {
    mainMenu.reset();
    gameStatus = GameStatus.MAIN_MENU;
    environment.getProtagonist().setCheckMenu(false);
    environment.getProtagonist().setlastHitTime(System.currentTimeMillis());
    environment.getAgents().forEach(agent -> {
        agent.setCheckMenu(false);
        agent.setlastHitTime(System.currentTimeMillis());
    });
});

    //Setup Scene and Input
    StackPane root = new StackPane(canvas);
    Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
    theStage.setScene(scene);
    
    setupInputHandlers(scene);

    environment.getProtagonist().setPathImageHit("/images/agents/Link_Damage.png");
    environment.getProtagonist().setPathImageDeath("/images/agents/Link_Death.png");

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
                    // Congela invulnerabilidade de todos os agentes e protagonista
                    environment.getProtagonist().setCheckMenu(true);
                    environment.getProtagonist().setlastHitTime(System.currentTimeMillis());
                    environment.getAgents().forEach(agent -> {
                        agent.setCheckMenu(true);
                        agent.setlastHitTime(System.currentTimeMillis());
                    });
                }
                else if (!gameInput.contains(key)) gameInput.add(key);
                
                else if(e.getCode().toString().equals("SPACE"))  canSlash = true;  // permite novo slash após soltar

                break;
            default:
                if (e.getCode().toString().equals("ENTER")) {
                    gameStatus = GameStatus.MAIN_MENU;
                    mainMenu.reset();
                    // Garante que todos voltam ao estado normal ao sair para o menu
                    environment.getProtagonist().setCheckMenu(false);
                    environment.getAgents().forEach(agent -> agent.setCheckMenu(false));
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
        // Updates the position of agents and protagonist's hitboxes.
        for(Agent agent : environment.getAgents()) {
            agent.updateHitboxPosition();
        }
        environment.getProtagonist().updateHitboxPosition();

        // Protagonist movement and actions
        if (!gameInput.isEmpty()) {
            if((gameInput.contains("RIGHT") ||
            gameInput.contains("LEFT") ||
            gameInput.contains("DOWN") ||
            gameInput.contains("UP")) && 
            environment.getProtagonist().getCurrentSpritesheet() != "/images/agents/Link_Running.png" &&
            (System.currentTimeMillis() - environment.getProtagonist().getlastHitTime()) >=
             environment.getProtagonist().getInvulnerabilityCooldown()){
                environment.getProtagonist().setWidth(256);
                environment.getProtagonist().setAnimation("/images/agents/Link_Running.png", 6, 75);
                System.out.println("ChonBota is running.");
            }
            environment.getProtagonist().move(gameInput);
            updateCameraPosition();
            if (gameInput.contains("SPACE")) {
                //SoundManager.playMusic("/sounds/damage.wav"); 
                shotNow = System.currentTimeMillis();
                environment.getProtagonist().setWidth(256);
                environment.getProtagonist().setAnimation("/images/agents/Link_Attack.png", 4, 75);
                System.out.println("ChonBota is attacking.");
                gameInput.remove("SPACE");
                /* Stop the weapon to attack */
                canSlash = false;
                
                String direction;
                int widthAnimation = environment.getProtagonist().getPosX();
                if (environment.getProtagonist().isFlipped())
                    direction = "LEFT";
                else{
                    widthAnimation += environment.getProtagonist().getWidth() - environment.getProtagonist().getWeapon().getShotWidth();
                    direction = "RIGHT";                  
                }

                environment.getSlashes().add(
                    environment.getProtagonist().getCloseWeapon().slash(environment.getProtagonist().getPosX(),
                    environment.getProtagonist().getPosY(),
                    direction));

                environment.getShots().add(environment.getProtagonist().getWeapon().fire(widthAnimation,
                environment.getProtagonist().getPosY(),
                direction));
            }
            // Sempre atualiza o movimento vertical (pulo e queda)
            environment.getProtagonist().moveGravity(gameInput); //caso queira usar o jogo sem gravidade tem que mudar aqui para .move(gameInput)
            environment.checkBorders();
        }
        else {
            if (environment.getProtagonist().getCurrentSpritesheet() != "/images/agents/Link_Standing.png" && 
                (System.currentTimeMillis() - shotNow) >= 360 &&
                (System.currentTimeMillis() - environment.getProtagonist().getlastHitTime()) >= 
                environment.getProtagonist().getInvulnerabilityCooldown()){
                environment.getProtagonist().setWidth(256);
                environment.getProtagonist().setAnimation("/images/agents/Link_Standing.png", 4, 150);
                System.out.println("ChonBota is standing still.");
            }
        }
        environment.getProtagonist().updateAnimation();
        
        // method chase for each agent
        environment.getAgents().forEach(agent -> {
            if (environment.getProtagonist().getHitbox() != null) {
                agent.chase(
                    environment.getProtagonist().getHitbox().getPosX(),
                    environment.getProtagonist().getHitbox().getPosY()
                );
            }
        });

        // update all agents and their shots, and check if have collisions 
        checkCollisions();
        environment.detectCollision();
        environment.updateSlashes();
        environment.updateShots();
        environment.updateMessages();
    }

    /**
     * Draw the game world elements on the screen.
     */
    private void renderGameWorld() {
        mediator.drawBackgroundSideScrolling();
        mediator.drawAgentsSideScrolling();
        mediator.drawShotsSideScrolling();
        mediator.drawMessagesSideScrolling();
        mediator.drawSlashes();
    }

    /**
     * The camera follows the protagonist, but only moves when the protagonist is between 15% and 80% of the screen width.
     */
    private void updateCameraPosition() {
        double protagonistX = environment.getProtagonist().getPosX();
        double cameraX = environment.getCameraX();
        double protagonistSpeed = environment.getProtagonist().getSpeed();

        // Make camera move faster (e.g., 1.5x protagonist speed)
        double cameraSpeed = protagonistSpeed * 2.0;

        double rightBoundary = cameraX + WINDOW_WIDTH * 0.80;
        double leftBoundary = cameraX + WINDOW_WIDTH * 0.15;

        if (protagonistX > rightBoundary) {
            double newCameraX = cameraX + cameraSpeed;
            double maxCameraX = environment.getWidth() - WINDOW_WIDTH;
            environment.setCameraX(Math.min(newCameraX, maxCameraX));
        } else if (protagonistX < leftBoundary) {
            double newCameraX = cameraX - cameraSpeed;
            environment.setCameraX(Math.max(newCameraX, 0));
        }
    }

    private void verifyGameStatus() {
        if (environment.getProtagonist().isDead()) gameStatus = GameStatus.GAME_OVER;
        else if (currentRoom == 2 && environment.getAgents().isEmpty()) gameStatus = GameStatus.VICTORY;

        else if (currentRoom == 1 && environment.getAgents().isEmpty() && environment.getProtagonist().getPosX() >= (0.9 * environment.getWidth())) {
            System.out.println("Avançando para a sala do chefe...");
            Agent boss = new Agent(920, 440, 90, 65, 1, 500, "/images/agents/chonBot.png", true,false);
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
                        break;
                    }    
                }
                
                for (Agent agent : environment.getAgents()) {
                    agent.checkCollision(collision, environment);
                    if (collision.isDestroy()) break;
                }

                environment.getProtagonist().checkCollision(collision, environment);
                
                if (collision.isDestroy()) {
                    it.remove(); 
                }
            }
    }
}
