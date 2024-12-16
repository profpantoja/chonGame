package chon.group.agent;
import java.util.ArrayList;
import java.util.Arrays;

import javafx.scene.image.Image;

public class Agent {
    private int positionX;
    private int positionY;
    private int height;
    private int width;
    private Image image;
    private Boolean isProtagonist;
    public static int numberProtagonist = 0;

    private ArrayList<String> stoppedOrWalkingBackImages = new ArrayList<>(Arrays.asList(
        "/images/agent/Spaceship/spaceship_parada ou dando ré1.png",
        "/images/agent/Spaceship/spaceship_parada ou dando ré2.png",
        "/images/agent/Spaceship/spaceship_parada ou dando ré3.png",
        "/images/agent/Spaceship/spaceship_parada ou dando ré4.png"
    ));

    private ArrayList<String> walkingForwardImages = new ArrayList<>(Arrays.asList(
        "/images/agent/Spaceship/spaceship_andando1.png",
        "/images/agent/Spaceship/spaceship_andando2.png",
        "/images/agent/Spaceship/spaceship_andando3.png",
        "/images/agent/Spaceship/spaceship_andando4.png",
        "/images/agent/Spaceship/spaceship_andando5.png",
        "/images/agent/Spaceship/spaceship_andando6.png"
    ));
    
    private ArrayList<String> goingUpImages = new ArrayList<>(Arrays.asList(
        "/images/agent/Spaceship/spaceship_subindo_1_frame.gif",
        "/images/agent/Spaceship/spaceship_subindo_2_frame.gif",
        "/images/agent/Spaceship/spaceship_subindo_3_frame.gif"
    ));

    private ArrayList<String> goingDownImages = new ArrayList<>(Arrays.asList(
        "/images/agent/Spaceship/spaceship_descendo_1_frame.gif",
        "/images/agent/Spaceship/spaceship_descendo_2_frame.gif",
        "/images/agent/Spaceship/spaceship_descendo_3_frame.gif"
    ));

    private int currentFrameUP = 0;
    private int currentFrameDOWN = 0;
    private int currentFrameRIGHT = 0;
    private int currentFrameLEFT = 0;

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
            currentFrameDOWN = 0;
            currentFrameUP = 0;
            setPositionX(positionX += 1);
            startWalkingRightAnimation();
        } else if (input.contains("LEFT")) {
            currentFrameDOWN = 0;
            currentFrameUP = 0;
            setPositionX(positionX -= 1);
            startStoppedOrGoingBack();
        } else if (input.contains("UP")) {
            currentFrameDOWN = 0;
            setPositionY(positionY -= 1);
            startGoingUpAnimation();
        } else if (input.contains("DOWN")) {
            currentFrameUP = 0;
            setPositionY(positionY += 1);
            startGoingDownAnimation();
        } else if(input.isEmpty()) {
            startStoppedOrGoingBack();
        }
    }

    public void startWalkingRightAnimation() {

        setImage(walkingForwardImages.get(currentFrameRIGHT));
        currentFrameRIGHT++;
        if(currentFrameRIGHT >= walkingForwardImages.size()) {
            currentFrameRIGHT = 0;
        }
        
    }

    public void startStoppedOrGoingBack() {
        
        setImage(stoppedOrWalkingBackImages.get(currentFrameLEFT));
        currentFrameLEFT++;
        if(currentFrameLEFT >= stoppedOrWalkingBackImages.size()) {
            currentFrameLEFT = 0;
        }

    }

    public void startGoingUpAnimation() {
        if (currentFrameUP < goingUpImages.size()) {
            setImage(goingUpImages.get(currentFrameUP));
            currentFrameUP++;
        } else {
            setImage(goingUpImages.get(goingUpImages.size() - 1));
        }
    }

    public void startGoingDownAnimation() {
        if (currentFrameDOWN < goingDownImages.size()) {
            setImage(goingDownImages.get(currentFrameDOWN));
            currentFrameDOWN++;
        } else {
            setImage(goingDownImages.get(goingDownImages.size() - 1));
        }
    }

}
