package chon.group;

import java.util.ArrayList;
import java.util.List;

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

    // --- State and Core Components ---
    private GameStatus gameStatus = GameStatus.MAIN_MENU; // O jogo começa no menu principal
    private Environment environment;
    private JavaFxMediator mediator;
    private MainMenu mainMenu;
    private MenuPause menuPause;
    
    // --- Input Handling ---
    private final List<String> gameInput = new ArrayList<>();

    // --- Window Dimensions ---
    private static final double WINDOW_WIDTH = 1280;
    private static final double WINDOW_HEIGHT = 768;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage theStage) {
        theStage.setTitle("Chon: The Learning Game");

        // 1. Setup Canvas and Graphics
        Canvas canvas = new Canvas(WINDOW_WIDTH, WINDOW_HEIGHT);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // 2. Initialize Game Components using Setup class
        this.environment = Setup.createEnvironment();
        this.mediator = new JavaFxMediator(environment, gc);
        JavaFxDrawer drawer = new JavaFxDrawer(gc, null); // Drawer para os menus

        // 3. Initialize Menus
        Image menuBackground = Setup.loadImage("/images/environment/menu_background.png");
        this.mainMenu = new MainMenu(drawer, menuBackground);
        this.menuPause = new MenuPause(drawer, environment.getPauseImage());

        // 4. Configure Menu Actions (State Transitions)
        mainMenu.setOnStartGame(() -> {
            /* resetGame(); */ // Garante que o jogo reinicie do zero
            gameStatus = GameStatus.RUNNING;
        });
        mainMenu.setOnExit(() -> Platform.exit());
        // mainMenu.setOnOptions(...);

        menuPause.setOnResume(() -> gameStatus = GameStatus.RUNNING);
        menuPause.setOnExitToMenu(() -> gameStatus = GameStatus.MAIN_MENU);

        // 5. Setup Scene and Input
        StackPane root = new StackPane(canvas);
        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        theStage.setScene(scene);
        
        setupInputHandlers(scene);

        // 6. Start Game Loop
        new GameLoop().start();
        
        theStage.show();
    }

    /**
     * Configura um único handler de eventos de teclado que delega a ação
     * com base no estado atual do jogo.
     */
    private void setupInputHandlers(Scene scene) {
        scene.setOnKeyPressed((KeyEvent e) -> {
            String codeStr = e.getCode().toString();
            
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
                        menuPause.reset(); // Reseta a seleção do menu de pause
                    } else if (!gameInput.contains(codeStr)) {
                        gameInput.add(codeStr);
                    }
                    break;
                default:
                    // Em estados como GAME_OVER ou VICTORY, pode-se esperar por ENTER para reiniciar
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
   /*  private void resetGame() {
        this.environment = Setup.createEnvironment();
        // Recria o mediator para apontar para o novo ambiente
        this.mediator = new JavaFxMediator(this.environment, mediator.getDrawer().getGraphicsContext());
        gameInput.clear();
    } */


    /**
     * The main game loop wrapped in its own class for clarity.
     */
    private class GameLoop extends AnimationTimer {
        @Override
        public void handle(long currentNanoTime) {
            // Limpa a tela no início de cada frame
            mediator.clearEnvironmentSideScrolling();

            // Máquina de estados principal do jogo
            switch (gameStatus) {
                case MAIN_MENU:
                    mainMenu.draw();
                    break;
                case RUNNING:
                    updateGameLogic();
                    renderGameWorld();
                    // Verifica condições de vitória ou derrota para mudar o estado
                    if (environment.getProtagonist().isDead()) {
                        gameStatus = GameStatus.GAME_OVER;
                    } else if (environment.getAgents().isEmpty()) {
                        gameStatus = GameStatus.VICTORY;
                    }
                    break;
                case PAUSED:
                    renderGameWorld(); // Desenha o mundo do jogo estático no fundo
                    menuPause.draw(); // Desenha o menu de pause por cima
                    break;
                case GAME_OVER:
                    renderGameWorld(); // Desenha o último estado do jogo
                    mediator.drawGameOver();
                    break;
                case VICTORY:
                    renderGameWorld(); // Desenha o último estado do jogo
                    mediator.drawWinScreen();
                    break;
            }
        }
    }
    
    /**
     * Contém toda a lógica de atualização de estado dos elementos do jogo.
     */
    private void updateGameLogic() {
        // Movimento do Protagonista
        if (!gameInput.isEmpty()) {
            environment.getProtagonist().move(gameInput);
            updateCameraPosition();
            if (gameInput.contains("SPACE")) {
                // ... lógica de atirar ...
            }
            environment.checkBorders();
        }
        
        // IA dos Inimigos
        environment.getAgents().forEach(agent -> 
            agent.chase(environment.getProtagonist().getPosX(), environment.getProtagonist().getPosY()));

        // Atualizações do Ambiente
        environment.detectCollision();
        environment.updateShots();
        environment.updateMessages();
    }

    /**
     * Desenha todos os elementos visuais do jogo.
     */
    private void renderGameWorld() {
        mediator.drawBackgroundSideScrolling();
        mediator.drawAgentsSideScrolling();
        mediator.drawShotsSideScrolling();
        mediator.drawMessagesSideScrolling();
    }

    /**
     * Atualiza a posição da câmera para seguir o protagonista.
     */
    private void updateCameraPosition() {
        double protagonistX = environment.getProtagonist().getPosX();
        double cameraX = environment.getCameraX();
        double protagonistSpeed = environment.getProtagonist().getSpeed();

        double rightBoundary = cameraX + WINDOW_WIDTH * 0.80;
        double leftBoundary = cameraX + WINDOW_WIDTH * 0.15;

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