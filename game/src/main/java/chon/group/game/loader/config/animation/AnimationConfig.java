package chon.group.game.loader.config.animation;

import java.util.List;

public class AnimationConfig {

    private long duration;
    private boolean loop;
    private List<FrameConfig> frames;

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public boolean isLoop() {
        return loop;
    }

    public void setLoop(boolean loop) {
        this.loop = loop;
    }

    public List<FrameConfig> getFrames() {
        return frames;
    }

    public void setFrames(List<FrameConfig> frames) {
        this.frames = frames;
    }
}