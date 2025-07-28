package chon.group.game.core.animation;

import java.util.EnumMap;
import java.util.Map;

public class AnimationGraphics {
    private final Map<AnimationStatus, AnimationSpritesheet> spritesheets = new EnumMap<>(AnimationStatus.class);

    public void add(AnimationStatus status, AnimationSpritesheet sheet) {
        spritesheets.put(status, sheet);
    }

    public AnimationSpritesheet getSpritesheet(AnimationStatus status) {
        return spritesheets.get(status);
    }
}