package chon.group.game.core.agent;

import java.util.List;

/**
 * Represents a generic game object that can be collectible and/or destructible.
 */
public class Object extends Entity {
    private boolean collected = false;
    private boolean destructible;
    private boolean collectible;

    private List<Object> objects;
    private int totalObjects = 0;
    private int collectedObjects = 0;
    private int score = 0;

    /**
     * Constructs a game object with specified position, size, image, and
     * properties.
     *
     * @param posX         The x-coordinate of the object.
     * @param posY         The y-coordinate of the object.
     * @param height       The height of the object.
     * @param width        The width of the object.
     * @param pathImage    The image path of the object.
     * @param collectible  Whether the object is collectible.
     * @param destructible Whether the object is destructible.
     */
    public Object(int posX,
            int posY,
            int height,
            int width,
            int speed,
            int health,
            String pathImage,
            boolean flipped,
            boolean visibleBars,
            boolean collectible, 
            boolean destructible) {
        super(posX, posY, height, width, speed, health, pathImage, flipped, visibleBars);
        this.collectible = collectible;
        this.destructible = destructible;
    }

    /** @return Whether the object has been collected. */
    public boolean isCollected() {
        return collected;
    }

    /**
     * Sets the collected status of the object.
     *
     * @param collected True if the object has been collected.
     */
    public void setCollected(boolean collected) {
        this.collected = collected;
    }

    /** @return Whether the object is collectible. */
    public boolean isCollectible() {
        return collectible;
    }

    /**
     * Sets whether the object is collectible.
     *
     * @param collectible True if the object is collectible.
     */
    public void setCollectible(boolean collectible) {
        this.collectible = collectible;
    }

    /** @return Whether the object is destructible. */
    public boolean isDestructible() {
        return destructible;
    }

    /**
     * Sets whether the object is destructible.
     *
     * @param destructible True if the object is destructible.
     */
    public void setDestructible(boolean destructible) {
        this.destructible = destructible;
    }

    /**
     * Defines the behavior when the object is collected.
     * Can be overridden in subclasses.
     */
    public void onCollect() {
        this.collected = true;
    }

    /**
     * Defines the behavior when the object is destroyed.
     * Can be overridden in subclasses.
     */
    public void onDestroy() {
        this.collected = true;
    }

    /**
     * Makes the object follow a target entity if it's within a given radius.
     *
     * @param target           The target entity to follow.
     * @param attractionRadius The radius within which the object starts following.
     * @param speed            The speed of movement towards the target.
     */
    public void follow(Entity target, double attractionRadius, double speed) {
        double dx = target.getPosX() - this.getPosX();
        double dy = target.getPosY() - this.getPosY();
        double distance = Math.sqrt(dx * dx + dy * dy);

        if (distance < attractionRadius) {
            double directionX = dx / distance;
            double directionY = dy / distance;

            this.setPosX((int) (this.getPosX() + directionX * speed));
            this.setPosY((int) (this.getPosY() + directionY * speed));
        }
    }

    /**
     * Registers a new object in the object list.
     *
     * @param obj The object to register.
     */
    public void registerObject(Object obj) {
        objects.add(obj);
        totalObjects++;
    }

    /**
     * Notifies that an object has been collected.
     *
     * @param obj The collected object.
     */
    public void notifyCollected(Object obj) {
        collectedObjects++;
        objects.remove(obj);
    }

    /** @return The total number of registered objects. */
    public int getObjectTotal() {
        return totalObjects;
    }

    /** @return The total number of collected objects. */
    public int getObjectCollected() {
        return collectedObjects;
    }

    /** @return The current score. */
    public int getScore() {
        return score;
    }

    /**
     * Adds a specified amount to the current score.
     *
     * @param amount The amount to add.
     */
    public void addScore(int amount) {
        score += amount;
    }
}
