package chon.group.game.loader.config.entity;

import java.util.Map;

import chon.group.game.loader.config.entity.agent.AgentConfig;
import chon.group.game.loader.config.entity.object.ObjectConfig;
import chon.group.game.loader.config.entity.weapon.ShotConfig;
import chon.group.game.loader.config.entity.weapon.WeaponConfig;

public class EntitiesConfig {

    private Map<String, AgentConfig> agents;

    private Map<String, ObjectConfig> objects;

    private Map<String, ShotConfig> shots;

    private Map<String, WeaponConfig> weapons;

    public EntitiesConfig() {
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

}