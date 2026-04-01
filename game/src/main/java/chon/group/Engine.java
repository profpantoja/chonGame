package chon.group;

import java.util.ArrayList;

import chon.group.game.Game;
import chon.group.game.GameSet;
import chon.group.game.drawer.client.JavaFxDrawer;
import chon.group.game.drawer.service.GameDrawer;
import chon.group.game.drawer.service.GameMediator;
import chon.group.game.sound.client.JavaFxPlayer;
import chon.group.game.sound.service.GameSoundManager;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
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

            /* Handle keyboard input */
            ArrayList<String> input = new ArrayList<>();
            scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
                public void handle(KeyEvent e) {
                    String code = e.getCode().toString();
                    if (!input.contains(code)) {
                        input.add(code);
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

            GameSoundManager soundManager = new GameSoundManager(new JavaFxPlayer());
            GameDrawer mediator = new GameMediator(new JavaFxDrawer(gc));

            Game chonGame = new Game(
                    gameSet.getEnvironment(),
                    soundManager,
                    mediator,
                    gameSet.getMenu(),
                    input,
                    0);

            // Start the game loop
            new AnimationTimer() {
                public void handle(long now) {
                    chonGame.loop();
                }
            }.start();

            theStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}