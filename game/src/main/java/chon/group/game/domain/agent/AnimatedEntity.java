package chon.group.game.domain.agent;
import chon.group.game.core.Entity;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class AnimatedEntity extends Entity {
    /** Spritesheet image for animation (optional). */
    private Image spritesheet;
    /** Number of frames in the spritesheet. */
    private int frameCount = 1;
    /** Current frame index. */
    private int currentFrame = 0;
    /** Width of a single frame. */
    private int frameWidth = 0;
    /** Height of a single frame. */
    private int frameHeight = 0;
    /** Last time the frame was updated. */
    private long lastFrameTime = 0;
    /** Duration per frame in ms. */
    private long frameDuration = 100;
    /** Current spritesheet string */
    private String currentSpritesheet = null;

    private double opacity = 1.0;

    public AnimatedEntity(Image spritesheet, int posX, int posY, int height, int width, int speed, int health, String pathImage) {
        super(posX, posY, height, width, speed, health, pathImage);
        this.spritesheet = spritesheet;
    }

    public AnimatedEntity(Image spritesheet, int posX, int posY, int height, int width, int speed, int health, String pathImage, boolean flipped) {
        super(posX, posY, height, width, speed, health, pathImage, flipped);
        this.spritesheet = spritesheet;
    }

    public void setOpacity(double opacity) {
        this.opacity = opacity;
    }

    public double getOpacity() {
        return this.opacity;
    }

    public Image getSpritesheet() {
        return spritesheet;
    }

    public void setSpritesheet(Image spritesheet) {
        this.spritesheet = spritesheet;
    }

    public int getFrameCount() {
        return frameCount;
    }

    public void setFrameCount(int frameCount) {
        this.frameCount = frameCount;
    }

    public int getCurrentFrame() {
        return currentFrame;
    }

    public void setCurrentFrame(int currentFrame) {
        this.currentFrame = currentFrame;
    }

    public int getFrameWidth() {
        return frameWidth;
    }

    public void setFrameWidth(int frameWidth) {
        this.frameWidth = frameWidth;
    }

    public int getFrameHeight() {
        return frameHeight;
    }

    public void setFrameHeight(int frameHeight) {
        this.frameHeight = frameHeight;
    }

    public long getLastFrameTime() {
        return lastFrameTime;
    }

    public void setLastFrameTime(long lastFrameTime) {
        this.lastFrameTime = lastFrameTime;
    }

    public long getFrameDuration() {
        return frameDuration;
    }

    public void setFrameDuration(long frameDuration) {
        this.frameDuration = frameDuration;
    }

    public String getCurrentSpritesheet() {
        return currentSpritesheet;
    }

    public void setCurrentSpritesheet(String currentSpritesheet) {
        this.currentSpritesheet = currentSpritesheet;
    }

    /**
     * Sets up animation for the entity.
     * @param spritesheetPath Path to the spritesheet image
     * @param frameCount Number of frames in the spritesheet
     * @param frameDuration Duration of each frame in milliseconds
     */
    public void setAnimation(String spritesheetPath, int frameCount, long frameDuration) {
        this.spritesheet = new Image(getClass().getResource(spritesheetPath).toExternalForm());
        this.frameCount = frameCount;
        this.frameDuration = frameDuration;
        this.frameWidth = (int) (spritesheet.getWidth() / frameCount);
        this.frameHeight = (int) spritesheet.getHeight();
        this.currentFrame = 0;
        this.lastFrameTime = System.currentTimeMillis();
        this.currentSpritesheet = spritesheetPath;
    }   

    /**
     * Updates the animation frame based on time.
     */
    public void updateAnimation() {
        if (frameCount <= 1 || spritesheet == null) return; // No animation
        long now = System.currentTimeMillis();
        if (now - lastFrameTime >= frameDuration) {
            currentFrame = (currentFrame + 1) % frameCount;
            lastFrameTime = now;
        }
    }

    public Image getFlippedCurrentFrameImage() {
        Image frame = getCurrentFrameImage();
        ImageView flippedView = new ImageView(frame);
        flippedView.setScaleX(-1);
        SnapshotParameters params = new SnapshotParameters();
        params.setFill(Color.TRANSPARENT);
        return flippedView.snapshot(params, null);
    }
    
    /**
     * Returns the current animation frame as an Image.
     * If flipped, returns the flipped frame.
     */
    public Image getCurrentFrameImage() {
        if (spritesheet == null || frameCount <= 1) {
            Image baseImage = this.isFlipped() ? getFlippedImage() : this.getImage();
            ImageView imageView = new ImageView(baseImage);
            imageView.setOpacity(opacity);
            SnapshotParameters params = new SnapshotParameters();
            params.setFill(Color.TRANSPARENT);
            return imageView.snapshot(params, null);
        }
        WritableImage frame = new WritableImage(
            spritesheet.getPixelReader(),
            currentFrame * frameWidth,
            0,
            frameWidth,
            frameHeight
        );
        ImageView imageView = new ImageView(frame);
        imageView.setOpacity(opacity);
        if (this.isFlipped()) {
            imageView.setScaleX(-1);
        }
        SnapshotParameters params = new SnapshotParameters();
        params.setFill(Color.TRANSPARENT);
        return imageView.snapshot(params, null);
    }

    /**
     * Returns the base image flipped horizontally (for non-animated entities).
     */
    private Image getFlippedImage() {
        ImageView flippedView = new ImageView(this.getImage());
        flippedView.setScaleX(-1);
        SnapshotParameters params = new SnapshotParameters();
        params.setFill(Color.TRANSPARENT);
        return flippedView.snapshot(params, null);
    }
}
