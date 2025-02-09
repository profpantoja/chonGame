package chon.group.game.domain.environment;

import javafx.scene.image.Image;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;

/**
 * Represents the game environment by managing the background image.
 */
public class Environment {
    private BackgroundImage backgroundImage;

    /**
     * Constructs an Environment with a specified background image.
     *
     * @param imagePath the path to the background image
     * @param width the width of the background
     * @param height the height of the background
     */
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

    /**
     * Retrieves the background image.
     *
     * @return the current background image
     */
    public BackgroundImage getBackgroundImage() {
        return backgroundImage;
    }

    /**
     * Updates the background image with a new one.
     *
     * @param imagePath the path to the new background image
     * @param width the width of the new background
     * @param height the height of the new background
     */
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