package chon.group.game.animation;

import java.util.List;

import javafx.scene.image.Image;

public class Animation {

    private List<Image> frames;
    private long duration;
    private boolean inLoop;

    public List<Image> getFrames() {
        return frames;
    }

    public void setFrames(List<Image> frames) {
        this.frames = frames;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public boolean isInLoop() {
        return inLoop;
    }

    public void setInLoop(boolean inLoop) {
        this.inLoop = inLoop;
    }

}
