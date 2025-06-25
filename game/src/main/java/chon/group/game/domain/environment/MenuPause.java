package chon.group.game.domain.environment;

import chon.group.game.drawer.JavaFxDrawer;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;

public class MenuPause {

    private int selectedOptionIndex = 0;
    private final String[] options = {"Resume", "Go back to Menu"};
    private final JavaFxDrawer drawer;
    private final Image backgroundImage;

    private Runnable onResume;
    private Runnable onExitToMenu;

    public MenuPause(JavaFxDrawer drawer, Image backgroundImage) {
        this.drawer = drawer;
        this.backgroundImage = backgroundImage;
    }

    // Draws the pause menu on the screen
    public void draw() {
        drawer.drawMenuPause("Paused", selectedOptionIndex, backgroundImage, options);
    }

    // Handles input for navigating the pause menu
    public void handleInput(KeyCode code) {
        switch (code) {
            case UP:
                selectedOptionIndex = (selectedOptionIndex - 1 + options.length) % options.length;
                break;
            case DOWN:
                selectedOptionIndex = (selectedOptionIndex + 1) % options.length;
                break;
            case ENTER:
                executeSelectedOption();
                break;
        }
    }

    // Executes the action associated with the selected menu option
    private void executeSelectedOption() {
        switch (selectedOptionIndex) {
            case 0: if (onResume != null) onResume.run(); break;
            case 1: if (onExitToMenu != null) onExitToMenu.run(); break;
        }
    }
    
    // Setters for the actions to be executed when an option is selected
    public void setOnResume(Runnable onResume) {
        this.onResume = onResume; 
    }
    public void setOnExitToMenu(Runnable onExitToMenu) {
        this.onExitToMenu = onExitToMenu; 
    }
    public void reset() {
        this.selectedOptionIndex = 0; 
    }
}