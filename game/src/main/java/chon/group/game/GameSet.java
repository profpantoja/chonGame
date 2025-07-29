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
import chon.group.game.core.menu.MainOption;
import chon.group.game.core.menu.PauseOption;
import chon.group.game.core.weapon.Panel;
import chon.group.game.core.weapon.Weapon;
import chon.group.game.domain.weapon.Lancer;
import chon.group.game.core.menu.MenuTextManager;

public class GameSet {

    private int canvasWidth;
    private int canvasHeight;
    private Environment environment;
    private Panel panel;

    public GameSet() {
        this.load();
    }

    public int getCanvasWidth() {
        return canvasWidth;
    }

    public void setCanvasWidth(int canvasWidth) {
        this.canvasWidth = canvasWidth;
    }

    public int getCanvasHeight() {
        return canvasHeight;
    }

    public void setCanvasHeight(int canvasHeight) {
        this.canvasHeight = canvasHeight;
    }

    public Environment getEnvironment() {
        return environment;
    }

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    public Panel getPanel() {
        return panel;
    }

    public void setPanel(Panel panel) {
        this.panel = panel;
    }

    private void load() {
        /* Define some size properties for both Canvas and Environment */
        this.canvasWidth = 1280;
        this.canvasHeight = 780;

        /** Define a general panel for life, energy, points, and objects. */
        panel = new Panel(
                240,
                110);

        /* Initialize the game environment, levels, agents and weapons */
        Level level1 = new Level(
                0,
                0,
                canvasHeight,
                1918,
                "/images/environment/bg_png.png");

        Level level2 = new Level(
                0,
                0,
                canvasHeight,
                1918,
                "/images/environment/bg2_png.png");


        /* Set background music for each level */
        level1.setBackgroundMusic(Game.gameMusic);
        level2.setBackgroundMusic(Game.menuMusic);


        environment = new Environment(
                this.canvasHeight,
                level1.getWidth(),
                this.canvasWidth,
                panel);


        Agent Nick = new Agent(400, 540, 150, 90, 10, 1000, "/images/agents/standing4.png", false, false);
        AnimationSpritesheet idleSheet = new SimpleAnimationSpritesheet(64, 90, 4, 200, "/images/agents/standing4.png");
        AnimationSpritesheet runSheet = new SimpleAnimationSpritesheet(64, 90, 4, 200, "/images/agents/running2.png");

        AnimationGraphics graphics = new AnimationGraphics();

        graphics.addSpritesheet(AnimationStatus.IDLE, idleSheet);
                graphics.addSpritesheet(AnimationStatus.RUNNING, runSheet);

        AnimationSystem animationSystem = new AnimationSystem(graphics);
        Nick.setAnimationSystem(animationSystem);

        Nick.setGravityEffects(true);
        Nick.setProtagonist(true);

        Weapon lancer = new Lancer(400, 390, 0, 0, 3, 0, 0.05, "", false);
        Weapon lancer2 = new Lancer(400, 390, 0, 0, 3, 0, 0.05, "", false);
        Weapon lancer3 = new Lancer(400, 390, 0, 0, 3, 0, 0.05, "", false);


        Nick.setWeapon(lancer);

        Agent enemy = new Agent(600, 540, 120, 95, 1, 500, "/images/agents/monstro1.png", true, true);


        Agent enemy1 = new Agent(5, 540, 90, 65, 1, 500, "/images/agents/monstro1.png", true, true);
        Agent enemy2 = new Agent(50, 540, 90, 65, 1, 500, "/images/agents/monstro1.png", true, true);
        Agent enemy3 = new Agent(110, 540, 90, 65, 1, 500, "/images/agents/monstro1.png", true, true);
        Agent enemy4 = new Agent(160, 540, 90, 65, 1, 500, "/images/agents/monstro1.png", true, true);
        Agent boss = new Agent(920, 570, 190, 165, 1, 500, "/images/agents/boss.png", true, true);

        environment.setProtagonist(Nick);
        enemy.setWeapon(lancer2);
        boss.setWeapon(lancer3);

        enemy.setEnemy(true);
        enemy1.setEnemy(true);
        enemy2.setEnemy(true);
        enemy3.setEnemy(true);
        enemy4.setEnemy(true);
        boss.setEnemy(true);


        environment.setPauseImage("/images/environment/pause.png");
        environment.setGameOverImage("/images/environment/gameover_image.png");

        level1.getAgents().add(enemy);
        level2.getAgents().add(enemy1);
        level2.getAgents().add(enemy2);
        level2.getAgents().add(enemy3);
        level2.getAgents().add(enemy4);
        level2.getAgents().add(boss);


        /* Set up some collectable objects */
        List<Object> objects = new ArrayList<>();
        AnimationSpritesheet coinAnim = new SimpleAnimationSpritesheet(96, 96, 8, 200, "/images/agents/coinAnimated.png");
        objects.add(new Object(200, 350, 32, 32, 0, 0, "/images/agents/coin.png", false, false, true, false, coinAnim));
        objects.add(new Object(400, 380, 32, 32, 0, 0, "/images/agents/coin.png", false, false, true, false, coinAnim));
        objects.add(new Object(1000, 600, 32, 32, 0, 0, "/images/agents/coin.png", false, false, true, false, coinAnim));
        objects.add(new Object(1400, 380, 32, 32, 0, 0, "/images/agents/coin.png", false, false, true, false, coinAnim));
        objects.add(new Object(1800, 650, 32, 32, 0, 0, "/images/agents/coin.png", false, false, true, false, coinAnim));
        objects.add(new Object(2000, 580, 32, 32, 0, 0, "/images/agents/coin.png", false, false, true, false,coinAnim));
        objects.add(new Object(2300, 380, 32, 32, 0, 0, "/images/agents/coin.png", false, false, true, false, coinAnim));
        objects.add(new Object(2600, 500, 32, 32, 0, 0, "/images/agents/coin.png", false, false, true, false, coinAnim));
        objects.add(new Object(2900, 380, 32, 32, 0, 0, "/images/agents/coin.png", false, false, true, false, coinAnim));
        objects.add(new Object(3200, 400, 32, 32, 0, 0, "/images/agents/coin.png", false, false, true, false, coinAnim));
        objects.add(new Object(4100, 500, 32, 32, 0, 0, "/images/agents/coin.png", false, false, true, false, coinAnim));
        objects.add(new Object(5000, 380, 32, 32, 0, 0, "/images/agents/coin.png", false, false, true, false, coinAnim));
        objects.add(new Object(6200, 400, 32, 32, 0, 0, "/images/agents/coin.png", false, false, true, false, coinAnim));

        // Register objects into the environment and count total collectibles
        level1.setObjects(objects);
        level1.countCollectibles();
        environment.getLevels().add(level1);
        level2.setObjects(objects);
        level2.countCollectibles();
        environment.getLevels().add(level2);
        environment.setCurrentLevel(level1);

        MenuTextManager.getInstance().setText(MainOption.START_GAME, "Start game");
        MenuTextManager.getInstance().setText(MainOption.EXIT, "Exit");
        MenuTextManager.getInstance().setText(PauseOption.RESUME, "Resume");
        MenuTextManager.getInstance().setText(PauseOption.GO_BACK_TO_MENU, "Go back to menu");
    }

}
