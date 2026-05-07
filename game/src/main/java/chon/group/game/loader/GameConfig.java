package chon.group.game.loader;

import java.util.List;

import chon.group.game.loader.config.environment.EnvironmentConfig;
import chon.group.game.loader.config.entity.EntitiesConfig;
import chon.group.game.loader.config.level.LevelConfig;
import chon.group.game.loader.config.media.MediaConfig;

public class GameConfig {

    private EnvironmentConfig environment;

    private MediaConfig media;

    private EntitiesConfig entities;

    private List<LevelConfig> levels;

    public GameConfig() {
    }

    public EnvironmentConfig getEnvironment() {
        return environment;
    }

    public void setEnvironment(EnvironmentConfig environment) {
        this.environment = environment;
    }

    public MediaConfig getMedia() {
        return media;
    }

    public void setMedia(MediaConfig media) {
        this.media = media;
    }

    public EntitiesConfig getEntities() {
        return entities;
    }

    public void setEntities(EntitiesConfig entities) {
        this.entities = entities;
    }

    public List<LevelConfig> getLevels() {
        return levels;
    }

    public void setLevels(List<LevelConfig> levels) {
        this.levels = levels;
    }

}