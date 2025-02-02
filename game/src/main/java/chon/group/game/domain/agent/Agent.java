package chon.group.game.domain.agent;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.image.Image;

/**
 * Represents an agent in the game, with properties such as position, size, speed, and image.
 * The agent can move in specific directions, jump, and chase a target.
*/
public class Agent {

    private int posX;
    private int posY;
    private int height;
    private int width;
    private int speed;
    private Image image;
    private int minX = 0;
    private int maxX = 950;
    private int minY = 250;
    private int maxY = 780;
    private boolean isJumping = false;
    private int jumpHeight = 140;
    private int gravity = 7;
    
    public Agent(int posX, int posY, int height, int width, int speed, String pathImage) {
        this.posX = posX;
        this.posY = posY;
        this.height = height;
        this.width = width;
        this.speed = speed;
        this.image = new Image(getClass().getResource(pathImage).toExternalForm());
    }

    public int getPosX() { return posX; }
    public void setPosX(int posX) { this.posX = Math.max(minX, Math.min(posX, maxX - width)); }
    public int getPosY() { return posY; }
    public void setPosY(int posY) {
        if (isJumping) {
            this.posY = posY; // Permite ultrapassar minY enquanto pula
        } else {
            this.posY = Math.max(minY, Math.min(posY, maxY - height));
        }
    }
    public int getHeight() { return height; }
    public int getWidth() { return width; }
    public int getSpeed() { return speed; }
    public Image getImage() { return image; }

    public void move(List<String> movements) {
        int newX = posX;
        int newY = posY;

        if (movements.contains("RIGHT")) { newX += speed; }
        if (movements.contains("LEFT")) { newX -= speed; }
        if (movements.contains("UP")) { newY -= speed; }
        if (movements.contains("DOWN")) { newY += speed; }
        if (movements.contains("SPACE")) { jump(); }

        setPosX(newX);
        setPosY(newY);
    }

    public void jump() {
        if (!isJumping) {
            isJumping = true;
            new Thread(() -> {
                int initialY = posY;
                for (int i = 0; i < jumpHeight / gravity; i++) {
                    setPosY(posY - gravity);
                    try { Thread.sleep(20); } catch (InterruptedException e) { }
                }
                for (int i = 0; i < jumpHeight / gravity; i++) {
                    setPosY(posY + gravity);
                    try { Thread.sleep(20); } catch (InterruptedException e) { }
                }
                isJumping = false;
            }).start();
        }
    }

    public void chase(int targetX, int targetY) {
        int newX = posX;
        int newY = posY;

        if (targetX > posX) { newX += speed; }
        else if (targetX < posX) { newX -= speed; }
        if (targetY > posY) { newY += speed; }
        else if (targetY < posY) { newY -= speed; }

        setPosX(newX);
        setPosY(newY);
    }
}