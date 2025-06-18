package chon.group.game.domain.environment;

import java.util.EnumSet;
import java.util.Set;
import chon.group.game.domain.agent.Agent;
import chon.group.game.domain.agent.Weapon;
import chon.group.game.domain.agent.Fireball;
import chon.group.game.drawer.EnvironmentDrawer;
import chon.group.game.drawer.JavaFxMediator;
import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;


public class Setup {

    private double windowWidth = 1280;
    private double windowHeight = 768;
    private Environment environment;
    private Agent chonBota;
    private EnvironmentDrawer mediator;
    private Scene scene;
    private Set<KeyboardKey> activeKeys = EnumSet.noneOf(KeyboardKey.class);
    private StatusGame currentGameStatus = StatusGame.RUNNING;

    public Setup(Stage theStage) {
        initializeGameObjects();
        setupScene(theStage);
        setupInputHandlers();
        startGameLoop();
    }

    private void initializeGameObjects() {
        environment = new Environment(0, 0, 4096, 768, "/images/environment/castle.png");
        chonBota = new Agent(100, 390, 90, 65, 5, 1000, "/images/agents/chonBota.png", false);
        Weapon fireball = new Fireball(400, 390, 0, 0, 3, 0, "", false);
        chonBota.setWeapon(fireball);

        Agent chonBot = new Agent(920, 440, 90, 65, 1, 500, "/images/agents/chonBot.png", true);
        
        environment.setProtagonist(chonBota);
        environment.getAgents().add(chonBot);
        environment.setPauseImage("/images/environment/pause.png");
        environment.setGameOverImage("/images/environment/gameover.png");
    }

    private void setupScene(Stage theStage) {
        Canvas canvas = new Canvas(windowWidth, windowHeight);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        mediator = new JavaFxMediator(environment, gc);

        StackPane root = new StackPane();
        scene = new Scene(root, windowWidth, windowHeight);
        theStage.setTitle("Chon: The Learning Game");
        theStage.setScene(scene);

        root.getChildren().add(canvas);
        theStage.show();
    }

    private void setupInputHandlers() {
        scene.setOnKeyPressed(e -> {
                KeyboardKey key = KeyboardKey.valueOf(e.getCode().toString());
                activeKeys.add(key); 

                if (key == KeyboardKey.P) {
                    togglePause();
                }
            
                System.out.println("Tecla mapeada: " + e.getCode());
            
        });

        scene.setOnKeyReleased(e -> {
                KeyboardKey key = KeyboardKey.valueOf(e.getCode().toString());
                activeKeys.remove(key);             
            }
        );
    }

    private void startGameLoop() {
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                mediator.clearEnvironment();

                switch (currentGameStatus) {
                    case RUNNING:
                        updateAndRenderGameState();
                        break;
                    case PAUSED:
                        renderPausedState();
                        break;
                    case GAME_OVER:
                        renderGameOver();
                        break;
                    default:
                        break;
                }
            }
        }.start();
    }
    
    private void updateAndRenderGameState() {
        if (environment.getProtagonist().getStatus() == StatusProtagonist.DEAD) {
            currentGameStatus = StatusGame.GAME_OVER;
            return; 
        }
        environment.getProtagonist().update();
        environment.getProtagonist().move(activeKeys);
        updateCameraPosition(); 

        if (activeKeys.contains(KeyboardKey.SPACE)) {
            fireWeapon();
            activeKeys.remove(KeyboardKey.SPACE); 
        }
        
        for (Agent agent : environment.getAgents()) {
            agent.update();
            agent.chase(environment.getProtagonist().getPosX(), environment.getProtagonist().getPosY());
        }
        environment.checkBorders();
        environment.detectCollision();
        environment.updateShots();
        environment.updateMessages();
        
        renderAll();
    }

    private void renderPausedState() {
        renderAll();
        mediator.drawPauseScreen();
    }



    private void renderGameOver() {
        environment.updateMessages();
        environment.updateShots();
        renderAll();
        mediator.drawGameOver();
    }

    private void renderAll() {
        mediator.drawBackgroundSideScrolling();
        mediator.drawAgentsSideScrolling();
        mediator.drawShotsSideScrolling();
        mediator.drawMessagesSideScrolling();
    }

    private void fireWeapon() {
        KeyboardKey direction = chonBota.isFlipped() ? KeyboardKey.LEFT : KeyboardKey.RIGHT;
    
        environment.getShots().add(
        chonBota.getWeapon().fire(chonBota.getPosX(), chonBota.getPosY(), direction)
    );
    }
    
    private void togglePause() {
        if (currentGameStatus == StatusGame.RUNNING) {
            currentGameStatus = StatusGame.PAUSED;
        } 
        else if (currentGameStatus == StatusGame.PAUSED) {
            currentGameStatus = StatusGame.RUNNING;
        }
    }

    private void updateCameraPosition() {
        double rightBoundary = environment.getCameraX() + windowWidth * 0.80;
        double leftBoundary = environment.getCameraX() + windowWidth * 0.15;
        double protagonistSpeed = environment.getProtagonist().getSpeed();

        if (environment.getProtagonist().getPosX() > rightBoundary) {
            double newCameraX = environment.getCameraX() + protagonistSpeed;
            double maxCameraX = environment.getWidth() - windowWidth;
            environment.setCameraX(Math.min(newCameraX, maxCameraX));
        } else if (environment.getProtagonist().getPosX() < leftBoundary) {
            double newCameraX = environment.getCameraX() - protagonistSpeed;
            environment.setCameraX(Math.max(newCameraX, 0));
        }
    }
}