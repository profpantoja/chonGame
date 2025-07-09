package chon.group.game.domain.environment;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import chon.group.game.core.agent.Agent;
import chon.group.game.core.agent.Entity;
import chon.group.game.core.weapon.Shot;
import javafx.scene.image.Image;

/**
 * Represents the game environment, including properties such as dimensions,
 * position,
 * background image, agents, and the protagonist.
 * The environment also controls rendering, restricts the environment area,
 * prints an agent's coordinates, and detects collisions between the protagonist
 * and agents.
 */
public class Environment extends Entity {

    /** The background image of the pause. */
    private Image pauseImage;

    /** The background image of the game over. */
    private Image gameOverImage;

    /** The protagonist instance. */
    private Agent protagonist;

    /** List of agents present in the environment. */
    private List<Agent> agents;

    /** List of shots present in the environment. */
    private List<Shot> shots;

    /**
     * Constructor to initialize the environment with dimensions, position, and a
     * background image.
     *
     * @param posX      the initial X (horizontal) position of the environment
     * @param posY      the initial Y (vertical) position of the environment
     * @param width     the width of the environment
     * @param height    the height of the environment
     * @param pathImage the path to the background image
     */
    public Environment(int posX, int posY, int width, int height, String pathImage) {
        super(posX, posY, height, width, width, height, pathImage);
        this.agents = new ArrayList<Agent>();
        this.shots = new ArrayList<Shot>();
    }

    /**
     * Constructor to initialize the environment with dimensions, position, a
     * background image, and a list of agents.
     *
     * @param posX      the initial X (horizontal) position of the environment
     * @param posY      the initial Y (vertical) position of the environment
     * @param width     the width of the environment
     * @param height    the height of the environment
     * @param pathImage the path to the background image
     * @param agents    the list of agents in the environment
     */
    public Environment(int posX, int posY, int width, int height, String pathImage, ArrayList<Agent> agents) {
        super(posX, posY, height, width, width, height, pathImage);
        this.agents = agents;
        this.shots = new ArrayList<Shot>();
    }

    /**
     * Gets the background image for the pause.
     *
     * @return the pause image
     */
    public Image getPauseImage() {
        return pauseImage;
    }

    /**
     * Sets the background image for the pause.
     *
     * @param pathImage the path to the new pause image
     */
    public void setPauseImage(String pathImage) {
        this.pauseImage = new Image(getClass().getResource(pathImage).toExternalForm());
    }

    /**
     * Gets the background image for the game over.
     *
     * @return the game over image
     */
    public Image getGameOverImage() {
        return gameOverImage;
    }

    /**
     * Sets the background image for the game over.
     *
     * @param pathImage the path to the new game over image
     */
    public void setGameOverImage(String pathImage) {
        this.gameOverImage = new Image(getClass().getResource(pathImage).toExternalForm());
    }

    /**
     * Gets the protagonist of the environment.
     *
     * @return the protagonist of the environment
     */
    public Agent getProtagonist() {
        return protagonist;
    }

    /**
     * Sets the protagonist of the environment.
     *
     * @param protagonist the new protagonist of the environment
     */
    public void setProtagonist(Agent protagonist) {
        this.protagonist = protagonist;
    }

    /**
     * Gets the list of agents present in the environment.
     *
     * @return the list of agents
     */
    public List<Agent> getAgents() {
        return agents;
    }

    /**
     * Sets the list of agents present in the environment.
     *
     * @param agents the new list of agents
     */
    public void setAgents(ArrayList<Agent> agents) {
        this.agents = agents;

    }

    /**
     * Gets the list of shots present in the environment.
     *
     * @return the list of shots
     */
    public List<Shot> getShots() {
        return shots;
    }

    /**
     * Sets the list of shots present in the environment.
     *
     * @param agents the new list of shots
     */
    public void setShots(List<Shot> shots) {
        this.shots = shots;
    }

    /**
     * Checks if the protagonist is within the environment's boundaries and adjusts
     * its position if necessary.
     */
    public void checkBorders() {
        if (this.protagonist.getPosX() < 0) {
            this.protagonist.setPosX(0);
        } else if ((this.protagonist.getPosX() + this.protagonist.getWidth()) > this.getWidth()) {
            this.protagonist.setPosX(this.getWidth() - protagonist.getWidth());
        } else if (this.protagonist.getPosY() < 0) {
            this.protagonist.setPosY(0);
        } else if ((this.protagonist.getPosY() + this.protagonist.getHeight()) > this.getHeight()) {
            this.protagonist.setPosY(this.getHeight() - this.protagonist.getHeight());
        }
    }

    /**
     * Detects collisions between the protagonist and other agents in the
     * environment.
     * When a collision occurs and the protagonist takes damage, a message is
     * created to display the damage amount.
     */
    public void detectCollision() {
        for (Agent agent : this.agents) {
            if (protagonist != null && intersect(this.protagonist, agent)) {
                /* Removing the console output for collision. */
                // System.out.println("Collision detected with agent: " + agent);
                int damage = 100;
                /* The protagonist takes damage when colliding with an agent. */
                protagonist.takeDamage(damage);
            }
        }
    }

    /**
     * Checks if two agents collide with each other based on their positions and
     * dimensions.
     *
     * This method uses the coordinates and dimensions of both agents to determine
     * if their areas overlap. The collision is calculated by comparing the edges
     * of the image represented by each agent.
     *
     * @param a the first agent
     * @param b the second agent
     * @return true if the agents collide, otherwise false
     */

    private boolean intersect(Entity a, Entity b) {
        // Returns true if there is a collision between two agents
        return a.getPosX() < b.getPosX() + b.getWidth() &&
                a.getPosX() + a.getWidth() > b.getPosX() &&
                a.getPosY() < b.getPosY() + b.getHeight() &&
                a.getPosY() + a.getHeight() > b.getPosY();
    }

    public void updateShots() {
        Iterator<Shot> itShot = this.shots.iterator();
        while (itShot.hasNext()) {
            Shot shot = itShot.next();
            if ((shot.getPosX() > this.getWidth()) || ((shot.getPosX() + shot.getWidth()) < 0)) {
                itShot.remove();
            } else {
                if (this.intersect(protagonist, shot)) {
                    protagonist.takeDamage(shot.getDamage());
                    itShot.remove();
                } else {
                    Iterator<Agent> itAgent = this.agents.iterator();
                    while (itAgent.hasNext()) {
                        Agent agent = itAgent.next();
                        if (this.intersect(agent, shot)) {
                            agent.takeDamage(shot.getDamage());
                            if (agent.isDead())
                                itAgent.remove();
                            itShot.remove();
                        }
                    }
                }
                shot.move(new ArrayList<>(List.of(shot.getDirection())));
            }
        }
    }

}