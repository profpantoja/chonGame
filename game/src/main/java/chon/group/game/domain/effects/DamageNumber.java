package chon.group.game.domain.effects;

/**
 * Represents a floating damage number that appears when an agent takes damage.
 * The number moves in a curved trajectory and fades out over time.
 */
public class DamageNumber {
    
    /** The damage amount to display */
    private int damage;
    
    /** Current X coordinate of the number */
    private double x;
    
    /** Current Y coordinate of the number */
    private double y;
    
    /** Time when this damage number was created */
    private long creationTime;
    
    /** Current opacity value between 0.0 (transparent) and 1.0 (opaque) */
    private double opacity;
    
    /** Duration in milliseconds that the number stays visible */
    private static final long LIFETIME = 1600;
    
    /** Controls the upward movement speed. Increasing makes the number rise faster */
    private static final double VERTICAL_SPEED = 0.5;
    
    /** Controls the rightward movement speed. Increasing makes the number move right faster */ 
    private static final double HORIZONTAL_SPEED = 0.3;
    
    /** Initial X position where the number appeared */
    private double initialX;
    
    /** Initial Y position where the number appeared */
    private double initialY;
    
    /** Time parameter for controlling the curved movement (increases over time) */
    private double time = 0;

    /**
     * Creates a new damage number effect.
     *
     * @param damage The amount of damage to display
     * @param x The initial x coordinate where the number will appear
     * @param y The initial y coordinate where the number will appear
     */
    public DamageNumber(int damage, double x, double y) {
        this.damage = damage;
        this.initialX = x;
        this.initialY = y;
        this.x = x;
        this.y = y;
        this.creationTime = System.currentTimeMillis();
        this.opacity = 1.0;
    }

    /**
     * Updates the damage number's position and opacity.
     * The number moves in a curved trajectory upward and to the right while fading out.
     *
     * @return false if the number's lifetime has expired, true otherwise
     */
    public boolean update() {
        long currentTime = System.currentTimeMillis();
        long age = currentTime - creationTime;
        
        if (age >= LIFETIME) {
            return false;
        }

        time += 0.05;

        x = initialX + (HORIZONTAL_SPEED * time * 20);
        y = initialY - (VERTICAL_SPEED * time * 25);
        
        opacity = Math.max(0, 1.0 - ((double) age / LIFETIME));
        
        return true;
    }

    /**
     * Gets the damage value being displayed.
     * @return The damage amount
     */
    public int getDamage() { return damage; }

    /**
     * Gets the current X coordinate of the damage number.
     * @return The X coordinate
     */
    public double getX() { return x; }

    /**
     * Gets the current Y coordinate of the damage number.
     * @return The Y coordinate
     */
    public double getY() { return y; }

    /**
     * Gets the current opacity value of the damage number.
     * @return The opacity value between 0.0 (transparent) and 1.0 (opaque)
     */
    public double getOpacity() { return opacity; }
}