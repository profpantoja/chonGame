package chon.group;

import java.util.ArrayList;

import chon.group.game.domain.agent.Agent;
import chon.group.game.domain.environment.Environment;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;

public class Engine extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage theStage) {
		try {
			Environment environment = new Environment(0, 0, 1280, 780, "/images/environment/forest.png");
			Agent jon = new Agent(400, 390, 165, 68, 5, "/images/agents/jon.png");
			Agent zumbi1 = new Agent(920, 400, 110, 130, 1, "/images/agents/zumbi.png");
			Agent zumbi2 = new Agent(820, 400, 110, 130, 1, "/images/agents/zumbi.png");
			Environment environment2 = new Environment(0, 0, 1280, 780, "/images/environment/forest2.png");

			environment.setProtagonist(jon);
			environment.getAgents().add(zumbi1);
			environment.getAgents().add(zumbi2);

			Canvas canvas = new Canvas(environment.getWidth(), environment.getHeight());
			GraphicsContext gc = canvas.getGraphicsContext2D();
			environment.setGc(gc);
			environment2.setGc(gc);

			StackPane root = new StackPane();
			Scene scene = new Scene(root, environment.getWidth(), environment.getHeight());
			theStage.setTitle("Chon: The Learning Game");
			theStage.setScene(scene);

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

				@Override
				public void handle(long arg0) {
					/* jon Only Moves if the Player Press Something */
					if (!input.isEmpty()) {
						/* Jon Movements */
						environment.getProtagonist().move(input);
						environment.checkBorders();
						environment.detectCollision();
						environment2.drawBackground();
					} 
					/* Zumbis Automatic Movements */

					environment.getAgents().get(0).chase(environment.getProtagonist().getPosX(),
							environment.getProtagonist().getPosY());
					environment.getAgents().get(1).chase(environment.getProtagonist().getPosX(),
							environment.getProtagonist().getPosY());
					/* Rendering Objects */
					environment.drawBackground();
					environment.drawAgents();
					environment.checkAndAdjustPosition(environment.getAgents().get(0));
					environment.checkAndAdjustPosition(environment.getAgents().get(1));
					environment.checkAndAdjustPosition(environment.getProtagonist());
					environment.detectCollision();
					environment2.drawBackground();
				}

			}.start();
			theStage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
