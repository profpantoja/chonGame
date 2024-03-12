package engine;

import java.util.ArrayList;

import application.Character;
import javafx.scene.image.Image;

public class BulletTwo extends Character {

	public BulletTwo(int positionX, int positionY, int velocityX, int velocityY, int width, int height) {
		super(positionX, positionY, velocityX, velocityY, width, height);
		this.setImages(new ArrayList<Image>());
		this.getImages().add(new Image(getClass().getResource("/images/bulletTwo/bulletTwo001.png").toExternalForm()));
		this.getImages().add(new Image(getClass().getResource("/images/bulletTwo/bulletTwo002.png").toExternalForm()));
		this.getImages().add(new Image(getClass().getResource("/images/bulletTwo/bulletTwo003.png").toExternalForm()));
		this.getImages().add(new Image(getClass().getResource("/images/bulletTwo/bulletTwo004.png").toExternalForm()));
		this.getImages().add(new Image(getClass().getResource("/images/bulletTwo/bulletTwo005.png").toExternalForm()));
		this.getImages().add(new Image(getClass().getResource("/images/bulletTwo/bulletTwo006.png").toExternalForm()));
		this.getImages().add(new Image(getClass().getResource("/images/bulletTwo/bulletTwo007.png").toExternalForm()));
		this.getImages().add(new Image(getClass().getResource("/images/bulletTwo/bulletTwo008.png").toExternalForm()));
	}

}

