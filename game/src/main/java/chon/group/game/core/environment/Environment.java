package chon.group.game.core.environment;

import java.util.ArrayList;
import java.util.List;

import chon.group.game.core.agent.Agent;
import chon.group.game.messaging.Messenger;
import chon.group.game.sound.Sound;
import chon.group.game.sound.SoundEvent;
import javafx.scene.image.Image;

/**
 * Represents the game environment, including properties such as dimensions,
 * position, background image, agents, protagonist, objects, shots, messages,
 * and camera.
 * The environment is responsible for updating the state of all elements,
 * detecting collisions, and tracking game progress such as score and
 * collectible status.
 */
public class Environment {

    /** The background image for the pause screen. */
    private Image pauseImage;

    /** The background image for the game over screen. */
    private Image gameOverImage;

    /** The background image for the game over screen. */
    private Image theEndImage;

    /** The protagonist of the environment. */
    private Agent protagonist;

    private List<Level> levels;

    private Level currentLevel;

    /** The messenger system. */
    private Messenger messenger;

    /** List of sounds currently being emitted. */
    private List<Sound> sounds;

    /** The camera used to follow the protagonist. */
    private Camera camera;

    private Panel panel;

    /** Number of objects collected so far. */
    private int collectedCount = 0;

    /** Current score of the player. */
    private int score = 0;

    private boolean debugMode = false;

    /**
     * Constructor to initialize the environment with dimensions, position, and a
     * background image.
     *
     * @param posX        the initial X position of the environment
     * @param posY        the initial Y position of the environment
     * @param width       the width of the environment
     * @param screenWidth the screen width used for camera calculations
     * @param pathImage   the path to the background image
     */
    public Environment(int width, double screenWidth, Panel panel) {
        this.sounds = new ArrayList<Sound>();
        this.levels = new ArrayList<Level>();
        this.camera = new Camera(screenWidth, width, 0.49, 0.51);
        this.messenger = new Messenger();
        this.panel = panel;
    }

    public Image getPauseImage() {
        return pauseImage;
    }

    /**
     * Sets the background image for the pause screen.
     *
     * @param pathImage the path to the image
     */
    public void setPauseImage(String pathImage) {
        this.pauseImage = new Image(getClass().getResource(pathImage).toExternalForm());
    }

    public Image getGameOverImage() {
        return gameOverImage;
    }

    /**
     * Sets the background image for the game over screen.
     *
     * @param pathImage the path to the image
     */
    public void setGameOverImage(String pathImage) {
        this.gameOverImage = new Image(getClass().getResource(pathImage).toExternalForm());
    }

    public Image getTheEndImage() {
        return theEndImage;
    }

    public void setTheEndImage(String pathImage) {
        this.theEndImage = new Image(getClass().getResource(pathImage).toExternalForm());
    }

    public Agent getProtagonist() {
        return protagonist;
    }

    /**
     * Sets the protagonist of the game and attaches the camera to it.
     *
     * @param protagonist the protagonist agent
     */
    public void setProtagonist(Agent protagonist) {
        this.protagonist = protagonist;
        if (camera != null)
            camera.setTarget(protagonist);
    }

    public List<Level> getLevels() {
        return levels;
    }

    public Level getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(Level currentLevel) {
        this.currentLevel = currentLevel;
    }

    public Messenger getMessenger() {
        return this.messenger;
    }

    public List<Sound> getSounds() {
        return sounds;
    }

    public Camera getCamera() {
        return camera;
    }

    public Panel getPanel() {
        return panel;
    }

    /**
     * Gets the number of objects collected by the protagonist.
     *
     * @return the number of collected objects
     */
    public int getCollectedCount() {
        return collectedCount;
    }

    public void setCollectedCount(int collectedCount) {
        this.collectedCount = collectedCount;
    }

    /**
     * Gets the current score of the player.
     *
     * @return the current score
     */
    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public boolean isDebugMode() {
        return debugMode;
    }

    public void setDebugMode(boolean debugMode) {
        this.debugMode = debugMode;
    }

    /**
     * Performs a full update cycle of the environment.
     * Includes updating objects, shots, messages, camera,
     * checking collisions, and recovering protagonist energy.
     */
    public void update() {
        this.currentLevel.getBehavior().update(this);
    }

    public void loadNextLevel() {
        if (this.levels == null || this.levels.isEmpty())
            return;
        int levelIndex = this.getLevels().indexOf(this.currentLevel);
        // If the current level do not exist in Levels or it is null.
        if (currentLevel == null || levelIndex < 0) {
            this.currentLevel = levels.get(0);
            this.setupCurrentLevel();
            return;
        }
        // This is the last level. So, we cannot setup the level again!
        if (levelIndex >= this.levels.size() - 1)
            return;
        // Then, there is a next level to play.
        this.currentLevel = this.levels.get(levelIndex + 1);
        this.setupCurrentLevel();
    }

    public void loadNextPlayableLevel() {
        if (this.levels == null || this.levels.isEmpty())
            return;
        int startIndex = 0;
        if (this.currentLevel != null) {
            int currentIndex = this.levels.indexOf(this.currentLevel);
            if (currentIndex >= 0) {
                startIndex = currentIndex + 1;
            }
        }
        Level nextPlayableLevel = this.levels.stream()
                .skip(startIndex)
                .filter(level -> level.getType() == StoryType.PLAYABLE)
                .findFirst()
                .orElse(null);
        if (nextPlayableLevel == null)
            return;
        this.currentLevel = nextPlayableLevel;
        this.setupCurrentLevel();
    }

    private void setupCurrentLevel() {
        this.protagonist.setPosX(10);
        this.protagonist.setPosY(450);
        this.camera.setPosX(0);
        this.camera.setLevelWidth(this.currentLevel.getWidth());
        this.loadSounds();
    }

    private void loadSounds() {
        Sound ambient = this.currentLevel.getSoundSet().get(SoundEvent.AMBIENT);
        Sound background = this.currentLevel.getSoundSet().get(SoundEvent.BACKGROUND);
        if (ambient != null) {
            this.sounds.add(ambient);
        }
        if (background != null) {
            this.sounds.add(background);
        }
    }

}