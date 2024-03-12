package engine;

import java.util.ArrayList;

import application.Character;
import javafx.scene.image.Image;

public class Hero extends Character {

	public Hero(int positionX, int positionY, int velocityX, int velocityY, int width, int height) {
		super(positionX, positionY, velocityX, velocityY, width, height);
		this.setImages(new ArrayList<Image>());
		this.getImages().add(new Image(getClass().getResource("/images/hero/heroRight001.png").toExternalForm()));
		this.getImages().add(new Image(getClass().getResource("/images/hero/heroRight002.png").toExternalForm()));
		this.getImages().add(new Image(getClass().getResource("/images/hero/heroRight003.png").toExternalForm()));

		
		ArrayList<Character> weapons = new ArrayList<>();
		weapons.add(new BulletOne(0, 0, 6, 6, 75, 47));
		weapons.add(new BulletTwo(100, 100, 6, 6, 64, 42));
		this.setWeapons(weapons);
	}
}