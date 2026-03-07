package chon.group.game.states;

import chon.group.game.Game;
import chon.group.game.menu.Action;

public class PauseState implements GameState {

    @Override
    public void update(Game game) {
        game.getEnvironment().updateMessages();
        game.getMediator().renderGame();
        /** Rendering the Pause Screen */
        game.getMediator().drawPauseScreen();
        game.getMediator().drawMenu();
        Action action = game.getEnvironment().getCurrentMenu().handleInput(game.getInput());
        if (action.equals(Action.RESET)) {
            game.reset();
            game.setCurrentState(new StartState());
        } else if (action.equals(Action.CONTINUE)) {
            game.getInput().remove("P");
            game.setCurrentState(new RunningState());
        }
    }
}
