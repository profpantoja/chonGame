package chon.group;

import java.util.ArrayList;

import chon.group.game.domain.agent.Agent;
import chon.group.game.domain.environment.Environment;
import chon.group.game.Drawer.EnvironmentDrawer;
import chon.group.game.Drawer.JavaFxMediator;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Engine extends Application {

    private boolean isPaused = false;
    private boolean gameOver = false;
    private boolean victory = false;
    private int timerSeconds = 60;  // 60-second timer

    public static void main(String[] args) {
        launch(args);
    }

    @Override
public void start(Stage theStage) {
    try {
        // Set up text for the timer display
        Text timeText = new Text("Time remaining: " + timerSeconds + "s");

        // Initialize the game environment and agents
        Environment environment = new Environment(0, 0, 1280, 780, "/images/environment/backgroundGPT.png");
        Agent jerry = new Agent(400, 390, 70, 85, 4, 100, "/images/agents/jerrySprite.png", false);
        Agent tom = new Agent(920, 440, 130, 96, 2, 3, "/images/agents/tom.png", true);
        environment.setProtagonist(jerry);
        environment.getAgents().add(tom);
        environment.setPauseImage("/images/environment/pause.png");
        environment.setGameOverImage("/images/environment/gameOver1.png");
        environment.setVictoryImage("/images/environment/victoryJerry.png");

        // Set up the graphical canvas
        Canvas canvas = new Canvas(environment.getWidth(), environment.getHeight());
        GraphicsContext gc = canvas.getGraphicsContext2D();
        EnvironmentDrawer mediator = new JavaFxMediator(environment, gc);
        environment.setGc(gc);

        AnimationTimer timer = new AnimationTimer() {
            private long lastUpdate = 0;
        
            @Override
            public void handle(long now) {
                if (lastUpdate == 0) {
                    lastUpdate = now;
                }
        
                // Update every second
                if (now - lastUpdate >= 1_000_000_000) { // 1 second in nanoseconds
                    lastUpdate = now;
        
                    // Decrease the time remaining
                    if (timerSeconds > 0) {
                        timerSeconds--;
                        timeText.setText("Time remaining: " + timerSeconds + "s");
                    } else {
                        // When time runs out and Jerry is not dead
                        if (!gameOver && !jerry.isDead() && !victory) {  // Check if Jerry is not dead and hasn't won yet
                            victory = true;  // Jerry won
                            environment.setVictoryImage("/images/environment/victoryJerry.png");
                            timeText.setText("You won! Jerry survived!");
                        }
                    }
                }
            }
        };

        timer.start();  // Start the timer for counting down

        // Set up the scene and stage
        StackPane root = new StackPane();
        Scene scene = new Scene(root, environment.getWidth(), environment.getHeight());
        theStage.setTitle("Chon: The Learning Game");
        theStage.setScene(scene);

        // Add elements to the scene
        root.getChildren().addAll(timeText, canvas);

        // Show the stage
        theStage.show();

        // Handle keyboard input
        ArrayList<String> input = new ArrayList<>();
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            public void handle(KeyEvent e) {
                String code = e.getCode().toString();
                input.clear();

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

        // Start the game loop
        new AnimationTimer() {
            @Override
            public void handle(long arg0) {
                // Render gameOver image
                if (gameOver) {
                    mediator.drawBackground();
                    mediator.drawAgents();
                    mediator.drawGameOverScreen();  // Displays the game over screen
                } else if (victory) {
                    mediator.drawBackground();
                    mediator.drawAgents();
                    mediator.drawVictoryImage();  // Displays the victory screen
                } else if (isPaused) {
                    // Render pause image
                    mediator.drawBackground();
                    mediator.drawAgents();
                    mediator.drawPauseScreen();
                } else {
                    // Update Jerry and Tom's movements
                    if (!input.isEmpty()) {
                        environment.getProtagonist().move(input);
                        environment.checkBorders();
                    }
                
                    // Update Tom's movements
                    environment.getAgents().get(0).chase(environment.getProtagonist().getPosX(),
                            environment.getProtagonist().getPosY());
                
                    // Detect collisions
                    environment.detectCollision();
                
                    // Check if Jerry is dead
                    if (environment.getProtagonist().isDead()) {
                        gameOver = true;
                        environment.setGameOverImage("/images/environment/gameOver1.png");
                    }
                
                    // Render the environment and agents
                    mediator.drawBackground();
                    mediator.drawAgents();
                }   
            }
            
        }.start();

    } catch (Exception e) {
        e.printStackTrace();
    }
}
}
