package chon.group.game.messaging;

/**
 * Represents a floating message that appears in the screen.
 * The message moves in a curved trajectory and fades out over time.
 */
public class Message {

    private int size;

    /** The message to display */
    private String message;

    /** Current X coordinate of the message */
    private double posX;

    /** Current Y coordinate of the message */
    private double posY;

    /** Time when this message was created */
    private long creationTime;

    /** Current opacity value between 0.0 (transparent) and 1.0 (opaque) */
    private double opacity;

    /** Duration in milliseconds that the message stays visible */
    private static final long LIFETIME = 1000;

    /**
     * Controls the upward movement speed. Increasing makes the message rise or
     * decline faster
     */
    private static final double VERTICAL_SPEED = 0.5;

    /**
     * Controls the rightward movement speed. Increasing makes the message move
     * right or left
     * faster
     */
    private static final double HORIZONTAL_SPEED = 0.0;

    /**
     * Creates a new message number effect.
     *
     * @param message The amount of message to display
     * @param posX    The initial x coordinate where the number will appear
     * @param posY    The initial y coordinate where the number will appear
     */
    public Message(String message, double posX, double posY, int size) {
        this.message = message;
        this.posX = posX;
        this.posY = posY;
        this.size = size;
        this.creationTime = System.currentTimeMillis();
        this.opacity = 1.0;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    /**
     * Gets the message value being displayed.
     * 
     * @return The message amount
     */
    public String getMessage() {
        return message;
    }

    /**
     * Gets the current X coordinate of the message.
     * 
     * @return The X coordinate
     */
    public double getPosX() {
        return posX;
    }

    /**
     * Gets the current Y coordinate of the message.
     * 
     * @return The Y coordinate
     */
    public double getPosY() {
        return posY;
    }

    /**
     * Gets the current opacity value of the message.
     * 
     * @return The opacity value between 0.0 (transparent) and 1.0 (opaque)
     */
    public double getOpacity() {
        return opacity;
    }

    /**
     * Updates the message's position and opacity.
     * The message moves in a curved trajectory upward/downward and to the left/right while fading
     * out.
     *
     * @return false if the message's lifetime has expired, true otherwise
     */
    public boolean update() {
        long currentTime = System.currentTimeMillis();
        long age = currentTime - creationTime;

        if (age >= LIFETIME) {
            return false;
        }

        this.posX += HORIZONTAL_SPEED;
        this.posY -= VERTICAL_SPEED;

        opacity = Math.max(0, 1.0 - ((double) age / LIFETIME));

        return true;
    }

}