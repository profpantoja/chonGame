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
 */

public class Engine extends Application {

    /**
     * Main entry point of the application.
     *
     * @param args command-line arguments passed to the application.
     */
    public static void main(String[] args) {
        launch(args);
    }

    /*
    * The following variables are used to manage the game state and graphical on the method resetGame
    */
    private GameSet gameSet;
    private JavaFxDrawer drawer;
    private MainMenu mainMenu;
    private PauseMenu menuPause;
    private ArrayList<String> input;
    private EnvironmentDrawer mediator;
    private Game chonGame;
    private AnimationTimer gameLoop;


    @Override
    public void start(Stage theStage) {
        try {

            /* 
             * Initialize the game set, which contains the environment and other game components.
            */
            gameSet = new GameSet();

            /* Set up the graphical canvas */
            Canvas canvas = new Canvas(gameSet.getCanvasWidth(), gameSet.getCanvasHeight());
            GraphicsContext gc = canvas.getGraphicsContext2D();
            
            /*  Reset the game state and initialize the game components */
            resetGame(gc);

            /* Set up the scene and stage */
            StackPane root = new StackPane();
            Scene scene = new Scene(root, gameSet.getCanvasWidth(), gameSet.getCanvasHeight());
            theStage.setTitle("Chon: The Learning Game");
            theStage.setScene(scene);

            root.getChildren().add(canvas);

            /* Set the main menu and pause menu for the game. */
            chonGame.setMainMenu(mainMenu);
            chonGame.setMenuPause(menuPause);

            scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
                public void handle(KeyEvent e) {                    
                    
                    chonGame.handleInput(e);

                    if (chonGame.wantsToStartGame()) {
                        chonGame.setWantsToStartGame(false); // consome a flag
                        resetGame(gc);
                        chonGame.setStatus(GameStatus.RUNNING);
                        mainMenu.reset();
                    }
                }
        });

            scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
                public void handle(KeyEvent e) {
                    String code = e.getCode().toString();
                    if (!code.equals("P"))
                        input.remove(code);
                }
            });

            // Start the game loop
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