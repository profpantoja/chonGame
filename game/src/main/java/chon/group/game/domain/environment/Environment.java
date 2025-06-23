package chon.group.game.domain.environment;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import chon.group.game.core.Entity;
import chon.group.game.domain.agent.Agent;
import chon.group.game.domain.agent.Collision;
import chon.group.game.domain.agent.Shot;
import chon.group.game.messaging.Message;
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

    /** The background image of the game over. */
    private Image gameOverImage;

    /** The background image of the win screen. */
    private Image winImage;

    /** The protagonist instance. */
    private Agent protagonist;

    /** List of agents present in the environment. */
    private List<Agent> agents;

    /** List of collisions present in the environment. */
    private List<Collision> collisions;

    /** List of messages to display. */
    private List<Message> messages;

    /** List of shots present in the environment. */
    private List<Shot> shots;
     
    /** The X position of the camera in the environment. */
    private double cameraX = 0;
    

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
        this.collisions = new ArrayList<Collision>();
        this.messages = new ArrayList<Message>();
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
        this.posX = posX;
        this.posY = posY;
        this.height = height;
        this.width = width;
        this.setImage(pathImage);
        this.agents = agents;
        this.messages = new ArrayList<Message>();
        this.shots = new ArrayList<Shot>();
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
     * Gets the background image for the game over.
     *
     * @return the game over image
     */
    public Image getWinImage() {
        return winImage;
    }

    /**
     * Sets the background image for the game over.
     *
     * @param pathImage the path to the new game over image
     */
    public void setWinImage(String pathImage) {
        this.winImage = new Image(getClass().getResource(pathImage).toExternalForm());
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
     * Gets the list of collisions present in the environment.
     *
     * @return the list of collisions
     */
    public List<Collision> getCollisions() {
        return collisions;
    }

    /**
     * Sets the list of collisions present in the environment.
     *
     * @param collisions the new list of collisions
     */
    public void setCollisions(ArrayList<Collision> collisions) {
        this.collisions = collisions;

    }

    /**
     * Gets the list of active messages.
     * 
     * @return List of messages objects currently being displayed
     */
    public List<Message> getMessages() {
        return messages;
    }

    /**
     * Sets the list of messages.
     * 
     * @param messages The new list of messages to display
     */
    public void setMessages(List<Message> messages) {
        this.messages = messages;
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
     * Gets the X position of the camera in the environment.
     *
     * @return the X position of the camera
     */
    public double getCameraX() {
        return cameraX;
    }

    /**
     * Sets the X position of the camera in the environment.
     *
     * @param cameraX the new X position of the camera
     */
    public void setCameraX(double cameraX) {
        this.cameraX = cameraX;
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
                protagonist.takeDamage(damage, this.messages);
            }
        }
    }

    public void loadNextRoom(String image, Agent newAgent) {
        System.out.println("Carregando próxima sala...");
        this.setImage(image);
        this.agents.clear();
        this.shots.clear();
        this.agents.add(newAgent);
        this.protagonist.setPosX(100);
        this.protagonist.setPosY(390);
        this.setCameraX(0);
    }


    public void roomChanger(String image, Agent newAgent) {
        if (!protagonist.isDead() && protagonist.getPosX() >= (0.9*this.width)) {
            System.out.println("Protagonist reached the end of the room. Checking for enemies...");
            boolean allEnemiesDead = true;

            for (Agent agent : this.agents) {
                if (agent != protagonist && !agent.isDead()) {
                    allEnemiesDead = false;
                    break;
                }
            }

            if (allEnemiesDead){
                System.out.println("All enemies are dead. Proceeding to the next room.");
                loadNextRoom(image, newAgent);
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

    public void updateMessages() {
        Iterator<Message> iterator = this.messages.iterator();
        while (iterator.hasNext()) {
            Message message = iterator.next();
            if (!message.update()) {
                iterator.remove();
            }
        }
    }

   public void updateShots() {
        Iterator<Shot> itShot = this.shots.iterator();
        while (itShot.hasNext()) {
            Shot shot = itShot.next();
            if ((shot.getPosX() > this.width) || ((shot.getPosX() + shot.getWidth()) < 0)) {
                itShot.remove();
            } else {
                boolean shotRemoved = false;
                if (this.intersect(protagonist, shot)) {
                    protagonist.takeDamage(shot.getDamage(), this.messages);
                    itShot.remove();
                    shotRemoved = true;
                } else {
                    Iterator<Agent> itAgent = this.agents.iterator();
                    while (itAgent.hasNext()) {
                        Agent agent = itAgent.next();
                        if (this.intersect(agent, shot)) {
                            agent.takeDamage(shot.getDamage(), this.messages);
                            if (agent.isDead())
                                itAgent.remove();
                            itShot.remove();
                            shotRemoved = true;
                            break; 
                        }
                    }
                }
                if (!shotRemoved) {
                    shot.move(new ArrayList<>(List.of(shot.getDirection())));
                }
            }
        }
    }

    public void createGround(int height, String image) {
        Collision ground = new Collision(0, this.height - height, this.width, 64, image, false, false, 0, false, false, false);
        getCollisions().add(ground);
    }

}