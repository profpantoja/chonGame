package chon.group.game.animation;

import javafx.scene.SnapshotParameters;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class AnimationState {

    private Animation currentAnimation;
    private int currentFrameIndex = 0;
    private long lastTime = 0;
    private boolean finished = false;
    /** Indicates if the animation is facing left. */
    private boolean flipped = false;

    public Animation getCurrentAnimation() {
        return currentAnimation;
    }

    public void setCurrentAnimation(Animation currentAnimation) {
        this.currentAnimation = currentAnimation;
    }

    public int getCurrentFrameIndex() {
        return currentFrameIndex;
    }

    public void setCurrentFrameIndex(int currentFrameIndex) {
        this.currentFrameIndex = currentFrameIndex;
    }

    public long getLastTime() {
        return lastTime;
    }

    public void setLastTime(long lastTime) {
        this.lastTime = lastTime;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    /**
     * Gets if the animation is flipped.
     *
     * @return if the animation is flipped
     */
    public boolean isFlipped() {
        return flipped;
    }

    /**
     * Sets the animation flipped status.
     *
     * @param flipped the new flipped status
     */
    public void setFlipped(boolean flipped) {
        this.flipped = flipped;
    }

    /**
     * Gets the next current frame.
     */
    public int nextFrame() {
        this.lastTime = System.currentTimeMillis();
        return (this.currentFrameIndex + 1) % this.currentAnimation.getFrames().size();
    }

    public long tick() {
        long now = System.currentTimeMillis();
        long elapsedTime = now - lastTime;
        return elapsedTime;
    }

    public Image getCurrentImage() {
        Image image = this.getCurrentAnimation().getFrames().get(currentFrameIndex);
        return image;
    }

    /**
     * Flips the Image horizontally.
     */
    public Image flipImage(Image image) {
        ImageView flippedImage = new ImageView(image);
        flippedImage.setScaleX(-1);
        SnapshotParameters params = new SnapshotParameters();
        params.setFill(Color.TRANSPARENT);
        return flippedImage.snapshot(params, null);
    }

    public Image _flipImage(Image image) {
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();
        PixelReader reader = image.getPixelReader();
        WritableImage flipped = new WritableImage(width, height);
        PixelWriter writer = flipped.getPixelWriter();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                writer.setArgb(width - x - 1, y, reader.getArgb(x, y));
            }
        }
        return flipped;
    }

}
