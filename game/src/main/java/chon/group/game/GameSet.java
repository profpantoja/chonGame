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
import javafx.animation.Animation;
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
        this.canvasHeight = 640;

        /** Define a general panel for life, energy, points, and objects. */
        panel = new Panel(
                240,
                110);

        /* Initialize the game environment, levels, agents and weapons */
        Level level1 = new Level(
                0,
                0,
                canvasHeight,
                2000,
                "/images/environment/backgroundlvl1.png");

        Level level2 = new Level(
                0,
                0,
                canvasHeight,
                8000,
                "/images/environment/mountain.png");

        /* Set background music for each level */
        level1.setBackgroundMusic(Game.gameMusic);
        level2.setBackgroundMusic(Game.menuMusic);

        environment = new Environment(
                this.canvasHeight,
                level1.getWidth(),
                this.canvasWidth,
                panel);

        //CHARIZARD (LVL1)
        Agent chonBota = new Agent(400, 390, 90, 65, 5, 1000, "/images/agents/chonBota.png", false, false);
        AnimationSpritesheet idleChonBota = new SimpleAnimationSpritesheet(100, 100, 5, 2000, "/images/agents/charizard_idle.png");
        AnimationSpritesheet runChonBota = new SimpleAnimationSpritesheet(130, 100, 4, 500, "/images/agents/charizard_walk.png");
        AnimationSpritesheet jumpChonBota = new SimpleAnimationSpritesheet(100, 100, 2, 500, "/images/agents/charizard_idle.png");
        AnimationSpritesheet attackChonBota = new SimpleAnimationSpritesheet(130, 100, 4, 360, "/images/agents/charizard_attack.png");
        AnimationSpritesheet hitChonBota = new SimpleAnimationSpritesheet(130, 100, 3, 1000, "/images/agents/charizard_dmg.png");
        AnimationSpritesheet dieChonBota = new SimpleAnimationSpritesheet(130, 100, 3, 1000, "/images/agents/charizard_death.png");


        AnimationGraphics chonBotaGraphics = new AnimationGraphics();
        chonBotaGraphics.addSpritesheet(AnimationStatus.IDLE, idleChonBota);
        chonBotaGraphics.addSpritesheet(AnimationStatus.RUNNING, runChonBota);
        chonBotaGraphics.addSpritesheet(AnimationStatus.ATTACKING, attackChonBota);
        chonBotaGraphics.addSpritesheet(AnimationStatus.HIT, hitChonBota);
        chonBotaGraphics.addSpritesheet(AnimationStatus.DYING, dieChonBota);
        chonBotaGraphics.addSpritesheet(AnimationStatus.JUMPING, jumpChonBota);
        AnimationSystem chonBotaSystem = new AnimationSystem(chonBotaGraphics);
        chonBota.setAnimationSystem(chonBotaSystem);

        Weapon cannon = new Cannon(400, 390, 0, 0, 3, 0, 0.05, "", false);
        Weapon lancer = new Lancer(400, 390, 0, 0, 3, 0, 0.05, "", false);
        Weapon lancer2 = new Lancer(400, 390, 0, 0, 3, 0, 0.05, "", false);
        CloseWeapon sword = new Sword(400, 390, 0, 0, 3, 0,  "", false);
        CloseWeapon sword2 = new Sword(400, 390, 0, 0, 3, 0, "", false);

        
        chonBota.setWeapon(lancer);  
        //chonBota.setWeapon(lancer);

        // ENEMY LVL1 (SQUIRTLE)
        Agent chonBot = new Agent(920, 440, 90, 65, 1, 500, "/images/agents/chonBot.png", true, true);
        AnimationSpritesheet idleChonBot = new SimpleAnimationSpritesheet(90, 90, 6, 1000, "/images/agents/venusaur_walk.png");
        AnimationSpritesheet runChonBot = new SimpleAnimationSpritesheet(90, 90, 5, 500, "/images/agents/venusaur_walk.png");
        AnimationSpritesheet jumpChonBot = new SimpleAnimationSpritesheet(90, 90, 2, 500, "/images/agents/venusaur_walk.png");
        AnimationSpritesheet attackChonBot = new SimpleAnimationSpritesheet(90, 90  , 5, 360, "/images/agents/venusaur_attack.png");
        AnimationSpritesheet hitChonBot = new SimpleAnimationSpritesheet(90, 90, 2, 100, "/images/agents/venusaur_dmg.png");
        AnimationSpritesheet dieChonBot = new SimpleAnimationSpritesheet(45, 45, 2, 1000, "/images/agents/squirtle_death.png");

        AnimationGraphics chonBotGraphics = new AnimationGraphics();
        chonBotGraphics.addSpritesheet(AnimationStatus.IDLE, idleChonBot);
        chonBotGraphics.addSpritesheet(AnimationStatus.RUNNING, runChonBot);
        chonBotGraphics.addSpritesheet(AnimationStatus.ATTACKING, attackChonBot);
        chonBotGraphics.addSpritesheet(AnimationStatus.HIT, hitChonBot);
        chonBotGraphics.addSpritesheet(AnimationStatus.DYING, dieChonBot);
        chonBotGraphics.addSpritesheet(AnimationStatus.JUMPING, jumpChonBot);

        AnimationSystem chonBotSystem = new AnimationSystem(chonBotGraphics);
        chonBot.setAnimationSystem(chonBotSystem);
        
        chonBot.setCloseWeapon(sword2);
        environment.setProtagonist(chonBota);

        chonBot.setEnemy(true);
        //enemy.setEnemy(true) ;
        environment.setPauseImage("/images/environment/pause.png") ;
        environment.setGameOverImage("/images/environment/gameover.png");   

        level1.getAgents().add(chonBot);
        //level2.getAgents().add(enemy);


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

        MenuTextManager.getInstance().setText(MainOption.START_GAME, "Aventura");
        MenuTextManager.getInstance().setText(MainOption.EXIT, "Sair");
        MenuTextManager.getInstance().setText(PauseOption.RESUME, "Continuar");
        MenuTextManager.getInstance().setText(PauseOption.GO_BACK_TO_MENU, "Voltar para o Menu");
    }

}
