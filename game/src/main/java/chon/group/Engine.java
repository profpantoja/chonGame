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
import chon.group.game.domain.environment.MenuOption;
import chon.group.game.domain.environment.MenuPause;
import chon.group.game.domain.environment.Setup;
import chon.group.game.domain.environment.SoundManager;
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

public class Engine extends Application {

    private GameStatus gameStatus = GameStatus.MAIN_MENU;
    private Environment environment;
    private JavaFxMediator mediator;
    private MainMenu mainMenu;
    private MenuPause menuPause;
    private GraphicsContext graphicsContext; 
    private final List<String> gameInput = new ArrayList<>();

    private static final double WINDOW_WIDTH = 1280;
    private static final double WINDOW_HEIGHT = 768;

    private List<Environment> levels;
    private int currentLevelIndex;

    long shotNow;
    private boolean canSlash = true;
    private boolean drawHitboxes = true;
    private boolean victoryMusicPlayed = false;
    private boolean gameOverMusicPlayed = false;

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage theStage) {
    // Setup Canvas and Graphics
    theStage.setTitle("Chon: The Learning Game");
    Canvas canvas = new Canvas(WINDOW_WIDTH, WINDOW_HEIGHT);
    this.graphicsContext = canvas.getGraphicsContext2D(); 

    resetGame();

    this.mediator = new JavaFxMediator(environment, this.graphicsContext); 
    JavaFxDrawer drawer = new JavaFxDrawer(this.graphicsContext, null); 

    // Initialize Menus
    Image menuBackground = Setup.loadImage("/images/environment/menu_background_new.png");
    this.mainMenu = new MainMenu(drawer, menuBackground);
    this.menuPause = new MenuPause(drawer, environment.getPauseImage());

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
    scene.setOnKeyPressed(this::handleKeyPressed);
    scene.setOnKeyReleased(this::handleKeyReleased);
}

private void handleKeyPressed(KeyEvent e) {
    String key = e.getCode().toString();
    System.out.println("Key pressed: " + key); 
    switch (gameStatus) {
        case MAIN_MENU:
            MenuOption.Main mainOption = mainMenu.handleInput(e.getCode());
            if (mainOption != null) {
                switch (mainOption) {
                    case START_GAME:
                        resetGame();
                        gameStatus = GameStatus.RUNNING;
                        break;
                    case EXIT:
                        Platform.exit();
                        break;
                }
            }
            break;
        case PAUSED:
            MenuOption.Pause pauseOption = menuPause.handleInput(e.getCode());
            if (pauseOption != null) {
                switch (pauseOption) {
                    case RESUME:
                        gameStatus = GameStatus.RUNNING;
                        restoreAgentsState(false);
                        break;
                    case GO_BACK_TO_MENU:
                        mainMenu.reset();
                        gameStatus = GameStatus.MAIN_MENU;
                        restoreAgentsState(false);
                        break;
                }
            }
            break;
        case RUNNING:
            if (e.getCode().toString().equals("P")) {
                gameStatus = GameStatus.PAUSED;
                menuPause.reset();
                restoreAgentsState(true);
            } else if (!gameInput.contains(key)) {
                gameInput.add(key);
            }
            break;
        default:
            if (e.getCode().toString().equals("ENTER")) {
                gameStatus = GameStatus.MAIN_MENU;
                mainMenu.reset();
                restoreAgentsState(false);
            }
            break;
    }
}

private void handleKeyReleased(KeyEvent e) {
    if (gameStatus == GameStatus.RUNNING) {
        gameInput.remove(e.getCode().toString());
    }
}

   /**
 * Centraliza a lógica de pausar/despausar agentes e protagonista.
 */
private void restoreAgentsState(boolean pause) {
    environment.getProtagonist().setCheckMenu(pause);
    environment.getProtagonist().setlastHitTime(System.currentTimeMillis());

    List<Agent> agents = environment.getAgents();
    for (int i = 0; i < agents.size(); i++) {
        Agent agent = agents.get(i);
        agent.setCheckMenu(pause);
        agent.setlastHitTime(System.currentTimeMillis());
    }
}

    private void resetGame() {
        this.levels = Setup.createLevels();
        this.currentLevelIndex = 0;
        this.environment = this.levels.get(currentLevelIndex);
        this.mediator = new JavaFxMediator(this.environment, this.graphicsContext);
        setHitboxesVisibility(drawHitboxes);
        gameInput.clear();
        SoundManager.playMusic("/sounds/zelda.wav");
    }

    /**
     * The main game loop 
     */
    private class GameLoop extends AnimationTimer {
        public void handle(long currentNanoTime) {
            graphicsContext.clearRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
  switch (gameStatus) {
            case MAIN_MENU: 
                SoundManager.playMusic("sounds/menuSound.wav");
                mainMenu.draw(); 
                victoryMusicPlayed = false;
                gameOverMusicPlayed = false;
                break;
            case RUNNING:
                updateGameLogic();
                renderGameWorld();
                SoundManager.resumeMusic();
                SoundManager.resumeAllSoundEffects();
                break;
            case PAUSED:
                SoundManager.pauseMusic();
                SoundManager.pauseAllSoundEffects();
                renderGameWorld();
                menuPause.draw();
                break;
            case GAME_OVER:
                if (!gameOverMusicPlayed) {
                    SoundManager.stopAll();
                    SoundManager.playSound("/sounds/zelda.wav"); // coloque o caminho correto do som de derrota
                    gameOverMusicPlayed = true;
                }
                renderGameWorld(); 
                mediator.drawGameOver();
                break;
            case VICTORY:
                if (!victoryMusicPlayed) {
                    SoundManager.stopAll();
                    SoundManager.playSound("/sounds/zelda.wav"); // coloque o caminho correto do som de vitória
                    victoryMusicPlayed = true;
                }
                renderGameWorld(); 
                mediator.drawWinScreen();
                break;
        }
    }
    }
    
    private void updateGameLogic() {
        verifyGameStatus();
        if(gameStatus != GameStatus.RUNNING) return;

        for(Agent agent : environment.getAgents()) agent.updateHitboxPosition();
        if(environment.getProtagonist() != null && environment.getProtagonist().getHitbox() != null) {
            environment.getProtagonist().updateHitboxPosition();
        }

        Agent protagonist = environment.getProtagonist();
        if (!gameInput.isEmpty()) {
            boolean isMoving = gameInput.contains("RIGHT") || gameInput.contains("LEFT") || gameInput.contains("UP") || gameInput.contains("DOWN");
            if(isMoving && !protagonist.isInvulnerable()) {
                protagonist.setAnimation("/images/agents/Link_Running.png", 6, 75);
            }
            
            updateCameraPosition();
            
            if (gameInput.contains("SPACE")) {
                shotNow = System.currentTimeMillis();
                protagonist.setAnimation("/images/agents/Link_Attack.png", 4, 75);
                gameInput.remove("SPACE");
                
                String direction = protagonist.isFlipped() ? "LEFT" : "RIGHT";
                int widthAnimation = protagonist.getPosX();
                if (!protagonist.isFlipped()) {
                    widthAnimation += protagonist.getWidth() - protagonist.getWeapon().getShotWidth();
                }
                environment.getSlashes().add(protagonist.getCloseWeapon().slash(protagonist.getPosX(), protagonist.getPosY(), direction));
                Shot shot = protagonist.getWeapon().fire(widthAnimation, protagonist.getPosY(), direction);
                if (shot != null) {
                    environment.getShots().add(shot);
                }
                /* else {
                    String currentSheet = protagonist.getCurrentSpritesheet();
                    boolean isAttacking = currentSheet != null && currentSheet.equals("/images/agents/Link_Attack.png");
                    
                    if ((!isAttacking || (System.currentTimeMillis() - shotNow) >= 360) && !protagonist.isInvulnerable()) {
                         if (currentSheet == null || !currentSheet.equals("/images/agents/Link_Standing.png")) {
                            protagonist.setAnimation("/images/agents/Link_Standing.png", 4, 150);
                        }
                    }
                }
                 */
            }
            
            protagonist.moveGravity(gameInput);
            environment.checkBorders();
        }
        
        // method chase for each agent
        List<Agent> agents = environment.getAgents();
        for (int i = 0; i < agents.size(); i++) {
            Agent agent = agents.get(i);
            if (agent.getHitbox() != null && environment.getProtagonist().getHitbox() != null) {
               agent.chase(environment.getProtagonist().getHitbox().getPosX(), environment.getProtagonist().getHitbox().getPosY());
           }
        }
        
        protagonist.updateAnimation();

        // update all agents and their shots
        checkCollisions();
        environment.detectCollision();
        environment.updateSlashes();
        environment.updateShots();
        environment.updateMessages();
    }

    private void renderGameWorld() {
        mediator.drawBackgroundSideScrolling();
        mediator.drawAgentsSideScrolling();
        mediator.drawShotsSideScrolling();
        mediator.drawMessagesSideScrolling();
        mediator.drawSlashes();
    }

    private void updateCameraPosition() {
        double protagonistX = environment.getProtagonist().getPosX();
        double cameraX = environment.getCameraX();
        double protagonistSpeed = environment.getProtagonist().getSpeed();
        double cameraSpeed = protagonistSpeed * 2.0;
        double rightBoundary = cameraX + WINDOW_WIDTH * 0.80;
        double leftBoundary = cameraX + WINDOW_WIDTH * 0.10;

        if (protagonistX > rightBoundary) {
            double newCameraX = cameraX + cameraSpeed;
            double maxCameraX = environment.getWidth() - WINDOW_WIDTH;
            environment.setCameraX(Math.min(newCameraX, maxCameraX));
        } else if (protagonistX < leftBoundary) {
            double newCameraX = cameraX - cameraSpeed;
            environment.setCameraX(Math.max(newCameraX, 0));
        }
    }

    /**
     * AJUSTE: Lógica de vitória corrigida.
     * A condição de vitória prematura foi removida.
     */
    private void verifyGameStatus() {
        // 1. Verifica se o jogador perdeu (maior prioridade)
        if (environment.getProtagonist().isDead()) {
            gameStatus = GameStatus.GAME_OVER;
            return;
        }
        
        // A CONDIÇÃO DE VITÓRIA PREMATURA FOI REMOVIDA DAQUI.
        // else if(environment.getAgents().isEmpty()){ ... } <-- LINHA REMOVIDA

        // 2. Verifica se o jogador completou as condições para trocar de nível
        if (environment.levelChanger(currentLevelIndex == levels.size() - 1)) {
            currentLevelIndex++;
            if (currentLevelIndex >= levels.size()) {
                gameStatus = GameStatus.VICTORY;
            } else {
                // Caso contrário, carrega o próximo nível
                System.out.println("Trocando para o nível: " + (currentLevelIndex + 1));
                this.environment = this.levels.get(currentLevelIndex);
                this.mediator = new JavaFxMediator(this.environment, this.graphicsContext);
                this.environment.getProtagonist().setPosX(100);
                this.environment.getProtagonist().setPosY(390);
                this.environment.setCameraX(0);
                setHitboxesVisibility(drawHitboxes);
            }
        }
        else if (environment.levelChanger()) {
            currentLevelIndex++;
            
            // 3. Se o índice do nível for maior ou igual ao número de níveis, o jogador venceu o jogo.
            if (currentLevelIndex >= levels.size()) {
                gameStatus = GameStatus.VICTORY;
            } else {
                // Caso contrário, carrega o próximo nível
                System.out.println("Trocando para o nível: " + (currentLevelIndex + 1));
                this.environment = this.levels.get(currentLevelIndex);
                this.mediator = new JavaFxMediator(this.environment, this.graphicsContext);
                this.environment.getProtagonist().setPosX(100);
                this.environment.getProtagonist().setPosY(390);
                this.environment.setCameraX(0);
                setHitboxesVisibility(drawHitboxes);
            }
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
                    if (collision.isDestroy()) break;    
                }
                for (Agent agent : environment.getAgents()) {
                    agent.checkCollision(collision, environment);
                    if (collision.isDestroy()) break;
                }
                environment.getProtagonist().checkCollision(collision, environment);
                if (collision.isDestroy()) it.remove(); 
            }
    }
    
    private void setHitboxesVisibility(boolean visible) {
        if (environment == null) return;
        for(Agent agent : environment.getAgents()) {
            if (agent.getHitbox() != null) agent.getHitbox().setDrawHitbox(visible);
        }
        if (environment.getProtagonist() != null && environment.getProtagonist().getHitbox() != null) {
            environment.getProtagonist().getHitbox().setDrawHitbox(visible);
        }
    }
}