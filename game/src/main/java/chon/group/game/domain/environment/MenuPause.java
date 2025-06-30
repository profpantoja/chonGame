package chon.group.game.domain.environment;

import chon.group.game.drawer.JavaFxDrawer;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;

public class MenuPause {

    private int selectedOptionIndex = 0;
    private final MenuOption.Pause[] options = MenuOption.Pause.values();
    private final JavaFxDrawer drawer;
    private final Image backgroundImage;

    public MenuPause(JavaFxDrawer drawer, Image backgroundImage) {
        this.drawer = drawer;
        this.backgroundImage = backgroundImage;
    }

    public void draw() {
        String[] labels = {"Resume", "Settings", "Go back to Menu"};
        drawer.drawMenuPause("Paused", selectedOptionIndex, backgroundImage, labels);
    }

    // Retorna a opção escolhida ao pressionar ENTER, ou null caso contrário
    public MenuOption.Pause handleInput(KeyCode code) {
        switch (code) {
            case UP:
                selectedOptionIndex = (selectedOptionIndex - 1 + options.length) % options.length;
                break;
            case DOWN:
                selectedOptionIndex = (selectedOptionIndex + 1) % options.length;
                break;
            case ENTER:
                return options[selectedOptionIndex];
        }
        return null;
    }

    public void reset() {
        this.selectedOptionIndex = 0;
    }
}