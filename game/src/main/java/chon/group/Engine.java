package chon.group;

import java.util.ArrayList;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
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
			Image chonBota = new Image(getClass().getResource("/images/agents/chonBota.png").toExternalForm());
			Image chonBot = new Image(getClass().getResource("/images/agents/chonBot.png").toExternalForm());

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

				/* ChonBota's Attributes */
				int xImageChonBota = 400;
				int yImageChonBota = 390;
				int wImageChonBota = 65;
				int hImageChonBota = 90;
				/* ChonBota's Speed */
				int speedChonBota = 2;

				/* ChonBot's Attributes */
				int xImageChonBot = 920;
				int yImageChonBot = 440;
				int wImageChonBot = 65;
				int hImageChonBot = 90;
				/* ChonBot's Speed */
				int speedChonBot = 1;

				@Override
				public void handle(long arg0) {
					/* ChonBota Only Moves if the Player Press Something */
					if (!input.isEmpty()) {
						/* ChonBota's Movements */
						if (input.contains("LEFT")) {
							if (xImageChonBota - speedChonBota > 0)
								xImageChonBota -= speedChonBota;
							else
								xImageChonBota = 0;

						} else if (input.contains("RIGHT")) {
							if ((xImageChonBota + wImageChonBota) + speedChonBota < (wCanvas))
								xImageChonBota += speedChonBota;
							else
								xImageChonBota = wCanvas - wImageChonBota;

						} else if (input.contains("UP")) {
							if (yImageChonBota - speedChonBota > 0)
								yImageChonBota -= speedChonBota;
							else
								yImageChonBota = 0;

						} else if (input.contains("DOWN")) {
							if ((yImageChonBota + hImageChonBota) + speedChonBota < hCanvas)
								yImageChonBota += speedChonBota;
							else
								yImageChonBota = (hCanvas - hImageChonBota);
						}
					}
					/* ChonBot's Automatic Movements */
					/* Moving Infinitely to the LEFT Beyond Borders */
					// xImageChonBot -= 1;

					/* Moving Infinitely to the LEFT Until the Border */
					/*
					 * if (xImageChonBot - 1 > 0)
					 * xImageChonBot -= 1;
					 * else
					 * xImageChonBot = 0;
					 */

					/* Moving Infinitely to the RIGHT Beyond Borders */
					// xImageChonBot += 1;

					/* Chasing the Player */
					/*
					 * if (xImageChonBota > xImageChonBot) {
					 * xImageChonBot += 1;
					 * } else if (xImageChonBota < xImageChonBot) {
					 * xImageChonBot -= 1;
					 * }
					 * 
					 * if (yImageChonBota > yImageChonBot) {
					 * yImageChonBot += 1;
					 * } else if (yImageChonBota < yImageChonBot) {
					 * yImageChonBot -= 1;
					 * }
					 */

					/* Chasing the Player with Speed */
					if (xImageChonBota > xImageChonBot) {
						xImageChonBot += speedChonBot;
					} else if (xImageChonBota < xImageChonBot) {
						xImageChonBot -= speedChonBot;
					}

					if (yImageChonBota > yImageChonBot) {
						yImageChonBot += speedChonBot;
					} else if (yImageChonBota < yImageChonBot) {
						yImageChonBot -= speedChonBot;
					}

					/* Rendering Objects */
					gc.clearRect(0, 0, 1280, 780);
					gc.drawImage(background, 0, 0, canvas.getWidth(), canvas.getHeight());
					gc.drawImage(background, 0, 0, 1280, 780);
					gc.drawImage(chonBota, xImageChonBota, yImageChonBota, wImageChonBota, hImageChonBota);
					printStatusPanel(gc, xImageChonBota, yImageChonBota);
					gc.drawImage(chonBot, xImageChonBot, yImageChonBot, wImageChonBot, hImageChonBot);
				}
			}.start();
			theStage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void printStatusPanel(GraphicsContext gc, int xImageChonBota, int yImageChonBota) {
		gc.setFill(Color.BLACK);
		gc.setStroke(Color.BLACK);
		gc.setLineWidth(2);
		Font theFont = Font.font("Verdana", FontWeight.BOLD, 14);
		gc.setFont(theFont);
		gc.fillText("X: " + xImageChonBota, xImageChonBota + 10, yImageChonBota - 25);
		gc.fillText("Y: " + yImageChonBota, xImageChonBota + 10, yImageChonBota - 10);
	}

}
