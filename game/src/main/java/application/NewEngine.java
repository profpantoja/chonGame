package application;

import engine.Environment;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;

public class NewEngine extends Application {

	private Environment environment = null;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage theStage) {
		try {

			this.environment = new Environment();
			StackPane root = new StackPane();
			Scene scene = new Scene(root, this.environment.getLargura(), this.environment.getAltura());
			theStage.setTitle("The Learning Game");
			theStage.setScene(scene);
			Canvas canvas = new Canvas(this.environment.getLargura(), this.environment.getAltura());

			GraphicsContext gc = canvas.getGraphicsContext2D();

			System.out.println("Working Directory = " + System.getProperty("user.dir"));
			Image phase = new Image(getClass().getResource("/images/environment3.jpg").toExternalForm());
			System.out.println("Image URL: " + phase.getUrl());
			System.out.println(phase.isError());

			gc.drawImage(phase, 0, 0, 1500, 3000);
			gc.fillRoundRect(80, 20, 30, 30, 0, 0);
			gc.fillOval(160, 40, 20, 20);

			gc.fillText("Lets Gooooo...", 400, 400);
			root.getChildren().add(canvas);
			theStage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
