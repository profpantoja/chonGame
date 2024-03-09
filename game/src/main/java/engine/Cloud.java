package engine;

import javafx.scene.image.Image;

public class Cloud extends Agent{

	public Cloud(int posX, int posY){
		super(0,100,18,84,posX,posY,100,100,84);
		this.setImage(new Image("\\images\\cloud.png"));	
	}
	
}
