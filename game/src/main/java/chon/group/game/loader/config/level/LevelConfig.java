package chon.group.game.loader.config.level;

import java.util.List;
import java.util.Map;

public class LevelConfig {

    private String id;
    private String type;
    private String behavior;
    private LevelProtagonistConfig protagonist;
    private Integer topY;
    private Integer bottomY;
    private String text;
    private String animation;
    private Map<String, String> sounds;
    private List<LevelAgentConfig> agents;
    private List<LevelObjectConfig> objects;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBehavior() {
        return behavior;
    }

    public void setBehavior(String behavior) {
        this.behavior = behavior;
    }

    public LevelProtagonistConfig getProtagonist() {
        return protagonist;
    }

    public void setProtagonist(LevelProtagonistConfig protagonist) {
        this.protagonist = protagonist;
    }

    public Integer getTopY() {
        return topY;
    }

    public void setTopY(Integer topY) {
        this.topY = topY;
    }

    public Integer getBottomY() {
        return bottomY;
    }

    public void setBottomY(Integer bottomY) {
        this.bottomY = bottomY;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAnimation() {
        return animation;
    }

    public void setAnimation(String animation) {
        this.animation = animation;
    }

    public Map<String, String> getSounds() {
        return sounds;
    }

    public List<LevelAgentConfig> getAgents() {
        return agents;
    }

    public void setAgents(List<LevelAgentConfig> agents) {
        this.agents = agents;
    }

    public List<LevelObjectConfig> getObjects() {
        return objects;
    }

    public void setObjects(List<LevelObjectConfig> objects) {
        this.objects = objects;
    }

}