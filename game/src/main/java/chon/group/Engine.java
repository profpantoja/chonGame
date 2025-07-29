package chon.group;

import java.util.ArrayList;
import chon.group.game.Game;
import chon.group.game.GameSet;
import chon.group.game.drawer.EnvironmentDrawer;
import chon.group.game.drawer.JavaFxMediator;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Engine extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage theStage) {
        try {
            GameSet gameSet = new GameSet();
            Canvas canvas = new Canvas(gameSet.getCanvasWidth(), gameSet.getCanvasHeight());
            GraphicsContext gc = canvas.getGraphicsContext2D();
            StackPane root = new StackPane();
            Scene scene = new Scene(root, gameSet.getCanvasWidth(), gameSet.getCanvasHeight());
            theStage.setTitle("The Mage");
            theStage.setScene(scene);
            root.getChildren().add(canvas);

            ArrayList<String> input = new ArrayList<>();
            scene.setOnKeyPressed(e -> {
                String code = e.getCode().toString();
                System.out.println("[DEBUG ENGINE] Tecla Pressionada: " + code); // <-- ESPIÃO AQUI
                if (!input.contains(code)) input.add(code);
            });
            scene.setOnKeyReleased(e -> {
                String code = e.getCode().toString();
                input.remove(code);
            });

            EnvironmentDrawer mediator = new JavaFxMediator(gameSet.getEnvironment(), gc, gameSet.getMainMenu());
            Game chonGame = new Game(gameSet, mediator, input);

            new AnimationTimer() {
                public void handle(long now) {
                    chonGame.loop();
                }
            }.start();

            theStage.show();
        } catch (Exception e) {
            System.err.println("!!! ERRO FATAL DURANTE A INICIALIZAÇÃO DO JOGO !!!");
            e.printStackTrace();
        }
    }
}