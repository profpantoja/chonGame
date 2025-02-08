package chon.group.game.domain.agent;

import javafx.scene.image.ImageView;

public class Enemy {
    private ImageView imageView;
    private int direction;
    private int stepsRemaining;

    public Enemy(ImageView imageView, int direction, int stepsRemaining) {
        this.imageView = imageView;
        this.direction = direction;
        this.stepsRemaining = stepsRemaining;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public int getStepsRemaining() {
        return stepsRemaining;
    }

    public void setStepsRemaining(int stepsRemaining) {
        this.stepsRemaining = stepsRemaining;
    }
}
