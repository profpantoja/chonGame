package chon.group.game.drawer;

import javafx.scene.Scene;
import javafx.scene.image.Image;

public class MainMenu {
    
    private int selectedOption = 0;

    private final String[] options = { "Iniciar Jogo", "Opções", "Sair" };

    private  JavaFxDrawer drawer;
    private Image backgroundImage = null;

    public MainMenu(JavaFxDrawer drawer, Image backgroundImage){
        
    }
    // Actions for each menu option
    private Runnable onStartGame;
    private Runnable onOptions;
    private Runnable onExit;

    public void draw(){
        drawer.drawMainMenu(backgroundImage, "Menu Principal", selectedOption, options);
    }
    public void attachToScene(Scene scene) {
        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case UP:
                    selectedOption = (selectedOption - 1 + options.length) % options.length;
                    draw();
                    break;
                case DOWN:
                    selectedOption = (selectedOption + 1) % options.length;
                    draw();
                    break;
                case ENTER:
                    executeSelectedOption();
                    break;
                default:
                    break;
            }
        });
        
    }

    public void executeSelectedOption() {
        switch (selectedOption) {
            case 0:
                if (onStartGame != null) {
                    onStartGame.run();
                }
                break;
            case 1:
                if (onOptions != null) {
                    onOptions.run();
                }
                break;
            case 2:
                if (onExit != null) {
                    onExit.run();
                }
                break;
            default:
                break;
        }
    }
    public int getSelectedOption() {
        return selectedOption;
    }
    public void setSelectedOption(int selectedOption) {
        this.selectedOption = selectedOption;
    }
    public void setOnStartGame(Runnable onStartGame) {
        this.onStartGame = onStartGame;
    }
    public void setOnOptions(Runnable onOptions) {
        this.onOptions = onOptions;
    }
    public void setOnExit(Runnable onExit) {
        this.onExit = onExit;
    }
    public void reset() {
        selectedOption = 0;
    }



}