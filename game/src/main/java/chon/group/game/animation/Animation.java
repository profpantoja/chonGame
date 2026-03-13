package chon.group.game.animation;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.image.Image;

/**
 * Represents a single Animation with all its frames, duration, and if it has to
 * end or it is in loop.
 */
public class Animation {

    private List<Image> frames = new ArrayList<Image>();
    private long duration = 100;
    private boolean inLoop = true;

    public Animation() {

    }

    public Animation(long duration, boolean inLoop) {
        this.duration = duration;
        this.inLoop = inLoop;
    }

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
