package chon.group.game.states;

import chon.group.game.Game;
import chon.group.game.menu.Action;

public class GameOverState implements GameState {

    @Override
    public void handleInput(Game game) {
        /* It gets which action the player has chosen in the menu. */
        Action action = game.getEnvironment().getCurrentMenu().handleAction(game.getInput());
        if (action.equals(Action.RESET)) {
            /* The Game is reset to the Start State. */
            game.reset();
            game.setCurrentState(new StartState());
            game.getEnvironment().setCurrentMenu(game.getMenu().getStart());
        } else if (action.equals(Action.CONTINUE)) {
            /* The same level is restarted. */
            game.resetLevel();
            game.setCurrentState(new RunningState());
        }
    }

    @Override
    public void update(Game game) {
        /* Although the game has ended, messages and shots keep flowing. */
        game.getEnvironment().updateMessages();
        game.getEnvironment().updateShots();
        /* It animates the protagonist. */
        game.getAnimator().update(game.getEnvironment().getProtagonist());
    }

    @Override
    public void render(Game game) {
        /*
         * It needs to reder the game, because there is no customized game over screen.
         */
        game.getMediator().renderGame();
        /** Rendering the Game Over Screen and menu. */
        game.getMediator().drawGameOver();
        game.getMediator().drawMenu();
    }

}
