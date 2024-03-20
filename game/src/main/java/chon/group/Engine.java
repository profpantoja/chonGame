package chon.group;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

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

			GraphicsContext gc = canvas.getGraphicsContext2D();

			System.out.println("Working Directory = " + System.getProperty("user.dir"));
			Image background = new Image(getClass().getResource("/images/environment/castle.png").toExternalForm());
			Image chonBot = new Image(getClass().getResource("/images/agents/chonBot.png").toExternalForm());
			Image chonBota = new Image(getClass().getResource("/images/agents/chonBota.png").toExternalForm());
			System.out.println("Image URL: " + chonBot.getUrl());
			System.out.println(chonBot.isError());

			gc.drawImage(background, 0, 0, 1280, 780);
			gc.drawImage(chonBot, 920, 440, 65, 90);
			gc.drawImage(chonBota, 400, 390, 65, 90);

			gc.fillRoundRect(770, 500, 30, 30, 0, 0);
			gc.fillOval(650, 410, 20, 20);

			gc.setFill(Color.BLACK);
			gc.setStroke(Color.BLACK);
			gc.setLineWidth(2);
			Font theFont = Font.font("Verdana", FontWeight.BOLD, 14);
			gc.setFont(theFont);
			gc.fillText("Hey, I'm ChonBota...", 365, 380);

			root.getChildren().add(canvas);
			theStage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
