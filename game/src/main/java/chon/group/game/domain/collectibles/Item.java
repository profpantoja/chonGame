package chon.group.game.domain.collectibles;

import javafx.scene.image.Image;

/** This class represents a basic item that when player collide, increase points */

public class Item extends Collectible{
    private String pathImage;

    public Item(int posX, int posY, int width, int height, String pathImage) {
        super(posX, posY, width, height, pathImage);
        this.pathImage = pathImage;
    }

    @Override
    public void onCollect() {
        System.out.println("Item coletado!");
        // No futuro, podemos adicionar pontos aqui.
    }
        @Override
    public Image getImage() {
        return new Image(getClass().getResource(pathImage).toExternalForm());
    }

    public String getPathImage() {
        return pathImage;
    }
}
