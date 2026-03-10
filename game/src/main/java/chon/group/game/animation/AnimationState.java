package chon.group.game.animation;

public class AnimationState {

    private Animation currentAnimation;
    private int currentFrameIndex = 0;
    private long elapsedTime;
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

    public long getElapsedTime() {
        return elapsedTime;
    }

    public void setElapsedTime(long elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

}
