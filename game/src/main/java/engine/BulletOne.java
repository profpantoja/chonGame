package engine;

import javafx.scene.image.Image;
import java.util.ArrayList;
import application.Character;

public class BulletOne extends Character {

	public BulletOne(int positionX, int positionY, int velocityX, int velocityY, int width, int height) {
		super(positionX, positionY, velocityX, velocityY, width, height);
		this.setImages(new ArrayList<Image>());
		this.getImages().add(new Image("\\images\\bulletOne\\bulletOne001.png"));
		this.getImages().add(new Image("\\images\\bulletOne\\bulletOne002.png"));
		this.getImages().add(new Image("\\images\\bulletOne\\bulletOne003.png"));
	}

}

