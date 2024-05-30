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

			int wCanvas = 1280;
			int hCanvas = 780;
			Canvas canvas = new Canvas(wCanvas, hCanvas);

			GraphicsContext gc = canvas.getGraphicsContext2D();

			System.out.println("Working Directory = " + System.getProperty("user.dir"));
			Image background = new Image(getClass().getResource("/images/environment/castle.png").toExternalForm());
			Image chonBot = new Image(getClass().getResource("/images/agents/chonBot.png").toExternalForm());
			Image chonBota = new Image(getClass().getResource("/images/agents/chonBota.png").toExternalForm());

			int wImageBot = 65;
			int hImageBot = 90;
			gc.drawImage(background, 0, 0, 1280, 780);
			gc.drawImage(chonBot, 920, 440, wImageBot, hImageBot);
			gc.drawImage(chonBota, 400, 390, wImageBot, hImageBot);

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
				int xImage = 400;
				int yImage = 390;

				@Override
				public void handle(long arg0) {
					if (!input.isEmpty()) {
						gc.clearRect(0, 0, 1280, 780);
						gc.drawImage(background, 0, 0, canvas.getWidth(), canvas.getHeight());
						if (input.contains("LEFT")) {
							if (xImage - 1 > 0) {
								xImage -= 1;
							} else {
								xImage = 0;
							}
						} else if (input.contains("RIGHT")) {
							if ((xImage + wImageBot) + 1 < (wCanvas)) {
								xImage += 1;
							} else {
								xImage = wCanvas - wImageBot;
							}
						} else if (input.contains("UP")) {
							if (yImage - 1 > 0) {
								yImage -= 1;
							} else {
								yImage = 0;
							}
						} else if (input.contains("DOWN")) {
							if ((yImage + hImageBot) + 1 < hCanvas) {
								yImage += 1;
							} else {
								yImage = (hCanvas - hImageBot);
							}
						}
						gc.drawImage(chonBota, xImage, yImage, wImageBot, hImageBot);
						printStatusPanel(gc, xImage, yImage);
					}
					gc.drawImage(chonBot, 920, 440, 65, 90);
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

	public static void printStatusPanel(GraphicsContext gc, int xImage, int yImage) {
		gc.setFill(Color.BLACK);
		gc.setStroke(Color.BLACK);
		gc.setLineWidth(2);
		Font theFont = Font.font("Verdana", FontWeight.BOLD, 14);
		gc.setFont(theFont);
		gc.fillText("X: " + xImage, xImage + 10, yImage - 25);
		gc.fillText("Y: " + yImage, xImage + 10, yImage - 10);
	}

}
