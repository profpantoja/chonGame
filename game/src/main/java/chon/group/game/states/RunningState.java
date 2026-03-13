package chon.group.game.states;

import chon.group.game.Game;
import chon.group.game.core.agent.Agent;
import chon.group.game.core.agent.EntityStatus;
import chon.group.game.core.agent.Object;
import chon.group.game.core.weapon.Shot;

public class RunningState implements GameState {

    @Override
    public void handleInput(Game game) {
        /** ChonBota Only Moves if the Player Press Something */
        /* If nothing happens, the protagonist stays IDLE. */
        game.getEnvironment().getProtagonist().setStatus(EntityStatus.IDLE);
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
                    /* The Shoot button is removed for not shooting forever. */
                    game.getInput().remove("SPACE");
                    Shot shot = game.getEnvironment().getProtagonist().useWeapon();
                    game.getEnvironment().getProtagonist().setStatus(EntityStatus.ATTACK);
                    /* If there is an associate shot with the weapon. Some weapons don't shoot. */
                    if (shot != null)
                        /* The shot is added to the environment's current level. */
                        game.getEnvironment().getCurrentLevel().getShots().add(shot);
                } else {
                    /* If there is any movement key pressed. */
                    if (game.getInput().contains("RIGHT") ||
                            game.getInput().contains("LEFT") ||
                            game.getInput().contains("DOWN") ||
                            game.getInput().contains("UP")) {
                        /* Protagonist's Moves based on Joystick inputs. */
                        game.getEnvironment().getProtagonist().move(game.getDirections(game.getInput()));
                        game.getEnvironment().getProtagonist().setStatus(EntityStatus.WALK);
                    }
                }
            }
        }
    }

    @Override
    public void update(Game game) {
        /* It checks if the protagonist is outside boundaries. */
        game.getEnvironment().checkBorders();
        /* ChonBot's Automatic Movements */
        /* Update the other agents' movements */
        for (Agent agent : game.getEnvironment().getCurrentLevel().getAgents()) {
            /* Every agent chases the protagonist. */
            agent.chase(game.getEnvironment().getProtagonist().getPosX(),
                    game.getEnvironment().getProtagonist().getPosY());
            /* It animates all agents. */
            game.getAnimator().update(agent);
        }
        game.getEnvironment().update();
        /* If the agent died in this loop, the state changes. */
        if (game.getEnvironment().getProtagonist().isDead()) {
            /* If the agent dies, the game moves to the Game Over state. */
            game.getEnvironment().getProtagonist().setStatus(EntityStatus.TERMINATE);
            game.getEnvironment().setCurrentMenu(game.getMenu().getGameOver());
            game.setCurrentState(new GameOverState());
        }
        /* It animates the protagonist. */
        game.getAnimator().update(
                game.getEnvironment().getProtagonist());
        /* It animates all objects. */
        for (Object object : game.getEnvironment().getCurrentLevel().getObjects()) {
            game.getAnimator().update(object);
        }
        /* It animates all shots. */
        for (Shot shot : game.getEnvironment().getCurrentLevel().getShots()) {
            game.getAnimator().update(shot);
        }
    }

    @Override
    public void render(Game game) {
        /* Render the game and agents */
        game.getMediator().renderGame();
    }

}
