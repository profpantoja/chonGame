package chon.group.agent;

import java.util.ArrayList;
import java.util.Arrays;

import javafx.scene.image.Image;

public class HeroMovement extends Agent{

    private Image HeroImage;

    private ArrayList<String> stoppedOrWalkingBackImages = new ArrayList<>(Arrays.asList(
        "/images/agent/Spaceship/spaceship_parada ou dando ré1.gif"
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

    public HeroMovement(int positionX, int positionY, int width, int height, String pathImage, boolean isProtagonist,
            int life) {
        super(positionX, positionY, width, height, pathImage, isProtagonist, life);
    }

    public Image getHeroImage() {
        return HeroImage;
    }

    public void setHeroImage(Image heroImage) {
        HeroImage = heroImage;
    }

    @Override
    public void move(String input) {
        if (input.contains("RIGHT")) {
            currentFrameDOWN = 0;
            currentFrameUP = 0;
            setPositionX(getPositionX() + 7);
            startWalkingRightAnimation();
        } else if (input.contains("LEFT")) {
            currentFrameDOWN = 0;
            currentFrameUP = 0;
            setPositionX(getPositionX() - 7);
            startStoppedOrGoingBack();
        } else if (input.contains("UP")) {
            currentFrameDOWN = 0;
            setPositionY(getPositionY() - 7);
            startGoingUpAnimation();
        } else if (input.contains("DOWN")) {
            currentFrameUP = 0;
            setPositionY(getPositionY() + 7);
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
