package chon.group.game.animation;

public class AnimationState {

    private Animation currentAnimation;
    private int currentFrameIndex = 0;
    private long lastTime = 0;
    private boolean finished = false;

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

}
