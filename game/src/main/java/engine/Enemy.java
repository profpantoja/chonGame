package engine;

import javafx.scene.image.Image;

public class Enemy extends Agent {

	public Enemy(int x, int y) {
		super(0, 64, 0, 64, x, y, 256, 64, 64);
		this.setImage(new Image(("\\images\\monster.png")));
	}
}
