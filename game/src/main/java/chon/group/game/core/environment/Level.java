package chon.group.game.core.environment;

import java.util.ArrayList;
import java.util.List;

import chon.group.game.core.agent.Agent;
import chon.group.game.core.agent.Entity;
import chon.group.game.core.agent.Object;
import chon.group.game.core.weapon.Shot;
import chon.group.game.core.weapon.Slash;

public class Level extends Entity {
    
    /** Background music for the level. */
    private String backgroundMusic;
    
    /** List of agents present in the environment. */
    private List<Agent> agents;

    /** List of collectible/destructible objects in the environment. */
    private List<Object> objects;

    /** List of shots in the environment. */
    private List<Shot> shots;

    /** List of slashes in the environment. */
    private List<Slash> slashes;

    /** Total number of collectible objects in the environment. */
    private int totalCollectibleCount = 0;

    public Level(int posX, int posY, int height, int width, String pathImage) {
        super(posX, posY, height, width, 0, 0, pathImage, false, false);
        this.agents = new ArrayList<Agent>();
        this.objects = new ArrayList<Object>();
        this.shots = new ArrayList<Shot>();

        this.slashes = new ArrayList<Slash>();

    public List<Agent> getAgents() {
        return agents;
    }

    
    public void setAgents(ArrayList<Agent> agents) {
        this.agents = agents;
    }
    
    public String getBackgroundMusic() {
        return backgroundMusic;
    }

    public void setBackgroundMusic(String backgroundMusic) {
        this.backgroundMusic = backgroundMusic;
    }
    
    public List<Object> getObjects() {
        return objects;
    }

    public void setObjects(List<Object> objects) {
        this.objects = objects;
    }

    public List<Shot> getShots() {
        return shots;
    }

    public void setShots(List<Shot> shots) {
        this.shots = shots;
    }

    public List<Slash> getSlashes() {
        return slashes;
    }

    public void setSlashes(List<Slash> slashes) {
        this.slashes = slashes;
    }
    

    /**
     * Gets the total number of collectible objects in the environment.
     *
     * @return the total number of collectibles
     */
    public int getTotalCollectibleCount() {
        return totalCollectibleCount;
    }

    public boolean isCompleted(Environment env) {
        if (env.getProtagonist().getPosX() >= 0.95 * this.getWidth()) {
            for (Agent agent : this.agents) {
                if (agent != env.getProtagonist() && !agent.isDead()) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    /**
     * Counts how many collectible objects are currently in the environment.
     */
    public void countCollectibles() {
        totalCollectibleCount = (int) this.objects.stream()
                .filter(Object::isCollectible)
                .count();
    }

}
