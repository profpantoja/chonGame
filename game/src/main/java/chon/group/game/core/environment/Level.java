package chon.group.game.core.environment;

import chon.group.game.core.agent.Agent;
import chon.group.game.core.agent.Direction;
import chon.group.game.core.agent.Entity;
import chon.group.game.core.agent.Object;
import chon.group.game.core.environment.behavior.BaseBehavior;
import chon.group.game.core.environment.behavior.EnvironmentBehavior;
import chon.group.game.core.weapon.Shot;

import java.util.ArrayList;
import java.util.List;

public class Level extends Entity {

    /** Just a brief for the level. */
    private String description = new String();

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

    private StoryType type = StoryType.PLAYABLE;

    private EnvironmentBehavior behavior;

    public Level(int posX,
            int posY,
            int topY,
            int bottomY,
            StoryType type,
            EnvironmentBehavior behavior) {
        super(posX, posY, 0, 0, 0, 0, 0, Direction.IDLE, false, false);
        this.topY = topY;
        this.bottomY = bottomY;
        this.agents = new ArrayList<Agent>();
        this.objects = new ArrayList<Object>();
        this.shots = new ArrayList<Shot>();
        this.type = type;
        this.behavior = behavior;
    }

    public Level(int topY, int bottomY, StoryType type, EnvironmentBehavior behavior) {
        super(0, 0, 0, 0, 0, 0, 0, Direction.IDLE, false, false);
        this.topY = topY;
        this.bottomY = bottomY;
        this.agents = new ArrayList<Agent>();
        this.objects = new ArrayList<Object>();
        this.shots = new ArrayList<Shot>();
        this.type = type;
        this.behavior = behavior;
    }

    public Level(StoryType type) {
        super(0, 0, 0, 0, 0, 0, 0, Direction.IDLE, false, false);
        this.agents = new ArrayList<Agent>();
        this.objects = new ArrayList<Object>();
        this.shots = new ArrayList<Shot>();
        this.type = type;
        this.behavior = new BaseBehavior();
    }

    public Level(StoryType type, String description) {
        super(0, 0, 0, 0, 0, 0, 0, Direction.IDLE, false, false);
        this.description = description;
        this.agents = new ArrayList<Agent>();
        this.objects = new ArrayList<Object>();
        this.shots = new ArrayList<Shot>();
        this.type = type;
        this.behavior = new BaseBehavior();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public StoryType getType() {
        return type;
    }

    public void setType(StoryType type) {
        this.type = type;
    }

    public EnvironmentBehavior getBehavior() {
        return behavior;
    }

    public void setBehavior(EnvironmentBehavior behavior) {
        this.behavior = behavior;
    }

    /**
     * Gets the total number of collectible objects in the environment.
     *
     * @return the total number of collectibles
     */
    public int getTotalCollectibleCount() {
        return (int) this.objects.stream().filter(Object::isCollectible).count();
    }

    /**
     * Checks whether the level has been completed.
     *
     * <p>
     * A level is considered completed when the protagonist has reached at least
     * 95% of the level width and all agents in the level are dead.
     * </p>
     *
     * @param env the current game environment
     * @return {@code true} if the level is completed; {@code false} otherwise
     */
    public boolean isCompleted(Environment env) {
        boolean reachedLevelEnd = env.getProtagonist().getPosX() >= 0.95 * this.getWidth();
        if (!reachedLevelEnd) {
            return false;
        }
        return this.agents.stream().allMatch(Agent::isDead);
    }

}
