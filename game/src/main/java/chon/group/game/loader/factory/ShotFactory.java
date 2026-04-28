package chon.group.game.loader.factory;

import java.util.HashMap;
import java.util.Map;

import chon.group.game.animation.Animation;
import chon.group.game.animation.AnimationType;
import chon.group.game.core.weapon.ConcreteShot;
import chon.group.game.core.weapon.Shot;
import chon.group.game.loader.config.weapon.ShotConfig;

public class ShotFactory {

    public Map<String, Shot> buildAll(
            Map<String, ShotConfig> configs,
            Map<String, Animation> animations) {

        Map<String, Shot> shots = new HashMap<>();

        for (var entry : configs.entrySet()) {
            String id = entry.getKey();
            ShotConfig config = entry.getValue();

            shots.put(id, build(id, config, animations));
        }

        return shots;
    }

    public Shot build(
            String id,
            ShotConfig config,
            Map<String, Animation> animations) {

        ConcreteShot shot = new ConcreteShot(
                config.getSize().getHeight(),
                config.getSize().getWidth(),
                (int) config.getSpeed(),
                0,
                false,
                config.getDamage(),
                config.getRange());

        shot.setDestructible(config.isDestructible());

        if (config.getAnimation() != null) {
            Animation animation = animations.get(config.getAnimation());

            if (animation == null) {
                throw new IllegalArgumentException(
                        "Animation not found for shot '" + id + "': " + config.getAnimation());
            }

            shot.getAnimationSet().add(AnimationType.IDLE, animation);
            shot.getAnimationState().setCurrentAnimation(animation);
        }

        return shot;
    }
}