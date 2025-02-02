package chon.group;

import java.util.ArrayList;

import chon.group.game.domain.agent.Agent;
import chon.group.game.domain.environment.Environment;
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
            Environment environment = new Environment(0, 0, 1280, 780, "/images/environment/castle.png");
            Agent chonBota = new Agent(400, 390, 90, 65, 2, "/images/agents/chonBota.png");
            Agent chonBot = new Agent(920, 440, 90, 65, 1, "/images/agents/chonBot.png");
            
            environment.setProtagonist(chonBota);
            environment.getAgents().add(chonBot);
            
            Canvas canvas = new Canvas(environment.getWidth(), environment.getHeight());
            GraphicsContext gc = canvas.getGraphicsContext2D();
            environment.setGc(gc);
            
            StackPane root = new StackPane();
            Scene scene = new Scene(root, environment.getWidth(), environment.getHeight());
            theStage.setTitle("Chon: The Learning Game");
            theStage.setScene(scene);
            root.getChildren().add(canvas);
            theStage.show();
            
            ArrayList<String> input = new ArrayList<>();
            
            scene.setOnKeyPressed(event -> {
                String code = event.getCode().toString();
                if (!input.contains(code)) {
                    input.add(code);
                }
                
                // Se pressionar espaço, o protagonista pula
                if (code.equals("SPACE")) {
                    environment.getProtagonist().jump();
                }
            });
            
            scene.setOnKeyReleased(event -> input.remove(event.getCode().toString()));
            
            new AnimationTimer() {
                @Override
                public void handle(long now) {
                    if (!input.isEmpty()) {
                        environment.getProtagonist().move(input);
                        environment.checkBorders();
                    }
                    
                    environment.getAgents().get(0).chase(
                        environment.getProtagonist().getPosX(), 
                        environment.getProtagonist().getPosY()
                    );
                    
                    environment.detectCollision();
                    environment.drawBackground();
                    environment.drawAgents();
                }
            }.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}