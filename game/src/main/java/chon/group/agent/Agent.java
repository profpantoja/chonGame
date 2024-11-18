package chon.group.agent;

import java.util.ArrayList;

import javafx.scene.image.Image;

public class Agent {
    private int positionX;
    private int positionY;
    private int height;
    private int width;
    private Image image;
    private Boolean isProtagonist;
    public static int numberProtagonist = 0;

    public Agent(int positionX, int positionY, int width, int height, String pathImage, boolean isProtagonist ) {        
        this.positionX = positionX;
        this.positionY = positionY;
        this.width = width;
        this.height = height;
		this.image = new Image(getClass().getResource(pathImage).toExternalForm());
        this.isProtagonist = isProtagonist;
        if(isProtagonist){
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

    public void move(String input){
		if (input.contains("RIGHT")) {
			setPositionX(positionX += 1);	
		} else if (input.contains("LEFT")) {
			setPositionX(positionX -= 1);
		} else if (input.contains("UP")) {
			setPositionY(positionY -= 1);
		} else if (input.contains("DOWN")) {
			setPositionY(positionY += 1);
		}
	}

    public boolean isProtagonist() {
        throw new UnsupportedOperationException("Unimplemented method 'isProtagonist'");
    }
}