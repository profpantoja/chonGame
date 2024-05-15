package chon.group;

import chon.group.agent.Agent;
import chon.group.enviroment.Environment;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Engine extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage theStage) {
		try {

			StackPane root = new StackPane();
			Scene scene = new Scene(root, 1280, 780);
			theStage.setTitle("Chon: The Learning Game");
			theStage.setScene(scene);
			Canvas canvas = new Canvas(1280, 780);

			System.out.println("Working Directory = " + System.getProperty("user.dir"));

			root.getChildren().add(canvas);
			theStage.show();

			Environment background = new Environment(0, 0, 1280, 780, "/images/environment/castle.png", canvas.getGraphicsContext2D());
			background.drawBackground();

			Agent chonBot = new Agent(920, 440, 90, 65, "/images/agents/chonBot.png", canvas.getGraphicsContext2D());
			chonBot.draw();

			Agent chonBota = new Agent(400, 390, 90, 65, "/images/agents/chonBota.png", canvas.getGraphicsContext2D());
			chonBota.draw();

			chonBota.startAnimation(canvas, scene, background, chonBot);

			theStage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static WritableImage flip(Image image) {
		ImageView iv = new ImageView(image);
		iv.setScaleX(-1);
		SnapshotParameters params = new SnapshotParameters();
		params.setFill(Color.TRANSPARENT);
		return iv.snapshot(params, null);
	}
    
}
