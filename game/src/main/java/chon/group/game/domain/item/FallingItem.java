package chon.group.game.domain.item;

import java.util.HashMap;
import java.util.Map;

import javafx.scene.image.Image;

/**
 * Represents a falling item in the game, with properties such as position,
 * size,
 * speed, and image.
 * The items can be either collectible hextech crystals or harmful bombs that
 * fall from above.
 */
public class FallingItem {
    /** X position (horizontal) of the item. */
    private double posX;

    /** Y (vertical) position of the item. */
    private double posY;

    /** Height of the item. */
    private int width;

    /** Width of the item. */
    private int height;

    /** Item falling speed. */
    private double speed;

    /** Path to the item's image resource. */
    private String imagePath;

    /** Indicates if the item is a bomb (harmful) or not. */
    private boolean isBomb;

    /** Cache for item images to optimize memory usage. */
    private static final Map<String, Image> IMAGE_CACHE = new HashMap<>();

    /**
     * Constructor to initialize the falling item properties.
     *
     * @param posX      the item's initial X (horizontal) position
     * @param width     the item's width
     * @param height    the item's height
     * @param speed     the item's falling speed
     * @param imagePath the path to the item's image
     * @param isBomb    whether the item is a bomb or not
     */
    public FallingItem(double posX, int width, int height, double speed, String imagePath, boolean isBomb) {
        this.posX = posX;
        this.posY = 170;
        this.width = width;
        this.height = height;
        this.speed = speed;
        this.imagePath = imagePath;
        this.isBomb = isBomb;
    }

    /**
     * Gets the X (horizontal) position of the item.
     *
     * @return the X position of the item
     */
    public double getPosX() {
        return posX;
    }

    /**
     * Sets the item's X (horizontal) position.
     *
     * @param posX the new X position
     */
    public void setPosX(double posX) {
        this.posX = posX;
    }

    /**
     * Gets the Y (vertical) position of the item.
     *
     * @return the Y position of the item
     */
    public double getPosY() {
        return posY;
    }

    /**
     * Sets the Y (vertical) position of the item.
     *
     * @param posY the new Y position
     */
    public void setPosY(double posY) {
        this.posY = posY;
    }

    /**
     * Gets the width of the item.
     *
     * @return the width of the item
     */
    public int getWidth() {
        return width;
    }

    /**
     * Gets the height of the item.
     *
     * @return the height of the item
     */
    public int getHeight() {
        return height;
    }

    /**
     * Gets the path to the item's image.
     *
     * @return the image path
     */
    public String getImagePath() {
        return imagePath;
    }

    /**
     * Checks if the item is a bomb.
     *
     * @return true if the item is a bomb, false otherwise
     */
    public boolean isBomb() {
        return isBomb;
    }

    /**
     * Gets the image cache map.
     *
     * @return the map containing cached images
     */
    public static Map<String, Image> getImageCache() {
        return IMAGE_CACHE;
    }

    /**
     * Gets the cached image for this item.
     *
     * @return the cached Image instance
     */
    public Image getCachedImage() {
        return getItemImage();
    }

    /**
     * Retrieves or creates a cached image for this item.
     * Uses computeIfAbsent to optimize image loading.
     *
     * @return the Image instance from cache or newly created
     */
    private Image getItemImage() {
        return IMAGE_CACHE.computeIfAbsent(imagePath, path -> new Image(getClass().getResource(path).toExternalForm()));
    }

    /**
     * Updates the item's position by moving it downward based on its speed.
     */
    public void fall() {
        posY += speed;
    }
}