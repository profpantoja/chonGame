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
import chon.group.game.core.weapon.Weapon;
import chon.group.game.core.weapon.Panel;
import chon.group.game.domain.weapon.Blaster;
import chon.group.game.domain.weapon.DL44;
import chon.group.game.domain.weapon.LightSaber;


public class GameSet {

    private int canvasWidth;
    private int canvasHeight;
    private Environment environment;
    private Panel panel;
    private static int weaponDecision = 2; // 1 for han solo, 2 for luke skywalker
    //adicionar o sistema de menu para trocar o personagem

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

    public static int getWeaponDecision() {
        return weaponDecision;
    }
    public void setWeaponDecision(int weaponDecision) {
        this.weaponDecision = weaponDecision;
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
                canvasHeight ,
                1920,
                "/images/environment/insideOfTheDeathStar.png");

        Level level2 = new Level(
                0,
                0,
                canvasHeight,
                1920,
                "/images/environment/insideOfTheDeathStar2.png");

        Level level3 = new Level(
                0,
                0,
                canvasHeight,
                1920,
                "/images/environment/palpatinesRoom.png");
        /* Set background music for each level */
        level1.setBackgroundMusic(Game.gameMusic);
        level3.setBackgroundMusic(Game.BossMusic);

        environment = new Environment(
                this.canvasHeight,
                level1.getWidth(),
                this.canvasWidth,
                panel);

        
        if(weaponDecision == 1) {//Adicionar sprites do Han Solo
            Agent scout = new Agent(400, 390, 120, 85, 5, 1000, "/images/agents/chonBot.png", false, false);
            AnimationSpritesheet idleScout = new SimpleAnimationSpritesheet(70, 120, 4, 1000, "/images/agents/scout/scoutIdleFlipped.png");
            AnimationSpritesheet runScout = new SimpleAnimationSpritesheet(70, 120, 4, 1000, "/images/agents/scout/scoutRunningFlipped.png");
            AnimationSpritesheet attackScout = new SimpleAnimationSpritesheet(100, 120, 6, 1000, "/images/agents/scout/scoutShootFlipped.png");
            AnimationSpritesheet hitScout = new SimpleAnimationSpritesheet(90, 120, 4, 1000, "/images/agents/scout/scoutHitFlipped.png");

            Weapon blaster = new DL44(400, 390, 0, 0, 3, 0, 0.05, "", false);
            scout.setWeapon(blaster);
            AnimationGraphics scoutGraphics = new AnimationGraphics();
            scoutGraphics.addSpritesheet(AnimationStatus.IDLE, idleScout);
            scoutGraphics.addSpritesheet(AnimationStatus.RUNNING, runScout);
            scoutGraphics.addSpritesheet(AnimationStatus.ATTACKING, attackScout);
            scoutGraphics.addSpritesheet(AnimationStatus.HIT, hitScout);
            AnimationSystem scoutSystem = new AnimationSystem(scoutGraphics);
            scout.setAnimationSystem(scoutSystem);
            environment.setProtagonist(scout);

        } else if(weaponDecision == 2){
            Agent luke = new Agent(400, 390, 120, 85, 5, 1000, "/images/agents/luke.png", false, false);
            AnimationSpritesheet idleLuke = new SimpleAnimationSpritesheet(90, 170, 6, 1000, "/images/agents/luke/lukeIdle.png");
            AnimationSpritesheet runLuke = new SimpleAnimationSpritesheet(90, 170, 7, 1000, "/images/agents/luke/lukeRunning.png");
            AnimationSpritesheet attackLuke = new SimpleAnimationSpritesheet(160, 170, 10, 300, "/images/agents/luke/lukeAttack.png");
            AnimationSpritesheet hitLuke = new SimpleAnimationSpritesheet(90, 170, 4, 1000, "/images/agents/luke/lukeHit.png");
            AnimationSpritesheet dyingLuke = new SimpleAnimationSpritesheet(100, 170, 2, 1000, "/images/agents/luke/lukeDead.png");
            CloseWeapon lightSaber = new LightSaber(400, 390, 0, 0, 3, 0, "", false);
            luke.setCloseWeapon(lightSaber);
            environment.setProtagonist(luke);
            AnimationGraphics lukeGraphics = new AnimationGraphics();
            lukeGraphics.addSpritesheet(AnimationStatus.IDLE, idleLuke);
            lukeGraphics.addSpritesheet(AnimationStatus.RUNNING, runLuke);
            lukeGraphics.addSpritesheet(AnimationStatus.ATTACKING, attackLuke);
            lukeGraphics.addSpritesheet(AnimationStatus.HIT, hitLuke);
            lukeGraphics.addSpritesheet(AnimationStatus.DYING, dyingLuke);
            AnimationSystem lukeSystem = new AnimationSystem(lukeGraphics);
            luke.setAnimationSystem(lukeSystem);
            environment.setProtagonist(luke);

        }

        //chonBota.setWeapon(lancer);

        // Sprites compartilhados
        AnimationSpritesheet idleScoutEnemy = new SimpleAnimationSpritesheet(70, 120, 4, 1000, "/images/agents/scout/scoutIdleFlipped.png");
        AnimationSpritesheet runScoutEnemy = new SimpleAnimationSpritesheet(70, 120, 4, 1000, "/images/agents/scout/scoutRunningFlipped.png");
        AnimationSpritesheet attackScoutEnemy = new SimpleAnimationSpritesheet(100, 120, 6, 1000, "/images/agents/scout/scoutShootFlipped.png");
        AnimationSpritesheet hitScoutEnemy = new SimpleAnimationSpritesheet(90, 120, 4, 1000, "/images/agents/scout/scoutHitFlipped.png");

        // Posições dispersas no cenário
        int[][] enemyPositions = {
            {920, 440},
            {1300, 400},
            {1600, 460},
            {2000, 420},
            {2500, 480}
        };

        for (int i = 0; i < 5; i++) {
            int posX = enemyPositions[i][0];
            int posY = enemyPositions[i][1];

            Agent scout = new Agent(posX, posY, 90, 65, 1, 500, "/images/agents/chonBot.png", false, false);
            Weapon blaster = new DL44(400, 390, 0, 0, 3, 0, 0.05, "", false);
            scout.setWeapon(blaster);
            scout.setShotCooldown(i*100+4000);
            AnimationGraphics scoutGraphics = new AnimationGraphics();
            scoutGraphics.addSpritesheet(AnimationStatus.IDLE, idleScoutEnemy);
            scoutGraphics.addSpritesheet(AnimationStatus.RUNNING, runScoutEnemy);
            scoutGraphics.addSpritesheet(AnimationStatus.ATTACKING, attackScoutEnemy);
            scoutGraphics.addSpritesheet(AnimationStatus.HIT, hitScoutEnemy);

            AnimationSystem scoutSystem = new AnimationSystem(scoutGraphics);
            scout.setAnimationSystem(scoutSystem);
            scout.setEnemy(true);

            level1.getAgents().add(scout);
        }
        for (int i = 0; i < 3; i++) {
            int posX = enemyPositions[i][0];
            int posY = enemyPositions[i][1];

            Agent scout = new Agent(posX, posY, 90, 65, 1, 500, "/images/agents/chonBot.png", false, false);
            Weapon blaster = new DL44(400, 390, 0, 0, 3, 0, 0.05, "", false);
            scout.setWeapon(blaster);
            scout.setShotCooldown(i*1000+4000);

            AnimationGraphics scoutGraphics = new AnimationGraphics();
            scoutGraphics.addSpritesheet(AnimationStatus.IDLE, idleScoutEnemy);
            scoutGraphics.addSpritesheet(AnimationStatus.RUNNING, runScoutEnemy);
            scoutGraphics.addSpritesheet(AnimationStatus.ATTACKING, attackScoutEnemy);
            scoutGraphics.addSpritesheet(AnimationStatus.HIT, hitScoutEnemy);

            AnimationSystem scoutSystem = new AnimationSystem(scoutGraphics);
            scout.setAnimationSystem(scoutSystem);
            scout.setEnemy(true);

            level2.getAgents().add(scout);
        }

        //enemy.setEnemy(true) ;
        environment.setPauseImage("/images/environment/pause.png") ;
        environment.setGameOverImage("/images/environment/gameover.png");   

        
        //Adicionar o Darth Vader como inimigo final

        //level2.getAgents().add(enemy);


        /* Set up some collectable objects */
        List<Object> objects = new ArrayList<>();//mudar o objeto
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
        level3.setObjects(objects);
        level3.countCollectibles();
        environment.getLevels().add(level3);
        environment.setCurrentLevel(level1);

        MenuTextManager.getInstance().setText(MainOption.START_GAME, "Start game");
        MenuTextManager.getInstance().setText(MainOption.EXIT, "Exit");
        MenuTextManager.getInstance().setText(PauseOption.RESUME, "Resume");
        MenuTextManager.getInstance().setText(PauseOption.GO_BACK_TO_MENU, "Go back to menu");
    }

}