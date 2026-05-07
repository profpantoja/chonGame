package chon.group.game.loader.config.entity.object;

import java.util.Map;

import chon.group.game.loader.config.entity.agent.SizeConfig;
import chon.group.game.loader.config.entity.agent.StateConfig;
import chon.group.game.loader.config.entity.agent.StatsConfig;

public class ObjectConfig {

    private SizeConfig size;
    private StatsConfig stats;
    private StateConfig state;
    private ObjectPropertiesConfig properties;
    private Map<String, String> animations;
    private Map<String, String> sounds;

    public SizeConfig getSize() {
        return size;
    }

    public void setSize(SizeConfig size) {
        this.size = size;
    }

    public StatsConfig getStats() {
        return stats;
    }

    public void setStats(StatsConfig stats) {
        this.stats = stats;
    }

    public StateConfig getState() {
        return state;
    }

    public void setState(StateConfig state) {
        this.state = state;
    }

    public ObjectPropertiesConfig getProperties() {
        return properties;
    }

    public void setProperties(ObjectPropertiesConfig properties) {
        this.properties = properties;
    }

    public Map<String, String> getAnimations() {
        return animations;
    }

    public void setAnimations(Map<String, String> animations) {
        this.animations = animations;
    }

    public Map<String, String> getSounds() {
        return sounds;
    }

    public void setSounds(Map<String, String> sounds) {
        this.sounds = sounds;
    }
}