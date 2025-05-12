package chon.group.game.domain.environment;

import chon.group.game.domain.collectibles.Coin;

import java.util.ArrayList;
import java.util.List;

import chon.group.game.domain.agent.Agent;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

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

    private List<Coin> coins = new ArrayList<>();

    /** The graphics context used to render the environment. */
    private GraphicsContext gc;

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

    public List<Coin> getCoins() { return coins; }
    public void setCoins(List<Coin> coins) { this.coins = coins; }


    /**
     * Gets the graphics context used to render the environment.
     *
     * @return the graphics context
     */
    public GraphicsContext getGc() {
        return gc;
    }

    /**
     * Sets the graphics context used to render the environment.
     *
     * @param gc the new graphics context
     */
    public void setGc(GraphicsContext gc) {
        this.gc = gc;
    }

    /**
     * Draws the environment's background on the graphics context.
     */
    public void drawBackground() {
        gc.drawImage(this.image, this.posX, this.posY, this.width, this.height);
    }

    public void drawPauseScreen() {
        if (pauseImage != null && gc != null) {
            double centerX = (this.width - pauseImage.getWidth()) / 2;
            double centerY = (this.height - pauseImage.getHeight()) / 2;

            // Draw image on the center of screen
            gc.drawImage(pauseImage, centerX, centerY);
        } else {
            System.out.println("Pause image not set or GraphicsContext is null.");
        }
    }

    /**
     * Renders all agents and the protagonist in the environment.
     */
    public void drawAgents() {
        for (Agent agent : this.agents) {
            gc.drawImage(agent.getImage(), agent.getPosX(), agent.getPosY(), agent.getWidth(), agent.getHeight());
        }
        gc.drawImage(this.protagonist.getImage(), this.protagonist.getPosX(), this.protagonist.getPosY(),
                this.protagonist.getWidth(), this.protagonist.getHeight());
        printStatusPanel(this.protagonist);
        drawLifeBar();
    }


    /**
     * Clears the environment area, removing previously drawn elements.
     */
    public void clearEnvironment() {
        gc.clearRect(0, 0, this.width, this.height);
    }

    /**
     * Displays a status panel showing the protagonist's coordinates.
     *
     * @param agent the protagonist whose information will be displayed
     */
    public void printStatusPanel(Agent agent) {
        Font theFont = Font.font("Verdana", FontWeight.BOLD, 14);
        gc.setFont(theFont);
        gc.setFill(Color.BLACK);
        gc.fillText("X: " + agent.getPosX(), agent.getPosX() + 10, agent.getPosY() - 40);
        gc.fillText("Y: " + agent.getPosY(), agent.getPosX() + 10, agent.getPosY() - 25);
    }

    /**
     * Draws a health bar above the protagonist to represent their current health
     * visually.
     * The health bar consists of:
     * <ul>
     * <li>A black border representing the health bar's background.</li>
     * <li>A green inner bar representing the actual health percentage of the
     * protagonist.</li>
     * </ul>
     * 
     * The size of the health bar is dynamically calculated based on the
     * protagonist's
     * current health (`getHealth()`), maximum health (`getFullHealth()`), and width
     * (`getWidth()`).
     * 
     * <p>
     * The health bar is drawn a fixed distance above the protagonist's position.
     * </p>
     *
     * <p>
     * <strong>Key Calculations:</strong>
     * </p>
     * <ul>
     * <li><strong>lifePercentage:</strong> Percentage of current health relative to
     * maximum health.</li>
     * <li><strong>lifeSpan:</strong> Width of the green inner bar based on health
     * percentage.</li>
     * </ul>
     */
    public void drawLifeBar() {
        /* The border's thickness. */
        int borderThickness = 2;
        /* The bar's height. */
        int barHeight = 5;
        /* The life span proportion calculated based on actual and maximum health. */ 
        int lifeSpan = Math.round((float) ((protagonist.getHealth() * 100 / protagonist.getFullHealth()) * protagonist.getWidth()) / 100);
        /* Int points before the agent's y position. The initial bar's position. */
        int barY = 15;
        /* The outside background of the health bar. */
        gc.setFill(Color.BLACK);
        /* The height is a little bit bigger to give a border experience. */
        gc.fillRect(protagonist.getPosX(),
                protagonist.getPosY() - barY,
                protagonist.getWidth(),
                barHeight + (borderThickness * 2));
        /** 
         * The inside of the health bar. It is the effective life of the agent.
         * The border height plus the thickness multiplied by two (beggining and end at
         * X).  
         */
        gc.setFill(Color.GREEN);
        /** 
         * The initial position considering the border from both X and Y points.
         * The life span less the border thickness multiplied by two (beggining and end
         * at Y).
         */ 
        gc.fillRect(protagonist.getPosX() + borderThickness,
                protagonist.getPosY() - (barY - borderThickness),
                (lifeSpan - (borderThickness * 2)),
                barHeight);
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

        public void drawCoins() {
    for (Coin coin : coins) {
        if (!coin.isCollected()) {
            gc.drawImage(coin.getImage(), coin.getPosX(), coin.getPosY(), 50, 50);
        }
    }
}


    /**
     * Detects collisions between the protagonist and other agents in the
     * environment.
     */


    public void detectCollision() {
        for (Agent agent : this.agents) {
            if (intersect(this.protagonist, agent)) {
                System.out.println("Collision detected with agent: " + agent);
                //if (!(this.protagonist.getHealth() <= 0))
                //    this.protagonist.setHealth(this.protagonist.getHealth() - 1);
            }
        }
    }
    public void detectCoinCollection() {
    for (Coin coin : coins) {
        if (!coin.isCollected()) {
            double dx = Math.abs(protagonist.getPosX() - coin.getPosX());
            double dy = Math.abs(protagonist.getPosY() - coin.getPosY());
            if (dx < 50 && dy < 50) {
                coin.setCollected(true);
                System.out.println("Moeda coletada!");
            }
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
    private boolean intersect(Agent a, Agent b) {
        return a.getPosX() < b.getPosX() + b.getWidth() &&
                a.getPosX() + a.getWidth() > b.getPosX() &&
                a.getPosY() < b.getPosY() + b.getHeight() &&
                a.getPosY() + a.getHeight() > b.getPosY();
    }

}