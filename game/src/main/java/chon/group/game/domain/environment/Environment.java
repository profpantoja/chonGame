package chon.group.game.domain.environment;

import java.util.ArrayList;
import java.util.List;

import chon.group.game.domain.agent.Agent;
import chon.group.game.domain.collectibles.Collectible;
import chon.group.game.domain.collectibles.PointsItem;
import javafx.scene.image.Image;

/**
 * Represents the game environment, including properties such as dimensions,
 * position,
 * background image, agents, and the protagonist.
 * The environment also controls rendering, restricts the environment area,
 * prints an agent's coordinates, and detects collisions between the protagonist
 * and agents.
 */
public class Environment {

    /** The X (horizontal) position of the environment. */
    private int posX;

    /** The Y (vertical) position of the environment. */
    private int posY;

    /** The width of the environment. */
    private int width;

    /** The height of the environment. */
    private int height;

    /** The background image of the environment. */
    private Image image;

    /** The background image of the pause. */
    private Image pauseImage;

    // Background image of the GameOver
    private Image gameOverImage;

    // Background image of victory
    private Image victoryImage;

    /** The protagonist instance. */
    private Agent protagonist;

    /** List of agents present in the environment. */
    private List<Agent> agents = new ArrayList<Agent>();

    /** List of collectibles present in the environment. */
    private List<Collectible> collectibles = new ArrayList<Collectible>();

    /** Control the max of collectibles entities that can be on environment */
    private int maxCollectibles = 10;

    private List<PointsItem> models = new ArrayList<PointsItem>();

    // Spawn Interval for a points Item;
    private long piSpawnInterval = 5000;

    /** Last time a collectible was generated */
    private long lastCollectibleSpawnTime = 0;

    /**
     * Default constructor to create an empty environment.
     */
    public Environment() {
    }

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
        this.posX = posX;
        this.posY = posY;
        this.height = height;
        this.width = width;
        this.setImage(pathImage);
        this.agents = new ArrayList<Agent>();
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
        this.posX = posX;
        this.posY = posY;
        this.height = height;
        this.width = width;
        this.setImage(pathImage);
        this.setAgents(agents);
    }

    /**
     * Gets the X (horizontal) position of the environment.
     *
     * @return the X position of the environment
     */
    public int getPosX() {
        return posX;
    }

    /**
     * Sets the X (horizontal) position of the environment.
     *
     * @param posX the new X position of the environment
     */
    public void setPosX(int posX) {
        this.posX = posX;
    }

    /**
     * Gets the Y (vertical) position of the environment.
     *
     * @return the Y position of the environment
     */
    public int getPosY() {
        return posY;
    }

    /**
     * Sets the Y (vertical) position of the environment.
     *
     * @param posY the new Y position of the environment
     */
    public void setPosY(int posY) {
        this.posY = posY;
    }

    /**
     * Gets the width of the environment.
     *
     * @return the width of the environment
     */
    public int getWidth() {
        return width;
    }

    /**
     * Sets the width of the environment.
     *
     * @param width the new width of the environment
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * Gets the height of the environment.
     *
     * @return the height of the environment
     */
    public int getHeight() {
        return height;
    }

    /**
     * Sets the height of the environment.
     *
     * @param height the new height of the environment
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * Gets the background image of the environment.
     *
     * @return the background image of the environment
     */
    public Image getImage() {
        return image;
    }

    /**
     * Sets the background image of the environment.
     *
     * @param pathImage the path to the new background image
     */
    public void setImage(String pathImage) {
        this.image = new Image(getClass().getResource(pathImage).toExternalForm());
    }

    public Image getPauseImage() {
        return pauseImage;
    }

    public void setPauseImage(String pathImage) {
        this.pauseImage = new Image(getClass().getResource(pathImage).toExternalForm());
    }

    // Get game over image
    public Image getGameOverImage() {
        return gameOverImage;
    }

    // Set the backgournd of Game Over image
    public void setGameOverImage(String pathImage) {
        this.gameOverImage = new Image(getClass().getResource(pathImage).toExternalForm());
    }

    public Image getVictoryImage() {
        return victoryImage;
    }

    public void setVictoryImage(String pathImage) {
        this.victoryImage = new Image(getClass().getResource(pathImage).toExternalForm());
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

    public List<Collectible> getCollectibles() {
        return collectibles;
    }

    public void setCollectibles(List<Collectible> collectibles) {
        this.collectibles = collectibles;
    }

    public void setMaxCollectibles(int maxCollectibles) {
        this.maxCollectibles = maxCollectibles;
    }

    public List<PointsItem> getModels() {
        return models;
    }

    public void setModels(List<PointsItem> models) {
        this.models = models;
    }

    public void setPiSpawnInterval(long piSpawnInterval) {
        this.piSpawnInterval = piSpawnInterval;
    }

    /**
     * Checks if the protagonist is within the environment's boundaries and adjusts
     * its position if necessary.
     */
    public void checkBorders() {
        if (this.protagonist.getPosX() < 0) {
            this.protagonist.setPosX(0);
        } else if ((this.protagonist.getPosX() + this.protagonist.getWidth()) > this.width) {
            this.protagonist.setPosX(this.width - protagonist.getWidth());
        } else if (this.protagonist.getPosY() < 0) {
            this.protagonist.setPosY(0);
        } else if ((this.protagonist.getPosY() + this.protagonist.getHeight()) > this.height) {
            this.protagonist.setPosY(this.height - this.protagonist.getHeight());
        }
    }

    /**
     * Detects collisions between the protagonist and other agents in the
     * environment.
     */
    public void detectCollision() {
        for (Agent agent : this.agents) {
            if (protagonist != null && intersect(this.protagonist, agent)) {
                System.out.println("Collision detected with agent: " + agent);
                /* The protagonist takes damage when colliding with an agent. */
                protagonist.takeDamage(10);
            }
        }

        collectibles.removeIf(collectible -> {
            if (intersectCollectible(protagonist, collectible)) {
                collectible.onCollect(protagonist);
                return true; // Remove the item when collected
            }
            return false;
        });
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

    private boolean intersect(Agent a, Agent b) {
        // Returns true if there is a collision between two agents
        return a.getPosX() < b.getPosX() + b.getWidth() &&
                a.getPosX() + a.getWidth() > b.getPosX() &&
                a.getPosY() < b.getPosY() + b.getHeight() &&
                a.getPosY() + a.getHeight() > b.getPosY();
    }

    private boolean intersectCollectible(Agent a, Collectible b) {
        // Returns true if there is a collision between two agents
        return a.getPosX() < b.getPosX() + b.getWidth() &&
                a.getPosX() + a.getWidth() > b.getPosX() &&
                a.getPosY() < b.getPosY() + b.getHeight() &&
                a.getPosY() + a.getHeight() > b.getPosY();
    }

    // Generate Points items
    public void generatePointsItem() {

        //Pick a random model in pointsItem list and use to add to Collectibles list
        PointsItem randomModel = models.get((int) (Math.random() * models.size()));

        //Set an random x and y position and add the random model to the collectibles list
        if (collectibles.size() < maxCollectibles && !models.isEmpty()) {
            int x = (int) (Math.random() * (this.width - randomModel.getWidth()));
            int y = (int) (Math.random() * (this.height - randomModel.getHeight()));

            collectibles.add(
                    new PointsItem(x, y, randomModel.getWidth(), randomModel.getHeight(), randomModel.getPathImage(),
                            randomModel.getPoints()));
        }

    }

    // Update collectibles based on his spawn interval
    public void updateCollectible() {
        long currentTime = System.currentTimeMillis();
        //Update Points Item 
        if (currentTime - lastCollectibleSpawnTime >= piSpawnInterval) {
            generatePointsItem();
            lastCollectibleSpawnTime = currentTime;
        }
    }

}