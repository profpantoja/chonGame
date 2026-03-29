package chon.group.game.states;

import chon.group.game.Game;
import chon.group.game.menu.Action;
import chon.group.game.menu.Item;

public class StartState implements GameState {

    @Override
    public void handleInput(Game game) {
        Action action = game.getMenu().getCurrentMenu().handleAction(game.getInput());
        if (action.equals(Action.ENTER)) {
            Item item = game.getMenu().getCurrentMenu().getSelectedItem();
            game.getMenu().push(game.getMenu().getCurrentMenu());
            game.getMenu().setCurrentMenu(item.getSubMenu());
        }
        if (action.equals(Action.POP)) {
            game.getMenu().pop();
        }
        /* If the player has selected an option, an action is executed. */
        if (action.equals(Action.START)) {
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
