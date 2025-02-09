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

/**
 * The main game engine responsible for initializing and running the game logic.
 * The game includes a protagonist, zombies, projectiles, and a 2-minute gameplay duration.
 * It also handles the spawning of zombies, handling player input, game timers, and victory/defeat conditions.
 * 
 * The game ends either when the protagonist dies, all zombies are defeated, or when the timer runs out.
 * 
 * @see javafx.application.Application
 */
public class Engine extends Application {
    private boolean isPaused = false;
    private final int ZOMBIES_TO_SPAWN = 1;
    private Timeline gameTimer; // Declare global gameTimer
    private final long GAME_DURATION = 1 * 40 * 1000; // 1 minute in milliseconds
    private boolean gameOver = false; // Controls when the game ends
    private long startTime; // Variable to store the start time of the game
    private boolean isVictory = false; // Variable to control victory condition
    private static final long ZOMBIE_SPAWN_TIME_LIMIT = 30 * 1000; // 1 minute in milliseconds
    private long zombieSpawnStartTime; // Stores the time when zombie spawning starts
    private boolean canSpawnZombies = true; // Controls when zombies can be spawned

    public static void main(String[] args) {
        launch(args);  
    }

    @Override
    public void start(Stage theStage) {
        
        try {
            // Set up environment and characters
            Environment environment = new Environment(0, 0, 1280, 780, "/images/environment/forest.png");
            Agent jon = new Agent(400, 390, 165, 68, 5, 200, "/images/agents/jon.png", true);
            environment.setProtagonist(jon);
            environment.setPauseImage("/images/environment/pause.png");
            environment.setImagevictory("/images/environment/victory.png");
            environment.setImageGameOver("/images/environment/gameover.png");
            Environment environment2 = new Environment(0, 0, 1280, 780, "/images/environment/forest2.png");

            spawnZombies(environment, 1); // Spawn 1 initial zombie
            zombieSpawnStartTime = System.currentTimeMillis(); // Marks the start time of zombie spawning

            ArrayList<Projectile> projectiles = new ArrayList<>();
            Canvas canvas = new Canvas(environment.getWidth(), environment.getHeight());
            GraphicsContext gc = canvas.getGraphicsContext2D();
            EnvironmentDrawer mediator = new JavaFxMediator(environment, gc);
            EnvironmentDrawer mediator2 = new JavaFxMediator(environment2, gc);

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
                        isPaused = !isPaused; // Toggle game pause
                    }

                    if (code.equals("Q") && !isPaused && !gameOver) {
                        Agent protagonist = environment.getProtagonist();
                        String direction = protagonist.isFlipped() ? "LEFT" : "RIGHT";
                        projectiles.add(new Projectile(protagonist.getPosX(), protagonist.getPosY(), 10, 30, direction, "/images/projectiles/bala.png"));
                    }

                    if (code.equals("R") && gameOver) {
                        resetGame(environment, projectiles); // Restart the game if it's over
                    }

                    if (!isPaused && !gameOver && !input.contains(code)) {
                        input.add(code); // Add key press to input list
                    }
                }
            });

            scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
                public void handle(KeyEvent e) {
                    String code = e.getCode().toString();
                    input.remove(code); // Remove key release from input list
                }
            });

            // Timer for spawning zombies
            Timeline zombieSpawnTimer = new Timeline(new KeyFrame(Duration.seconds(2.5), event -> {
                // Check if 1 minute has passed
                if (System.currentTimeMillis() - zombieSpawnStartTime <= ZOMBIE_SPAWN_TIME_LIMIT && canSpawnZombies) {
                    spawnZombies(environment, ZOMBIES_TO_SPAWN);
                } else {
                    canSpawnZombies = false; // Disable zombie spawning after 1 minute
                }
            }));
            zombieSpawnTimer.setCycleCount(Timeline.INDEFINITE);
            zombieSpawnTimer.play();

            // Game duration - 1 minute
            startTime = System.currentTimeMillis();
            gameTimer = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
                long elapsedTime = System.currentTimeMillis() - startTime;
                if (elapsedTime >= GAME_DURATION && !gameOver) {
                    isVictory = true; // Game wins after time limit
                    gameOver = true;
                }
            }));
            gameTimer.setCycleCount(Timeline.INDEFINITE);
            gameTimer.play(); // Start the timer

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

                    if (gameOver) {
                        if (isVictory) {
                            mediator.drawBackground();
                            mediator.drawVictoryScreen();
                        } else {
                            mediator.drawGameOverScreen();
                        }
                        return; // Game ends, no further processing
                    }

                    if (environment.getProtagonist().getHealth() <= 0) {
                        gameOver = true;
                        isVictory = false; // If the protagonist dies
                    }

                    // Check if all zombies are dead
                    if (environment.getAgents().isEmpty()) {
                        isVictory = true; // Victory because all zombies are dead
                        gameOver = true;
                    }

                    if (gameOver) {
                        if (!isVictory) {
                            mediator.drawGameOverScreen();
                        }
                        return; // Stop game execution
                    }

                    // Game still running, continue processing
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
                    mediator2.drawBackground();

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

                    environment.getAgents().removeAll(toRemoveAgents); // Remove dead zombies
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

    private void resetGame(Environment environment, ArrayList<Projectile> projectiles) {
        // Parar o timer atual antes de reiniciar
        if (gameTimer != null) {
            gameTimer.stop();
        }
    
        // Resetar as variáveis de estado do jogo
        gameOver = false;
        isVictory = false;
        isPaused = false;
    
        // Resetar o tempo do jogo
        startTime = System.currentTimeMillis(); // Reinicia o tempo de jogo
    
        // Limpar zombies e projectiles
        environment.getAgents().clear();
        projectiles.clear();
    
        // Recriar o protagonista e spawnar novos zombies
        Agent jon = new Agent(400, 390, 165, 68, 5, 200, "/images/agents/jon.png", true);
        environment.setProtagonist(jon);
        spawnZombies(environment, ZOMBIES_TO_SPAWN);
    
        // Reiniciar o timer do jogo de 1 minuto
        gameTimer = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            long elapsedTime = System.currentTimeMillis() - startTime;
            if (elapsedTime >= GAME_DURATION && !gameOver) {
                isVictory = true; // Game wins after time limit
                gameOver = true;
            }
        }));
        gameTimer.setCycleCount(Timeline.INDEFINITE);
        gameTimer.play(); // Inicia o timer novamente
    
        // Certificar-se de que o tempo de spawn dos zombies também foi reiniciado
        zombieSpawnStartTime = System.currentTimeMillis();
        canSpawnZombies = true;
    }
}