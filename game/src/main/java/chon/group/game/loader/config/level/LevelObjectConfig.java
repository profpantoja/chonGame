package chon.group.game.loader.config.level;

import chon.group.game.loader.config.entity.agent.PositionConfig;

public class LevelObjectConfig {

    private String ref;
    private PositionConfig spawn;

    private Boolean visibleBars;
    private Boolean destructible;
    private Integer health;
    private Boolean flipped;

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public PositionConfig getSpawn() {
        return spawn;
    }

    public void setSpawn(PositionConfig spawn) {
        this.spawn = spawn;
    }

    public Boolean getVisibleBars() {
        return visibleBars;
    }

    public void setVisibleBars(Boolean visibleBars) {
        this.visibleBars = visibleBars;
    }

    public Boolean getDestructible() {
        return destructible;
    }

    public void setDestructible(Boolean destructible) {
        this.destructible = destructible;
    }

    public Integer getHealth() {
        return health;
    }

    public void setHealth(Integer health) {
        this.health = health;
    }

    public Boolean getFlipped() {
        return flipped;
    }

    public void setFlipped(Boolean flipped) {
        this.flipped = flipped;
    }

}