package chon.group.game;

import java.util.ArrayList;

import chon.group.game.core.agent.Agent;
import chon.group.game.core.agent.Object;
import chon.group.game.core.environment.Environment;
import chon.group.game.core.weapon.Shot;
import chon.group.game.drawer.EnvironmentDrawer;

public class Game {

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

    public void loop() {
        this.updateControls();
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
                    environment.getCurrentLevel().getShots().add(shot);
            }
            /* ChonBota's Movements */
            environment.getProtagonist().move(input);
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
        /* Update the collisions between agents and shots */
        for (Object object : environment.getCurrentLevel().getObjects()) {
            for (Agent agent : environment.getCurrentLevel().getAgents()) {
                object.onCollide(agent, environment.getMessages());
            }
            for (Shot shot : environment.getCurrentLevel().getShots()) {
                object.onCollide(shot, environment.getMessages());
            }
            object.onCollide(environment.getProtagonist(), environment.getMessages());
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

}
