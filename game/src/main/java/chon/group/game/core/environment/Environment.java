package chon.group.game.core.environment;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import chon.group.game.Panel;
import chon.group.game.core.agent.Agent;
import chon.group.game.core.agent.Entity;
import chon.group.game.core.agent.Object;
import chon.group.game.core.weapon.Shot;
import chon.group.game.messaging.Message;
import javafx.scene.image.Image;

/**
 * Represents the game environment, including properties such as dimensions,
 * position, background image, agents, protagonist, objects, shots, messages,
 * and camera.
 * The environment is responsible for updating the state of all elements,
 * detecting collisions, and tracking game progress such as score and
 * collectible status.
 */
public class Environment extends Entity {

    /** The background image for the pause screen. */
    private Image pauseImage;

    /** The background image for the game over screen. */
    private Image gameOverImage;

    /** The protagonist of the environment. */
    private Agent protagonist;

    /** List of agents present in the environment. */
    private List<Agent> agents;

    /** List of collectible/destructible objects in the environment. */
    private List<Object> objects;

    /** List of messages currently being displayed. */
    private List<Message> messages;

    /** List of shots in the environment. */
    private List<Shot> shots;

    /** The camera used to follow the protagonist. */
    private Camera camera;

    private Panel panel;

    /** Number of objects collected so far. */
    private int collectedCount = 0;

    /** Total number of collectible objects in the environment. */
    private int totalCollectibleCount = 0;

    /** Current score of the player. */
    private int score = 0;

    /**
     * Constructor to initialize the environment with dimensions, position, and a
     * background image.
     *
     * @param posX        the initial X position of the environment
     * @param posY        the initial Y position of the environment
     * @param height      the height of the environment
     * @param width       the width of the environment
     * @param screenWidth the screen width used for camera calculations
     * @param pathImage   the path to the background image
     */
    public Environment(int posX, int posY, int height, int width, double screenWidth, String pathImage, Panel panel) {
        super(posX, posY, height, width, 0, 0, pathImage, false, false);
        this.agents = new ArrayList<>();
        this.objects = new ArrayList<>();
        this.messages = new ArrayList<>();
        this.shots = new ArrayList<>();
        this.camera = new Camera(screenWidth, width, 0.49, 0.51);
        this.panel = panel;
    }

    public Image getPauseImage() {
        return pauseImage;
    }

    /**
     * Sets the background image for the pause screen.
     *
     * @param pathImage the path to the image
     */
    public void setPauseImage(String pathImage) {
        this.pauseImage = new Image(getClass().getResource(pathImage).toExternalForm());
    }

    public Image getGameOverImage() {
        return gameOverImage;
    }

    /**
     * Sets the background image for the game over screen.
     *
     * @param pathImage the path to the image
     */
    public void setGameOverImage(String pathImage) {
        this.gameOverImage = new Image(getClass().getResource(pathImage).toExternalForm());
    }

    public Agent getProtagonist() {
        return protagonist;
    }

    /**
     * Sets the protagonist of the game and attaches the camera to it.
     *
     * @param protagonist the protagonist agent
     */
    public void setProtagonist(Agent protagonist) {
        this.protagonist = protagonist;
        if (camera != null)
            camera.setTarget(protagonist);
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

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public List<Shot> getShots() {
        return shots;
    }

    public void setShots(List<Shot> shots) {
        this.shots = shots;
    }

    public Camera getCamera() {
        return camera;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    public Panel getPanel() {
        return panel;
    }

    public void setPanel(Panel panel) {
        this.panel = panel;
    }

    /**
     * Gets the number of objects collected by the protagonist.
     *
     * @return the number of collected objects
     */
    public int getCollectedCount() {
        return collectedCount;
    }

    /**
     * Gets the total number of collectible objects in the environment.
     *
     * @return the total number of collectibles
     */
    public int getTotalCollectibleCount() {
        return totalCollectibleCount;
    }

    /**
     * Gets the current score of the player.
     *
     * @return the current score
     */
    public int getScore() {
        return score;
    }

    /**
     * Ensures the protagonist stays within the boundaries of the environment.
     */
    public void checkBorders() {
        if (protagonist.getPosX() < 0)
            protagonist.setPosX(0);
        if ((protagonist.getPosX() + protagonist.getWidth()) > width)
            protagonist.setPosX(width - protagonist.getWidth());
        if (protagonist.getPosY() < 0)
            protagonist.setPosY(0);
        if ((protagonist.getPosY() + protagonist.getHeight()) > height)
            protagonist.setPosY(height - protagonist.getHeight());
    }

    /**
     * Detects collisions between the protagonist and agents.
     * Applies damage if a collision is detected.
     */
    public void detectCollision() {
        for (Agent agent : agents) {
            if (protagonist != null && intersect(protagonist, agent)) {
                int damage = 100;
                protagonist.takeDamage(damage, messages);
            }
        }
    }

    /**
     * Checks if two entities intersect (collide).
     *
     * @param a the first entity
     * @param b the second entity
     * @return true if their areas overlap, false otherwise
     */
    private boolean intersect(Entity a, Entity b) {
        return a.getPosX() < b.getPosX() + b.getWidth() &&
                a.getPosX() + a.getWidth() > b.getPosX() &&
                a.getPosY() < b.getPosY() + b.getHeight() &&
                a.getPosY() + a.getHeight() > b.getPosY();
    }

    /**
     * Updates all collectible objects in the environment.
     * Makes them follow the protagonist and handles collection.
     */
    public void updateObjects() {
        Iterator<Object> iterator = objects.iterator();
        while (iterator.hasNext()) {
            Object object = iterator.next();
            if (!object.isCollected() && object.isCollectible()) {
                object.follow(protagonist, 200, 5);
                double dx = object.getPosX() - protagonist.getPosX();
                double dy = object.getPosY() - protagonist.getPosY();
                double distance = Math.sqrt(dx * dx + dy * dy);
                if (distance < 20) {
                    object.onCollect();
                    collectedCount++;
                    score += 10;
                }
            } else if (object.isCollected() && object.isCollectible()) {
                iterator.remove();
            }
        }
    }

    /**
     * Updates and removes expired messages from the environment.
     */
    public void updateMessages() {
        Iterator<Message> iterator = messages.iterator();
        while (iterator.hasNext()) {
            Message message = iterator.next();
            if (!message.update()) {
                iterator.remove();
            }
        }
    }

    /**
     * Updates the state of all shots in the environment.
     * Handles movement, boundary removal, and collision with agents or protagonist.
     */
    public void updateShots() {
        Iterator<Shot> itShot = shots.iterator();
        while (itShot.hasNext()) {
            Shot shot = itShot.next();
            if ((shot.getPosX() > getWidth()) || ((shot.getPosX() + shot.getWidth()) < 0)) {
                itShot.remove();
            } else {
                if (intersect(protagonist, shot)) {
                    protagonist.takeDamage(shot.getDamage(), messages);
                    itShot.remove();
                } else {
                    Iterator<Agent> itAgent = agents.iterator();
                    while (itAgent.hasNext()) {
                        Agent agent = itAgent.next();
                        if (intersect(agent, shot)) {
                            agent.takeDamage(shot.getDamage(), messages);
                            if (agent.isDead())
                                itAgent.remove();
                            itShot.remove();
                            break;
                        }
                    }
                }
                shot.move(new ArrayList<>(List.of(shot.getDirection())));
            }
        }
    }

    /**
     * Updates the camera based on the protagonist’s current position.
     */
    public void updateCamera() {
        if (camera != null)
            camera.update();
    }

    /**
     * Performs a full update cycle of the environment.
     * Includes updating objects, shots, messages, camera,
     * checking collisions, and recovering protagonist energy.
     */
    public void update() {
        updateObjects();
        updateShots();
        updateMessages();
        updateCamera();
        detectCollision();
        protagonist.recoverEnergy();
    }

    /**
     * Counts how many collectible objects are currently in the environment.
     */
    public void countTotalCollectibles() {
        totalCollectibleCount = (int) objects.stream()
                .filter(Object::isCollectible)
                .count();
    }
}
