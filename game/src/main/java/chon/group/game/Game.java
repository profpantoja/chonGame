package chon.group.game;

import java.util.ArrayList;
import chon.group.game.core.environment.Environment;
import chon.group.game.drawer.EnvironmentDrawer;
import chon.group.game.drawer.JavaFxMediator;
import chon.group.game.ui.Menu;
import javafx.application.Platform;

public class Game {
    // ... (atributos continuam os mesmos)
    private Environment environment;
    private EnvironmentDrawer drawer;
    private ArrayList<String> input;
    private GameStatus currentStatus;
    private Menu mainMenu;
    private boolean enterReleased = true;
    private boolean upDownReleased = true;
    private GameSet gameSet; 

    public Game(GameSet gameSet, EnvironmentDrawer drawer, ArrayList<String> input) {
        this.gameSet = gameSet;
        this.environment = gameSet.getEnvironment();
        this.drawer = drawer;
        this.input = input;
        this.mainMenu = gameSet.getMainMenu();
        this.currentStatus = GameStatus.START;
    }

    public void loop() {
        update();
        render();
    }

    // MÉTODO handleMenuInput MODIFICADO
    private void handleMenuInput() {
        if (input.contains("UP") && upDownReleased) {
            mainMenu.selectPreviousOption();
            upDownReleased = false;
        } else if (input.contains("DOWN") && upDownReleased) {
            mainMenu.selectNextOption();
            upDownReleased = false;
        } else if (!input.contains("UP") && !input.contains("DOWN")) {
            upDownReleased = true;
        }

        if (input.contains("ENTER") && enterReleased) {
            String selectedOption = mainMenu.getSelectedOption();
            switch (selectedOption) {
                case "New Game":
                    resetGame();
                    currentStatus = GameStatus.RUNNING;
                    break;
                case "Continue": // NOVA OPÇÃO ADICIONADA
                    // Por enquanto, "Continue" funciona como "New Game"
                    resetGame();
                    currentStatus = GameStatus.RUNNING;
                    break;
                case "Exit":
                    Platform.exit();
                    break;
            }
            enterReleased = false;
        } else if (!input.contains("ENTER")) {
            enterReleased = true;
        }
    }

    // --- O RESTO DA CLASSE CONTINUA IGUAL ---
    
    private void update() {
        switch (currentStatus) {
            case START: handleMenuInput(); break;
            case RUNNING:
                if (environment.getProtagonist() != null) { environment.getProtagonist().update(input); }
                environment.update();
                if (input.contains("P") && enterReleased) { currentStatus = GameStatus.PAUSED; enterReleased = false; } else if (!input.contains("P")) { enterReleased = true; }
                if (environment.getProtagonist() != null && environment.getProtagonist().getHealth() <= 0) { currentStatus = GameStatus.GAME_OVER; }
                break;
            case PAUSED:
                if (input.contains("P") && enterReleased) { currentStatus = GameStatus.RUNNING; enterReleased = false; } else if (!input.contains("P")) { enterReleased = true; }
                break;
            case WIN:
            case GAME_OVER:
                if (input.contains("ENTER") && enterReleased) { currentStatus = GameStatus.START; enterReleased = false; resetGame(); } else if (!input.contains("ENTER")) { enterReleased = true; }
                break;
        }
    }

    private void render() {
        drawer.clearEnvironment();
        switch (currentStatus) {
            case START: drawer.drawMainMenu(mainMenu); break;
            case RUNNING: drawer.renderGame(); break;
            case PAUSED: drawer.renderGame(); drawer.drawPauseScreen(); break;
            case WIN: drawer.drawWinScreen(); break;
            case GAME_OVER: drawer.drawGameOver(); break;
        }
    }

    private void resetGame() {
        GameSet newGameSet = new GameSet();
        this.gameSet = newGameSet;
        this.environment = newGameSet.getEnvironment();
        this.mainMenu = newGameSet.getMainMenu();
        if (drawer instanceof JavaFxMediator) { ((JavaFxMediator) drawer).setEnvironment(this.environment); ((JavaFxMediator) drawer).setMenu(this.mainMenu); }
    }
}