package chon.group.game.menu;

import chon.group.game.drawer.JavaFxDrawer;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;

public class MenuPause {

    private int selectedOptionIndex = 0;
    private final MenuOption.Pause[] options = MenuOption.Pause.values();
    private final Image backgroundImage;

    public MenuPause(JavaFxDrawer drawer, Image backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    // Retorna a opção escolhida ao pressionar ENTER, ou null caso contrário
    public MenuOption.Pause handleInput(KeyCode code) {
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
        return new String[]{"Resume", "Go back to Menu"}; 
    }
}