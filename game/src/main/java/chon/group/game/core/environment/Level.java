package chon.group.game.core.environment;

import chon.group.game.core.agent.Agent;
import chon.group.game.core.agent.Entity;
import chon.group.game.core.agent.Object;
import chon.group.game.core.weapon.Shot;
import java.util.ArrayList;
import java.util.List;

public class Level extends Entity {

    /** List of agents present in the environment. */
    private List<Agent> agents;

    /** List of collectible/destructible objects in the environment. */
    private List<Object> objects;

    /** List of shots in the environment. */
    private List<Shot> shots;

    public Level(int posX, int posY, int height, int width, String pathImage) {
        super(posX, posY, height, width, 0, 0, pathImage, false, false);
        this.agents = new ArrayList<Agent>();
        this.objects = new ArrayList<Object>();
        this.shots = new ArrayList<Shot>();

    }

    public List<Agent> getAgents() {
        return agents;
    }

    public void setAgents(ArrayList<Agent> agents) {
        this.agents = agents;
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

    public boolean isCompleted(Environment env) {
        if (!env.getProtagonist().isDead() && env.getProtagonist().getPosX() >= 0.9 * this.getWidth()) {
            for (Agent agent : this.agents) {
                if (agent != env.getProtagonist() && !agent.isDead()) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

}
