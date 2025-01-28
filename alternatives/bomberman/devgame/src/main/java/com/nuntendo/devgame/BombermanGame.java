package com.nuntendo.devgame;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class BombermanGame extends Application {
    private final int ENEMIES_STEP = 4;
    private final int WIDTH = 31;
    private final int HEIGHT = 15;
    private final int TILE_SIZE = 40;
    private final int TIME_BOMB_MILI = 3000;
    private final int TAM_BOMB = 3;

    private Stage primaryStage;
    private final ImageView[][] grid = new ImageView[WIDTH][HEIGHT];
    private boolean[][] isWalkable = new boolean[WIDTH][HEIGHT];
    private ImageView player;
    private ImageView bomb;
    private int bombX, bombY;
    private int playerX = 1, playerY = 1;
    private static final int ENEMY_COUNT = 5;
    private final List<Enemy> enemies = new ArrayList<>();
    private final Random random = new Random();
    private boolean isGameOver = false;

    private final Image blockImage = new Image(getClass().getResourceAsStream("/floor.png"));
    private final Image groundImage = new Image(getClass().getResourceAsStream("/icons_indestrutivel.png"));
    private final Image wallImage = new Image(getClass().getResourceAsStream("/wall.png"));
    private final Image fire1 = new Image(getClass().getResourceAsStream("/fire1.png"));
    private final Image fire2 = new Image(getClass().getResourceAsStream("/fire2.png"));
    private final Image fire3 = new Image(getClass().getResourceAsStream("/fire3.png"));
    private final Image fire4 = new Image(getClass().getResourceAsStream("/fire4.png"));

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;

        if (blockImage.isError() || groundImage.isError()) {
            System.out.println("Erro ao carregar imagens:" + blockImage.getException());
        }

        GridPane root = new GridPane();
        root.setHgap(0);
        root.setVgap(0);
        root.setGridLinesVisible(true);
        Scene scene = new Scene(root, WIDTH * TILE_SIZE, HEIGHT * TILE_SIZE);

        initializeGrid(root);

        player = new ImageView(new Image(getClass().getResourceAsStream("/playerDownStopImage.png")));
        player.setFitWidth(TILE_SIZE);
        player.setFitHeight(TILE_SIZE);
        root.add(player, playerX, playerY);

        bomb = new ImageView(new Image(getClass().getResourceAsStream("/bomb_init.png")));
        bomb.setFitWidth(TILE_SIZE);
        bomb.setFitHeight(TILE_SIZE);

        scene.setOnKeyPressed(this::handleKeyPressed);

        Timeline gameLoop = new Timeline(new KeyFrame(Duration.millis(100), e -> gameLoop()));
        gameLoop.setCycleCount(Timeline.INDEFINITE);
        gameLoop.play();

        addEnemies(root);

        primaryStage.setTitle("Bomberman Style Game");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void initializeGrid(GridPane root) {
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                ImageView floor = new ImageView(blockImage);
                floor.setFitWidth(TILE_SIZE);
                floor.setFitHeight(TILE_SIZE);

                ImageView tile = new ImageView();
                tile.setFitWidth(TILE_SIZE);
                tile.setFitHeight(TILE_SIZE);

                boolean walkable = true;

                if (x == 0 || y == 0 || x == WIDTH - 1 || y == HEIGHT - 1) {
                    tile.setImage(wallImage);
                    walkable = false;
                } else if (x % 2 == 0 && y % 2 == 0) {
                    tile.setImage(groundImage);
                    walkable = false;
                }

                isWalkable[x][y] = walkable;

                StackPane cell = new StackPane(floor, tile);
                cell.setPrefSize(TILE_SIZE, TILE_SIZE);

                grid[x][y] = tile;
                root.add(cell, x, y);
            }
        }
    }

    private void handleKeyPressed(KeyEvent event) {
        int newX = playerX, newY = playerY;

        switch (event.getCode()) {
            case UP:
                newY--;
                break;
            case DOWN:
                newY++;
                break;
            case LEFT:
                newX--;
                break;
            case RIGHT:
                newX++;
                break;
            case SPACE:
                placeBomb();
                return;
        }

        if (isWalkable[newX][newY]) {
            playerX = newX;
            playerY = newY;
            updatePlayerPosition();
        }
    }

    private void addEnemies(GridPane root) {
        for (int i = 0; i < ENEMY_COUNT; i++) {
            int enemyX, enemyY;
            do {
                enemyX = random.nextInt(WIDTH);
                enemyY = random.nextInt(HEIGHT);
            } while (!isWalkable[enemyX][enemyY] || (enemyX == playerX && enemyY == playerY));

            ImageView enemyImageView = new ImageView(new Image(getClass().getResourceAsStream("/enemy.png")));
            enemyImageView.setFitWidth(TILE_SIZE);
            enemyImageView.setFitHeight(TILE_SIZE);
            root.add(enemyImageView, enemyX, enemyY);

            int initialDirection = random.nextInt(4);
            Enemy enemy = new Enemy(enemyImageView, initialDirection, ENEMIES_STEP);
            enemies.add(enemy);
            isWalkable[enemyX][enemyY] = false;
        }
    }

    private void updatePlayerPosition() {
        GridPane.setColumnIndex(player, playerX);
        GridPane.setRowIndex(player, playerY);
    }

    private void placeBomb() {
        GridPane gridPane = (GridPane) player.getParent();
        if (gridPane != null && !gridPane.getChildren().contains(bomb)) {
            gridPane.add(bomb, playerX, playerY);
            bombX = playerX;
            bombY = playerY;
            isWalkable[playerX][playerY] = false;
        }

        Timeline bombTimer = new Timeline(new KeyFrame(Duration.millis(TIME_BOMB_MILI), e -> {
            if (gridPane != null) {
                gridPane.getChildren().remove(bomb);
                createFire(gridPane, bombX, bombY);
                for (int dx = -TAM_BOMB; dx <= TAM_BOMB; dx++) {
                    for (int dy = -TAM_BOMB; dy <= TAM_BOMB; dy++) {
                        if ((dx == 0 || dy == 0) && (dx != dy)) {
                            int fireX = bombX + dx;
                            int fireY = bombY + dy;
                            if (fireX >= 0 && fireX < WIDTH && fireY >= 0 && fireY < HEIGHT && isWalkable[fireX][fireY]) {
                                createFire(gridPane, fireX, fireY);
                            }
                        }
                    }
                }
                isWalkable[bombX][bombY] = true;
                removeEnemiesInBlastRadius(gridPane, bombX, bombY);
            }
        }));
        bombTimer.setCycleCount(1);
        bombTimer.play();
    }

    private void removeEnemiesInBlastRadius(GridPane gridPane, int bombX, int bombY) {
        Iterator<Enemy> iterator = enemies.iterator();
        while (iterator.hasNext()) {
            Enemy enemy = iterator.next();
            int enemyX = GridPane.getColumnIndex(enemy.imageView);
            int enemyY = GridPane.getRowIndex(enemy.imageView);

            if ((Math.abs(enemyX - bombX) <= TAM_BOMB && enemyY == bombY) || (Math.abs(enemyY - bombY) <= TAM_BOMB && enemyX == bombX)) {
                gridPane.getChildren().remove(enemy.imageView);
                isWalkable[enemyX][enemyY] = true;
                iterator.remove();
            }
        }
    }

    private void createFire(GridPane gridPane, int fireX, int fireY) {
        ImageView fire = new ImageView(fire1);
        fire.setFitWidth(TILE_SIZE);
        fire.setFitHeight(TILE_SIZE);
        gridPane.add(fire, fireX, fireY);

        Timeline fireAnimation = new Timeline(
                new KeyFrame(Duration.millis(0), ev -> fire.setImage(fire1)),
                new KeyFrame(Duration.millis(250), ev -> fire.setImage(fire2)),
                new KeyFrame(Duration.millis(500), ev -> fire.setImage(fire3)),
                new KeyFrame(Duration.millis(750), ev -> fire.setImage(fire4)),
                new KeyFrame(Duration.millis(1000), ev -> gridPane.getChildren().remove(fire))
        );
        fireAnimation.setCycleCount(1);
        fireAnimation.play();

        if (fireX == playerX && fireY == playerY) {
            gridPane.getChildren().remove(player);
            showGameOverScreen();
            gameOver(gridPane);
        }
    }

    private void showGameOverScreen() {
        StackPane gameOverRoot = new StackPane();
        gameOverRoot.setStyle("-fx-background-color: black; -fx-alignment: center;");

        javafx.scene.control.Label gameOverLabel = new javafx.scene.control.Label("GAME OVER");
        gameOverLabel.setStyle("-fx-text-fill: red; -fx-font-size: 48px; -fx-font-weight: bold;");

        javafx.scene.control.Button restartButton = new javafx.scene.control.Button("Reiniciar");
        restartButton.setStyle("-fx-font-size: 18px; -fx-padding: 10px;");
        restartButton.setOnAction(e -> restartGame());

        javafx.scene.control.Button exitButton = new javafx.scene.control.Button("Sair");
        exitButton.setStyle("-fx-font-size: 18px; -fx-padding: 10px;");
        exitButton.setOnAction(e -> System.exit(0));

        javafx.scene.layout.VBox buttonsBox = new javafx.scene.layout.VBox(20, restartButton, exitButton);
        buttonsBox.setStyle("-fx-alignment: center;");
        gameOverRoot.getChildren().addAll(gameOverLabel, buttonsBox);

        Scene gameOverScene = new Scene(gameOverRoot, primaryStage.getScene().getWidth(), primaryStage.getScene().getHeight());
        primaryStage.setScene(gameOverScene);
    }

    private void restartGame() {
        playerX = 1;
        playerY = 1;
        isWalkable = new boolean[WIDTH][HEIGHT];
        start(primaryStage);
        isGameOver = false;
    }

    private void gameLoop() {
        GridPane gridPane = (GridPane) player.getParent();
        if (isGameOver) return;

        for (Enemy enemy : enemies) {
            int currentX = GridPane.getColumnIndex(enemy.imageView);
            int currentY = GridPane.getRowIndex(enemy.imageView);

            if (enemy.stepsRemaining > 0) {
                enemy.stepsRemaining--;
            } else {
                enemy.direction = random.nextInt(4);
                enemy.stepsRemaining = ENEMIES_STEP;
            }

            int newX = currentX, newY = currentY;

            switch (enemy.direction) {
                case 0 -> newX--;
                case 1 -> newX++;
                case 2 -> newY--;
                case 3 -> newY++;
            }

            if (newX >= 0 && newX < WIDTH && newY >= 0 && newY < HEIGHT && isWalkable[newX][newY]) {
                isWalkable[currentX][currentY] = true;
                isWalkable[newX][newY] = false;
                GridPane.setColumnIndex(enemy.imageView, newX);
                GridPane.setRowIndex(enemy.imageView, newY);
            }

            if (newX == playerX && newY == playerY) {
                gridPane.getChildren().remove(player);
                showGameOverScreen();
                gameOver(gridPane);
                return;
            }
        }
    }

    private void gameOver(GridPane gridPane) {
        isGameOver = true;
        Iterator<Enemy> iterator = enemies.iterator();
        while (iterator.hasNext()) {
            Enemy enemy = iterator.next();
            int enemyX = GridPane.getColumnIndex(enemy.imageView);
            int enemyY = GridPane.getRowIndex(enemy.imageView);
            gridPane.getChildren().remove(enemy.imageView);
            isWalkable[enemyX][enemyY] = true;
            iterator.remove();
        }
    }
}