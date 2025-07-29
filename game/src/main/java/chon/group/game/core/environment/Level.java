package chon.group.game.core.environment;

import chon.group.game.core.agent.Agent;
import chon.group.game.core.agent.Entity;
import chon.group.game.core.agent.Object;
import chon.group.game.core.weapon.Shot;
import java.util.ArrayList;
import java.util.List;

public class Level extends Entity {

    private List<Agent> agents;
    private List<Object> objects;
    private List<Shot> shots;
    private int totalEnemiesToSpawn;
    private int enemiesKilledCount;
    private boolean bossFightActive = false;

    public Level(int posX, int posY, int height, int width, String pathImage) {
        super(posX, posY, height, width, 0, 0, pathImage, false, false);
        this.agents = new ArrayList<>();
        this.objects = new ArrayList<>();
        this.shots = new ArrayList<>();
        this.enemiesKilledCount = 0;
        this.totalEnemiesToSpawn = 0;
    }
    
    public boolean isCompleted(Environment env) {
        if (bossFightActive) {
            return this.getAgents().isEmpty();
        }
        return getEnemiesKilledCount() >= getTotalEnemiesToSpawn() && getTotalEnemiesToSpawn() > 0;
    }

    public boolean isBossFightActive() {
        return bossFightActive;
    }

    public void setBossFightActive(boolean active) {
        this.bossFightActive = active;
    }
    
    public void setTotalEnemiesToSpawn(int total) { this.totalEnemiesToSpawn = total; }
    public int getTotalEnemiesToSpawn() { return totalEnemiesToSpawn; }
    public void incrementEnemiesKilled() { this.enemiesKilledCount++; }
    public int getEnemiesKilledCount() { return this.enemiesKilledCount; }
    public void reset() { this.agents.clear(); this.shots.clear(); this.enemiesKilledCount = 0; this.bossFightActive = false; }
    public List<Agent> getAgents() { return agents; }
    public void setAgents(List<Agent> agents) { this.agents = agents; }
    public List<Object> getObjects() { return objects; }
    public void setObjects(List<Object> objects) { this.objects = objects; }
    public List<Shot> getShots() { return shots; }
    public void setShots(List<Shot> shots) { this.shots = shots; }
}