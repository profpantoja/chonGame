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
import chon.group.game.core.menu.MenuTextManager;
import chon.group.game.core.menu.PauseOption;
import chon.group.game.core.weapon.CloseWeapon;
import chon.group.game.core.weapon.Panel;
import chon.group.game.core.weapon.Weapon;
import chon.group.game.domain.weapon.Cannon;
import chon.group.game.domain.weapon.Lancer;
import chon.group.game.domain.weapon.Sword;

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
                1280,
                "/images/environment/desertVillage.png");

        Level level2 = new Level(
                0,
                0,
                canvasHeight,
                1280,
                "/images/environment/desertRoad.png");

        Level level3 = new Level(
                0,
                0,
                canvasHeight,
                1280,
                "/images/environment/tunnel.png");

        Level level4 = new Level(
                0,
                0,
                canvasHeight,
                1280,
                "/images/environment/forest.png");

        /* Set background music for each level */
        level1.setBackgroundMusic(Game.gameMusic);
        level2.setBackgroundMusic(Game.menuMusic);

        environment = new Environment(
                this.canvasHeight,
                level1.getWidth(),
                this.canvasWidth,
                panel);

        
        /* Set the Mr Man */
        Agent mrMan = new Agent(400, 390, 90, 65, 5, 5000, "/images/agents/MrMan.png", false, false);
        AnimationSpritesheet idleMrMan = new SimpleAnimationSpritesheet(36, 48, 5, 500, "/images/agents/MrMan.png");
        AnimationSpritesheet runMrMan = new SimpleAnimationSpritesheet(36, 48, 6, 500, "/images/agents/MrManRun.png");
        AnimationSpritesheet attackMrMan = new SimpleAnimationSpritesheet(42, 48, 5, 15000, "/images/agents/MrManAttack.png");

        AnimationGraphics MrManGraphics = new AnimationGraphics();
        MrManGraphics.addSpritesheet(AnimationStatus.IDLE, idleMrMan);
        MrManGraphics.addSpritesheet(AnimationStatus.RUNNING, runMrMan);
        MrManGraphics.addSpritesheet(AnimationStatus.ATTACKING, attackMrMan);
        AnimationSystem MrManSystem = new AnimationSystem(MrManGraphics);
        mrMan.setAnimationSystem(MrManSystem);

        /* Set Weapons*/
        Weapon cannon = new Cannon(400, 390, 0, 0, 3, 0, 0.05, "", false);
        Weapon lancer = new Lancer(400, 390, 0, 0, 3, 0, 0.05, "", false);
        Weapon lancer2 = new Lancer(400, 390, 0, 0, 3, 0, 0.05, "", false);
        CloseWeapon sword = new Sword(400, 390, 0, 0, 3, 0,  "", false);

        mrMan.setWeapon(lancer);

        /*
         * Set the magician agent with its animations and weapon.
         * The magician is an enemy agent with a ranged weapon (lancer2).
         * It has its own animations for idle, run, jump, attack, hit, and die.
         */
        Agent magician = new Agent(920, 440, 90, 65, 1, 2000, "/images/agents/chonBot.png", true, true);
        AnimationSpritesheet idleMagician = new SimpleAnimationSpritesheet(69, 63, 4, 1000, "/images/agents/magic.png");
        AnimationSpritesheet runMagician = new SimpleAnimationSpritesheet(69, 63, 4, 500, "/images/agents/magic.png");
        AnimationSpritesheet jumpMagician = new SimpleAnimationSpritesheet(69, 63, 4, 500, "/images/agents/magic.png");
        AnimationSpritesheet attackMagician = new SimpleAnimationSpritesheet(69, 63, 4, 360, "/images/agents/magic.png");
        AnimationSpritesheet hitMagician = new SimpleAnimationSpritesheet(69, 63, 4, 1000, "/images/agents/magic.png");
        AnimationSpritesheet dieMagician = new SimpleAnimationSpritesheet(69, 63, 4, 1000, "/images/agents/magic.png");

        AnimationGraphics magicianGraphics = new AnimationGraphics();
        magicianGraphics.addSpritesheet(AnimationStatus.IDLE, idleMagician);
        magicianGraphics.addSpritesheet(AnimationStatus.RUNNING, runMagician);
        magicianGraphics.addSpritesheet(AnimationStatus.ATTACKING, attackMagician);
        magicianGraphics.addSpritesheet(AnimationStatus.HIT, hitMagician);
        magicianGraphics.addSpritesheet(AnimationStatus.DYING, dieMagician);
        magicianGraphics.addSpritesheet(AnimationStatus.JUMPING, jumpMagician);

        AnimationSystem magicianSystem = new AnimationSystem(magicianGraphics);
        magician.setAnimationSystem(magicianSystem);

        magician.setWeapon(lancer2);

        /*
         * Set the goblin agents with their animations and weapons.
         * The goblins are enemy agents with a close weapon (sword).
         * The goblins are positioned at different locations in the level.
         * * Each goblin has its own instance of the AnimationSystem with the same animations.
         */
        Agent goblin1 = new Agent(200, 400, 90, 65, 2, 1, "/images/agents/chonBot.png", true, true);
        Agent goblin2 = new Agent(920, 440, 90, 65, 3, 1, "/images/agents/chonBot.png", true, true);
        Agent goblin3 = new Agent(500, 700, 90, 65, 2, 1, "/images/agents/chonBot.png", true, true);
        AnimationSpritesheet idleGoblin = new SimpleAnimationSpritesheet(46, 52, 6, 1000, "/images/agents/goblin.png");
        AnimationSpritesheet runGoblin = new SimpleAnimationSpritesheet(46, 52, 6, 500, "/images/agents/goblin.png");
        AnimationSpritesheet jumpGoblin = new SimpleAnimationSpritesheet(46, 52, 6, 500, "/images/agents/goblin.png");
        AnimationSpritesheet attackGoblin = new SimpleAnimationSpritesheet(46, 52, 6, 360, "/images/agents/goblin.png");
        AnimationSpritesheet hitGoblin = new SimpleAnimationSpritesheet(46, 52, 6, 1000, "/images/agents/goblin.png");
        AnimationSpritesheet dieGoblin = new SimpleAnimationSpritesheet(46, 52, 6, 1000, "/images/agents/goblin.png");

        AnimationGraphics goblinGraphics = new AnimationGraphics();
        goblinGraphics.addSpritesheet(AnimationStatus.IDLE, idleGoblin);
        goblinGraphics.addSpritesheet(AnimationStatus.RUNNING, runGoblin);
        goblinGraphics.addSpritesheet(AnimationStatus.ATTACKING, attackGoblin);
        goblinGraphics.addSpritesheet(AnimationStatus.HIT, hitGoblin);
        goblinGraphics.addSpritesheet(AnimationStatus.DYING, dieGoblin);
        goblinGraphics.addSpritesheet(AnimationStatus.JUMPING, jumpGoblin);

        AnimationSystem goblinSystem = new AnimationSystem(goblinGraphics);
        goblin1.setAnimationSystem(goblinSystem);
        goblin2.setAnimationSystem(goblinSystem);
        goblin3.setAnimationSystem(goblinSystem);

        goblin1.setCloseWeapon(sword);
        goblin2.setCloseWeapon(sword);
        goblin3.setCloseWeapon(sword);

        /* Set the golem agent with its animations and weapon. 
         * The golem is an enemy agent with a ranged weapon (cannon).
         * It has its own animations for idle, run, jump, attack, hit, and die.
         * The golem is positioned at a specific location in the level.
         * The golem has a larger health pool and is more powerful than the goblins.
         * The golem is designed to be a challenging enemy for the player.
        */
        
        Agent golem = new Agent(1000, 440, 90, 65, 1, 5000, "/images/agents/chonBot.png", true, true);
        AnimationSpritesheet idleGolem = new SimpleAnimationSpritesheet(76, 118, 7, 1000, "/images/agents/golemRun.png");
        AnimationSpritesheet runGolem = new SimpleAnimationSpritesheet(76, 118, 7, 500, "/images/agents/golemRun.png");
        AnimationSpritesheet jumpGolem = new SimpleAnimationSpritesheet(76, 118, 7, 500, "/images/agents/golemRun.png");
        AnimationSpritesheet attackGolem = new SimpleAnimationSpritesheet(106, 146, 7, 360, "/images/agents/golemAttack.png");
        AnimationSpritesheet hitGolem = new SimpleAnimationSpritesheet(76, 118, 7, 1000, "/images/agents/golemRun.png");
        AnimationSpritesheet dieGolem = new SimpleAnimationSpritesheet(76, 118, 7, 1000, "/images/agents/golemRun.png");

        AnimationGraphics golemGraphics = new AnimationGraphics();
        golemGraphics.addSpritesheet(AnimationStatus.IDLE, idleGolem);
        golemGraphics.addSpritesheet(AnimationStatus.RUNNING, runGolem);
        golemGraphics.addSpritesheet(AnimationStatus.ATTACKING, attackGolem);
        golemGraphics.addSpritesheet(AnimationStatus.HIT, hitGolem);
        golemGraphics.addSpritesheet(AnimationStatus.DYING, dieGolem);
        golemGraphics.addSpritesheet(AnimationStatus.JUMPING, jumpGolem);

        AnimationSystem golemSystem = new AnimationSystem(golemGraphics);
        golem.setAnimationSystem(golemSystem);
        golem.setFlipped(true);

        golem.setWeapon(cannon);



        environment.setProtagonist(mrMan);
        
        magician.setEnemy(true);
        level1.getAgents().add(magician);

        goblin1.setEnemy(true);
        goblin2.setEnemy(true);
        goblin3.setEnemy(true);
        level2.getAgents().add(goblin1);
        level2.getAgents().add(goblin2);
        level2.getAgents().add(goblin3);

        golem.setEnemy(true);
        level3.getAgents().add(golem);
        
        
        environment.setPauseImage("/images/environment/pause.png") ;
        environment.setGameOverImage("/images/environment/gameover.png");   

        /* Set up some collectable objects */
        List<Object> objects = new ArrayList<>();
        AnimationSpritesheet coinAnim = new SimpleAnimationSpritesheet(96, 96, 8, 200, "/images/agents/coinAnimated.png");
        objects.add(new Object(710, 187, 32, 32, 0, 0, "/images/agents/coin.png", false, false, true, false, coinAnim));
        objects.add(new Object(1200, 445, 32, 32, 0, 0, "/images/agents/coin.png", false, false, true, false, coinAnim));
        objects.add(new Object(800, 600, 32, 32, 0, 0, "/images/agents/coin.png", false, false, true, false, coinAnim));
        /*
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
         */

        // Register objects into the environment and count total collectibles
        level1.setObjects(objects);
        level1.countCollectibles();
        environment.getLevels().add(level1);
        level2.setObjects(objects);
        level2.countCollectibles();
        environment.getLevels().add(level2);
        environment.getLevels().add(level3);
        environment.getLevels().add(level4);
        environment.setCurrentLevel(level1);

        MenuTextManager.getInstance().setText(MainOption.START_GAME, "Iniciar");
        MenuTextManager.getInstance().setText(MainOption.EXIT, "Sair");
        MenuTextManager.getInstance().setText(PauseOption.RESUME, "Voltar ao jogo");
        MenuTextManager.getInstance().setText(PauseOption.GO_BACK_TO_MENU, "Retornar ao menu");
    }

}
