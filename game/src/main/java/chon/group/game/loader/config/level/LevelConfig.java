package chon.group.game.loader.config.level;

public class LevelConfig {

    private String id;
    private String type;
    private String behavior;
    private LevelProtagonistConfig protagonist;

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
}