package chon.group;

import java.util.ArrayList;
import java.util.List;

import chon.group.game.domain.agent.Agent;
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
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;

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
                } else if (!gameInput.contains(key)) {
                    gameInput.add(key);
                }
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
                    // Verify if the game is over or if the player has won
                    if (environment.getProtagonist().isDead()) {
                        gameStatus = GameStatus.GAME_OVER;
                    } else if (environment.getAgents().isEmpty()) {
                        gameStatus = GameStatus.VICTORY;
                    }
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

        // update all agents and their shots
        environment.detectCollision();
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
}
