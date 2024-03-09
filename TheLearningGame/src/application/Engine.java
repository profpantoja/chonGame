package application;

import java.util.ArrayList;
import java.util.Iterator;

import application.Character;
import engine.Box;
import engine.Environment;
import engine.Hero;
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

public class Engine extends Application {

	private Environment environment = null;

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
			Image phase = new Image("\\images\\environment3.jpg");
			gc.drawImage(phase, 0, 0, 1500, 3000);

			Character hero = new Hero(100, 100, 3, 3, 32, 48);
			// Character hero = new BulletOne(100, 100, 6, 6, 32, 48);

			// Image bullet = new Image("\\images\\bulletOne\\bulletOne001.png");
			// System.out.println("-> " + bullet.getHeight() + " - " + bullet.getWidth());
			// System.out.println(this.environment.getLargura() + " - " +
			// this.environment.getAltura());

			ArrayList<Character> obstacles = new ArrayList<>();
			Box box1 = new Box(300, 508, 0, 0, 44, 43);
			Box box2 = new Box(400, 460, 0, 0, 44, 43);
			Box box3 = new Box(444, 460, 0, 0, 44, 43);
			Box box4 = new Box(488, 417, 0, 0, 44, 43);
			Box box5 = new Box(532, 460, 0, 0, 44, 43);
			obstacles.add(box1);
			obstacles.add(box2);
			obstacles.add(box3);
			obstacles.add(box4);
			obstacles.add(box5);

			ArrayList<Character> bullets = new ArrayList<>();

			Character screen = new Character(0, 552, 0, 0, 1250, 48);

			root.getChildren().add(canvas);

			ArrayList<String> input = new ArrayList<String>();
			scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
				public void handle(KeyEvent e) {
					String code = e.getCode().toString();
					input.clear();
					if (!input.contains(code))
						input.add(code);
				}
			});

			scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
				public void handle(KeyEvent e) {
					String code = e.getCode().toString();
					input.remove(code);
				}
			});

			new AnimationTimer() {

				public void handle(long currentNanoTime) {
					// System.out.println("---> " + input.size());
					gc.drawImage(phase, 0, 0, 2000, 600);

					if (input.size() >= 3)
						input.clear();

					if (input.contains("RIGHT") && input.contains("LEFT")) {
						input.remove("RIGHT");
						input.remove("LEFT");
					}

					if (input.contains("UP") && input.contains("DOWN")) {
						input.remove("UP");
						input.remove("DOWN");
					}

					if (input.contains("RIGHT"))
						hero.moveRight(gc);

					if (input.contains("LEFT"))
						hero.moveLeft(gc);

					if (input.contains("DOWN"))
						hero.falling(gc);
					// hero.moveDown(gc);
			
					if (input.contains("UP")) {
						if (!hero.isJumping()) {
							hero.setJumping(true);
							hero.jumping(gc);
						} 
						// hero.moveUp(gc);
					}

					if (hero.isJumping()) {
						hero.falling(gc);
					}
					
					if (input.isEmpty())
						hero.stopped(gc);

					Boolean wall = false;
					Iterator<Character> iterator = bullets.iterator();
					while (iterator.hasNext()) {
						Character bullet = iterator.next();
						if (bullet.getDirection() == Character.Direction.LEFT) {
							bullet.moveLeft(gc);
							if (bullet.getPositionX() >= (-bullet.getWidth()))
								bullet.draw(gc);
							else {
								iterator.remove();
								System.out.println("Getting out at Left!");
							}
						} else {
							bullet.moveRight(gc);
							if (bullet.getPositionX() <= 1250) {
								bullet.draw(gc);
							} else {
								iterator.remove();
								System.out.println("Getting out at Right!");
							}
						}

						for (Character obstacle : obstacles) {
							if (bullet.intersects(obstacle)) {
								wall = true;
								System.out.println("I found a wall!");
							}
						}

						if (wall) {
							iterator.remove();
							wall = false;
						}
					}

					/*
					 * int counter = 0; for (String keys : input) { if (keys == "SPACE") counter++;
					 * } System.out.println("----------------------------->" + counter);
					 */
					if (input.contains("SPACE")) {
						if (bullets.isEmpty())
							bullets.add(hero.fire(gc));
						else {
							hero.stopped(gc);
						}
					}
					//
					// hero.drawPanel(gc);

					if (input.contains("ALT")) {
						System.out.println("Changing Weapons!");
						input.remove("ALT");
						hero.changeWeapon();
						hero.draw(gc);
						hero.drawPanel(gc);
					}

					for (Character obstacle : obstacles) {
						obstacle.draw(gc);
						if (hero.intersects(obstacle))
							obstacle.drawPanel(gc);
					}

					if (hero.intersects(screen)) {
						hero.setJumping(false);
						System.out.println("Ground!");
					}

				}
			}.start();

			theStage.show();

		} catch (

		Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * 
	 * this.environment.getGost().draw(g); if
	 * (!this.environment.getEnemy().isVisible()) this.environment.getEnemy().die();
	 * else this.environment.getEnemy().draw(g);
	 * 
	 * if (this.environment.getGost().direction == Agent.Direction.UP &&
	 * this.environment.getGost().jump == false) { this.environment.getGost().jump =
	 * true; //animator = new Thread(this.environment.getGost());
	 * //animator.start(); }
	 * 
	 * ArrayList bullets = this.environment.getGost().bullets; for (int w = 0; w <
	 * bullets.size(); w++) { Agent m = (Agent) bullets.get(w); m.moveLine();
	 * m.draw(g); }
	 * 
	 * }
	 */

	public static void main(String[] args) {
		launch(args);
	}
}
