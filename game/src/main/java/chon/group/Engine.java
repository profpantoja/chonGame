package chon.group;

import chon.group.game.domain.agent.Agent;
import chon.group.game.domain.environment.Environment;
import chon.group.game.drawer.EnvironmentDrawer;
import chon.group.game.drawer.JavaFxMediator;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Engine extends Application {
    private static final double WIDTH = 800;
    private static final double HEIGHT = 400;
    private static final double PADDLE_RADIUS = 30;
    private static final double PUCK_RADIUS = 15;

    private Agent agents;
    private Environment environment;
    private EnvironmentDrawer mediator;

    private double puckSpeedX = 5;
    private double puckSpeedY = 5;

    private boolean wPressed, aPressed, sPressed, dPressed;
    private boolean upPressed, downPressed, leftPressed, rightPressed;
    private boolean isPaused = false;
    private boolean gameOver = false;

    private int scorePlayer1 = 0;
    private int scorePlayer2 = 0;
    private String team1 = "Player 1";
    private String team2 = "Player 2";

    private Text winnerText;
    private Stage mainStage;
    
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.mainStage = primaryStage;
        agents = new Agent(PADDLE_RADIUS, PUCK_RADIUS);
        environment = new Environment("/images/environment/camp.png", WIDTH, HEIGHT);
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        mediator = new JavaFxMediator(environment, agents, gc, WIDTH, HEIGHT);
        showTeamSelectionScreen(primaryStage);
    }

    private void showTeamSelectionScreen(Stage primaryStage) {
        VBox root = new VBox(20);
        root.setStyle("-fx-alignment: center; -fx-padding: 20px;");
    
        Text title = new Text("Selecione os Times");
        title.setFont(Font.font("Arial", 24));
    
        HBox teamSelection = new HBox(20);
        teamSelection.setStyle("-fx-alignment: center;");
    
        Text player1Choice = new Text("Jogador 1: Não selecionado");
        Text player2Choice = new Text("Jogador 2: Não selecionado");
        
        String[] teams = {"Flamengo", "Fluminense", "Vasco", "Botafogo"};
        Button[] buttons = new Button[teams.length];
        
        for (int i = 0; i < teams.length; i++) {
            String team = teams[i];
            ImageView imageView = new ImageView(new Image(getClass().getResource("/images/agents/" + team.toLowerCase() + ".png").toExternalForm()));
            imageView.setFitWidth(100);
            imageView.setFitHeight(100);
    
            Button button = new Button(team, imageView);
            button.setOnAction(e -> {
                if (team1.equals("Player 1")) {
                    team1 = team;
                    player1Choice.setText("Jogador 1: " + team);
                    button.setDisable(true); 
                } else if (team2.equals("Player 2")) {
                    team2 = team;
                    player2Choice.setText("Jogador 2: " + team);
                    button.setDisable(true);
                    startGame(primaryStage);
                }
            });
    
            buttons[i] = button;
            teamSelection.getChildren().add(button);
        }
    
        root.getChildren().addAll(title, player1Choice, player2Choice, teamSelection);
        Scene scene = new Scene(root, WIDTH, HEIGHT);
        mainStage.setScene(scene);
        mainStage.setTitle("Seleção de Times");
        mainStage.show();
    }    

    private void startGame(Stage primaryStage) {
        Pane root = new Pane();
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        root.getChildren().add(canvas);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        mediator = new JavaFxMediator(environment, agents, gc, WIDTH, HEIGHT);

        Line centerLine = new Line(WIDTH / 2, 0, WIDTH / 2, HEIGHT);
        centerLine.setStroke(Color.GRAY);
        centerLine.setStrokeWidth(2);
        centerLine.getStrokeDashArray().addAll(10.0, 5.0);

        Circle centerCircle = new Circle(WIDTH / 2, HEIGHT / 2, 50, Color.TRANSPARENT);
        centerCircle.setStroke(Color.GRAY);
        centerCircle.setStrokeWidth(2);

        agents.getPaddle1().setTranslateX(PADDLE_RADIUS + 10);
        agents.getPaddle1().setTranslateY(HEIGHT / 2);

        agents.getPaddle2().setTranslateX(WIDTH - PADDLE_RADIUS - 10);
        agents.getPaddle2().setTranslateY(HEIGHT / 2);

        updatePlayerAppearance();

        agents.resetPuckPosition(WIDTH / 2, HEIGHT / 2);

        Text scoreText = new Text(team1 + ": 0  |  " + team2 + ": 0");
        scoreText.setFill(Color.BLACK);
        scoreText.setFont(Font.font("Arial", 24));
        scoreText.setTranslateX(WIDTH / 2 - 130);
        scoreText.setTranslateY(40);

        Image pauseImage = new Image(getClass().getResource("/images/environment/pause.png").toExternalForm());
        ImageView pauseImageView = new ImageView(pauseImage);
        pauseImageView.setTranslateX(WIDTH / 2 - pauseImage.getWidth() / 2);
        pauseImageView.setTranslateY(HEIGHT / 2 - pauseImage.getHeight() / 2);
        pauseImageView.setVisible(false);

        winnerText = new Text();
        winnerText.setFill(Color.RED);
        winnerText.setFont(Font.font("Arial", 36));
        winnerText.setTranslateX(WIDTH / 2 - 150);
        winnerText.setTranslateY(HEIGHT / 2);
        winnerText.setVisible(false);

        root.getChildren().addAll(centerLine, centerCircle, agents.getPaddle1(), agents.getPaddle2(), agents.getPuck(), scoreText, pauseImageView, winnerText);
        Scene scene = new Scene(root, WIDTH, HEIGHT, Color.BEIGE);

        scene.setOnKeyPressed(event -> {
            if (gameOver) {
                resetGame(agents.getPuck(), scoreText);
                return;
            }
            if (event.getCode() == KeyCode.W) wPressed = true;
            if (event.getCode() == KeyCode.A) aPressed = true;
            if (event.getCode() == KeyCode.S) sPressed = true;
            if (event.getCode() == KeyCode.D) dPressed = true;
            if (event.getCode() == KeyCode.UP) upPressed = true;
            if (event.getCode() == KeyCode.LEFT) leftPressed = true;
            if (event.getCode() == KeyCode.DOWN) downPressed = true;
            if (event.getCode() == KeyCode.RIGHT) rightPressed = true;
            if (event.getCode() == KeyCode.P) togglePause(pauseImageView);
            if (event.getCode() == KeyCode.ESCAPE) System.exit(0);
        });

        scene.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.W) wPressed = false;
            if (event.getCode() == KeyCode.A) aPressed = false;
            if (event.getCode() == KeyCode.S) sPressed = false;
            if (event.getCode() == KeyCode.D) dPressed = false;
            if (event.getCode() == KeyCode.UP) upPressed = false;
            if (event.getCode() == KeyCode.LEFT) leftPressed = false;
            if (event.getCode() == KeyCode.DOWN) downPressed = false;
            if (event.getCode() == KeyCode.RIGHT) rightPressed = false;
        });

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (isPaused || gameOver) return;

                if (wPressed && agents.getPaddle1().getTranslateY() - PADDLE_RADIUS > 0)
                    agents.getPaddle1().setTranslateY(agents.getPaddle1().getTranslateY() - 5);
                if (sPressed && agents.getPaddle1().getTranslateY() + PADDLE_RADIUS < HEIGHT)
                    agents.getPaddle1().setTranslateY(agents.getPaddle1().getTranslateY() + 5);
                if (aPressed && agents.getPaddle1().getTranslateX() - PADDLE_RADIUS > 0)
                    agents.getPaddle1().setTranslateX(agents.getPaddle1().getTranslateX() - 5);
                if (dPressed && agents.getPaddle1().getTranslateX() + PADDLE_RADIUS < WIDTH / 2 - PUCK_RADIUS)
                    agents.getPaddle1().setTranslateX(agents.getPaddle1().getTranslateX() + 5);

                if (upPressed && agents.getPaddle2().getTranslateY() - PADDLE_RADIUS > 0)
                    agents.getPaddle2().setTranslateY(agents.getPaddle2().getTranslateY() - 5);
                if (downPressed && agents.getPaddle2().getTranslateY() + PADDLE_RADIUS < HEIGHT)
                    agents.getPaddle2().setTranslateY(agents.getPaddle2().getTranslateY() + 5);
                if (leftPressed && agents.getPaddle2().getTranslateX() - PADDLE_RADIUS > WIDTH / 2 + PUCK_RADIUS)
                    agents.getPaddle2().setTranslateX(agents.getPaddle2().getTranslateX() - 5);
                if (rightPressed && agents.getPaddle2().getTranslateX() + PADDLE_RADIUS < WIDTH)
                    agents.getPaddle2().setTranslateX(agents.getPaddle2().getTranslateX() + 5);

                agents.getPuck().setTranslateX(agents.getPuck().getTranslateX() + puckSpeedX);
                agents.getPuck().setTranslateY(agents.getPuck().getTranslateY() + puckSpeedY);

                if (agents.getPuck().getTranslateY() - PUCK_RADIUS <= 0 || agents.getPuck().getTranslateY() + PUCK_RADIUS >= HEIGHT) {
                    puckSpeedY *= -1;
                }

                if (agents.getPuck().getBoundsInParent().intersects(agents.getPaddle1().getBoundsInParent()) ||
                    agents.getPuck().getBoundsInParent().intersects(agents.getPaddle2().getBoundsInParent())) {
                    puckSpeedX *= -1;
                }

                if (agents.getPuck().getTranslateX() - PUCK_RADIUS <= 0) {
                    checkWinner(scoreText, 2);
                    resetPuck(agents.getPuck());
                } else if (agents.getPuck().getTranslateX() + PUCK_RADIUS >= WIDTH) {
                    checkWinner(scoreText, 1);
                    resetPuck(agents.getPuck());
                }

                mediator.clearEnvironment();
                mediator.drawBackground();
                //mediator.drawAgents();
            }
        };

        timer.start();

        primaryStage.setScene(scene);
        primaryStage.setTitle("Air Hockey");
        primaryStage.show();
    }

    private void updatePlayerAppearance() {
        if (!team1.equals("Player 1")) {
            Image team1Image = new Image(getClass().getResource("/images/agents/" + team1.toLowerCase() + ".png").toExternalForm());
            agents.setPaddle1Image(new ImagePattern(team1Image));
        }
        
        if (!team2.equals("Player 2")) {
            Image team2Image = new Image(getClass().getResource("/images/agents/" + team2.toLowerCase() + ".png").toExternalForm());
            agents.setPaddle2Image(new ImagePattern(team2Image));
        }
    }

    private void togglePause(ImageView pauseImageView) {
        isPaused = !isPaused;
        pauseImageView.setVisible(isPaused);
    }

    private void resetPuck(Circle puck) {
        puck.setTranslateX(WIDTH / 2);
        puck.setTranslateY(HEIGHT / 2);
        puckSpeedX *= -1;
    }

    private void checkWinner(Text scoreText, int scorer) {
        if (scorer == 1) {
            scorePlayer1++;
        } else {
            scorePlayer2++;
        }    
        scoreText.setText(team1 + ": " + scorePlayer1 + "  |  " + team2 + ": " + scorePlayer2);
    
        if (scorePlayer1 == 10) {
            gameOver = true;
            winnerText.setText("Vencedor: " + team1);
            winnerText.setVisible(true);
        } else if (scorePlayer2 == 10) {
            gameOver = true;
            winnerText.setText("Vencedor: " + team2);
            winnerText.setVisible(true);
        }
    }      

    private void resetGame(Circle puck, Text scoreText) {
        scorePlayer1 = 0;
        scorePlayer2 = 0;
        gameOver = false;
        winnerText.setVisible(false);
        resetPuck(puck);
        
        scoreText.setText(team1 + ": 0  |  " + team2 + ": 0");
    }
}