package chon.group;

import java.util.ArrayList;
import chon.group.game.core.agent.Agent;
import chon.group.game.core.weapon.Weapon;
import chon.group.game.domain.environment.Environment;
import chon.group.game.domain.weapon.Cannon;
import chon.group.game.domain.weapon.Lancer;
import chon.group.game.drawer.EnvironmentDrawer;
import chon.group.game.drawer.JavaFxMediator;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;

public class Engine extends Application {

    private boolean isPaused = false;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage theStage) {
        try {
            
            double windowWidth = 1280;
            double windowHeight = 768;
            int worldWidth = 4096; 

            
            Environment environment = new Environment(0, 0, worldWidth, (int)windowHeight, "/images/environment/castle.png", windowWidth);
            
            Agent chonBota = new Agent(400, 390, 90, 65, 5, 1000, "/images/agents/chonBota.png", false);
            Weapon cannon = new Cannon(400, 390, 0, 0, 3, 0, "", false);
            Weapon lancer = new Lancer(400, 390, 0, 0, 3, 0, "", false);
            chonBota.setWeapon(lancer); 

            Agent chonBot = new Agent(920, 440, 90, 65, 1, 500, "/images/agents/chonBot.png", true);
            environment.setProtagonist(chonBota); 
            environment.getAgents().add(chonBot);
            environment.setPauseImage("/images/environment/pause.png");
            environment.setGameOverImage("/images/environment/gameover.png");

            
            Canvas canvas = new Canvas(windowWidth, windowHeight);
            GraphicsContext gc = canvas.getGraphicsContext2D();
            EnvironmentDrawer mediator = new JavaFxMediator(environment, gc);

            StackPane root = new StackPane();
            Scene scene = new Scene(root, windowWidth, windowHeight);
            theStage.setTitle("Chon: The Learning Game - Side Scrolling");
            theStage.setScene(scene);

            root.getChildren().add(canvas);
            
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

            new AnimationTimer() {
                @Override
                public void handle(long arg0) {
                    mediator.clearEnvironment();
                    
                    if (environment.getProtagonist().isDead()) {
                        environment.updateMessages();
                        environment.updateShots();
                        mediator.drawBackground();
                        mediator.drawAgents();
                        mediator.drawShots();
                        mediator.drawMessages();
                        mediator.drawGameOver();
                    } else {
                        if (isPaused) {
                            mediator.drawBackground();
                            mediator.drawAgents();
                            mediator.drawMessages();
                            mediator.drawShots();
                            mediator.drawPauseScreen();
                        } else {
                            if (!input.isEmpty()) {
                                if (input.contains("SPACE")) {
                                    String direction = chonBota.isFlipped() ? "LEFT" : "RIGHT";
                                    environment.getShots().add(chonBota.getWeapon().fire(chonBota.getPosX(), chonBota.getPosY(), direction));
                                    input.remove("SPACE"); 
                                }
                                environment.getProtagonist().move(input);
                                environment.checkBorders();
                            }
                            
                            for (Agent agent : environment.getAgents()) {
                                agent.chase(environment.getProtagonist().getPosX(), environment.getProtagonist().getPosY());
                            }
                            
                            environment.detectCollision();
                            environment.updateShots();
                            environment.updateMessages();
                            
                            environment.updateCamera();

                            mediator.drawBackground();
                            mediator.drawAgents();
                            mediator.drawShots();
                            mediator.drawMessages();
                        }
                    }
                }
            }.start();
            theStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}