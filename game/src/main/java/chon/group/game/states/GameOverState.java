package chon.group.game.states;

import chon.group.game.Game;
import chon.group.game.core.environment.Environment;
import chon.group.game.menu.Action;

public class GameOverState implements GameState {

    @Override
    public void handleInput(Game game) {
        /* It gets which action the player has chosen in the menu. */
        Action action = game.getMenu().getCurrentMenu().handleAction(game.getInput());
        if (action.equals(Action.RESET)) {
            /* The Game is reset to the Start State. */
            game.reset();
            game.setCurrentState(new StartState());
            game.getMenu().openStart();
        } else if (action.equals(Action.CONTINUE)) {
            /* The same level is restarted. */
            game.resetLevel();
            game.setCurrentState(new PlayableState());
        }
    }

    @Override
    public void update(Game game) {
        Environment environment = game.getEnvironment();
        /* Although the game has ended, messages and shots keep flowing. */
        environment.getMessenger().update();
        environment.updateShots();
        /* It animates the level. */
        game.getAnimator().animateLevel(
                environment.getCurrentLevel(),
                environment.getProtagonist());
    }

    @Override
    public void render(Game game) {
        /*
         * It needs to render the game, because there is no customized game over screen.
         */
        game.getMediator().renderGame();
        /** Rendering the Game Over Screen and menu. */
        game.getMediator().drawGameOver();
        game.getMediator().drawMenu();
    }

}
