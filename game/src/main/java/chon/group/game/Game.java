package chon.group.game;

import java.util.ArrayList;
import java.util.List;

import chon.group.game.core.agent.Agent;
import chon.group.game.core.agent.Direction;
import chon.group.game.core.environment.Environment;
import chon.group.game.core.environment.Level;
import chon.group.game.core.weapon.Shot;
import chon.group.game.drawer.EnvironmentDrawer;
import chon.group.game.menu.Action;
import chon.group.game.menu.MenuHandler;
import chon.group.game.states.GameState;
import chon.group.game.states.PauseState;
import chon.group.game.states.RunningState;
import chon.group.game.states.StartState;

public class Game {

    private Environment environment;
    private EnvironmentDrawer mediator;
    private MenuHandler menu;
    private ArrayList<String> input;
    private GameStatus status = GameStatus.START;
    private GameState currentState = new StartState();
    private boolean debugMode = true;

    public Game(Environment environment, EnvironmentDrawer mediator, MenuHandler menu, ArrayList<String> input) {
        this.environment = environment;
        this.mediator = mediator;
        this.menu = menu;
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

    public MenuHandler getMenu() {
        return menu;
    }

    public void setMenu(MenuHandler menu) {
        this.menu = menu;
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

    public GameState getCurrentState() {
        return currentState;
    }

    public void setCurrentState(GameState currentState) {
        this.currentState = currentState;
    }

    public boolean isDebugMode() {
        return debugMode;
    }

    public void setDebugMode(boolean debugMode) {
        this.debugMode = debugMode;
    }

    public void loop() {
        this.updateControls();
        this.currentState.update(this);
    }

    public void oldLoop() {
        this.updateControls();
        // It needs to change the pattern considering the status.
        // In this case, I have 5 states: I'm not considering player selection and skip
        // states so far.
        // I need a class for each case bellow.
        switch (this.status) {
            case START:
                this.init();
                break;
            case RUNNING:
                this.running();
                break;
            case PAUSED:
                this.pause();
                break;
            case WIN:
                this.win();
                break;
            case GAME_OVER:
                this.gameOver();
                break;
        }
    }

    public void init() {
        this.environment.setCurrentMenu(menu.getStart());
        mediator.drawBackground();
        mediator.drawMenu();
        if (this.environment.getCurrentMenu().handleInput(input).equals(Action.START)) {
            this.environment.loadNextLevel();
            this.environment.setCurrentMenu(this.menu.getPause());
            this.status = GameStatus.RUNNING;
        }
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
                    environment.getCurrentLevel().getShots().add(shot);
            }
            /* ChonBota's Movements */
            environment.getProtagonist().move(this.getDirections(input));
            environment.checkBorders();
        }
        /* ChonBot's Automatic Movements */
        /* Update the other agents' movements */
        for (Agent agent : environment.getCurrentLevel().getAgents()) {
            agent.chase(environment.getProtagonist().getPosX(),
                    environment.getProtagonist().getPosY());
        }
        /* ChonBot's Automatic Movements */
        /* Update the other agents' movements */
        for (Agent agent : environment.getCurrentLevel().getAgents()) {
            agent.chase(environment.getProtagonist().getPosX(),
                    environment.getProtagonist().getPosY());
        }
        /* Render the game environment and agents */
        environment.update();
        mediator.renderGame();
        /* If the agent died in this loop */
        if (environment.getProtagonist().isDead()) {
            this.environment.setCurrentMenu(this.menu.getGameOver());
            this.status = GameStatus.GAME_OVER;
        }
    }

    public void pause() {
        this.environment.updateMessages();
        mediator.renderGame();
        /** Rendering the Pause Screen */
        mediator.drawPauseScreen();
        mediator.drawMenu();
        Action action = this.environment.getCurrentMenu().handleInput(input);
        if (action.equals(Action.RESET))
            this.reset();
        else if (action.equals(Action.CONTINUE)) {
            this.input.remove("P");
            this.status = GameStatus.RUNNING;
        }
    }

    public void win() {
        this.status = GameStatus.START;
        mediator.renderGame();
    }

    public void gameOver() {
        environment.updateMessages();
        environment.updateShots();
        mediator.renderGame();
        /** Rendering the Game Over Screen */
        mediator.drawGameOver();
        mediator.drawMenu();
        Action action = this.environment.getCurrentMenu().handleInput(input);
        if (action.equals(Action.RESET))
            this.reset();
        else if (action.equals(Action.CONTINUE))
            this.resetLevel();
    }

    public void reset() {
        this.environment = new GameSet().getEnvironment();
        this.mediator.setEnvironment(this.environment);
        this.input.clear();
        this.status = GameStatus.START;
    }

    public void resetLevel() {
        GameSet gameSet = new GameSet();
        int myLevelIndex = this.environment.getLevels().indexOf(this.environment.getCurrentLevel());
        Level newLevel = gameSet.getEnvironment().getLevels().get(myLevelIndex);
        this.environment.getLevels().set(myLevelIndex, newLevel);
        this.environment.setCurrentLevel(newLevel);
        this.environment.setCurrentMenu(this.menu.getPause());
        this.environment.setProtagonist(gameSet.getEnvironment().getProtagonist());
        this.environment.getCamera().setPosX(0);
        this.environment.getCamera().setLevelWidth(newLevel.getWidth());
        this.environment.setCollectedCount(0);
        this.environment.setScore(0);
        this.input.clear();
        this.status = GameStatus.RUNNING;
    }

    private void updateControls() {
        if (this.input.contains("P")) {
            if (this.status.equals(GameStatus.RUNNING)) {
                this.status = GameStatus.PAUSED;
                this.currentState = new PauseState();
            } else {
                if (this.status.equals(GameStatus.PAUSED))
                    this.status = GameStatus.RUNNING;
                this.currentState = new RunningState();
            }
            this.input.remove("P");
        }
    }

    public List<Direction> getDirections(List<String> input) {
        ArrayList<Direction> directions = new ArrayList<Direction>();
        for (String command : input) {
            switch (command) {
                case "RIGHT":
                    directions.add(Direction.RIGHT);
                    break;
                case "LEFT":
                    directions.add(Direction.LEFT);
                    break;
                case "DOWN":
                    directions.add(Direction.DOWN);
                    break;
                case "UP":
                    directions.add(Direction.UP);
                    break;
            }
        }
        return directions;
    }

}
