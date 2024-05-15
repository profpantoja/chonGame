package chon.group.enviroment;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Environment {
    private int width;
    private int height;
    private int positionX;
    private int positionY;
    private String pathImage;
    private GraphicsContext gc;

    public Environment() {

    }

    public Environment(int positionX, int positionY, int width, int height, String pathImage, GraphicsContext gc) {
        this.positionX = positionX;
        this.positionY = positionY;
        this.height = height;
        this.width = width;
        this.pathImage = pathImage;
        this.gc = gc;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getPositionX() {
        return positionX;
    }

    public void setPositionX(int positionX) {
        this.positionX = positionX;
    }

    public int getPositionY() {
        return positionY;
    }

    public void setPositionY(int positionY) {
        this.positionY = positionY;
    }

    public String getPathImage() {
        return pathImage;
    }

    public void setPathImage(String pathImage) {
        this.pathImage = pathImage;
    }

    public GraphicsContext getGc() {
        return gc;
    }

    public void setGc(GraphicsContext gc) {
        this.gc = gc;
    }

    public Image generateBackground() {
        Image backgroundImage = new Image(getClass().getResource(pathImage).toExternalForm());
        return backgroundImage;
    }

    public void drawBackground() {
        gc.drawImage(generateBackground(), positionX, positionY, width, height);
    }
}
