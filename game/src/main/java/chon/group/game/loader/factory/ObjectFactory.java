package chon.group.game.loader.factory;

import java.util.Map;

import chon.group.game.animation.Animation;
import chon.group.game.animation.AnimationType;
import chon.group.game.core.agent.Direction;
import chon.group.game.core.agent.Object;
import chon.group.game.loader.config.entity.object.ObjectConfig;
import chon.group.game.sound.Sound;
import chon.group.game.sound.SoundEvent;

public class ObjectFactory {

    private final Map<String, Animation> animations;
    private final Map<String, Sound> sounds;

    public ObjectFactory(
            Map<String, Animation> animations,
            Map<String, Sound> sounds) {
        this.animations = animations;
        this.sounds = sounds;
    }

    public Object create(String id, ObjectConfig config) {
        Object object = new Object(
                0,
                0,
                config.getSize().getWidth(),
                config.getSize().getHeight(),
                config.getSize().getHitboxRatio(),
                config.getStats().getSpeed(),
                config.getStats().getHealth(),
                Direction.valueOf(config.getState().getDirection()),
                config.getState().isFlipped(),
                config.getState().isVisibleBars(),
                config.getProperties().isCollectible(),
                config.getProperties().isDestructible(),
                config.getProperties().getValue());

        applyAnimations(object, config);
        applySounds(object, config);

        return object;
    }

    private void applyAnimations(Object object, ObjectConfig config) {
        if (config.getAnimations() == null) {
            return;
        }

        for (var entry : config.getAnimations().entrySet()) {
            AnimationType type = AnimationType.valueOf(entry.getKey());
            Animation animation = animations.get(entry.getValue());

            if (animation == null) {
                throw new IllegalArgumentException(
                        "Animation not found for object: " + entry.getValue());
            }

            object.getAnimationSet().add(type, animation);

            if (type == AnimationType.IDLE) {
                object.getAnimationState().setCurrentAnimation(animation);
            }
        }
    }

    private void applySounds(Object object, ObjectConfig config) {
        if (config.getSounds() == null) {
            return;
        }

        for (var entry : config.getSounds().entrySet()) {
            SoundEvent event = SoundEvent.valueOf(entry.getKey());
            Sound sound = sounds.get(entry.getValue());

            if (sound == null) {
                throw new IllegalArgumentException(
                        "Sound not found for object: " + entry.getValue());
            }

            object.getSoundSet().add(event, sound);
        }
    }
}