package chon.group;

import java.util.ArrayList;

import chon.group.game.domain.agent.Agent;
import chon.group.game.domain.agent.Cannon;
import chon.group.game.domain.agent.Fireball;
import chon.group.game.domain.agent.Weapon;
import chon.group.game.domain.environment.Environment;
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

/**
 * The {@code Engine} class represents the main entry point of the application
 * and serves as the game engine for "Chon: The Learning Game."
 */
public class Engine extends Application {

    private boolean isPaused = false;
    private long lastEnergyUpdate = 0;
    private static final long ENERGY_UPDATE_INTERVAL = 16_000_000; // 16ms (≈60fps)

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage theStage) {
        try {
            /* Initialize the game environment and agents */
            Environment environment = new Environment(0, 0, 1280, 780, "/images/environment/castle.png");
            Agent chonBota = new Agent(400, 390, 90, 65, 3, 1000, "/images/agents/chonBota.png", false);
            Weapon cannon = new Cannon(400, 390, 0, 0, 3, 0, "", false);
            Weapon fireball = new Fireball(400, 390, 0, 0, 3, 0, "", false);
            chonBota.setWeapon(fireball);

            Agent chonBot = new Agent(920, 440, 90, 65, 1, 500, "/images/agents/chonBot.png", true);
            environment.setProtagonist(chonBota);
            environment.getAgents().add(chonBot);
            environment.setPauseImage("/images/environment/pause.png");
            environment.setGameOverImage("/images/environment/gameover.png");

            /* Set up the graphical canvas */
            Canvas canvas = new Canvas(environment.getWidth(), environment.getHeight());
            GraphicsContext gc = canvas.getGraphicsContext2D();
            EnvironmentDrawer mediator = new JavaFxMediator(environment, gc);

            /* Set up the scene and stage */
            StackPane root = new StackPane();
            Scene scene = new Scene(root, environment.getWidth(), environment.getHeight());
            theStage.setTitle("Chon: The Learning Game");
            theStage.setScene(scene);

            root.getChildren().add(canvas);
            theStage.show();

            /* Handle keyboard input */
            ArrayList<String> input = new ArrayList<String>();
            scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
                public void handle(KeyEvent e) {
                    String code = e.getCode().toString();
                    input.clear();

                    if (code.equals("P")) {
                        isPaused = !isPaused;
                    }
                    else if (code.equals("E") && !environment.getProtagonist().isEnergyDepleted()) {
                        // Habilidade especial que consome energia
                        environment.getProtagonist().consumeEnergy(0.3);
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

            /* Start the game loop */
            new AnimationTimer() {
                @Override
                public void handle(long now) {
                    mediator.clearEnvironment();
                    
                    /* Energy system update */
                    if (now - lastEnergyUpdate >= ENERGY_UPDATE_INTERVAL) {
                        updateEnergySystem(environment, input);
                        lastEnergyUpdate = now;
                    }

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
                            handleGameplay(environment, input, chonBota);
                            mediator.drawBackground();
                            mediator.drawAgents();
                            mediator.drawShots();
                            mediator.drawMessages();
                        }
                    }
                }
            }.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateEnergySystem(Environment environment, ArrayList<String> input) {
        Agent protagonist = environment.getProtagonist();
        
        // Consome energia ao se mover
        if (!input.isEmpty()) {
            protagonist.consumeEnergy(0.005);
        } 
        // Recupera energia quando parado
        else {
            protagonist.recoverEnergy(0.003);
        }
        
        // Penalidade quando energia acaba
        if (protagonist.isEnergyDepleted()) {
            protagonist.takeDamage(1, environment.getMessages());
        }
    }

    private void handleGameplay(Environment environment, ArrayList<String> input, Agent protagonist) {
        /* Update the protagonist's movements if input exists */
        if (!input.isEmpty()) {
            if (input.contains("SPACE")) {
                input.remove("SPACE");
                String direction = protagonist.isFlipped() ? "LEFT" : "RIGHT";
                environment.getShots().add(protagonist.getWeapon().fire(
                    protagonist.getPosX(),
                    protagonist.getPosY(),
                    direction));
            }
            protagonist.move(input);
            environment.checkBorders();
        }
        
        /* Update other agents' movements */
        for (Agent agent : environment.getAgents()) {
            agent.chase(protagonist.getPosX(), protagonist.getPosY());
        }
        
        environment.detectCollision();
        environment.updateShots();
        environment.updateMessages();
    }
}