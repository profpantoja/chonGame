package chon.group.game.domain.environment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Iterator;
import chon.group.game.domain.agent.Agent;
import chon.group.game.domain.item.FallingItem;
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

    /** The protagonist instance. */
    private Agent protagonist;

    /** List of agents present in the environment. */
    private List<Agent> agents = new ArrayList<Agent>();

    /** List of falling items in the environment. */
    private List<FallingItem> fallingItems = new ArrayList<>();

    /** Current game score. */
    private int score;

    /** Image for the score panel display. */
    private Image scoreImage;

    /** Image for the score panel display. */
    private static final Map<String, Image> IMAGE_CACHE = new HashMap<>();

    /**
     * Default constructor to create an empty environment.
     */
    public Environment() {
        this.fallingItems = new ArrayList<>();
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
     * Gets the list of falling items in the environment.
     *
     * @return the list of falling items
     */
    public List<FallingItem> getFallingItems() {
        return fallingItems;
    }

    /**
     * Sets the list of falling items in the environment.
     *
     * @param fallingItems the new list of falling items
     */
    public void setFallingItems(List<FallingItem> fallingItems) {
        this.fallingItems = fallingItems;
    }

    /**
     * Gets the current game score.
     *
     * @return the current score
     */
    public int getScore() {
        return score;
    }

    /**
     * Sets the game score.
     *
     * @param score the new score value
     */
    public void setScore(int score) {
        this.score = score;
    }

    /**
     * Gets the score panel image.
     *
     * @return the score panel image
     */
    public Image getScoreImage() {
        return scoreImage;
    }

    /**
     * Sets the score panel image, using image caching for better performance.
     *
     * @param pathImage the path to the score panel image
     */
    public void setScoreImage(String pathImage) {
        this.scoreImage = IMAGE_CACHE.computeIfAbsent(pathImage,
                path -> new Image(getClass().getResource(path).toExternalForm()));
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
     * Removes falling items that have reached the ground level.
     */
    public void cleanupItems() {
        int groundOffset = 100;
        fallingItems.removeIf(item -> item.getPosY() > (height - groundOffset));
    }

    /**
     * Checks for collisions between the protagonist and falling items.
     * Updates score and protagonist health based on item type.
     */
    public void detectFallingItemCollision() {
        // Usar um iterator para remover itens de forma mais eficiente
        Iterator<FallingItem> iterator = fallingItems.iterator();
        while (iterator.hasNext()) {
            FallingItem item = iterator.next();
            if (intersectWithItem(protagonist, item)) {
                if (item.isBomb()) {
                    protagonist.takeDamage(1000);
                } else {
                    score++;
                }
                iterator.remove();
                break; // Sai do loop após primeira colisão
            }
        }
        // Limpa itens fora da tela
        cleanupItems();
    }

    /**
     * Checks if an agent intersects with a falling item.
     *
     * @param agent The agent to check for collision
     * @param item  The falling item to check for collision
     * @return true if there is an intersection, false otherwise
     */
    private boolean intersectWithItem(Agent agent, FallingItem item) {
        return agent.getPosX() < item.getPosX() + item.getWidth() &&
                agent.getPosX() + agent.getWidth() > item.getPosX() &&
                agent.getPosY() < item.getPosY() + item.getHeight() &&
                agent.getPosY() + agent.getHeight() > item.getPosY();
    }

}