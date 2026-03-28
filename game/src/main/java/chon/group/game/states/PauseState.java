package chon.group.game.states;

import chon.group.game.Game;
import chon.group.game.menu.Action;
import chon.group.game.menu.Item;

public class PauseState implements GameState {

    @Override
    public void handleInput(Game game) {
        /*
         * If the player press Pause, the game returns to the Running State (since it is
         * at Pause State).
         */
        if (game.getInput().contains("P")) {
            /* The Pause needs to be removed. Otherwise, it will stay forever paused. */
            game.getInput().remove("P");
            game.setCurrentState(new RunningState());
        }
        /* It gets which action the player has chosen in the menu. */
        Action action = game.getMenu().getCurrentMenu().handleAction(game.getInput());
        if (action.equals(Action.DEBUG))
            game.getEnvironment().setDebugMode(!game.getEnvironment().isDebugMode());
        if (action.equals(Action.ENTER)) {
            Item item = game.getMenu().getCurrentMenu().getSelectedItem();
            game.getMenu().push(game.getMenu().getCurrentMenu());
            game.getMenu().setCurrentMenu(item.getSubMenu());
        }
        if (action.equals(Action.POP)) {
            game.getMenu().pop();
        }
        if (action.equals(Action.RESET)) {
            /* The Game is reset to the Start State. */
            game.reset();
            game.setCurrentState(new StartState());
            game.getMenu().setCurrentMenu(game.getMenu().getStart());
        } else if (action.equals(Action.CONTINUE)) {
            /* The game returns to the Running State. */
            game.setCurrentState(new RunningState());
        }
    }

    @Override
    public void update(Game game) {
        /* Even if the game is paused, the messages keep flowing in the air. */
        game.getEnvironment().updateMessages();
    }

    @Override
    public void render(Game game) {
        /* It needs to reder the game, because there is no customized pause screen. */
        game.getMediator().renderGame();
        /** Rendering the Pause Screen and menu. */
        game.getMediator().drawPauseScreen();
        game.getMediator().drawMenu();
    }

}
