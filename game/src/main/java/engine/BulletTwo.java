package engine;

import java.util.ArrayList;

import application.Character;
import javafx.scene.image.Image;

public class BulletTwo extends Character {

	public BulletTwo(int positionX, int positionY, int velocityX, int velocityY, int width, int height) {
		super(positionX, positionY, velocityX, velocityY, width, height);
		this.setImages(new ArrayList<Image>());
		this.getImages().add(new Image("file:images/bulletTwo/bulletTwo001.png"));
		this.getImages().add(new Image("file:images/bulletTwo/bulletTwo002.png"));
		this.getImages().add(new Image("file:images/bulletTwo/bulletTwo003.png"));
		this.getImages().add(new Image("file:images/bulletTwo/bulletTwo004.png"));
		this.getImages().add(new Image("file:images/bulletTwo/bulletTwo005.png"));
		this.getImages().add(new Image("file:images/bulletTwo/bulletTwo006.png"));
		this.getImages().add(new Image("file:images/bulletTwo/bulletTwo007.png"));
		this.getImages().add(new Image("file:images/bulletTwo/bulletTwo008.png"));
	}

}

