package chon.group.game.loader.factory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import chon.group.game.animation.Animation;
import chon.group.game.animation.AnimationType;
import chon.group.game.core.environment.Level;
import chon.group.game.core.environment.StoryType;
import chon.group.game.core.environment.behavior.BeatThemUp;
import chon.group.game.core.environment.behavior.EnvironmentBehavior;
import chon.group.game.core.environment.behavior.ShootThemUp;
import chon.group.game.loader.config.level.LevelConfig;
import chon.group.game.sound.Sound;
import chon.group.game.sound.SoundEvent;

public class LevelFactory {

    private final Map<String, Animation> animations;
    private final Map<String, Sound> sounds;

    public LevelFactory(
            Map<String, Animation> animations,
            Map<String, Sound> sounds) {
        this.animations = animations;
        this.sounds = sounds;
    }

    public List<Level> buildAll(List<LevelConfig> configs) {
        List<Level> levels = new ArrayList<>();

        for (LevelConfig config : configs) {
            levels.add(build(config));
        }

        return levels;
    }

    public Level build(LevelConfig config) {
        StoryType type = StoryType.valueOf(config.getType());
        EnvironmentBehavior behavior = createBehavior(config.getBehavior());

        Level level;

        if (type == StoryType.PLAYABLE) {
            level = new Level(
                    config.getTopY(),
                    config.getBottomY(),
                    type,
                    behavior);
        } else if (config.getText() != null && !config.getText().isBlank()) {
            level = new Level(
                    type,
                    config.getText(),
                    behavior);
        } else {
            level = new Level(
                    type,
                    behavior);
        }

        applyAnimation(level, config);
        applySounds(level, config);

        return level;
    }

    private void applyAnimation(Level level, LevelConfig config) {
        if (config.getAnimation() == null) {
            return;
        }

        Animation animation = animations.get(config.getAnimation());

        if (animation == null) {
            throw new IllegalArgumentException(
                    "Animation not found for level '" + config.getId() + "': "
                            + config.getAnimation());
        }

        level.getAnimationSet().add(AnimationType.IDLE, animation);
        level.getAnimationState().setCurrentAnimation(animation);
    }

    private void applySounds(Level level, LevelConfig config) {
        if (config.getSounds() == null) {
            return;
        }

        for (var entry : config.getSounds().entrySet()) {
            SoundEvent event = SoundEvent.valueOf(entry.getKey());
            Sound sound = sounds.get(entry.getValue());

            if (sound == null) {
                throw new IllegalArgumentException(
                        "Sound not found for level '" + config.getId() + "': "
                                + entry.getValue());
            }

            level.getSoundSet().add(event, sound);
        }
    }

    private EnvironmentBehavior createBehavior(String behavior) {
        return switch (behavior) {
            case "beatThemUp" -> new BeatThemUp();
            case "shootThemUp" -> new ShootThemUp();
            default -> throw new IllegalArgumentException(
                    "Unknown environment behavior: " + behavior);
        };
    }
}