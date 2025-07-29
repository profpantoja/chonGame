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

public class GameSet {

    private int canvasWidth;
    private int canvasHeight;
    private Environment environment;
    private Panel panel;
    private Agent agentBase;
    private static int weaponDecision = 1; // Default weapon decision for slash

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

    public static int getWeaponDecision() {
        return weaponDecision;
    }

    public static void setWeaponDecision(int weaponDecision) {
        GameSet.weaponDecision = weaponDecision;
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

    private void load(){ 
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
                2880,
                "/images/environment/map.png");

        Level level2 = new Level(
                0,
                0,
                canvasHeight,
                2880,
                "/images/environment/map2.png");
        
        Level level3 = new Level(
                0,
                0,
                canvasHeight,
                2880,
                "/images/environment/map3.png");

        Level level4 = new Level(
                0,
                0,
                canvasHeight,
                2880,
                "/images/environment/map4.png");       
        /* Set background music for each level */
        level1.setBackgroundMusic(Game.level1);
        level2.setBackgroundMusic(Game.level2);
        level3.setBackgroundMusic(Game.level3);
        level4.setBackgroundMusic(Game.level4);

        environment = new Environment(
                this.canvasHeight,
                level1.getWidth(),
                this.canvasWidth,
                panel);

        Agent protagonist = new Agent(200, 544, 88, 152, 5, 2000, "/images/agents/protagonist/protagonist.png", false, false);
        AnimationSpritesheet idleProtagonist = new SimpleAnimationSpritesheet(152, 88, 10, 2000, "/images/agents/protagonist/idle_animation.png");
        AnimationSpritesheet runProtagonist = new SimpleAnimationSpritesheet(152, 88, 6, 500, "/images/agents/protagonist/walk_animation.png");
        AnimationSpritesheet attackProtagonist = new SimpleAnimationSpritesheet(152, 88, 6, 200, "/images/agents/protagonist/attack_animation.png");
        AnimationSpritesheet hitProtagonist = new SimpleAnimationSpritesheet(152, 88, 1, 60, "/images/agents/protagonist/damage_animation.png");
        AnimationSpritesheet jumpProtagonist = new SimpleAnimationSpritesheet(152, 88, 7, 1000, "/images/agents/protagonist/jump_animation.png");
        AnimationSpritesheet deathProtagonist = new SimpleAnimationSpritesheet(152, 88, 5, 10000, "/images/agents/protagonist/death_animation.png");

        AnimationGraphics protagonistGraphics = new AnimationGraphics();
        protagonistGraphics.addSpritesheet(AnimationStatus.IDLE, idleProtagonist);
        protagonistGraphics.addSpritesheet(AnimationStatus.RUNNING, runProtagonist);
        protagonistGraphics.addSpritesheet(AnimationStatus.ATTACKING, attackProtagonist);
        protagonistGraphics.addSpritesheet(AnimationStatus.HIT, hitProtagonist);
        protagonistGraphics.addSpritesheet(AnimationStatus.JUMPING, jumpProtagonist);
        protagonistGraphics.addSpritesheet(AnimationStatus.DYING, deathProtagonist);
        AnimationSystem protagonistSystem = new AnimationSystem(protagonistGraphics);
        
        protagonist.setAnimationSystem(protagonistSystem);

        Weapon protagonistCannon = new Cannon(400, 390, 0, 0, 5, 0, 0.05, "", false);
        CloseWeapon protagonistSword = new Sword(400, 390, 0, 0, 3, 0,  "", false);
        CloseWeapon swordBoss = new Sword(400, 390, 0, 0, 3, 0,  "", false);

        switch (weaponDecision) {
            case 1:
                protagonist.setWeapon(protagonistCannon);
                break;
            case 2:
                protagonist.setCloseWeapon(protagonistSword);
                break;
            default:
                protagonist.setCloseWeapon(protagonistSword);
        }

        // Sets for the BOSS

        Agent boss = new Agent(1500, 440, 70, 88, 2, 10000, "/images/agents/boss/rico.png", true, true);

        // Animações do Boss Without
        AnimationSpritesheet bossIdle = new SimpleAnimationSpritesheet(88, 70, 6, 1000, "/images/agents/boss/Enrico_walk_animation.png");
        AnimationSpritesheet bossWalk = new SimpleAnimationSpritesheet(88, 70, 6, 1000, "/images/agents/boss/Enrico_walk_animation.png");
        AnimationSpritesheet bossAttack = new SimpleAnimationSpritesheet(88, 70, 8, 200, "/images/agents/boss/Enrico_attack_animation.png");
        AnimationSpritesheet bossHit = new SimpleAnimationSpritesheet(88, 70, 1, 1000, "/images/agents/boss/enrico_damage.png");
        AnimationSpritesheet bossDeath = new SimpleAnimationSpritesheet(88, 70, 4, 1000, "/images/agents/boss/Enrico_death_animation.png");
        AnimationSpritesheet bossJump = new SimpleAnimationSpritesheet(88, 70, 3, 1000, "/images/agents/boss/Enrico_jump_animation.png");

        AnimationGraphics bossGraphics = new AnimationGraphics();
        bossGraphics.addSpritesheet(AnimationStatus.IDLE, bossIdle);
        bossGraphics.addSpritesheet(AnimationStatus.RUNNING, bossWalk);
        bossGraphics.addSpritesheet(AnimationStatus.ATTACKING, bossAttack);
        bossGraphics.addSpritesheet(AnimationStatus.HIT, bossHit);
        bossGraphics.addSpritesheet(AnimationStatus.DYING, bossDeath);
        bossGraphics.addSpritesheet(AnimationStatus.JUMPING, bossJump);

        AnimationSystem bossSystem = new AnimationSystem(bossGraphics);
        boss.setAnimationSystem(bossSystem);
        boss.setEnemy(true);
        boss.setCloseWeapon(swordBoss);


        
        // Adiciona Inimigos ao level
    int[][] enemies = {
        // Level 1
        {1, 0, 1010, 550},
        {1, 0, 1200, 570},
        {1, 0, 1400, 600},
        {1, 0, 1600, 400},
        {1, 2, 800, 640},

        // Level 2
        {2, 1, 1300, 400},
        {2, 1, 400, 500},
        {2, 1, 800, 600},

        // Level 3
        {3, 1, 1200, 300},
        {3, 1, 700, 600},
        {3, 2, 1200, 359},
        {3, 2, 800, 400},
        {3, 0, 800, 540},
    };

    for (int[] enemy : enemies) {
        int level = enemy[0];
        int type = enemy[1];
        int x = enemy[2];
        int y = enemy[3];

        Agent a = null;

        switch (type) {
            case 0:
                a = createSkeletonWH(x, y);
                break;
            case 1:
                a = createShooter(x, y);
                break;
            case 2:
                a = createSkeleton(x, y);
                break;
        }

        if (a != null) {
            switch (level) {
                case 1:
                    level1.getAgents().add(a);
                    break;
                case 2:
                    level2.getAgents().add(a);
                    break;
                case 3:
                    level3.getAgents().add(a);
                    break;
            }
        }
    }

        // Level 4
        level4.getAgents().add(boss);



        environment.setProtagonist(protagonist);
        environment.setPauseImage("/images/environment/pause.png") ;
        environment.setGameOverImage("/images/environment/gameover.png");   



        /* Set up some collectable objects */
        List<Object> objects = new ArrayList<>();
        AnimationSpritesheet crownAnimated = new SimpleAnimationSpritesheet(17, 17, 6, 200, "/images/agents/crownAnimated.png");
        objects.add(new Object(900, 360, 32, 32, 0, 0, "/images/agents/crown.png", false, false, true, false, crownAnimated));
        objects.add(new Object(2000, 495, 32, 32, 0, 0, "/images/agents/crown.png", false, false, true, false, crownAnimated));
        objects.add(new Object(1400, 505, 32, 32, 0, 0, "/images/agents/crown.png", false, false, true, false, crownAnimated));
        objects.add(new Object(850, 645, 32, 32, 0, 0, "/images/agents/crown.png", false, false, true, false, crownAnimated));
        objects.add(new Object(1180, 495, 32, 32, 0, 0, "/images/agents/crown.png", false, false, true, false, crownAnimated));
        objects.add(new Object(1650, 595, 32, 32, 0, 0, "/images/agents/crown.png", false, false, true, false,crownAnimated));
        objects.add(new Object(330, 495, 32, 32, 0, 0, "/images/agents/crown.png", false, false, true, false, crownAnimated));

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
        
        level4.setObjects(objects);
        level4.countCollectibles();
        environment.getLevels().add(level4);
        environment.setCurrentLevel(level1);
        

        MenuTextManager.getInstance().setText(MainOption.START_GAME, "Start game");
        MenuTextManager.getInstance().setText(MainOption.EXIT, "Exit");
        MenuTextManager.getInstance().setText(PauseOption.RESUME, "Resume");
        MenuTextManager.getInstance().setText(PauseOption.GO_BACK_TO_MENU, "Go back to menu");
    }

    // Methods to create enemies
    private Agent createShooter(int x, int y) {
        Agent shooter = new Agent(x, y, 70, 80, 1, 5000, "/images/agents/shooter/shooter.png", true, true);

        AnimationGraphics graphics = new AnimationGraphics();
        graphics.addSpritesheet(AnimationStatus.IDLE, new SimpleAnimationSpritesheet(80, 70, 6, 1000, "/images/agents/shooter/Shooter_idle_animation.png"));
        graphics.addSpritesheet(AnimationStatus.RUNNING, new SimpleAnimationSpritesheet(80, 70, 6, 600, "/images/agents/shooter/Shooter_walk_animation.png"));
        graphics.addSpritesheet(AnimationStatus.ATTACKING, new SimpleAnimationSpritesheet(80, 70, 6, 500, "/images/agents/shooter/Shooter_attack_animation.png"));
        graphics.addSpritesheet(AnimationStatus.HIT, new SimpleAnimationSpritesheet(80, 70, 1, 400, "/images/agents/shooter/Shooter_damage_animation.png"));
        graphics.addSpritesheet(AnimationStatus.DYING, new SimpleAnimationSpritesheet(80, 70, 3, 1000, "/images/agents/shooter/Shooter_death_animation.png"));
        graphics.addSpritesheet(AnimationStatus.JUMPING, new SimpleAnimationSpritesheet(80, 70, 2, 1000, "/images/agents/shooter/Shooter_jump_animation.png"));

        shooter.setAnimationSystem(new AnimationSystem(graphics));
        shooter.setEnemy(true);
        shooter.setWeapon(new Lancer(400, 390, 0, 0, 3, 0, 0.05, "", false));
        return shooter;
    }
    private Agent createSkeleton(int x, int y) {
        Agent skeleton = new Agent(x, y, 64, 64, 1, 500, "/images/agents/skeleton/sk_walk_animation.png", true, true);

        AnimationSpritesheet anim = new SimpleAnimationSpritesheet(64, 64, 5, 1000, "/images/agents/skeleton/sk_walk_animation.png");

        AnimationGraphics graphics = new AnimationGraphics();
        graphics.addSpritesheet(AnimationStatus.IDLE, anim);
        graphics.addSpritesheet(AnimationStatus.RUNNING, anim);
        graphics.addSpritesheet(AnimationStatus.ATTACKING, anim);
        graphics.addSpritesheet(AnimationStatus.HIT, anim);
        graphics.addSpritesheet(AnimationStatus.DYING, anim);
        graphics.addSpritesheet(AnimationStatus.JUMPING, anim);

        skeleton.setAnimationSystem(new AnimationSystem(graphics));
        skeleton.setEnemy(true);
        return skeleton;
    }
    private Agent createSkeletonWH(int x, int y) {
        Agent skeletonWH = new Agent(x, y, 62, 70, 1, 500, "/images/agents/skeleton/skeleton_wh_animation.png", true, true);

        AnimationSpritesheet anim = new SimpleAnimationSpritesheet(70, 62, 6, 1000, "/images/agents/skeleton/skeleton_wh_animation.png");

        AnimationGraphics graphics = new AnimationGraphics();
        graphics.addSpritesheet(AnimationStatus.IDLE, anim);
        graphics.addSpritesheet(AnimationStatus.RUNNING, anim);
        graphics.addSpritesheet(AnimationStatus.ATTACKING, anim);
        graphics.addSpritesheet(AnimationStatus.HIT, anim);
        graphics.addSpritesheet(AnimationStatus.DYING, anim);
        graphics.addSpritesheet(AnimationStatus.JUMPING, anim);

        skeletonWH.setAnimationSystem(new AnimationSystem(graphics));
        skeletonWH.setEnemy(true);
        return skeletonWH;
    }


}
