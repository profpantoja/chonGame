package chon.group.game.drawer;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;

public class JavaFxDrawer {
    private final GraphicsContext gc;
    @SuppressWarnings("unused")
    private final EnvironmentDrawer mediator;

    public JavaFxDrawer(GraphicsContext gc, EnvironmentDrawer mediator) {
        this.gc = gc;
        this.mediator = mediator;
    }

    public void clearScreen(double width, double height) {
        gc.clearRect(0, 0, width, height);
    }

    public void drawImage(Paint fill, double x, double y, double width, double height) {
        if (fill instanceof Color) {
            gc.setFill((Color) fill);
            gc.fillRect(x, y, width, height);
        } else if (fill instanceof ImagePattern) {
            ImagePattern imagePattern = (ImagePattern) fill;
            gc.drawImage(imagePattern.getImage(), x, y, width, height);
        }
    }

    public void drawImage(Image image, double x, double y, double width, double height) {
        gc.drawImage(image, x, y, width, height);
    }

    public void drawLifeBar(double health, double fullHealth, double width, double x, double y, Color color) {
        double barWidth = (health / fullHealth) * width;
        gc.setFill(color);
        gc.fillRect(x, y - 10, barWidth, 5);
    }

    public void drawPauseScreen(Image pauseImage, int imageWidth, int imageHeight, double canvasWidth, double canvasHeight) {
        double x = (canvasWidth - imageWidth) / 2;
        double y = (canvasHeight - imageHeight) / 2;
        gc.drawImage(pauseImage, x, y);
    }
}
