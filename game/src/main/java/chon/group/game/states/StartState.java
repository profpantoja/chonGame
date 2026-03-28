package chon.group.game.states;

import chon.group.game.Game;
import chon.group.game.menu.Action;

public class StartState implements GameState {

    @Override
    public void handleInput(Game game) {
        /* If the player has selected an option, an action is executed. */
        if (game.getMenu().getCurrentMenu().handleAction(game.getInput()).equals(Action.START)) {
            game.getSoundPlayer().stop();
            /* It moves to next level. In this version, the Start State is the level 0. */
            game.getEnvironment().loadNextLevel();
            game.setCurrentState(new RunningState());
        }
    }

    @Override
    public void update(Game game) {
        /* Nothing happens when the game starts. Only the initial menu is available. */
    }

    @Override
    public void render(Game game) {
        /* The Background and Menus are redered. */
        game.getMediator().drawBackground();
        game.getMediator().drawMenu();
    }
}
