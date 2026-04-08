package chon.group.game.states;

import chon.group.game.Game;
import chon.group.game.drawer.service.GameDrawer;
import chon.group.game.menu.Action;

public class PauseState implements GameState {

    @Override
    public void handleInput(Game game) {
        if (this.handlePause(game))
            return;
        /* It gets which action the player has chosen in the menu. */
        Action action = game.getMenu().getCurrentMenu().handleAction(game.getInput());
        this.handleMenuAction(game, action);
    }

    @Override
    public void update(Game game) {
        /* Even if the game is paused, the messages keep flowing in the air. */
        game.getEnvironment().getMessenger().update();
    }

    @Override
    public void render(Game game) {
        GameDrawer mediator = game.getMediator();
        /* It needs to render the game, because there is no customized pause screen. */
        mediator.renderGame();
        /** Rendering the Pause Screen and menu. */
        mediator.drawPauseScreen();
        mediator.drawMenu();
    }

    private boolean handlePause(Game game) {
        /*
         * If the player press Pause, the game returns to the Running State (since it is
         * at Pause State).
         */
        if (game.getInput().contains("P")) {
            /* The Pause needs to be removed. Otherwise, it will stay forever paused. */
            game.getInput().remove("P");
            game.setCurrentState(new PlayableState());
            return true;
        }
        return false;
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
            case DEBUG:
                game.getEnvironment().setDebugMode(!game.getEnvironment().isDebugMode());
                break;
            case VOLUME:
                if (game.getInput().contains("LEFT"))
                    game.getSoundPlayer().decreaseVolume();
                if (game.getInput().contains("RIGHT"))
                    game.getSoundPlayer().increaseVolume();
                game.getInput().clear();
                break;
            case RESET:
                /* The Game is reset to the Start State. */
                game.reset();
                game.setCurrentState(new StartState());
                game.getMenu().openStart();
                break;
            case CONTINUE:
                /* The game returns to the Running State. */
                game.setCurrentState(new PlayableState());
                break;
            default:
                break;
        }
    }

}
