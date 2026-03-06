package chon.group.game.states;

import chon.group.game.Game;
import chon.group.game.GameStatus;
import chon.group.game.menu.Action;

public class StartState implements GameState {

    @Override
    public void update(Game game) {
        game.getEnvironment().setCurrentMenu(game.getMenu().getStart());
        game.getMediator().drawBackground();
        game.getMediator().drawMenu();
        if (game.getEnvironment().getCurrentMenu().handleInput(game.getInput()).equals(Action.START)) {
            game.getEnvironment().loadNextLevel();
            game.getEnvironment().setCurrentMenu(game.getMenu().getPause());
            game.setStatus(GameStatus.RUNNING);
            game.setCurrentState(new RunningState());
        }
    }
}
