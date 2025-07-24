package chon.group.game.menu;

import chon.group.game.drawer.JavaFxDrawer;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;

public abstract class Menu<T extends Enum<T>> {
    protected int selectedOptionIndex = 0;
    protected final T[] options;
    protected final Image backgroundImage;
    protected final MenuTextManager textManager;

    public Menu(JavaFxDrawer drawer, Image backgroundImage, T[] options) {
        this.backgroundImage = backgroundImage;
        this.options = options;
        this.textManager = MenuTextManager.getInstance();
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

    public String[] getLabels() {
        String[] labels = new String[options.length];
        for (int i = 0; i < options.length; i++) {
            labels[i] = textManager.getText(options[i]);
        }
        return labels;
    }
}