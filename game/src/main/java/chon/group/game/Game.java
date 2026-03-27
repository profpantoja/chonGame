package chon.group.game;

import java.util.ArrayList;
import java.util.List;

import chon.group.game.animation.Animator;
import chon.group.game.core.agent.Direction;
import chon.group.game.core.environment.Environment;
import chon.group.game.core.environment.Level;
import chon.group.game.drawer.EnvironmentDrawer;
import chon.group.game.menu.MenuHandler;
import chon.group.game.sound.Sound;
import chon.group.game.sound.SoundEvent;
import chon.group.game.sound.service.GameSoundManager;
import chon.group.game.states.GameState;
import chon.group.game.states.StartState;

public class Game {

    private Environment environment;
    private EnvironmentDrawer mediator;
    private GameSoundManager soundPlayer;
    private MenuHandler menu;
    private Animator animator = new Animator();
    private ArrayList<String> input;
    private GameState currentState = new StartState();

    public Game(Environment environment, EnvironmentDrawer mediator, GameSoundManager soundPlayer, MenuHandler menu,
            ArrayList<String> input) {
        this.environment = environment;
        this.mediator = mediator;
        this.menu = menu;
        this.input = input;
        this.soundPlayer = soundPlayer;
        this.start();
    }

    public Environment getEnvironment() {
        return environment;
    }

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    public EnvironmentDrawer getMediator() {
        return mediator;
    }

    public void setMediator(EnvironmentDrawer mediator) {
        this.mediator = mediator;
    }

    public GameSoundManager getSoundPlayer() {
        return soundPlayer;
    }

    public void setSoundPlayer(GameSoundManager soundPlayer) {
        this.soundPlayer = soundPlayer;
    }

    public MenuHandler getMenu() {
        return menu;
    }

    public void setMenu(MenuHandler menu) {
        this.menu = menu;
    }

    public Animator getAnimator() {
        return animator;
    }

    public void setAnimator(Animator animator) {
        this.animator = animator;
    }

    public ArrayList<String> getInput() {
        return input;
    }

    public void setInput(ArrayList<String> input) {
        this.input = input;
    }

    public GameState getCurrentState() {
        return currentState;
    }

    public void setCurrentState(GameState currentState) {
        this.currentState = currentState;
    }

    public void loop() {
        this.updateLevel();
        this.currentState.handleInput(this);
        this.currentState.update(this);
        this.playSounds();
        this.currentState.render(this);
    }

    private void playSounds() {
        for (Sound sound : this.environment.getSounds()) {
            switch (sound.getType()) {
                case SFX:
                    this.soundPlayer.play(sound);
                    break;
                case MUSIC:
                    this.soundPlayer.playMusic(sound);
                    break;
                case AMBIENT:
                    this.soundPlayer.playAmbient(sound);
                    break;
                default:
                    this.soundPlayer.playMusic(sound);
            }
        }
        this.environment.getSounds().clear();
    }

    public void start() {
        this.environment.setCurrentMenu(this.menu.getStart());
    }

    public void reset() {
        this.environment = new GameSet().getEnvironment();
        this.mediator.setEnvironment(this.environment);
        this.input.clear();
        this.soundPlayer.stop();
        Sound ambient = this.environment.getCurrentLevel().getSoundSet().get(SoundEvent.AMBIENT);
        Sound background = this.environment.getCurrentLevel().getSoundSet().get(SoundEvent.BACKGROUND);
        if (ambient != null)
            this.soundPlayer.playAmbient(ambient);
        if (background != null)
            this.soundPlayer.playMusic(background);
    }

    public void updateLevel() {
        if (this.environment.getCurrentLevel().isCompleted(this.environment)) {
            this.soundPlayer.stop();
            environment.loadNextLevel();
        }
    }

    public void resetLevel() {
        GameSet gameSet = new GameSet();
        int myLevelIndex = this.environment.getLevels().indexOf(this.environment.getCurrentLevel());
        Level newLevel = gameSet.getEnvironment().getLevels().get(myLevelIndex);
        this.environment.getLevels().set(myLevelIndex, newLevel);
        this.environment.setCurrentLevel(newLevel);
        this.environment.setCurrentMenu(this.menu.getPause());
        this.environment.setProtagonist(gameSet.getEnvironment().getProtagonist());
        this.environment.getCamera().setPosX(0);
        this.environment.getCamera().setLevelWidth(newLevel.getWidth());
        this.environment.setCollectedCount(0);
        this.environment.setScore(0);
        this.input.clear();
        this.soundPlayer.stop();
        Sound ambient = this.environment.getCurrentLevel().getSoundSet().get(SoundEvent.AMBIENT);
        Sound background = this.environment.getCurrentLevel().getSoundSet().get(SoundEvent.BACKGROUND);
        if (ambient != null)
            soundPlayer.playAmbient(ambient);
        if (background != null)
            soundPlayer.playMusic(background);
    }

    public List<Direction> getDirections(List<String> input) {
        ArrayList<Direction> directions = new ArrayList<Direction>();
        for (String command : input) {
            switch (command) {
                case "RIGHT":
                    directions.add(Direction.RIGHT);
                    break;
                case "LEFT":
                    directions.add(Direction.LEFT);
                    break;
                case "DOWN":
                    directions.add(Direction.DOWN);
                    break;
                case "UP":
                    directions.add(Direction.UP);
                    break;
            }
        }
        return directions;
    }

}
