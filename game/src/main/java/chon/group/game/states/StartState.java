package chon.group.game.states;

import chon.group.game.Game;
import chon.group.game.menu.Action;
import chon.group.game.menu.Item;

public class StartState implements GameState {

    @Override
    public void handleInput(Game game) {
        Action action = game.getMenu().getCurrentMenu().handleAction(game.getInput());
        switch (action) {
            case ENTER:
                Item item = game.getMenu().getCurrentMenu().getSelectedItem();
                game.getMenu().push(game.getMenu().getCurrentMenu());
                game.getMenu().setCurrentMenu(item.getSubMenu());
                break;
            case POP:
                game.getMenu().pop();
                break;
            /* If the player has selected an option, an action is executed. */
            case START:
                game.getSoundPlayer().stop();
                /* It moves to next level. In this version, the Start State is the level 0. */
                game.getEnvironment().loadNextLevel();
                game.setCurrentState(new RunningState());
                break;
            case VOLUME:
                if (game.getInput().contains("LEFT"))
                    game.getSoundPlayer().decreaseVolume();
                if (game.getInput().contains("RIGHT"))
                    game.getSoundPlayer().increaseVolume();
                game.getInput().clear();
                break;
            default:
                break;
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
