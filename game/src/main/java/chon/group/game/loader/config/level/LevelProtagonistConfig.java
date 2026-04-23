package chon.group.game.loader.config.level;

import chon.group.game.loader.config.agent.PositionConfig;

public class LevelProtagonistConfig {

    private String ref;
    private PositionConfig spawn;

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
}
