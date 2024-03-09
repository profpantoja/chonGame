package engine;

import javafx.scene.image.Image;

public class Fire extends Agent{

	public Fire(int posX, int posY){
		super(0,44,0,43,posX,posY,44, 44, 43);
		this.setImage(new Image("\\images\\fire.png"));
	}
}
