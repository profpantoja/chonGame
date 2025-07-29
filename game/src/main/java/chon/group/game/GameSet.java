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
                900,
                1600,
                "/images/environment/dungeon.png");

        Level level2 = new Level(
                0,
                0,
                canvasHeight,
                1280,
                "/images/environment/cave.png");
        
        Level level3 = new Level(
                0,
                0,
                canvasHeight,
                1280,
                "/images/environment/cave2.png");

        Level level4 = new Level(
                0,
                0,
                canvasHeight,
                3500,
                "/images/environment/abyss.png");       
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

        Weapon lancer = new Lancer(400, 390, 0, 0, 3, 0, 0.05, "", false);
        CloseWeapon sword = new Sword(400, 390, 0, 0, 3, 0,  "", false);
        CloseWeapon swordBoss = new Sword(400, 390, 0, 0, 3, 0,  "", false);

        
        protagonist.setCloseWeapon(sword);

        // Sets for the enemy

        Agent shooter = new Agent(1000, 200, 70, 80, 1, 500, "/images/agents/shooter/shooter.png", true, true);

        // Animações do ShooterEnemy
        AnimationSpritesheet idleShooter = new SimpleAnimationSpritesheet(80, 70, 6, 1000, "/images/agents/shooter/Shooter_idle_animation.png");
        AnimationSpritesheet walkShooter = new SimpleAnimationSpritesheet(80, 70, 6, 600, "/images/agents/shooter/Shooter_walk_animation.png");
        AnimationSpritesheet attackShooter = new SimpleAnimationSpritesheet(80, 70, 6, 500, "/images/agents/shooter/Shooter_attack_animation.png");
        AnimationSpritesheet hitShooter = new SimpleAnimationSpritesheet(80, 70, 1, 400, "/images/agents/shooter/Shooter_damage_animation.png");
        AnimationSpritesheet deathShooter = new SimpleAnimationSpritesheet(80, 70, 3, 1000, "/images/agents/shooter/Shooter_death_animation.png");
        AnimationSpritesheet jumpShooter = new SimpleAnimationSpritesheet(80, 70, 2, 1000, "/images/agents/shooter/Shooter_jump_animation.png");

        AnimationGraphics shooterGraphics = new AnimationGraphics();
        shooterGraphics.addSpritesheet(AnimationStatus.IDLE, idleShooter);
        shooterGraphics.addSpritesheet(AnimationStatus.RUNNING, walkShooter);
        shooterGraphics.addSpritesheet(AnimationStatus.ATTACKING, attackShooter);
        shooterGraphics.addSpritesheet(AnimationStatus.HIT, hitShooter);
        shooterGraphics.addSpritesheet(AnimationStatus.DYING, deathShooter);
        shooterGraphics.addSpritesheet(AnimationStatus.JUMPING, jumpShooter);

        AnimationSystem shooterSystem = new AnimationSystem(shooterGraphics);
        shooter.setAnimationSystem(shooterSystem);
        shooter.setEnemy(true);
        shooter.setWeapon(lancer);


        // Sets for the enemy

        Agent skeleton = new Agent(700, 440, 64, 64, 1, 500, "/images/agents/skeleton/sk_walk_animation.png", true, true);

        // Animações do SkeletonEnemy
        AnimationSpritesheet idleSkeleton = new SimpleAnimationSpritesheet(64, 64, 5, 1000, "/images/agents/skeleton/sk_walk_animation.png");
        AnimationSpritesheet walkSkeleton = new SimpleAnimationSpritesheet(64, 64, 5, 1000, "/images/agents/skeleton/sk_walk_animation.png");
        AnimationSpritesheet attackSkeleton = new SimpleAnimationSpritesheet(64, 64, 5, 1000, "/images/agents/skeleton/sk_walk_animation.png");
        AnimationSpritesheet hitSkeleton = new SimpleAnimationSpritesheet(64, 64, 5, 1000, "/images/agents/skeleton/sk_walk_animation.png");
        AnimationSpritesheet deathSkeleton = new SimpleAnimationSpritesheet(64, 64, 5, 1000, "/images/agents/skeleton/sk_walk_animation.png");
        AnimationSpritesheet jumpSkeleton = new SimpleAnimationSpritesheet(64, 64, 5, 1000, "/images/agents/skeleton/sk_walk_animation.png");

        AnimationGraphics skeletonGraphics = new AnimationGraphics();
        skeletonGraphics.addSpritesheet(AnimationStatus.IDLE, idleSkeleton);
        skeletonGraphics.addSpritesheet(AnimationStatus.RUNNING, walkSkeleton);
        skeletonGraphics.addSpritesheet(AnimationStatus.ATTACKING, attackSkeleton);
        skeletonGraphics.addSpritesheet(AnimationStatus.HIT, hitSkeleton);
        skeletonGraphics.addSpritesheet(AnimationStatus.DYING, deathSkeleton);
        skeletonGraphics.addSpritesheet(AnimationStatus.JUMPING, jumpSkeleton);

        AnimationSystem skeletonSystem = new AnimationSystem(skeletonGraphics);
        skeleton.setAnimationSystem(skeletonSystem);
        skeleton.setEnemy(true);

        // Sets for the enemy

        Agent skeletonWH = new Agent(1010, 700, 62, 70, 1, 500, "/images/agents/skeleton/skeleton_wh_animation.png", true, true);

        // Animações do SkeletonEnemy Without
        AnimationSpritesheet idleSkeletonWH = new SimpleAnimationSpritesheet(70, 62, 6, 1000, "/images/agents/skeleton/skeleton_wh_animation.png");
        AnimationSpritesheet walkSkeletonWH = new SimpleAnimationSpritesheet(70, 62, 6, 1000, "/images/agents/skeleton/skeleton_wh_animation.png");
        AnimationSpritesheet attackSkeletonWH = new SimpleAnimationSpritesheet(70, 62, 6, 1000, "/images/agents/skeleton/skeleton_wh_animation.png");
        AnimationSpritesheet hitSkeletonWH = new SimpleAnimationSpritesheet(70, 62, 6, 1000, "/images/agents/skeleton/skeleton_wh_animation.png");
        AnimationSpritesheet deathSkeletonWH = new SimpleAnimationSpritesheet(70, 62, 6, 1000, "/images/agents/skeleton/skeleton_wh_animation.png");
        AnimationSpritesheet jumpSkeletonWH = new SimpleAnimationSpritesheet(70, 62, 6, 1000, "/images/agents/skeleton/skeleton_wh_animation.png");

        AnimationGraphics skeletonWHGraphics = new AnimationGraphics();
        skeletonWHGraphics.addSpritesheet(AnimationStatus.IDLE, idleSkeletonWH);
        skeletonWHGraphics.addSpritesheet(AnimationStatus.RUNNING, walkSkeletonWH);
        skeletonWHGraphics.addSpritesheet(AnimationStatus.ATTACKING, attackSkeletonWH);
        skeletonWHGraphics.addSpritesheet(AnimationStatus.HIT, hitSkeletonWH);
        skeletonWHGraphics.addSpritesheet(AnimationStatus.DYING, deathSkeletonWH);
        skeletonWHGraphics.addSpritesheet(AnimationStatus.JUMPING, jumpSkeletonWH);

        AnimationSystem skeletonWHSystem = new AnimationSystem(skeletonWHGraphics);
        skeletonWH.setAnimationSystem(skeletonWHSystem);
        skeletonWH.setEnemy(true);

        // Sets for the enemy

        Agent boss = new Agent(1500, 440, 70, 88, 1, 1000, "/images/agents/boss/rico.png", true, true);

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
        level1.getAgents().add(skeleton);
        level1.getAgents().add(skeletonWH);
        level1.getAgents().add(shooter);
        level2.getAgents().add(skeletonWH);
        level2.getAgents().add(shooter);
        level3.getAgents().add(skeletonWH);
        level3.getAgents().add(shooter);
        level3.getAgents().add(shooter);
        level4.getAgents().add(boss);



        environment.setProtagonist(protagonist);
        environment.setPauseImage("/images/environment/pause.png") ;
        environment.setGameOverImage("/images/environment/gameover.png");   



        /* Set up some collectable objects */
        List<Object> objects = new ArrayList<>();
        AnimationSpritesheet crownAnimated = new SimpleAnimationSpritesheet(17, 17, 6, 200, "/images/agents/crownAnimated.png");
        objects.add(new Object(900, 540, 32, 32, 0, 0, "/images/agents/crown.png", false, false, true, false, crownAnimated));
        objects.add(new Object(1000, 495, 32, 32, 0, 0, "/images/agents/crown.png", false, false, true, false, crownAnimated));
        objects.add(new Object(400, 305, 32, 32, 0, 0, "/images/agents/crown.png", false, false, true, false, crownAnimated));
        objects.add(new Object(850, 545, 32, 32, 0, 0, "/images/agents/crown.png", false, false, true, false, crownAnimated));
        objects.add(new Object(180, 295, 32, 32, 0, 0, "/images/agents/crown.png", false, false, true, false, crownAnimated));
        objects.add(new Object(650, 395, 32, 32, 0, 0, "/images/agents/crown.png", false, false, true, false,crownAnimated));
        objects.add(new Object(730, 495, 32, 32, 0, 0, "/images/agents/crown.png", false, false, true, false, crownAnimated));

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

}
