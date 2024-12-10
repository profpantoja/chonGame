package chon.group.game.domain.agent;

import java.util.List;

import chon.group.game.domain.movement.MovementImpl;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Agent implements MovementImpl {

    private int posX;
    private int posY;
    private int height;
    private int width;
    private int speed;
    private Image image;

    private String moveUp;
    private String moveDown;
    private String moveLeft;
    private String moveRight;
    private String finish;

    public Agent(int posX, int posY, int height, int width, int speed, String pathImage) {
        this.posX = posX;
        this.posY = posY;
        this.height = height;
        this.width = width;
        this.speed = speed;
        this.image = new Image(getClass().getResource(pathImage).toExternalForm());
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

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
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

    public void setImage(Image image) {
        this.image = image;
    }

    @Override
    public String getUp() {
        return moveUp;
    }

    @Override
    public String getDown() {
        return moveDown;
    }

    @Override
    public String getLeft() {
        return moveLeft;
    }

    @Override
    public String getRight() {
        return moveRight;
    }

    @Override
    public String getFinish() {
        return finish;
    }

    @Override
    public void setUp(String up) {
        this.moveUp = up;
    }

    @Override
    public void setDown(String down) {
        this.moveDown = down;
    }

    @Override
    public void setLeft(String left) {
        this.moveLeft = left;
    }

    @Override
    public void setRight(String right) {
        this.moveRight = right;
    }

    @Override
    public void setFinish(String finish) {
        this.finish = finish;
    }

    public void move(List<String> movements, Stage theStage) {
        if (movements.contains(getRight())) {
            setPosX(posX += speed);
        } else if (movements.contains(getLeft())) {
            setPosX(posX -= speed);
        } else if (movements.contains(getUp())) {
            setPosY(posY -= speed);
        } else if (movements.contains(getDown())) {
            setPosY(posY += speed);
        } else if (movements.contains(getFinish())) {
            theStage.close();
        }
    }

    /*
    /* Chasing the Player with Speed 
    public void chase(int targetX, int targetY) {
        if (targetX > this.posX) {
            this.move(new ArrayList<String>(List.of("RIGHT")));
        } else if (targetX < this.posX) {
            this.move(new ArrayList<String>(List.of("LEFT")));
        }
        if (targetY > this.posY) {
            this.move(new ArrayList<String>(List.of("DOWN")));
        } else if (targetY < this.posY) {
            this.move(new ArrayList<String>(List.of("UP")));
        }
    }
    */
}