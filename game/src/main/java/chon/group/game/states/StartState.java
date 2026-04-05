package chon.group.game.states;

import chon.group.game.Game;
import chon.group.game.menu.Action;

public class StartState implements GameState {

    @Override
    public void handleInput(Game game) {
        Action action = game.getMenu().getCurrentMenu().handleAction(game.getInput());
        this.handleMenuAction(game, action);
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

    private void handleMenuAction(Game game, Action action) {
        switch (action) {
            case ENTER:
                game.getMenu().openSubMenu();
                break;
            case POP:
                game.getMenu().pop();
                break;
            /* If the player has selected an option, an action is executed. */
            case START:
                this.start(game);
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

    private void start(Game game) {
        game.getSoundPlayer().stop();
        /* It moves to next level. In this version, the Start State is the level 0. */
        game.getEnvironment().loadNextLevel();
        switch (game.getEnvironment().getCurrentLevel().getType()) {
            case PLAYABLE:
                game.setCurrentState(new PlayableState());
                break;
            case STORY:
                game.setCurrentState(new StoryState());
                game.getMenu().openSkip();
                game.getMenu().getCurrentMenu()
                        .setTitle(game.getEnvironment().getCurrentLevel().getDescription());
                break;
            default:
                break;
        }
    }

}
