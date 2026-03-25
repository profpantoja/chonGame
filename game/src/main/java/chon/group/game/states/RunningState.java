package chon.group.game.states;

import java.util.Iterator;

import chon.group.game.Game;
import chon.group.game.core.agent.Agent;
import chon.group.game.core.agent.Object;
import chon.group.game.core.weapon.Shot;
import chon.group.game.sound.SoundEvent;

public class RunningState implements GameState {

    @Override
    public void handleInput(Game game) {
        /** ChonBota Only Moves if the Player Press Something */
        /** Update the protagonist's movements if game.getInput() exists */
        if (!game.getInput().isEmpty()) {
            /**
             * If the player pressed the Pause buttom, the game moves to the pause state.
             */
            if (game.getInput().contains("P")) {
                game.setCurrentState(new PauseState());
                game.getEnvironment().setCurrentMenu(game.getMenu().getPause());
                /* The Pause needs to be removed. Otherwise, it will stay forever paused. */
                game.getInput().remove("P");
            } else {
                /** The protagonist Shoots Somebody Who Outdrew You */
                /** But only if it has enough energy */
                if (game.getInput().contains("SPACE")) {
                    Shot shot = game.getEnvironment().getProtagonist().useWeapon();
                    /* If there is an associate shot with the weapon. Some weapons don't shoot. */
                    if (shot != null) {
                        game.getSoundPlayer()
                                .play(game.getEnvironment().getProtagonist().getSoundSet().get(SoundEvent.ATTACK));
                        /* The shot is added to the environment's current level. */
                        game.getEnvironment().getCurrentLevel().getShots().add(shot);
                    }
                } else {
                    /* If there is any movement key pressed. */
                    if (game.getInput().contains("RIGHT") ||
                            game.getInput().contains("LEFT") ||
                            game.getInput().contains("DOWN") ||
                            game.getInput().contains("UP")) {
                        /* Protagonist's Moves based on Joystick inputs. */
                        game.getEnvironment().getProtagonist().move(game.getDirections(game.getInput()));
                    }
                }
            }
        } else
            /* If nothing happens, the protagonist stays IDLE. */
            game.getEnvironment().getProtagonist().idle();
    }

    @Override
    public void update(Game game) {
        /* It checks if the protagonist is outside boundaries. */
        game.getEnvironment().checkBorders();
        /* ChonBot's Automatic Movements */
        /* Update the other agents' movements */
        Iterator<Agent> itAgent = game.getEnvironment().getCurrentLevel().getAgents().iterator();
        while (itAgent.hasNext()) {
            Agent agent = itAgent.next();
            if (agent.canRemove()) {
                itAgent.remove();
                break;
            }
            /* Every agent chases the protagonist. */
            agent.chase(game.getEnvironment().getProtagonist().getPosX(),
                    game.getEnvironment().getProtagonist().getPosY());
            /* It animates all agents. */
            game.getAnimator().animate(agent);
        }
        game.getEnvironment().update();
        /* If the agent died in this loop, the state changes. */
        if (game.getEnvironment().getProtagonist().isDead()) {
            /* If the agent dies, the game moves to the Game Over state. */
            game.getEnvironment().setCurrentMenu(game.getMenu().getGameOver());
            game.setCurrentState(new GameOverState());
        }
        /* It animates the protagonist. */
        game.getAnimator().animate(
                game.getEnvironment().getProtagonist());
        /* It animates all objects. */
        Iterator<Object> itObject = game.getEnvironment().getCurrentLevel().getObjects().iterator();
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
        for (Shot shot : game.getEnvironment().getCurrentLevel().getShots()) {
            game.getAnimator().animate(shot);
        }
    }

    @Override
    public void render(Game game) {
        /* Render the game and agents */
        game.getMediator().renderGame();
    }

}
