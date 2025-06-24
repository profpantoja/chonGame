package chon.group.game.domain.environment;

import chon.group.game.drawer.JavaFxDrawer;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;

public class MainMenu {
    
    private int selectedOption = 0;
    private final String[] options = { "Start Game", "Exit" };
    private JavaFxDrawer drawer; 
    private Image backgroundImage; 

    // Actions for each menu option
    private Runnable onStartGame;
    private Runnable onExit;

    public MainMenu(JavaFxDrawer drawer, Image backgroundImage) {
        this.drawer = drawer;
        this.backgroundImage = backgroundImage;
    }

    // Draws the main menu on the screen 
    public void draw() {
        drawer.drawMainMenu(backgroundImage, "Chon Game", selectedOption, options);
    }

    // Handles input for navigating the menu
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

    // Executes the action associated with the selected menu option
    public void executeSelectedOption() {
        switch (selectedOption) {
            case 0: 
                if (onStartGame != null) 
                    onStartGame.run(); 
                break;
            case 1: 
                if (onExit != null)
                    onExit.run();  
                break;
        }
    }
    

    // Setters for the actions to be executed when an option is selected
    
    public void setOnStartGame(Runnable onStartGame) { 
        this.onStartGame = onStartGame; 
    }
    public void setOnExit(Runnable onExit){ 
        this.onExit = onExit;    
    }
    public void reset(){    
        this.selectedOption = 0; 
    }
}