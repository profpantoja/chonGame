package chon.group.game.domain.environment;

import chon.group.game.drawer.JavaFxDrawer;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;

public class MainMenu {
    
    private int selectedOption = 0;
    private final String[] options = { "Iniciar Jogo", "Opções", "Sair" };
    private JavaFxDrawer drawer; // Corrigido
    private Image backgroundImage; // Corrigido

    // Actions for each menu option
    private Runnable onStartGame;
    private Runnable onOptions;
    private Runnable onExit;

    public MainMenu(JavaFxDrawer drawer, Image backgroundImage) {
        this.drawer = drawer; // Corrigido: armazena a instância do drawer
        this.backgroundImage = backgroundImage;
    }

    public void draw() {
        drawer.drawMainMenu(backgroundImage, "Chon: The Learning Game", selectedOption, options);
    }

    // A Engine vai chamar este método quando o input for relevante para este menu
    public void handleInput(KeyCode code) {
        switch (code) {
            case UP:
                selectedOption = (selectedOption - 1 + options.length) % options.length;
                break;
            case DOWN:
                selectedOption = (selectedOption + 1) % options.length;
                break;
            case ENTER:
                executeSelectedOption();
                break;
            default:
                break;
        }
    }

    public void executeSelectedOption() {
        switch (selectedOption) {
            case 0: if (onStartGame != null) onStartGame.run(); break;
            case 1: if (onOptions != null) onOptions.run(); break;
            case 2: if (onExit != null) onExit.run(); break;
        }
    }
    
    public void setOnStartGame(Runnable onStartGame) { this.onStartGame = onStartGame; }
    public void setOnOptions(Runnable onOptions) { this.onOptions = onOptions; }
    public void setOnExit(Runnable onExit) { this.onExit = onExit; }
    public void reset() { this.selectedOption = 0; }
}