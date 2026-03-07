package chon.group.game.states;

import chon.group.game.Game;
import chon.group.game.menu.Action;

public class StartState implements GameState {

    @Override
    public void update(Game game) {
        game.getMediator().drawBackground();
        game.getMediator().drawMenu();
    }

    @Override
    public void handleInput(Game game) {
        game.getEnvironment().setCurrentMenu(game.getMenu().getStart());
        if (game.getEnvironment().getCurrentMenu().handleAction(game.getInput()).equals(Action.START)) {
            game.getEnvironment().loadNextLevel();
            game.getEnvironment().setCurrentMenu(game.getMenu().getPause());
            game.setCurrentState(new RunningState());
        }
    }
}
