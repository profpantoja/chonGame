package chon.group.game;

import java.util.ArrayList;
import java.util.List;

import chon.group.game.core.agent.Agent;
import chon.group.game.core.agent.Object;
import chon.group.game.core.animation.AnimationGraphics;
import chon.group.game.core.animation.AnimationSpritesheet;
import chon.group.game.core.animation.AnimationStatus;
import chon.group.game.core.animation.AnimationSystem;
import chon.group.game.core.animation.SimpleAnimationSpritesheet;
import chon.group.game.core.environment.Environment;
import chon.group.game.core.environment.Level;
import chon.group.game.core.weapon.CloseWeapon;
import chon.group.game.core.menu.MainOption;
import chon.group.game.core.menu.PauseOption;
import chon.group.game.core.weapon.Panel;
import chon.group.game.core.weapon.Weapon;
import chon.group.game.domain.weapon.Cannon;
import chon.group.game.domain.weapon.Lancer;
import chon.group.game.domain.weapon.Sword;
import chon.group.game.core.menu.MenuTextManager;

/**
 * The {@code GameSet} class represents the entire configuration and setup
 * of the game, including the canvas size, environment, panels, levels,
 * agents, weapons, and game menus.
 * 
 * <p>When instantiated, the class loads and initializes the default game
 * state, setting up environment levels, agents with animations and weapons,
 * collectible objects, and menu text options.</p>
 */
public class GameSet {

    /** Width of the game canvas */
    private int canvasWidth;

    /** Height of the game canvas */
    private int canvasHeight;

    /** The game environment including levels and agents */
    private Environment environment;

    /** The panel displaying UI elements like life, energy, points, etc */
    private Panel panel;

    /**
     * Constructs a new {@code GameSet} instance and initializes the game
     * configuration by calling {@link #load()}.
     */
    public GameSet() {
        this.load();
    }

    /**
     * Returns the width of the canvas.
     * 
     * @return the canvas width in pixels
     */
    public int getCanvasWidth() {
        return canvasWidth;
    }

    /**
     * Sets the width of the canvas.
     * 
     * @param canvasWidth width in pixels
     */
    public void setCanvasWidth(int canvasWidth) {
        this.canvasWidth = canvasWidth;
    }

    /**
     * Returns the height of the canvas.
     * 
     * @return the canvas height in pixels
     */
    public int getCanvasHeight() {
        return canvasHeight;
    }

    /**
     * Sets the height of the canvas.
     * 
     * @param canvasHeight height in pixels
     */
    public void setCanvasHeight(int canvasHeight) {
        this.canvasHeight = canvasHeight;
    }

    /**
     * Returns the current game environment, containing levels, agents, and other entities.
     * 
     * @return the environment instance
     */
    public Environment getEnvironment() {
        return environment;
    }

    /**
     * Sets the game environment.
     * 
     * @param environment the environment to set
     */
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    /**
     * Returns the UI panel that displays information such as life, energy, points, and objects.
     * 
     * @return the game panel
     */
    public Panel getPanel() {
        return panel;
    }

    /**
     * Sets the UI panel for the game.
     * 
     * @param panel the panel to set
     */
    public void setPanel(Panel panel) {
        this.panel = panel;
    }

    /**
     * Loads and initializes the full game setup, including:
     * <ul>
     *   <li>Canvas dimensions</li>
     *   <li>UI panel dimensions</li>
     *   <li>Environment and levels with backgrounds and music</li>
     *   <li>Protagonist and enemy agents, with animations and weapons</li>
     *   <li>Collectible objects</li>
     *   <li>Menu text options for start, exit, pause, etc.</li>
     * </ul>
     * 
     * This method is automatically called by the constructor.
     */
    private void load() {
        // Set canvas size
        this.canvasWidth = 1280;
        this.canvasHeight = 780;

        // Initialize UI panel size
        panel = new Panel(240, 110);

        // Create two game levels with backgrounds and music
        Level level1 = new Level(0, 0, canvasHeight, 1500, "/images/environment/blue_background.png");
        Level level2 = new Level(0, 0, canvasHeight, 1500, "/images/environment/red_background.png");

        level1.setBackgroundMusic(Game.gameMusic);
        level2.setBackgroundMusic(Game.menuMusic);

        // Create environment with dimensions and the panel
        environment = new Environment(this.canvasHeight, level1.getWidth(), this.canvasWidth, panel);

        // Initialize protagonist agent with animations and weapon
        Agent chonBota = new Agent(100, 540, 200, 300, 8, 3000, "/images/agents/chonBota.png", false, false);
        AnimationSpritesheet idleSheet = new SimpleAnimationSpritesheet(64, 90, 4, 200, "/images/agents/chonBotaAnimated.png");
        AnimationSpritesheet runSheet = new SimpleAnimationSpritesheet(64, 90, 4, 200, "/images/agents/chonBotaAnimated.png");

        AnimationGraphics graphics = new AnimationGraphics();
        graphics.addSpritesheet(AnimationStatus.IDLE, idleSheet);
        graphics.addSpritesheet(AnimationStatus.RUNNING, runSheet);

        AnimationSystem animationSystem = new AnimationSystem(graphics);
        chonBota.setAnimationSystem(animationSystem);
        chonBota.setGravityEffects(true);
        chonBota.setProtagonist(true);

        // Create weapons
        Weapon cannon = new Cannon(400, 390, 0, 0, 3, 0, 0.05, "", false);
        Weapon lancer = new Lancer(400, 390, 0, 0, 3, 0, 0.05, "", false);
        Weapon lancer2 = new Lancer(400, 390, 0, 0, 3, 0, 0.05, "", false);
        CloseWeapon sword = new Sword(400, 390, 0, 0, 3, 0,  "", false);

        chonBota.setWeapon(lancer);

        // Create enemy agents with weapons and add them to levels
        Agent chonBot = new Agent(200, 600, 100, 120, 1, 500, "/images/agents/ghost-blue.png", true, true);
        chonBot.setWeapon(lancer2);
        chonBot.setEnemy(true);

        Agent enemy = new Agent(600, 600, 100, 120, 1, 500, "/images/agents/ghost-red.png", true, true);
        enemy.setEnemy(true);

        Agent chonBot2 = new Agent(600, 600, 100, 120, 1, 500, "/images/agents/ghost-blue.png", true, true);
        chonBot2.setWeapon(new Lancer(800, 600, 0, 0, 1, 0, 0.05, "", false));
        chonBot2.setEnemy(true);

        Agent chonBot3 = new Agent(1000, 600, 100, 120, 1, 500, "/images/agents/ghost-blue.png", true, true);
        chonBot3.setWeapon(new Lancer(800, 600, 0, 0, 1, 0, 0.05, "", false));
        chonBot3.setEnemy(true);

        // Boss for level 2
        Agent boss2 = new Agent(1200, 550, 170, 180, 2, 1000, "/images/agents/boss-red.png", true, true);
        boss2.setWeapon(new Lancer(1900, 300, 0, 0, 1, 0, 0.05, "", false));
        boss2.setEnemy(true);

        // Add agents to levels and environment
        level1.getAgents().add(chonBot);
        level1.getAgents().add(chonBot2);
        level1.getAgents().add(chonBot3);

        level2.getAgents().add(enemy);
        level2.getAgents().add(boss2);

        environment.setProtagonist(chonBota);
        environment.setPauseImage("/images/environment/pause.png");
        environment.setGameOverImage("/images/environment/gameover.png");

        // Define collectible objects with animations
        List<Object> objects = new ArrayList<>();
        AnimationSpritesheet coinAnim = new SimpleAnimationSpritesheet(96, 96, 8, 200, "/images/agents/coinAnimated.png");
        objects.add(new Object(200, 670, 32, 32, 0, 0, "/images/agents/coin.png", false, false, true, false, coinAnim));
        objects.add(new Object(400, 670, 32, 32, 0, 0, "/images/agents/coin.png", false, false, true, false, coinAnim));
        objects.add(new Object(1000, 670, 32, 32, 0, 0, "/images/agents/coin.png", false, false, true, false, coinAnim));
        objects.add(new Object(1400, 670, 32, 32, 0, 0, "/images/agents/coin.png", false, false, true, false, coinAnim));
        objects.add(new Object(1800, 670, 32, 32, 0, 0, "/images/agents/coin.png", false, false, true, false, coinAnim));

        // Add collectibles to levels and count them
        level1.setObjects(objects);
        level1.countCollectibles();
        level2.setObjects(objects);
        level2.countCollectibles();

        // Add levels to environment and set current level
        environment.getLevels().add(level1);
        environment.getLevels().add(level2);
        environment.setCurrentLevel(level1);

        // Set menu texts for UI
        MenuTextManager.getInstance().setText(MainOption.START_GAME, "Start game");
        MenuTextManager.getInstance().setText(MainOption.EXIT, "Exit");
        MenuTextManager.getInstance().setText(PauseOption.RESUME, "Resume");
        MenuTextManager.getInstance().setText(PauseOption.GO_BACK_TO_MENU, "Go back to menu");
    }

}
