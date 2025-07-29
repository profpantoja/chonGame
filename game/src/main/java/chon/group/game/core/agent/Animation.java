package chon.group.game.core.agent;

import java.util.List;
import javafx.scene.image.Image;

public class Animation {

    private List<Image> frames;
    private double frameDuration;
    private double elapsedTime = 0;
    private int currentFrameIndex = 0;
    private boolean loop;
    private boolean finished = false;

    public Animation(List<Image> frames, double frameDuration, boolean loop) {
        this.frames = frames;
        this.frameDuration = frameDuration;
        this.loop = loop;
    }

    public void update(double deltaTime) {
        if (finished) return;
        elapsedTime += deltaTime;
        if (elapsedTime >= frameDuration) {
            elapsedTime = 0;
            currentFrameIndex++;
            if (currentFrameIndex >= frames.size()) {
                if (loop) {
                    currentFrameIndex = 0;
                } else {
                    currentFrameIndex = frames.size() - 1;
                    finished = true;
                }
            }
        }
    }

    public boolean isFinished() {
        return finished;
    }

    public boolean isLooping() {
        return this.loop;
    }

    public Image getCurrentFrame() {
        if (frames == null || frames.isEmpty()) {
            return null;
        }
        return frames.get(currentFrameIndex);
    }

    public void reset() {
        this.currentFrameIndex = 0;
        this.elapsedTime = 0;
        this.finished = false;
    }
}