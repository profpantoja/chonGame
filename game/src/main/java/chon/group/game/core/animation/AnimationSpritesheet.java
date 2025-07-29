package chon.group.game.core.animation;

import javafx.scene.image.Image;

public interface AnimationSpritesheet {
    int getFrameWidth();
    int getFrameHeight();
    int getFrameCount();
    int getDurationMs();
    Image getSpritesheetImage();
}