package chon.group.game.loader.config.agent;

import java.util.List;
import java.util.Map;

public class AgentConfig {

    private PositionConfig position;
    private SizeConfig size;
    private StatsConfig stats;
    private StateConfig state;

    private Map<String, String> animations;
    private Map<String, String> sounds;

    private List<String> weapons;
    private String defaultWeapon;

    public PositionConfig getPosition() {
        return position;
    }

    public void setPosition(PositionConfig position) {
        this.position = position;
    }

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

    public List<String> getWeapons() {
        return weapons;
    }

    public void setWeapons(List<String> weapons) {
        this.weapons = weapons;
    }

    public String getDefaultWeapon() {
        return defaultWeapon;
    }

    public void setDefaultWeapon(String defaultWeapon) {
        this.defaultWeapon = defaultWeapon;
    }
}