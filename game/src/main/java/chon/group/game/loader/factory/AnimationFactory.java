package chon.group.game.loader.factory;

import java.util.HashMap;
import java.util.Map;

import chon.group.game.animation.Animation;
import chon.group.game.animation.Frame;
import chon.group.game.loader.config.animation.AnimationConfig;
import chon.group.game.loader.config.animation.FrameConfig;
import javafx.scene.image.Image;

public class AnimationFactory {

    public Map<String, Animation> buildAll(Map<String, AnimationConfig> configs) {

        Map<String, Animation> animations = new HashMap<>();

        for (var entry : configs.entrySet()) {
            String id = entry.getKey();
            AnimationConfig config = entry.getValue();

            animations.put(id, build(config));
        }

        return animations;
    }

    public Animation build(AnimationConfig config) {

        Animation animation = new Animation(
                config.getDuration(),
                config.isLoop());

        for (FrameConfig frameConfig : config.getFrames()) {

            Image image = new Image(
                    getClass().getResource(frameConfig.getImage()).toExternalForm());

            Frame frame = new Frame(
                    image,
                    frameConfig.getWidth(),
                    frameConfig.getHeight());

            animation.getFrames().add(frame);
        }

        return animation;
    }
}