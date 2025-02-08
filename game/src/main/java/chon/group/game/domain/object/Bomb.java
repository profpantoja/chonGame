
package chon.group.game.domain.object;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;

public class Bomb {
    private final int WIDTH = 31;
    private final int HEIGHT = 15;
    private final ImageView bomb;
    private int bombX, bombY;
    private final int TIME_BOMB_MILI = 1000;
    private final int TAM_BOMB = 3;
    private final int TILE_SIZE = 40;
    private final Image fire1 = new Image(getClass().getResourceAsStream("/images/environment/fire1.png"));
    private final Image fire2 = new Image(getClass().getResourceAsStream("/images/environment/fire2.png"));
    private final Image fire3 = new Image(getClass().getResourceAsStream("/images/environment/fire3.png"));
    private final Image fire4 = new Image(getClass().getResourceAsStream("/images/environment/fire4.png"));

    public Bomb() {
        bomb = new ImageView(new Image(getClass().getResourceAsStream("/images/environment/bomb_init.png")));
        bomb.setFitWidth(TILE_SIZE);
        bomb.setFitHeight(TILE_SIZE);
    }

    public void placeBomb(GridPane gridPane, int playerX, int playerY, boolean[][] isWalkable) {
        if (!gridPane.getChildren().contains(bomb)) {
            gridPane.add(bomb, playerX, playerY);
            this.bombX = playerX;
            this.bombY = playerY;
            isWalkable[playerX][playerY] = false;
        }

        Timeline bombTimer = new Timeline(new KeyFrame(Duration.millis(TIME_BOMB_MILI), e -> {
            gridPane.getChildren().remove(bomb);
            createFire(gridPane, bombX, bombY, isWalkable);
            for (int dx = -TAM_BOMB; dx <= TAM_BOMB; dx++) {
                for (int dy = -TAM_BOMB; dy <= TAM_BOMB; dy++) {
                    if ((dx == 0 || dy == 0) && (dx != dy)) {
                        int fireX = bombX + dx;
                        int fireY = bombY + dy;
                        if (fireX >= 0 && fireX < WIDTH && fireY >= 0 && fireY < HEIGHT && isWalkable[fireX][fireY]) {
                            createFire(gridPane, fireX, fireY, null);;
                        }
                    }
                }
            }
            isWalkable[bombX][bombY] = true;
        }));
        bombTimer.setCycleCount(1);
        bombTimer.play();
    }
    private void createFire(GridPane gridPane, int fireX, int fireY,boolean[][] isWalkable) {
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
        isWalkable[fireX][fireY] = true;
    }
}
