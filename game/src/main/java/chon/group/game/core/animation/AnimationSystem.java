package chon.group.game.core.animation;

import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

public class AnimationSystem {
    private AnimationSpritesheet singleSpritesheet;
    private AnimationGraphics graphics;
    private AnimationStatus currentStatus = AnimationStatus.IDLE;
    private int currentFrame = 0;
    private long lastUpdate = 0;
    private boolean paused = false;


    public AnimationSystem(AnimationGraphics graphics) {
        this.graphics = graphics;
    }

    public AnimationSystem(AnimationSpritesheet spritesheet) {
        this.singleSpritesheet = spritesheet;
    }

    public void setStatus(AnimationStatus status) {
        if (status != currentStatus) {
            currentStatus = status;
            currentFrame = 0;
            lastUpdate = System.currentTimeMillis();
        }
    }

    public AnimationStatus getStatus() {
        return currentStatus;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    public boolean isPaused() {
        return this.paused;
    }

    public Image getCurrentFrameImage() {
        AnimationSpritesheet sheet = (graphics != null)
            ? graphics.getSpritesheet(currentStatus)
            : singleSpritesheet;
            
        // Fallback to IDLE if the current status has no animation
        if (sheet == null) {
            sheet = (graphics != null) ? graphics.getSpritesheet(AnimationStatus.IDLE) : singleSpritesheet;
            if (sheet == null) {
                return null;
            }
        }

        long now = System.currentTimeMillis();
        int duration = sheet.getDurationMs();
        int frameCount = sheet.getFrameCount();

        if (!isPaused() && (now - lastUpdate > duration / frameCount)) {
            
            // MODIFICATION: Check if the current animation is DYING
            if (currentStatus == AnimationStatus.DYING) {
                // Non-looping logic: only increments the frame if it's not the last one.
                if (currentFrame < frameCount - 1) {
                    currentFrame++;
                }
            } else {
                // Original looping logic for all other animations.
                currentFrame = (currentFrame + 1) % frameCount;
            }
            
            lastUpdate = now;
        }
        
        int x = currentFrame * sheet.getFrameWidth();
        return new WritableImage(sheet.getSpritesheetImage().getPixelReader(), x, 0, sheet.getFrameWidth(), sheet.getFrameHeight());
    }

    public AnimationGraphics getGraphics() {
        return graphics;
    }

    public void setGraphics(AnimationGraphics graphics) {
        this.graphics = graphics;
    }

    public AnimationStatus getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(AnimationStatus currentStatus) {
        this.currentStatus = currentStatus;
    }
}