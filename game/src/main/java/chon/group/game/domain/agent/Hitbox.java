package chon.group.game.domain.agent;

import javafx.scene.image.Image;

public class Hitbox {
    private int posX;
    private int posY;
    private int offsetX;
    private int offsetY;
    private int width;
    private int height;
    private Image image;
    private boolean drawHitbox = false;

    public Hitbox(int offsetX, int offsetY, int width, int height, String pathImage) {
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.width = width;
        this.height = height;
        this.image = new Image(getClass().getResource(pathImage).toExternalForm());
    }

    public Hitbox(int offsetX, int offsetY, int width, int height) {
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.width = width;
        this.height = height;
        this.image = new Image(getClass().getResource("/images/agents/Hitbox.png").toExternalForm());
    }

    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
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

    public Image getImage() {
        return image;
    }

    public void setImage(String pathImage) {
        this.image = new Image(getClass().getResource(pathImage).toExternalForm());
    }

    public boolean isDrawHitbox() {
        return drawHitbox;
    }

    public void setDrawHitbox(boolean drawHitbox) {
        this.drawHitbox = drawHitbox;
    }

    public int getOffsetX() {
        return offsetX;
    }

    public void setOffsetX(int offsetX) {
        this.offsetX = offsetX;
    }

    public int getOffsetY() {
        return offsetY;
    }

    public void setOffsetY(int offsetY) {
        this.offsetY = offsetY;
    }
}
