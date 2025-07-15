package chon.group;

import java.util.ArrayList;
import java.util.List;

import chon.group.game.Game;
import chon.group.game.core.agent.Agent;
import chon.group.game.core.agent.Object;
import chon.group.game.core.weapon.Weapon;
import chon.group.game.domain.environment.Environment;
import chon.group.game.domain.weapon.Cannon;
import chon.group.game.domain.weapon.Lancer;
import chon.group.game.drawer.EnvironmentDrawer;
import chon.group.game.drawer.JavaFxMediator;
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

    private boolean isPaused = false;

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
            /* Define some size properties for both Canvas and Environment */
            double canvasWidth = 1280;
            double canvasHeight = 780;
            int worldWidth = 8024;

            /* Initialize the game environment, agents and weapons */
            Environment environment = new Environment(0, 0, 780, worldWidth,
                    canvasWidth, "/images/environment/castleLong.png");
            Agent chonBota = new Agent(400, 390, 90, 65, 3, 1000, "/images/agents/chonBota.png", false);
            Weapon cannon = new Cannon(400, 390, 0, 0, 3, 0, 0.05, "", false);
            Weapon lancer = new Lancer(400, 390, 0, 0, 3, 0, 0.05, "", false);

            chonBota.setWeapon(cannon);
            chonBota.setWeapon(lancer);

            Agent chonBot = new Agent(920, 440, 90, 65, 1, 500, "/images/agents/chonBot.png", true);
            environment.setProtagonist(chonBota);
            environment.getAgents().add(chonBot);
            environment.setPauseImage("/images/environment/pause.png");
            environment.setGameOverImage("/images/environment/gameover.png");

            /* Set up some collectable objects */
            List<Object> objects = new ArrayList<>();
            objects.add(new Object(200, 350, 32, 32, "/images/agents/coin.png", true, false));
            objects.add(new Object(400, 380, 32, 32, "/images/agents/coin.png", true, false));
            objects.add(new Object(1000, 600, 32, 32, "/images/agents/coin.png", true, false));
            objects.add(new Object(1400, 380, 32, 32, "/images/agents/coin.png", true, false));
            objects.add(new Object(1800, 650, 32, 32, "/images/agents/coin.png", true, false));
            objects.add(new Object(2000, 580, 32, 32, "/images/agents/coin.png", true, false));
            objects.add(new Object(2300, 380, 32, 32, "/images/agents/coin.png", true, false));
            objects.add(new Object(2600, 500, 32, 32, "/images/agents/coin.png", true, false));
            objects.add(new Object(2900, 380, 32, 32, "/images/agents/coin.png", true, false));
            objects.add(new Object(2950, 400, 32, 32, "/images/agents/coin.png", true, false));
            environment.setObjects(objects);

            /* Set up the graphical canvas */
            Canvas canvas = new Canvas(canvasWidth, canvasHeight);
            GraphicsContext gc = canvas.getGraphicsContext2D();
            EnvironmentDrawer mediator = new JavaFxMediator(environment, gc);

            /* Set up the scene and stage */
            StackPane root = new StackPane();
            Scene scene = new Scene(root, canvasWidth, canvasHeight);
            theStage.setTitle("Chon: The Learning Game");
            theStage.setScene(scene);

            root.getChildren().add(canvas);

            /* Handle keyboard input */
            ArrayList<String> input = new ArrayList<>();
            scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
                public void handle(KeyEvent e) {
                    String code = e.getCode().toString();
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
                    input.remove(code);
                }
            });

            Game chonGame = new Game(environment, mediator, input);
            /* Start the game loop */
            new AnimationTimer() {
                @Override
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