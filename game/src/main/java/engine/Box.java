package engine;

import java.util.ArrayList;
import application.Character;
import javafx.scene.image.Image;

public class Box extends Character {

	public Box(int positionX, int positionY, int velocityX, int velocityY, int width, int height) {
		super(positionX, positionY, velocityX, velocityY, width, height);
		this.setImages(new ArrayList<Image>());
		this.getImages().add(new Image("file:images/box.png"));
	}
}
