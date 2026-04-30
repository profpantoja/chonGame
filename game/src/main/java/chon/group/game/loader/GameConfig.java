package chon.group.game.loader;

import java.util.List;
import java.util.Map;

import chon.group.game.loader.config.agent.AgentConfig;
import chon.group.game.loader.config.animation.AnimationConfig;
import chon.group.game.loader.config.level.LevelConfig;
import chon.group.game.loader.config.menu.MenuConfig;
import chon.group.game.loader.config.menu.MenuHandlerConfig;
import chon.group.game.loader.config.object.ObjectConfig;
import chon.group.game.loader.config.sound.SoundConfig;
import chon.group.game.loader.config.weapon.ShotConfig;
import chon.group.game.loader.config.weapon.WeaponConfig;

//@JsonIgnoreProperties(ignoreUnknown = true)
public class GameConfig {

    private String protagonist;

    private Map<String, AgentConfig> agents;
    private Map<String, ObjectConfig> objects;
    private Map<String, AnimationConfig> animations;
    private Map<String, SoundConfig> sounds;
    private Map<String, ShotConfig> shots;
    private Map<String, WeaponConfig> weapons;
    private Map<String, MenuConfig> menus;
    private MenuHandlerConfig menuHandler;

    private List<LevelConfig> levels;

    public String getProtagonist() {
        return protagonist;
    }

    public void setProtagonist(String protagonist) {
        this.protagonist = protagonist;
    }

    public Map<String, AgentConfig> getAgents() {
        return agents;
    }

    public void setAgents(Map<String, AgentConfig> agents) {
        this.agents = agents;
    }

    public Map<String, ObjectConfig> getObjects() {
        return objects;
    }

    public void setObjects(Map<String, ObjectConfig> objects) {
        this.objects = objects;
    }

    public Map<String, AnimationConfig> getAnimations() {
        return animations;
    }

    public void setAnimations(Map<String, AnimationConfig> animations) {
        this.animations = animations;
    }

    public Map<String, SoundConfig> getSounds() {
        return sounds;
    }

    public void setSounds(Map<String, SoundConfig> sounds) {
        this.sounds = sounds;
    }

    public Map<String, ShotConfig> getShots() {
        return shots;
    }

    public void setShots(Map<String, ShotConfig> shots) {
        this.shots = shots;
    }

    public Map<String, WeaponConfig> getWeapons() {
        return weapons;
    }

    public void setWeapons(Map<String, WeaponConfig> weapons) {
        this.weapons = weapons;
    }

    public Map<String, MenuConfig> getMenus() {
        return menus;
    }

    public void setMenus(Map<String, MenuConfig> menus) {
        this.menus = menus;
    }

    public List<LevelConfig> getLevels() {
        return levels;
    }

    public void setLevels(List<LevelConfig> levels) {
        this.levels = levels;
    }

    public MenuHandlerConfig getMenuHandler() {
        return menuHandler;
    }

    public void setMenuHandler(MenuHandlerConfig menuHandler) {
        this.menuHandler = menuHandler;
    }

}