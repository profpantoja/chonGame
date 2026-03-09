package chon.group.game.animation;

import java.util.HashMap;
import java.util.Map;

public class AnimationSet {

    private Map<AnimationType, Animation> animations = new HashMap<>();

    public void add(AnimationType action, Animation animation) {
        animations.put(action, animation);
    }

    public Animation get(AnimationType action) {
        return animations.get(action);
    }
}
