package com.hiro;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Parent;
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
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        StackPane root = new StackPane();
        Canvas canvas = new Canvas(640, 480);
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
        graphicsContext.fillRoundRect(340, 240, 15, 15, 0, 0);
        scene = new Scene(root, 640, 480);
        Image robo = new Image(getClass().getResource("designrobofodastico.png").toExternalForm());
        Image background = new Image(getClass().getResource("OIP2.jpeg").toExternalForm());
        graphicsContext.drawImage(background, 0, 0, canvas.getWidth(), canvas.getHeight());
        graphicsContext.drawImage(robo, 200, 200);
        stage.setTitle("Bastardos");
        stage.setScene(scene);
        root.getChildren().add(canvas);


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
                        graphicsContext.clearRect(0, 0, 640, 480);
                        graphicsContext.drawImage(background, 0, 0, canvas.getWidth(), canvas.getHeight());
                        if(input.contains("RIGHT")) {
                            graphicsContext.drawImage(robo, position+=1, 200);
                        } else if(input.contains("LEFT")) {
                            var roboFlipado = flip(robo);
                            graphicsContext.drawImage(roboFlipado, position-=1, 200);
                        } else if(input.contains("UP")) {
                            graphicsContext.drawImage(robo, position, position-=1);
                        } else if(input.contains("DOWN")) {
                            graphicsContext.drawImage(robo, position, position+=1);
                        }
                        graphicsContext.fillText(input.get(0), 10, 10);
                    }
                }
                
            }.start();

        stage.show();
    }

    static WritableImage flip(Image image) {
        ImageView iv = new ImageView(image);
        iv.setScaleX(-1);

        SnapshotParameters params = new SnapshotParameters();
        params.setFill(Color.TRANSPARENT);

        return iv.snapshot(params, null);
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        return null;
    }

    public static void main(String[] args) {
        launch();
    }

}