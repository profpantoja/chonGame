package chon.group.game.states;

import chon.group.game.Game;
import chon.group.game.menu.Action;

public class StoryState implements GameState {

    @Override
    public void handleInput(Game game) {
        Action action = game.getMenu().getCurrentMenu().handleAction(game.getInput());
        switch (action) {
            case SKIP:
                break;
            /* If the player has selected an option, an action is executed. */
            case START:
                game.getSoundPlayer().stop();
                /* It moves to next level. In this version, the Start State is the level 0. */
                game.getEnvironment().loadNextLevel();
                switch (game.getEnvironment().getCurrentLevel().getType()) {
                    case PLAYABLE:
                        game.setCurrentState(new PlayableState());
                        break;
                    case STORY:
                        game.setCurrentState(new StoryState());
                        break;
                    default:
                        break;
                }
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
