package chon.group.game.domain.collectibles;

import javafx.scene.image.Image;
import chon.group.game.domain.agent.Agent;

/** This class sets a standard for other collectible items in the game */

public abstract class Collectible {
    protected int posX;
    protected int posY;
    protected int width;
    protected int height;
    protected Image image;
    protected long spawnInterval;

    public Collectible(int posX, int posY, int width, int height, String pathImage, long spawnInterval) {
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;
        this.image = new Image(getClass().getResource(pathImage).toExternalForm());
        this.spawnInterval = spawnInterval;
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Image getImage() {
        return image;
    }

    public long getSpawnInterval() {
        return spawnInterval;
    }

    // A method to do something when protagonist collect a collectible
    public abstract void onCollect(Agent protagonist);

}
