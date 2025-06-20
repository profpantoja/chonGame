package chon.group.game.domain.environment;

import chon.group.game.drawer.JavaFxDrawer;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;

public class MenuPause {

    private int selectedOptionIndex = 0;
    private final String[] options = {"Continuar", "Opções", "Sair para o Menu"};
    private final JavaFxDrawer drawer;
    private final Image backgroundImage;

    private Runnable onResume;
    private Runnable onExitToMenu;

    public MenuPause(JavaFxDrawer drawer, Image backgroundImage) {
        this.drawer = drawer;
        this.backgroundImage = backgroundImage;
    }

    public void draw() {
        // Usa o método genérico de desenhar menu do JavaFxDrawer
        drawer.drawMenuPause("Pausado", selectedOptionIndex, backgroundImage, options);
    }

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

    private void executeSelectedOption() {
        switch (selectedOptionIndex) {
            case 0: if (onResume != null) onResume.run(); break;
            case 1: /* Lógica para Opções */ break;
            case 2: if (onExitToMenu != null) onExitToMenu.run(); break;
        }
    }
    
    public void setOnResume(Runnable onResume) { this.onResume = onResume; }
    public void setOnExitToMenu(Runnable onExitToMenu) { this.onExitToMenu = onExitToMenu; }
    public void reset() { this.selectedOptionIndex = 0; }
}