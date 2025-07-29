package chon.group.game.core.menu;

import chon.group.game.drawer.JavaFxDrawer;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;

public abstract class Menu<T extends Enum<T>> {
    protected int selectedOptionIndex = 0;
    protected final T[] options;
    protected final Image backgroundImage;

    public Menu(JavaFxDrawer drawer, Image backgroundImage, T[] options) {
        this.backgroundImage = backgroundImage;
        this.options = options;
    }

    public T handleInput(KeyCode code) {
        if (code == KeyCode.UP) {
            selectedOptionIndex = (selectedOptionIndex - 1 + options.length) % options.length;
        } else if (code == KeyCode.DOWN) {
            selectedOptionIndex = (selectedOptionIndex + 1) % options.length;
        } else if (code == KeyCode.ENTER) {
            return options[selectedOptionIndex];
        }
        return null;
    }

    public void reset() {
        this.selectedOptionIndex = 0;
    }

    public int getSelectedOptionIndex() {
        return selectedOptionIndex;
    }

    public Image getBackgroundImage() {
        return backgroundImage;
    }

    public abstract String[] getLabels();
}