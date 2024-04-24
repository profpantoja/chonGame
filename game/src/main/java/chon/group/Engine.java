package chon.group;

import java.util.ArrayList;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyEvent;
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

			ArrayList<String> input = new ArrayList<String>();
			scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
				public void handle(KeyEvent e) {
					String code = e.getCode().toString();
					input.clear();

					System.out.println("Pressed: " + code);
					input.add(code);
				}
			});

			scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
				public void handle(KeyEvent e) {
					String code = e.getCode().toString();
					System.out.println("Released: " + code);
					input.remove(code);
				}
			});

			new AnimationTimer() {
				int position = 200;

				@Override
				public void handle(long arg0) {
					if (!input.isEmpty()) {
						gc.clearRect(0, 0, 640, 480);
						gc.drawImage(background, 0, 0, canvas.getWidth(), canvas.getHeight());
						if (input.contains("RIGHT")) {
							gc.drawImage(chonBota, position += 1, 200);
						} else if (input.contains("LEFT")) {
							var chonBotaFlipado = flip(chonBota);
							gc.drawImage(chonBotaFlipado, position -= 1, 200);
						} else if (input.contains("UP")) {
							gc.drawImage(chonBota, position, position -= 1);
						} else if (input.contains("DOWN")) {
							gc.drawImage(chonBota, position, position += 1);
						}
						gc.fillText(input.get(0), 10, 10);
					}
				}

			}.start();

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
