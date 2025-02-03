package chon.group.game.domain.agent;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Projectile {
    private double x, y;
    private double width = 80, height = 80;
    private boolean movingRight;
    private static final double SPEED = 10;
    private static final Image BOMB_IMAGE = new Image(Projectile.class.getResource("/images/agents/bomb.png").toExternalForm());

    public Projectile(double x, double y, boolean movingRight) {
        this.x = x;
        this.y = y;
        this.movingRight = movingRight;
    }

    public void update() {
        x += movingRight ? SPEED : -SPEED;
    }

    public void render(GraphicsContext gc) {
        gc.drawImage(BOMB_IMAGE, x, y, width, height);
    }

    public boolean isOffScreenRight(double screenWidth) {
        return x > screenWidth;
    }

    public boolean isOffScreenLeft() {
        return x + width < 0;
    }
}

