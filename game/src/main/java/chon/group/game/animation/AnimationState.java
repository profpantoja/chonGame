package chon.group.game.animation;

import javafx.scene.image.Image;

public class AnimationState {

    private Animation currentAnimation;
    private int currentFrameIndex = 0;
    private long lastTime = 0;
    /** Indicates if the current animation has came to an end. */
    private boolean finished = false;
    /** Indicates if the animation is facing left. */
    private boolean flipped = false;

    public Animation getCurrentAnimation() {
        return currentAnimation;
    }

    public void setCurrentAnimation(Animation currentAnimation) {
        if (this.currentAnimation != currentAnimation) {
            this.currentAnimation = currentAnimation;
            this.reset();
        }
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

    public boolean isBlocked() {
        return currentAnimation != null
                && !currentAnimation.isInLoop()
                && !finished;
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
     * Advances to the next frame index.
     *
     * @return the next frame index
     */
    public int advanceFrame() {
        this.lastTime = System.currentTimeMillis();
        int lastFrameIndex = this.currentAnimation.getFrames().size() - 1;
        /* Non-looping animations stop at the last frame. */
        if (!this.currentAnimation.isInLoop()) {
            if (this.currentFrameIndex >= lastFrameIndex) {
                this.finished = true;
                return lastFrameIndex;
            }
            return this.currentFrameIndex + 1;
        }
        /*
         * Return the next frame. If the current animation is a loop the next frame can
         * return to 0.
         */
        return (this.currentFrameIndex + 1) % this.currentAnimation.getFrames().size();
    }

    public long tick() {
        long now = System.currentTimeMillis();
        if (lastTime == 0) {
            lastTime = now;
            return 0;
        }
        return now - lastTime;
    }

    public void reset() {
        this.lastTime = 0;
        this.currentFrameIndex = 0;
        this.finished = false;
    }

    public Image getCurrentImage() {
        Image image = this.getCurrentAnimation().getFrames().get(currentFrameIndex);
        return image;
    }

}
