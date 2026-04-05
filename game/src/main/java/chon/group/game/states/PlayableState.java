package chon.group.game.states;

import java.util.Iterator;
import java.util.List;

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
        /* Updating the Game Components. */
        this.updateBorders(environment);
        this.updateAgents(game, currentLevel, protagonist);
        this.updateEnvironment(environment);
        if (this.updateState(game))
            return;
        this.updateObjects(game, currentLevel);
        this.animate(game, currentLevel, protagonist);
    }

    @Override
    public void render(Game game) {
        /* Render the game and agents */
        game.getMediator().renderGame();
    }

    private boolean updateState(Game game) {
        Environment environment = game.getEnvironment();
        Level currentLevel = environment.getCurrentLevel();
        Agent protagonist = environment.getProtagonist();
        /* If the agent died in this loop, the state changes. */
        if (protagonist.isDead()) {
            game.getMenu().openGameOver();
            /* If the agent dies, the game moves to the Game Over state. */
            game.setCurrentState(new GameOverState());
            return true;
        }
        if (game.isGameCompleted()) {
            game.getMenu().openWin();
            game.setCurrentState(new WinState());
            return true;
        }
        switch (currentLevel.getType()) {
            case STORY:
                game.getMenu().openSkip();
                game.getMenu().getCurrentMenu().setTitle(currentLevel.getDescription());
                game.setCurrentState(new StoryState());
                return true;
            default:
                break;
        }
        return false;
    }

    private void updateBorders(Environment environment) {
        /* It checks if the protagonist is outside boundaries. */
        environment.checkBorders();
    }

    private void updateAgents(Game game, Level currentLevel, Agent protagonist) {
        Iterator<Agent> itAgent = currentLevel.getAgents().iterator();
        /* ChonBot's Automatic Movements */
        /* Update the other agents' movements */
        while (itAgent.hasNext()) {
            Agent agent = itAgent.next();
            if (agent.canRemove()) {
                itAgent.remove();
                continue;
            }
            /* Every agent chases the protagonist. */
            agent.chase(protagonist.getPosX(),
                    protagonist.getPosY());
        }
    }

    private void updateEnvironment(Environment environment) {
        environment.update();
    }

    private void updateObjects(Game game, Level currentLevel) {
        Iterator<Object> itObject = currentLevel.getObjects().iterator();
        while (itObject.hasNext()) {
            Object object = itObject.next();
            if (object.isDestroyed() && object.canRemove()) {
                itObject.remove();
                continue;
            }
        }
    }

    public void animate(Game game, Level currentLevel, Agent protagonist) {
        game.getAnimator().animateLevel(currentLevel, protagonist);
    }

    private boolean handlePause(Game game) {
        /**
         * If the player pressed the Pause buttom, the game moves to the pause state.
         */
        if (!game.getInput().contains("P")) {
            return false;
        }
        game.setCurrentState(new PauseState());
        game.getMenu().openPause();
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
        if (this.hasMovement(game.getInput())) {
            /* Protagonist's Moves based on Joystick inputs. */
            game.getEnvironment().getProtagonist().move(
                    game.getDirections(game.getInput()));
        }
    }

    private boolean hasMovement(List<String> input) {
        return input.contains("RIGHT") ||
                input.contains("LEFT") ||
                input.contains("DOWN") ||
                input.contains("UP");
    }

}
