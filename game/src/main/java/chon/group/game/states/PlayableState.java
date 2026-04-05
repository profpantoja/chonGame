package chon.group.game.states;

import java.util.Iterator;

import chon.group.game.Game;
import chon.group.game.core.agent.Agent;
import chon.group.game.core.agent.Object;
import chon.group.game.core.environment.Environment;
import chon.group.game.core.environment.Level;
import chon.group.game.core.weapon.Shot;
import chon.group.game.sound.SoundEvent;

public class PlayableState implements GameState {

    @Override
    public void handleInput(Game game) {
        /** ChonBota Only Moves if the Player Press Something */
        /** Update the protagonist's movements if game.getInput() exists */
        if (game.getInput().isEmpty()) {
            /* If nothing happens, the protagonist stays IDLE. */
            game.getEnvironment().getProtagonist().idle();
            return;
        }
        /**
         * If the player pressed the Pause buttom, the game moves to the pause state.
         */
        if (this.handlePause(game))
            return;
        /** The protagonist Shoots Somebody Who Outdrew You */
        /** But only if it has enough energy */
        if (this.handleAttack(game))
            return;
        this.handleMovement(game);
    }

    @Override
    public void update(Game game) {
        Environment environment = game.getEnvironment();
        Level currentLevel = environment.getCurrentLevel();
        Agent protagonist = environment.getProtagonist();

        switch (currentLevel.getType()) {
            case STORY:
                game.getMenu().getCurrentMenu()
                        .setTitle(currentLevel.getDescription());
                game.setCurrentState(new StoryState());
                break;
            default:
                break;
        }

        /* It checks if the protagonist is outside boundaries. */
        environment.checkBorders();
        /* ChonBot's Automatic Movements */
        /* Update the other agents' movements */
        Iterator<Agent> itAgent = currentLevel.getAgents().iterator();
        while (itAgent.hasNext()) {
            Agent agent = itAgent.next();
            if (agent.canRemove()) {
                itAgent.remove();
                break;
            }
            /* Every agent chases the protagonist. */
            agent.chase(protagonist.getPosX(),
                    protagonist.getPosY());
            /* It animates all agents. */
            game.getAnimator().animate(agent);
        }
        environment.update();
        /* If the agent died in this loop, the state changes. */
        if (protagonist.isDead()) {
            /* If the agent dies, the game moves to the Game Over state. */
            game.getMenu().setCurrentMenu(game.getMenu().getGameOver());
            game.setCurrentState(new GameOverState());
        }
        /* It animates the protagonist. */
        game.getAnimator().animate(protagonist);
        /* It animates all objects. */
        Iterator<Object> itObject = currentLevel.getObjects().iterator();
        while (itObject.hasNext()) {
            Object object = itObject.next();
            if (object.isDestroyed()) {
                if (object.canRemove()) {
                    itObject.remove();
                    break;
                }
            }
            game.getAnimator().animate(object);
        }
        /* It animates all shots. */
        for (Shot shot : currentLevel.getShots()) {
            game.getAnimator().animate(shot);
        }

        if (game.isGameCompleted()) {
            /* If the end of the game, the game moves to the Winning state. */
            game.getMenu().setCurrentMenu(game.getMenu().getWin());
            game.setCurrentState(new WinState());
        }

    }

    @Override
    public void render(Game game) {
        /* Render the game and agents */
        game.getMediator().renderGame();
    }

    private void updateStateTransition(Game game) {
        Environment environment = game.getEnvironment();
        Level currentLevel = environment.getCurrentLevel();
        Agent protagonist = environment.getProtagonist();
        if (protagonist.isDead()) {
            game.getMenu().setCurrentMenu(game.getMenu().getGameOver());
            game.setCurrentState(new GameOverState());
            return;
        }
        if (game.isGameCompleted()) {
            game.getMenu().setCurrentMenu(game.getMenu().getWin());
            game.setCurrentState(new WinState());
            return;
        }
        switch (currentLevel.getType()) {
            case STORY:
                game.getMenu().getCurrentMenu().setTitle(currentLevel.getDescription());
                game.setCurrentState(new StoryState());
                return;
            default:
                return;
        }
    }

    private boolean handlePause(Game game) {
        /**
         * If the player pressed the Pause buttom, the game moves to the pause state.
         */
        if (!game.getInput().contains("P")) {
            return false;
        }
        game.setCurrentState(new PauseState());
        game.getMenu().setCurrentMenu(game.getMenu().getPause());
        /* The Pause needs to be removed. Otherwise, it will stay forever paused. */
        game.getInput().remove("P");
        return true;
    }

    private boolean handleAttack(Game game) {
        /** The protagonist Shoots Somebody Who Outdrew You */
        /** But only if it has enough energy */
        if (!game.getInput().contains("SPACE")) {
            return false;
        }
        Shot shot = game.getEnvironment().getProtagonist().useWeapon();
        /* If there is an associate shot with the weapon. Some weapons don't shoot. */
        if (shot != null) {
            game.getEnvironment().getSounds()
                    .add(game.getEnvironment().getProtagonist().getSoundSet().get(SoundEvent.ATTACK));
            /* The shot is added to the environment's current level. */
            game.getEnvironment().getCurrentLevel().getShots().add(shot);
        }
        game.getInput().remove("SPACE");
        return true;
    }

    private void handleMovement(Game game) {
        /* If there is any movement key pressed. */
        if (game.getInput().contains("RIGHT") ||
                game.getInput().contains("LEFT") ||
                game.getInput().contains("DOWN") ||
                game.getInput().contains("UP")) {
            /* Protagonist's Moves based on Joystick inputs. */
            game.getEnvironment().getProtagonist().move(
                    game.getDirections(game.getInput()));
        }
    }

}
