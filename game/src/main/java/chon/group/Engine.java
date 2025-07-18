package chon.group;

import java.util.ArrayList;

import chon.group.game.Game;
import chon.group.game.GameSet;
import chon.group.game.GameStatus;
import chon.group.game.drawer.EnvironmentDrawer;
import chon.group.game.drawer.JavaFxDrawer;
import chon.group.game.drawer.JavaFxMediator;
import chon.group.game.menu.Menu;
import chon.group.game.menu.MenuOption;
import chon.group.game.menu.MenuPause;
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
    /* Game state variables */
    private GameStatus gameStatus = GameStatus.START;

    @Override
    public void start(Stage theStage) {
        try {
            GameSet gameSet = new GameSet();

            /* Set up the graphical canvas */
            Canvas canvas = new Canvas(gameSet.getCanvasWidth(), gameSet.getCanvasHeight());
            GraphicsContext gc = canvas.getGraphicsContext2D();

            /* Set up the scene and stage */
            StackPane root = new StackPane();
            Scene scene = new Scene(root, gameSet.getCanvasWidth(), gameSet.getCanvasHeight());
            theStage.setTitle("Chon: The Learning Game");
            theStage.setScene(scene);

            root.getChildren().add(canvas);

            /* Initialize the game menus */            
            JavaFxDrawer drawer = new JavaFxDrawer(gc, null);
            Menu mainMenu = new Menu(drawer, new Image(getClass().getResourceAsStream("/images/environment/menu_background_new.png")));
            MenuPause menuPause = new MenuPause(drawer,gameSet.getEnvironment().getPauseImage());

            /* Handle keyboard input */
            ArrayList<String> input = new ArrayList<>();
            scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
                public void handle(KeyEvent e) {
                   switch (gameStatus) {
                        case START:
                            MenuOption.Main mainOpt = mainMenu.handleInput(e.getCode());
                            if (mainOpt == MenuOption.Main.START_GAME) { 
                                gameStatus = GameStatus.RUNNING; mainMenu.reset(); 
                            }
                            else if (mainOpt == MenuOption.Main.EXIT) 
                            javafx.application.Platform.exit();
                            break;
                        case PAUSED:
                            MenuOption.Pause pauseOpt = menuPause.handleInput(e.getCode());
                            if (pauseOpt == MenuOption.Pause.RESUME) {
                                gameStatus = GameStatus.RUNNING; menuPause.reset(); 
                            }
                            else if (pauseOpt == MenuOption.Pause.GO_BACK_TO_MENU) { 
                                gameStatus = GameStatus.START; menuPause.reset(); mainMenu.reset(); 
                            }
                            break;
                        case RUNNING:
                            if (e.getCode().toString().equals("P")) { 
                                gameStatus = GameStatus.PAUSED; menuPause.reset(); 
                            }
                            else if (!input.contains(e.getCode().toString()))
                                input.add(e.getCode().toString());
                            break;
                        default: break;
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

            EnvironmentDrawer mediator = new JavaFxMediator(gameSet.getEnvironment(), gc);
            Game chonGame = new Game(gameSet.getEnvironment(), mediator, input);
            /* Start the game loop */
            new AnimationTimer() {
                @Override
                public void handle(long now) {
                    switch (gameStatus) {
                        case START: mainMenu.draw(); break;
                        case PAUSED: menuPause.draw(); break;
                        case RUNNING: chonGame.loop(); break;
                        case WIN: break;
                        case GAME_OVER: break;
                    }
                }
            }.start();
            theStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

        
    }

}