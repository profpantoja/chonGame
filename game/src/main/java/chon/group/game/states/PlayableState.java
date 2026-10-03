package chon.group.game.states;

import java.util.List;

import chon.group.game.Game;
import chon.group.game.core.agent.Agent;
import chon.group.game.core.agent.Direction;
import chon.group.game.core.environment.Environment;
import chon.group.game.core.environment.Level;
import chon.group.game.core.weapon.Shot;
import chon.group.game.joystick.GameCommand;
import chon.group.game.sound.SoundEvent;

public class PlayableState implements GameState {

    @Override
    public void handleInput(Game game) {
        /**
         * If the player pressed the Pause buttom, the game moves to the pause state.
         */
        if (this.handlePause(game))
            return;

        if (game.getEnvironment().getProtagonist().isExternallyControlled()) {
            return;
        }

        /** The protagonist Shoots Somebody Who Outdrew You */
        /** But only if it has enough energy */
        if (this.handleAttack(game))
            return;

        this.handleMovement(game);
    }

    @Override
    public void update(Game game) {
        /* It caches the environmen, the current level and the protagonist. */
        Environment environment = game.getEnvironment();
        /* Updating the Game Components. */
        /* It updates the entire environment based on the level behavior (physics). */
        this.updateEnvironment(environment);
        /* Updates the game state. */
        if (this.updateState(game))
            return;
        /* It animates the game's components. */
        this.animate(game, environment.getCurrentLevel(), environment.getProtagonist());
    }

    @Override
    public void render(Game game) {
        /* Render the game and agents */
        game.getMediator().renderGame();
    }

    private boolean updateState(Game game) {
        Environment environment = game.getEnvironment();
        Level currentLevel = environment.getCurrentLevel();
        if (!environment.hasLivingAllies()) {
            game.getMenu().openGameOver();
            game.setCurrentState(new GameOverState());
            return true;
        }
        if (environment.getProtagonist().isDead()) {
            environment.focusNextLivingAlly();
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

    private void updateEnvironment(Environment environment) {
        environment.update();
    }

    public void animate(Game game, Level currentLevel, Agent protagonist) {
        game.getAnimator().animateLevel(currentLevel, protagonist);
    }

    private boolean handlePause(Game game) {
        /**
         * If the player pressed the Pause buttom, the game moves to the pause state.
         */
        if (!game.getJoystick().press(GameCommand.PAUSE)) {
            return false;
        }

        game.setCurrentState(new PauseState());
        game.getMenu().openPause();
        return true;
    }

    private boolean handleAttack(Game game) {
        /** The protagonist Shoots Somebody Who Outdrew You */
        /** But only if it has enough energy */
        if (!game.getJoystick().press(GameCommand.ATTACK)) {
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
        return true;
    }

    private void handleMovement(Game game) {
        /* Protagonist's Moves based on Joystick inputs. */
        List<Direction> directions = game.getDirections();

        /* If nothing happens, the protagonist stays IDLE. */
        if (directions.isEmpty()) {
            game.getEnvironment().getProtagonist().idle();
            return;
        }

        game.getEnvironment().getProtagonist().move(directions);
    }
}