package chon.group;

import java.util.ArrayList;

import chon.group.game.Game;
import chon.group.game.GameSet;
import chon.group.game.GameStatus;
import chon.group.game.drawer.EnvironmentDrawer;
import chon.group.game.drawer.JavaFxDrawer;
import chon.group.game.drawer.JavaFxMediator;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import chon.group.game.core.menu.MainMenu;
import chon.group.game.core.menu.PauseMenu;

/**
 * The {@code Engine} class represents the main entry point of the application
 * and serves as the game engine for "Chon: The Learning Game."
 * 
 * It handles initialization of game components, graphical setup, input handling,
 * and the main game loop.
 */
public class Engine extends Application {

    /**
     * Main entry point of the application.
     * Launches the JavaFX application.
     *
     * @param args command-line arguments passed to the application.
     */
    public static void main(String[] args) {
        launch(args);
    }

    /** The current game setup including environment and entities */
    private GameSet gameSet;

    /** Responsible for drawing game graphics on the canvas */
    private JavaFxDrawer drawer;

    /** The main menu displayed at the start of the game */
    private MainMenu mainMenu;

    /** The pause menu displayed during the game */
    private PauseMenu menuPause;

    /** Stores the current active input keys */
    private ArrayList<String> input;

    /** Mediator responsible for connecting environment and graphical context */
    private EnvironmentDrawer mediator;

    /** The main game logic handler */
    private Game chonGame;

    /** The animation timer that drives the game loop */
    private AnimationTimer gameLoop;

    /**
     * Initializes the JavaFX stage, scene, canvas, menus, input handlers, 
     * and starts the game loop.
     *
     * @param theStage the primary stage for this application
     */
    @Override
    public void start(Stage theStage) {
        try {
            // Initialize the game set with environment and agents
            gameSet = new GameSet();

            // Create canvas and graphics context for rendering
            Canvas canvas = new Canvas(gameSet.getCanvasWidth(), gameSet.getCanvasHeight());
            GraphicsContext gc = canvas.getGraphicsContext2D();

            // Initialize game components and menus
            resetGame(gc);

            // Set up JavaFX scene graph
            StackPane root = new StackPane();
            Scene scene = new Scene(root, gameSet.getCanvasWidth(), gameSet.getCanvasHeight());
            theStage.setTitle("Hallowen");
            theStage.setScene(scene);

            root.getChildren().add(canvas);
            theStage.show();

            // Link menus to the game instance
            chonGame.setMainMenu(mainMenu);
            chonGame.setMenuPause(menuPause);

            // Handle key press events
            scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
                public void handle(KeyEvent e) {
                    chonGame.handleInput(e);

                    if (chonGame.wantsToStartGame()) {
                        chonGame.setWantsToStartGame(false); // consume the flag
                        resetGame(gc);
                        chonGame.setStatus(GameStatus.RUNNING);
                        mainMenu.reset();
                    }
                }
            });

            // Handle key release events
            scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
                public void handle(KeyEvent e) {
                    String code = e.getCode().toString();
                    if (!code.equals("P"))
                        input.remove(code);
                }
            });

            // Initialize and start the game loop using AnimationTimer
            gameLoop = new AnimationTimer() {
                public void handle(long now) {
                    chonGame.loop();
                }
            };
            gameLoop.start();

            theStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Resets the game state, including environment, drawer, menus, input,
     * and mediator.
     *
     * @param gc the GraphicsContext to draw on
     */
    private void resetGame(GraphicsContext gc) {
        gameSet = new GameSet();
        drawer = new JavaFxDrawer(gc, null);
        mainMenu = new MainMenu(drawer, new Image(getClass().getResourceAsStream("/images/environment/menu_background_new.png")));
        menuPause = new PauseMenu(drawer, gameSet.getEnvironment().getPauseImage());
        input = new ArrayList<>();
        mediator = new JavaFxMediator(gameSet.getEnvironment(), gc);
        chonGame = new Game(gameSet.getEnvironment(), mediator, input);
        chonGame.setMainMenu(mainMenu);
        chonGame.setMenuPause(menuPause);
    }
}
