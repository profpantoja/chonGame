package chon.group.game.loader.factory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import chon.group.game.animation.Animation;
import chon.group.game.animation.AnimationType;
import chon.group.game.core.agent.Agent;
import chon.group.game.core.environment.Level;
import chon.group.game.core.environment.StoryType;
import chon.group.game.core.environment.behavior.BeatThemUp;
import chon.group.game.core.environment.behavior.EnvironmentBehavior;
import chon.group.game.core.environment.behavior.ShootThemUp;
import chon.group.game.loader.config.entity.agent.AgentConfig;
import chon.group.game.loader.config.entity.object.ObjectConfig;
import chon.group.game.loader.config.level.LevelAgentConfig;
import chon.group.game.loader.config.level.LevelConfig;
import chon.group.game.loader.config.level.LevelObjectConfig;
import chon.group.game.sound.Sound;
import chon.group.game.sound.SoundEvent;

public class LevelFactory {

    private final Map<String, Animation> animations;
    private final Map<String, Sound> sounds;
    private final Map<String, AgentConfig> agentConfigs;
    private final AgentFactory agentFactory;
    private final Map<String, ObjectConfig> objectConfigs;
    private final ObjectFactory objectFactory;

    public LevelFactory(
            Map<String, Animation> animations,
            Map<String, Sound> sounds,
            Map<String, AgentConfig> agentConfigs,
            AgentFactory agentFactory,
            Map<String, ObjectConfig> objectConfigs,
            ObjectFactory objectFactory) {
        this.animations = animations;
        this.sounds = sounds;
        this.agentConfigs = agentConfigs;
        this.agentFactory = agentFactory;
        this.objectConfigs = objectConfigs;
        this.objectFactory = objectFactory;
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
        applyAgents(level, config);
        applyObjects(level, config);

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

    private void applyAgents(Level level, LevelConfig config) {
        if (config.getAgents() == null) {
            return;
        }

        for (LevelAgentConfig agentInstance : config.getAgents()) {
            AgentConfig baseConfig = agentConfigs.get(agentInstance.getRef());

            if (baseConfig == null) {
                throw new IllegalArgumentException(
                        "Agent config not found: " + agentInstance.getRef());
            }

            Agent agent = agentFactory.create(agentInstance.getRef(), baseConfig);

            agent.setPosX(agentInstance.getSpawn().getX());
            agent.setPosY(agentInstance.getSpawn().getY());

            if (agentInstance.getVisibleBars() != null) {
                agent.setVisibleBars(agentInstance.getVisibleBars());
            }

            level.getAgents().add(agent);
        }
    }

    private void applyObjects(Level level, LevelConfig config) {
        if (config.getObjects() == null) {
            return;
        }

        for (LevelObjectConfig objectInstance : config.getObjects()) {
            ObjectConfig baseConfig = objectConfigs.get(objectInstance.getRef());

            if (baseConfig == null) {
                throw new IllegalArgumentException(
                        "Object config not found: " + objectInstance.getRef());
            }

            chon.group.game.core.agent.Object object = objectFactory.create(objectInstance.getRef(), baseConfig);

            object.setPosX(objectInstance.getSpawn().getX());
            object.setPosY(objectInstance.getSpawn().getY());

            if (objectInstance.getVisibleBars() != null) {
                object.setVisibleBars(objectInstance.getVisibleBars());
            }

            if (objectInstance.getDestructible() != null) {
                object.setDestructible(objectInstance.getDestructible());
            }

            if (objectInstance.getHealth() != null) {
                object.setHealth(objectInstance.getHealth());
                object.setFullHealth(objectInstance.getHealth());
            }

            if (objectInstance.getFlipped() != null) {
                object.getAnimationState().setFlipped(objectInstance.getFlipped());
            }

            level.getObjects().add(object);
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