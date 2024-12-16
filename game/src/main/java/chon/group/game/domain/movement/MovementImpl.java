package chon.group.game.domain.movement;

/**
 * Defines movement-related operations for an entity.
 * <p>
 * The {@code MovementImpl} interface provides methods to get and set key mappings for movement
 * directions (up, down, left, right) and a finish action. Implementing classes are expected to
 * define how these movements interact with the entity's behavior and environment.
 * </p>
 */
public interface MovementImpl {
    /**
     * Gets the key associated with moving up.
     *
     * @return the key for moving up
     */
    public String getUp();

    /**
     * Gets the key associated with moving down.
     *
     * @return the key for moving down
     */
    public String getDown();

    /**
     * Gets the key associated with moving left.
     *
     * @return the key for moving left
     */
    public String getLeft();

    /**
     * Gets the key associated with moving right.
     *
     * @return the key for moving right
     */
    public String getRight();

    /**
     * Sets the key for moving up.
     *
     * @param up the new key for moving up
     */
    public void setUp(String up);

    /**
     * Sets the key for moving down.
     *
     * @param down the new key for moving down
     */
    public void setDown(String down);

    /**
     * Sets the key for moving left.
     *
     * @param left the new key for moving left
     */
    public void setLeft(String left);

    /**
     * Sets the key for moving right.
     *
     * @param right the new key for moving right
     */
    public void setRight(String right);

    /**
     * Gets the key associated with the finish action.
     * 
     * The finish action is typically used to end or terminate a process,
     * such as closing a game or stopping an operation.
     *
     * @return the key for the finish action
     */
    public String getFinish();

    /**
     * Sets the key for the finish action.
     *
     * The finish action can be customized by providing a new key.
     *
     * @param finish the new key for the finish action
     */
    public void setFinish(String finish);
}
