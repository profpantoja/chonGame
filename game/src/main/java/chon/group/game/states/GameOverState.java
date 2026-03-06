package chon.group.game.states;

import chon.group.game.Game;
import chon.group.game.menu.Action;

public class GameOverState implements GameState {

    @Override
    public void update(Game game) {
        game.getEnvironment().updateMessages();
        game.getEnvironment().updateShots();
        game.getMediator().renderGame();
        /** Rendering the Game Over Screen */
        game.getMediator().drawGameOver();
        game.getMediator().drawMenu();
        Action action = game.getEnvironment().getCurrentMenu().handleInput(game.getInput());
        if (action.equals(Action.RESET)) {
            game.reset();
            game.setCurrentState(new StartState());
        } else if (action.equals(Action.CONTINUE)) {
            game.resetLevel();
            game.setCurrentState(new RunningState());
        }
    }

}
