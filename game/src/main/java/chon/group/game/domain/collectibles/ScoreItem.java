package chon.group.game.domain.collectibles;

import javafx.scene.image.Image;
import chon.group.game.domain.agent.Agent;

/**
 * This class represents a basic item that when player collide, increase points
 */

public class ScoreItem extends Collectible {
    private String pathImage; // store just the path of the image
    private int points;

    public ScoreItem(int posX, int posY, int width, int height, String pathImage, int points) {
        super(posX, posY, width, height, pathImage);
        this.pathImage = pathImage;
        this.points = points;
    }

    @Override
    public void onCollect(Agent protagonist) {
        System.out.println("Item collected! + " + points + "!");
        protagonist.addScore(points);
    }

    @Override
    public Image getImage() {
        return new Image(getClass().getResource(pathImage).toExternalForm());
    }

    public String getPathImage() {
        return pathImage;
    }

    public int getPoints() {
        return points;
    }

}
