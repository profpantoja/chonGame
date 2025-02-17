package chon.group.agent;


import javafx.scene.image.Image;

public class Shot{
    private int positionX;
    private int positionY;
    private int speed;
    private Image image;
    private boolean alive;

    public Shot(int positionX, int positionY, int speed, String pathImage) {
        this.positionX = positionX;
        this.positionY = positionY;
        this.speed = speed;
        this.image = new Image(getClass().getResource("/images/bullet/blast1.gif").toExternalForm());
        this.alive = true;
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

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(String pathImage) {
        this.image = new Image(getClass().getResource("/images/bullet/blast1.gif").toExternalForm());
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public void move() {
        this.positionX += speed; 
    }

}