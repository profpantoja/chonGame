package chon.group;

import java.util.ArrayList;

import chon.group.game.domain.Projectile.Projectile;
import chon.group.game.domain.agent.Agent;
import chon.group.game.domain.environment.Environment;
import chon.group.game.drawer.EnvironmentDrawer;
import chon.group.game.drawer.JavaFxMediator;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
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
    private final int ZOMBIES_TO_SPAWN = 2;
    private long startTime;
    private final long GAME_DURATION = 1 * 60 * 1000; // 2 minutos em milissegundos
    private boolean gameOver = false; // Agora controla quando o jogo termina

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage theStage) {
        try {
            // Setup do ambiente e personagens
            Environment environment = new Environment(0, 0, 1280, 780, "/images/environment/forest.png");
            Agent jon = new Agent(400, 390, 165, 68, 5, 200, "/images/agents/jon.png", true);
            environment.setProtagonist(jon);
            environment.setPauseImage("/images/environment/pause.png");
            environment.setImagevictory("/images/environment/victory.png");
            environment.setImageGameOver("/images/environment/gameover.png");

            spawnZombies(environment, 2);

            ArrayList<Projectile> projectiles = new ArrayList<>();
            Canvas canvas = new Canvas(environment.getWidth(), environment.getHeight());
            GraphicsContext gc = canvas.getGraphicsContext2D();
            EnvironmentDrawer mediator = new JavaFxMediator(environment, gc);

            StackPane root = new StackPane();
            Scene scene = new Scene(root, environment.getWidth(), environment.getHeight());
            theStage.setTitle("Chon: The Learning Game");
            theStage.setScene(scene);
            root.getChildren().add(canvas);
            theStage.show();

            ArrayList<String> input = new ArrayList<>();
            scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
                public void handle(KeyEvent e) {
                    String code = e.getCode().toString();
                    input.clear();

                    if (code.equals("P") && !gameOver) { 
                        isPaused = !isPaused;
                    }

                    if (code.equals("Q") && !isPaused && !gameOver) { 
                        Agent protagonist = environment.getProtagonist();
                        String direction = protagonist.isFlipped() ? "LEFT" : "RIGHT";
                        projectiles.add(new Projectile(protagonist.getPosX(), protagonist.getPosY(), 10, 30, direction, "/images/projectiles/bala.png"));
                    }

                    if (!isPaused && !gameOver && !input.contains(code)) {
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

            // Timer para spawn de zumbis
            Timeline zombieSpawnTimer = new Timeline(new KeyFrame(Duration.seconds(10), event -> spawnZombies(environment, ZOMBIES_TO_SPAWN)));
            zombieSpawnTimer.setCycleCount(Timeline.INDEFINITE);
            zombieSpawnTimer.play();

            // Tempo de jogo - 2 minutos
            startTime = System.currentTimeMillis();
            Timeline gameTimer = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
                long elapsedTime = System.currentTimeMillis() - startTime;
                if (elapsedTime >= GAME_DURATION && !gameOver) {
                    gameOver = true; // Define o estado do jogo como finalizado após 2 minutos
                }
            }));
            gameTimer.setCycleCount(Timeline.INDEFINITE);
            gameTimer.play();

            new AnimationTimer() {
                @Override
                public void handle(long arg0) {
                    mediator.clearEnvironment();

                    if (isPaused) {
                        mediator.drawBackground();
                        mediator.drawAgents();
                        mediator.drawPauseScreen();
                        return;
                    }

                    if (environment.getProtagonist().getHealth() <= 0) {
                        gameOver = true;
                    }

                    if (gameOver) {
                        mediator.drawGameOverScreen();
                        return; // Para a execução do jogo
                    }

                    if (gameOver) {
                        mediator.drawBackground();
                        mediator.drawVictoryScreen();
                        return; // Se o jogo acabou, nada mais acontece
                    }

                    // Se o jogo ainda está rodando, segue normalmente
                    if (!input.isEmpty()) {
                        environment.getProtagonist().move(input);
                        environment.checkBorders();
                    }

                    for (Agent agent : environment.getAgents()) {
                        agent.chase(environment.getProtagonist().getPosX(), environment.getProtagonist().getPosY());
                        environment.checkAndAdjustPosition(agent);
                    }    

                    mediator.drawBackground();
                    mediator.drawAgents();
                    environment.checkAndAdjustPosition(environment.getProtagonist());

                    for (Agent agent : environment.getAgents()) {
                        environment.checkAndAdjustPosition(agent);
                    }

                    environment.detectCollision();

                    ArrayList<Projectile> toRemoveProjectiles = new ArrayList<>();
                    for (Projectile p : projectiles) {
                        p.move();
                        mediator.drawProjectile(p);

                        for (Agent enemy : environment.getAgents()) {
                            if (p.checkCollision(enemy)) {
                                enemy.takeDamage(p.getDamage());
                                p.deactivate();
                            }
                        }

                        if (!p.isActive()) {
                            toRemoveProjectiles.add(p);
                        }
                    }

                    projectiles.removeAll(toRemoveProjectiles);

                    ArrayList<Agent> toRemoveAgents = new ArrayList<>();
                    for (Agent agent : environment.getAgents()) {
                        if (!agent.isAlive()) {
                            toRemoveAgents.add(agent);
                        }
                    }

                    environment.getAgents().removeAll(toRemoveAgents);
                }
            }.start();

            theStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void spawnZombies(Environment environment, int numberOfZombies) {
        for (int i = 0; i < numberOfZombies; i++) {
            int randomX = (int) (Math.random() * environment.getWidth());
            int randomY = (int) (Math.random() * environment.getHeight());
            Agent zombie = new Agent(randomX, randomY, 110, 130, 1, 100, "/images/agents/zumbi.png", true);
            environment.getAgents().add(zombie);
        }
    }
}