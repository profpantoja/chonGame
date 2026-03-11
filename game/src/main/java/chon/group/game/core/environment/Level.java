package chon.group.game.core.environment;

import chon.group.game.core.agent.Agent;
import chon.group.game.core.agent.Direction;
import chon.group.game.core.agent.Entity;
import chon.group.game.core.agent.Object;
import chon.group.game.core.weapon.Shot;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.List;

public class Level extends Entity {

    /** It defines the top boundary of the level. */
    private int topY;

    /** It defines the bottom boundary of the level. */
    private int bottomY;

    /** List of agents present in the environment. */
    private List<Agent> agents;

    /** List of collectible/destructible objects in the environment. */
    private List<Object> objects;

    /** List of shots in the environment. */
    private List<Shot> shots;

    /** Total number of collectible objects in the environment. */
    private int totalCollectibleCount = 0;

    /** Image representing the entity. */
    protected Image image;

    public Level(int posX, int posY, int height, int width, int topY, int bottomY, String pathImage) {
        super(posX, posY, height, width, 0, 0, Direction.IDLE, false, false);
        this.topY = topY;
        this.bottomY = bottomY;
        this.image = new Image(getClass().getResource(pathImage).toExternalForm());
        this.agents = new ArrayList<Agent>();
        this.objects = new ArrayList<Object>();
        this.shots = new ArrayList<Shot>();
    }

    public int getTopY() {
        return topY;
    }

    public void setTopY(int topY) {
        this.topY = topY;
    }

    public int getBottomY() {
        return bottomY;
    }

    public void setBottomY(int bottomY) {
        this.bottomY = bottomY;
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

    /**
     * Gets the entity image.
     *
     * @return the entity image
     */
    public Image getImage() {
        return image;
    }

    /**
     * Gets the entity flipped status.
     *
     * @param image the new image
     */
    public void setImage(Image image) {
        this.image = image;
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
