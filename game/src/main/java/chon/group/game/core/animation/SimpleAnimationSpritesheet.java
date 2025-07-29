package chon.group.game.core.animation;

import javafx.scene.image.Image;

public class SimpleAnimationSpritesheet implements AnimationSpritesheet {
    private final int frameWidth;
    private final int frameHeight;
    private final int frameCount;
    private final int durationMs;
    private final Image spritesheetImage;

    public SimpleAnimationSpritesheet(int frameWidth, int frameHeight, int frameCount, int durationMs, String path) {
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;
        this.frameCount = frameCount;
        this.durationMs = durationMs;
        this.spritesheetImage = new Image(getClass().getResource(path).toExternalForm());
    }

    @Override public int getFrameWidth() { return frameWidth; }
    @Override public int getFrameHeight() { return frameHeight; }
    @Override public int getFrameCount() { return frameCount; }
    @Override public int getDurationMs() { return durationMs; }
    @Override public Image getSpritesheetImage() { return spritesheetImage; }
}