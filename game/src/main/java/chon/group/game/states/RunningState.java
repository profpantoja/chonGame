package chon.group.game.states;

import chon.group.game.Game;
import chon.group.game.core.agent.Agent;
import chon.group.game.core.weapon.Shot;

public class RunningState implements GameState {

    @Override
    public void update(Game game) {
        /** ChonBota Only Moves if the Player Press Something */
        /** Update the protagonist's movements if game.getInput() exists */
        if (!game.getInput().isEmpty()) {
            /** ChonBota Shoots Somebody Who Outdrew You */
            /** But only if she has enough energy */
            if (game.getInput().contains("SPACE")) {
                game.getInput().remove("SPACE");
                Shot shot = game.getEnvironment().getProtagonist().useWeapon();
                if (shot != null)
                    game.getEnvironment().getCurrentLevel().getShots().add(shot);
            }
            /* ChonBota's Movements */
            game.getEnvironment().getProtagonist().move(game.getDirections(game.getInput()));
            game.getEnvironment().checkBorders();
        }
        /* ChonBot's Automatic Movements */
        /* Update the other agents' movements */
        for (Agent agent : game.getEnvironment().getCurrentLevel().getAgents()) {
            agent.chase(game.getEnvironment().getProtagonist().getPosX(),
                    game.getEnvironment().getProtagonist().getPosY());
        }
        /* ChonBot's Automatic Movements */
        /* Update the other agents' movements */
        for (Agent agent : game.getEnvironment().getCurrentLevel().getAgents()) {
            agent.chase(game.getEnvironment().getProtagonist().getPosX(),
                    game.getEnvironment().getProtagonist().getPosY());
        }
        /* Render the game game.getEnvironment() and agents */
        game.getEnvironment().update();
        game.getMediator().renderGame();
        /* If the agent died in this loop */
        if (game.getEnvironment().getProtagonist().isDead()) {
            game.getEnvironment().setCurrentMenu(game.getMenu().getGameOver());
            game.setCurrentState(new GameOverState());
        }
    }
}
