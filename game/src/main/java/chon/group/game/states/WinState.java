package chon.group.game.states;

import chon.group.game.Game;
import chon.group.game.menu.Action;

public class WinState implements GameState {

    @Override
    public void handleInput(Game game) {
        /* It gets which action the player has chosen in the menu. */
        Action action = game.getMenu().getCurrentMenu().handleAction(game.getInput());
        if (action.equals(Action.RESET)) {
            /* The Game is reset to the Start State. */
            game.reset();
            game.setCurrentState(new StartState());
            game.getMenu().setCurrentMenu(game.getMenu().getStart());
        }
    }

    @Override
    public void update(Game game) {
        /* It animates the protagonist. */
        game.getAnimator().animate(game.getEnvironment().getProtagonist());
    }

    @Override
    public void render(Game game) {
        /*
         * It needs to render the game, because there is no customized winning screen.
         */
        game.getMediator().renderGame();
        /** Rendering The End Screen and menu. */
        game.getMediator().drawTheEndScreen();
        game.getMediator().drawMenu();
    }

}
