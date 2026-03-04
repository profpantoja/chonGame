package chon.group.game.core.agent;

/**
 * Represents a generic game object that can be collectible and/or destructible.
 */
public class Object extends Entity {

    private boolean collected;
    private boolean destructible;
    private boolean collectible;
    private double attractionRadius = 0.0;

    /**
     * Constructs a game object with specified position, size, image, and
     * properties.
     *
     * @param posX             The x-coordinate of the object.
     * @param posY             The y-coordinate of the object.
     * @param height           The height of the object.
     * @param width            The width of the object.
     * @param pathImage        The image path of the object.
     * @param collectible      Whether the object is collectible.
     * @param destructible     Whether the object is destructible.
     * @param attractionRadius The attraction radius in pixels.
     */
    public Object(int posX,
            int posY,
            int height,
            int width,
            int speed,
            int health,
            Direction direction,
            String pathImage,
            boolean flipped,
            boolean visibleBars,
            boolean collectible,
            boolean destructible,
            double attractionRadius) {
        super(posX, posY, height, width, speed, health, direction, pathImage, flipped, visibleBars);
        this.collectible = collectible;
        this.destructible = destructible;
        this.attractionRadius = attractionRadius;
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

    public double getAttractionRadius() {
        return attractionRadius;
    }

    public void setAttractionRadius(double attractionRadius) {
        this.attractionRadius = attractionRadius;
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
     */
    public void follow(Entity target) {
        double dx = target.getPosX() - this.getPosX();
        double dy = target.getPosY() - this.getPosY();
        double distance = Math.sqrt(dx * dx + dy * dy);
        /**
         * If the radius is inside the attraction value, then the object moves toward
         * the agent.
         */
        if (distance < this.attractionRadius) {
            double directionX = dx / distance;
            double directionY = dy / distance;
            /* It avoids the agent to runaway from the object. */
            if (this.speed <= target.speed)
                this.speed = target.speed + 2;
            this.setPosX((int) (this.getPosX() + directionX * this.speed));
            this.setPosY((int) (this.getPosY() + directionY * this.speed));
        }
    }

    /**
     * Gets if the object is destroyed.
     *
     * @return if the object is destroyed
     */
    public boolean isDestroyed() {
        return (this.isTerminated());
    }

}
