package chon.group.game.domain.environment;

import javafx.scene.image.Image;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;

public class Environment {
    private BackgroundImage backgroundImage;

    public Environment(String imagePath, double width, double height) {
        Image image = new Image(getClass().getResource(imagePath).toExternalForm());
        backgroundImage = new BackgroundImage(
            image,
            BackgroundRepeat.NO_REPEAT,
            BackgroundRepeat.NO_REPEAT,
            BackgroundPosition.DEFAULT,
            new BackgroundSize(width, height, false, false, false, false)
        );
    }

    public BackgroundImage getBackgroundImage() {
        return backgroundImage;
    }

    public void setBackgroundImage(String imagePath, double width, double height) {
        Image image = new Image(getClass().getResource(imagePath).toExternalForm());
        backgroundImage = new BackgroundImage(
            image,
            BackgroundRepeat.NO_REPEAT,
            BackgroundRepeat.NO_REPEAT,
            BackgroundPosition.DEFAULT,
            new BackgroundSize(width, height, false, false, false, false)
        );
    }
}