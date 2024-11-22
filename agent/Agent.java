package chon.group.agent;

import java.util.ArrayList;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.util.Duration;

public class Agent {
    private int positionX;
    private int positionY;
    private int height;
    private int width;
    private Image image;
    private Boolean isProtagonist;
    public static int numberProtagonist = 0;
    private final String[] walkingForwardImages = {
        "/images/agent/Spaceship/spaceship_andando2.png",
        "/images/agent/Spaceship/spaceship_andando3.png",
        "/images/agent/Spaceship/spaceship_andando4.png",
        "/images/agent/Spaceship/spaceship_andando5.png",
        "/images/agent/Spaceship/spaceship_andando6.png"
    };
    private int currentFrame = 0; // Índice da imagem atual
    private Timeline walkingAnimation;

    public Agent(int positionX, int positionY, int width, int height, String pathImage, boolean isProtagonist) {        
        this.positionX = positionX;
        this.positionY = positionY;
        this.width = width;
        this.height = height;
        this.image = new Image(getClass().getResource(pathImage).toExternalForm());
        this.isProtagonist = isProtagonist;
        if (isProtagonist) {
            numberProtagonist++;
        }
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

    public Boolean getIsProtagonist() {
        return isProtagonist;
    }

    public void setIsProtagonist(Boolean isProtagonist) {
        this.isProtagonist = isProtagonist;
    }

    public Image getImage() {
        return image;
    }    

    public void setImage(String pathImage) {
        this.image = new Image(getClass().getResource(pathImage).toExternalForm());
    }

    public void move(ArrayList<String> input) {
        if (input.contains("RIGHT")) {
            setPositionX(positionX += 1);
            startWalkingAnimation();
        } else if (input.contains("LEFT")) {
            setPositionX(positionX -= 1);
            startWalkingAnimation();
        } else if (input.contains("UP")) {
            setPositionY(positionY -= 1);
            startWalkingAnimation();
        } else if (input.contains("DOWN")) {
            setPositionY(positionY += 1);
            startWalkingAnimation();
        } else {
            stopWalkingAnimation();
        }
    }

    public void startWalkingAnimation() {
        if (walkingAnimation != null && walkingAnimation.getStatus() == Timeline.Status.RUNNING) {
            return;
        }

        walkingAnimation = new Timeline(new KeyFrame(
            Duration.millis(100),
            e -> {
                setImage(walkingForwardImages[currentFrame]);
                currentFrame = (currentFrame + 1) % walkingForwardImages.length;
            }
        ));
        walkingAnimation.setCycleCount(Timeline.INDEFINITE);
        walkingAnimation.play();
    }

    public void stopWalkingAnimation() {
        if (walkingAnimation != null) {
            walkingAnimation.stop();
            currentFrame = 0;
        }
    }
}
