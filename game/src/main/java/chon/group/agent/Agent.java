package chon.group.agent;

import java.util.ArrayList;

import chon.group.enviroment.Environment;
import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class Agent {
    private int positionX;
    private int positionY;
    private int height;
    private int width;
    private String pathImage;
	private Scene scene;	
	private ArrayList<String> input = new ArrayList<String>();
	private GraphicsContext gc;

    public Agent(int positionX, int positionY, int height, int width, String pathImage, GraphicsContext gc) {
        this.positionX = positionX;
        this.positionY = positionY;
        this.height = height;
        this.width = width;
        this.pathImage = pathImage;
        this.gc = gc;
    }

	public Agent(Scene scene) {
        this.scene = scene;
	}

    public int getPositionX() {
        return positionX;
    }

    public void setPositionX(int positionX) {
        this.positionX = positionX;
    }

    public int getPositionY() {
        return positionY;
    }

    public void setPositionY(int positionY) {
        this.positionY = positionY;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public String getPathImage() {
        return pathImage;
    }

    public void setPathImage(String pathImage) {
        this.pathImage = pathImage;
    }

    public GraphicsContext getGc() {
        return gc;
    }

    public void setGc(GraphicsContext gc) {
        this.gc = gc;
    }

	public Scene getScene() {
		return scene;
	}

	public void setScene(Scene scene) {
		this.scene = scene;
	}

	public ArrayList<String> getInput() {
		return input;
	}

	public void setInput(ArrayList<String> input) {
		this.input = input;
	}

	private Image generateImagem() {
        Image chonImage = new Image(getClass().getResource(pathImage).toExternalForm());
        return chonImage;
    }

    public void draw() {
        gc.drawImage(generateImagem(), positionX, positionY, width, height);
	}

	public static void printPosition(GraphicsContext gc, int positionX, int positionY) {
		gc.setFill(Color.BLACK);
		gc.setStroke(Color.BLACK);
		gc.setLineWidth(2);
		Font theFont = Font.font("Verdana", FontWeight.BOLD, 14);
		gc.setFont(theFont);
		gc.fillText("X: " + positionX, positionX + 10, positionY - 25);
		gc.fillText("Y: " + positionY, positionX + 10, positionY - 10);
	}

	public void startAnimation(Canvas canvas, Scene scene, Environment background, Agent chonBot) {
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
			int positionX = 400;
			int positionY = 390;

			@Override
			public void handle(long arg0) {
				GraphicsContext gc = canvas.getGraphicsContext2D();
				if (!input.isEmpty()) {
					gc.clearRect(0, 0, 1280, 780);
					gc.drawImage(background.generateBackground(), 0, 0, canvas.getWidth(), canvas.getHeight());
					if (input.contains("RIGHT")) {
						if (positionX < 1215) {
							positionX += 1;
							
						} else {
							positionX = 1215;
						}
					} else if (input.contains("LEFT")) {
						if (positionX - 1 > 0) {
							positionX -= 1;
						} else {
							positionX = 1;
						}
					} else if (input.contains("UP")) {
						if (positionY > 1) {
							positionY -= 1;
						} else {
							positionY = 1;
						}
					} else if (input.contains("DOWN")) {
						if (positionY < 690) {
							positionY += 1;
						} else {
							positionY = 690;
						}

					} else {
						gc.drawImage(generateImagem(), positionX, positionY, 65, 90);
						printStatusPanel(gc, positionX, positionY);
					}

					gc.drawImage(generateImagem(), positionX, positionY, 65, 90);
					printStatusPanel(gc, positionX, positionY);

					gc.fillText(input.get(0), 10, 10);
				}
				gc.drawImage(chonBot.generateImagem(), 920, 440, 65, 90);
			}

		}.start();
	}

	public static void printStatusPanel(GraphicsContext gc, int positionX, int positionY) {
		gc.setFill(Color.BLACK);
		gc.setStroke(Color.BLACK);
		gc.setLineWidth(2);
		Font theFont = Font.font("Verdana", FontWeight.BOLD, 14);
		gc.setFont(theFont);
		gc.fillText("X: " + positionX, positionX + 10, positionY - 25);
		gc.fillText("Y: " + positionY, positionX + 10, positionY - 10);
	}
}