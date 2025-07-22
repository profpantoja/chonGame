package chon.group.game;

import java.util.ArrayList;

import chon.group.game.core.agent.Agent;
import chon.group.game.core.environment.Environment;
import chon.group.game.core.weapon.Shot;
import chon.group.game.drawer.EnvironmentDrawer;
import chon.group.game.menu.Menu;
import chon.group.game.menu.MenuOption;
import chon.group.game.menu.MenuPause;
import javafx.scene.input.KeyEvent;

public class Game {
    private Menu mainMenu;
    private MenuPause menuPause;
    private Environment environment;
    private EnvironmentDrawer mediator;
    private ArrayList<String> input;
    private GameStatus status = GameStatus.START;
    private boolean debugMode = true;

    public Game(Environment environment, EnvironmentDrawer mediator, ArrayList<String> input) {
        this.environment = environment;
        this.mediator = mediator;
        this.input = input;
    }

    public Environment getEnvironment() {
        return environment;
    }

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    public EnvironmentDrawer getMediator() {
        return mediator;
    }

    public void setMediator(EnvironmentDrawer mediator) {
        this.mediator = mediator;
    }

    public ArrayList<String> getInput() {
        return input;
    }

    public void setInput(ArrayList<String> input) {
        this.input = input;
    }

    public GameStatus getStatus() {
        return status;
    }

    public void setStatus(GameStatus status) {
        this.status = status;
    }

    public boolean isDebugMode() {
        return debugMode;
    }

    public void setDebugMode(boolean debugMode) {
        this.debugMode = debugMode;
    }

    public Menu getMainMenu() { 
        return mainMenu;
    }

    public void setMainMenu(Menu mainMenu) {
        this.mainMenu = mainMenu;
    }

    public MenuPause getMenuPause() { 
        return menuPause;
    }

    public void setMenuPause(MenuPause menuPause) {
        this.menuPause = menuPause;
    }

    public void loop() {
        this.updateControls();
        switch (this.status) {
            case START:
                mediator.drawMainMenu(mainMenu);
                break;
            case RUNNING:
                this.running();
                break;
            case PAUSED:
                this.pause();
                mediator.drawPauseMenu(menuPause);
                break;
            case WIN:
                this.win();
                break;
            case GAME_OVER:
                this.gameOver();
                break;
        }
    }

    public void gameOver() {
        environment.updateMessages();
        environment.updateShots();
        mediator.renderGame();
        /** Rendering the Game Over Screen */
        mediator.drawGameOver();
    }

    public void running() {
        /** ChonBota Only Moves if the Player Press Something */
        /** Update the protagonist's movements if input exists */
        if (!input.isEmpty()) {
            /** ChonBota Shoots Somebody Who Outdrew You */
            /** But only if she has enough energy */
            if (input.contains("SPACE")) {
                input.remove("SPACE");
                Shot shot = environment.getProtagonist().useWeapon();
                if (shot != null)
                    environment.getShots().add(shot);
            }
            /* ChonBota's Movements */
            environment.getProtagonist().move(input);
            environment.checkBorders();
        }
        /* ChonBot's Automatic Movements */
        /* Update the other agents' movements */
        for (Agent agent : environment.getAgents()) {
            agent.chase(environment.getProtagonist().getPosX(),
                    environment.getProtagonist().getPosY());
        }
        /* ChonBot's Automatic Movements */
        /* Update the other agents' movements */
        for (Agent agent : environment.getAgents()) {
            agent.chase(environment.getProtagonist().getPosX(),
                    environment.getProtagonist().getPosY());
        }
        /* Render the game environment and agents */
        environment.update();
        mediator.renderGame();
        /* If the agent died in this loop */
        if (environment.getProtagonist().isDead())
            this.status = GameStatus.GAME_OVER;
    }

    public void pause() {
        this.environment.updateMessages();
        mediator.renderGame();
        /** Rendering the Pause Screen */
        mediator.drawPauseScreen();
    }

    public void init() {
        this.status = GameStatus.RUNNING;
        mediator.renderGame();
    }

    public void win() {
        this.status = GameStatus.START;
        mediator.renderGame();
    }

    private void updateControls() {
        if (this.input.contains("P")) {
            if (this.status.equals(GameStatus.RUNNING)) {
                this.status = GameStatus.PAUSED;
            } else {
                if (this.status.equals(GameStatus.PAUSED))
                    this.status = GameStatus.RUNNING;
            }
            this.input.remove("P");
        }
    }

    public void handleInput(KeyEvent e) {
        switch (status) {
            case START:
                MenuOption.Main mainOpt = mainMenu.handleInput(e.getCode());
                if (mainOpt == MenuOption.Main.START_GAME) {
                    // reinicialização, etc.
                    setStatus(GameStatus.RUNNING);
                    mainMenu.reset();
                } else if (mainOpt == MenuOption.Main.EXIT) {
                    javafx.application.Platform.exit();
                }
                break;
            case PAUSED:
                MenuOption.Pause pauseOpt = menuPause.handleInput(e.getCode());
                if (pauseOpt == MenuOption.Pause.RESUME) {
                    setStatus(GameStatus.RUNNING);
                    menuPause.reset();
                } else if (pauseOpt == MenuOption.Pause.GO_BACK_TO_MENU) {
                    setStatus(GameStatus.START);
                    menuPause.reset();
                    mainMenu.reset();
                }
                break;
            case RUNNING:
                if (e.getCode().toString().equals("P")) {
                    setStatus(GameStatus.PAUSED);
                    menuPause.reset();
                } else if (!input.contains(e.getCode().toString())) {
                    input.add(e.getCode().toString());
                }
                break;
            default: break;
        }
    }

    public void handleKeyReleased(KeyEvent e) {
        String code = e.getCode().toString();
        if (!code.equals("P")) {
            input.remove(code);
        }
    }

}
