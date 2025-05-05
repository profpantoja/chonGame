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
			Environment environment = new Environment(0, 0, 320*4, 288*4, "/images/environment/Pallet.png");
			Agent chonBota = new Agent(400, 390, 16*4, 16*4, 2, "/images/agents/Gold.png");
			Agent chonBot = new Agent(920, 440, 16*4, 16*4, 1, "/images/agents/Silver.png");
			Agent collisionLab = new Agent(648, 510, 250, 348, 0, "");
			Agent collisionRedHome = new Agent(196, 200, 182, 290, 0, "");
			Agent collisionBlueHome = new Agent(694, 200, 182, 290, 0, "");
			Agent collisionUpperFence = new Agent(248, 584, 48, 254, 0, "");
			Agent collisionLowerFence = new Agent(632, 850, 62, 378, 0, "");
			Agent collisionWater = new Agent(244, 894, 70, 258, 0, "");
	
			environment.setProtagonist(chonBota);
			environment.getAgents().add(chonBot);
			environment.getAgents().add(collisionLab);
			environment.getAgents().add(collisionRedHome);
			environment.getAgents().add(collisionBlueHome);
			environment.getAgents().add(collisionUpperFence);
			environment.getAgents().add(collisionLowerFence);
			environment.getAgents().add(collisionWater);


			Canvas canvas = new Canvas(environment.getWidth(), environment.getHeight());
			GraphicsContext gc = canvas.getGraphicsContext2D();
			environment.setGc(gc);

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
					/* ChonBota Only Moves if the Player Press Something */
					if (!input.isEmpty()) {
						/* ChonBota's Movements */
						environment.getProtagonist().move(input);
						environment.checkBorders();
					} 
					environment.detectCollision();
					/* ChonBot's Automatic Movements */

					//environment.getAgents().get(0).chase(environment.getProtagonist().getPosX(),
							//environment.getProtagonist().getPosY());
					/* Rendering Objects */
					environment.drawBackground();
					environment.drawAgents();
				}

			}.start();
			theStage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
