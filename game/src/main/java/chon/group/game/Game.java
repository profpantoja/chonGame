package chon.group.game;

import java.util.ArrayList;

import chon.group.game.core.agent.Agent;
import chon.group.game.core.weapon.Shot;
import chon.group.game.domain.environment.Environment;
import chon.group.game.drawer.EnvironmentDrawer;

public class Game {

    private Environment environment;
    private EnvironmentDrawer mediator;
    ArrayList<String> input;
    private boolean isPaused = false;

    public Game(Environment environment, EnvironmentDrawer mediator, ArrayList<String> input) {
        this.environment = environment;
        this.mediator = mediator;
        this.input = input;
    }

    public void loop() {

        //if (input.contains("P")) {
            ///isPaused = !isPaused;
            ///input.remove("P");
        //}

        mediator.clearEnvironment();
        /* Branching the Game Loop */
        /* If the agent died in the last loop */
        if (environment.getProtagonist().isDead()) {
            this.gameOver();
        } else {
            if (isPaused) {
                this.pause();
            } else {
                this.running();
            }
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
            environment.detectCollision();
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
    }

    public void pause() {
        mediator.renderGame();
        /** Rendering the Pause Screen */
        mediator.drawPauseScreen();
    }

    public void init() {

    }

    public void win() {

    }

}
