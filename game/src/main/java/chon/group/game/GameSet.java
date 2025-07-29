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
                canvasHeight,
                1280,
                "/images/environment/fase1.png");

        Level level2 = new Level(
                0,
                0,
                canvasHeight,
                1280,
                "/images/environment/fase3.png");

        Level level3 = new Level(
        0,
        0,
        canvasHeight,
        1280,
        "/images/environment/fasefinal.png");

        /* Set background music for each level */
        level1.setBackgroundMusic(Game.gameMusic1);
        level2.setBackgroundMusic(Game.gameMusic2);
        level3.setBackgroundMusic(Game.gameMusic3);

        environment = new Environment(
                this.canvasHeight,
                level1.getWidth(),
                this.canvasWidth,
                panel);

                // --- WOODY (protagonista) ---
            Agent woody = new Agent(400, 390, 90, 65, 5, 1000, "/images/agents/Woody_Idle.png", false, false);
            AnimationGraphics woodyGraphics = new AnimationGraphics();
        
            // Idle
            AnimationSpritesheet woodyIdle = new SimpleAnimationSpritesheet(50, 70, 2, 1500, "/images/agents/Woody_Idle.png");
            woodyGraphics.add(AnimationStatus.IDLE, woodyIdle);
        
            // Walk
            AnimationSpritesheet woodyWalk = new SimpleAnimationSpritesheet(50, 70, 3, 120, "/images/agents/Woody_Move.png");
            woodyGraphics.add(AnimationStatus.RUNNING, woodyWalk);
        
            // Attack
            AnimationSpritesheet woodyAttack = new SimpleAnimationSpritesheet(50, 70, 2, 100, "/images/agents/Woody_attack.png");
            woodyGraphics.add(AnimationStatus.ATTACKING, woodyAttack);
        
            // Hit
            AnimationSpritesheet woodyHit = new SimpleAnimationSpritesheet(50, 70, 2, 100, "/images/agents/Woody_damage.png");
            woodyGraphics.add(AnimationStatus.HIT, woodyHit);
        
            // Death
            AnimationSpritesheet woodyDeath = new SimpleAnimationSpritesheet(50, 70, 2, 150, "/images/agents/Woody_death.png");
            woodyGraphics.add(AnimationStatus.DYING, woodyDeath);
        
            AnimationSystem woodyAnimationSystem = new AnimationSystem(woodyGraphics);
            woody.setAnimationSystem(woodyAnimationSystem);
        
                // --- ZECA (inimigo) ---
            Agent zeca = new Agent(920, 440, 100, 75, 1, 500, "/images/agents/zeca_idle.png", true, true);
            AnimationGraphics zecaGraphics = new AnimationGraphics();
        
            // Idle
            AnimationSpritesheet zecaIdle = new SimpleAnimationSpritesheet(50, 65, 2, 200, "/images/agents/zeca_idle.png");
            zecaGraphics.add(AnimationStatus.IDLE, zecaIdle);
        
            // Walk
            AnimationSpritesheet zecaWalk = new SimpleAnimationSpritesheet(50, 65, 3, 120, "/images/agents/zeca_walk.png");
            zecaGraphics.add(AnimationStatus.RUNNING, zecaWalk);
        
            // Attack
            AnimationSpritesheet zecaAttack = new SimpleAnimationSpritesheet(50, 65, 3, 200, "/images/agents/zeca_attack.png");
            zecaGraphics.add(AnimationStatus.ATTACKING, zecaAttack);
        
            // Hit
            AnimationSpritesheet zecaHit = new SimpleAnimationSpritesheet(56, 75, 2, 1500, "/images/agents/zeca_damage.png");
            zecaGraphics.add(AnimationStatus.HIT, zecaHit);
        
            // Death
            AnimationSpritesheet zecaDeath = new SimpleAnimationSpritesheet(92, 59, 2, 150, "/images/agents/zeca_death.png");
            zecaGraphics.add(AnimationStatus.DYING, zecaDeath);
        
            AnimationSystem zecaAnimationSystem = new AnimationSystem(zecaGraphics);
            zeca.setAnimationSystem(zecaAnimationSystem);
        
         
            environment.setProtagonist(woody);
            level1.getAgents().add(zeca);
            level2.getAgents().add(zeca);
            level3.getAgents().add(zeca);
                
        Weapon cannon = new Cannon(400, 390, 0, 0, 3, 0, 0.05, "", false);
        Weapon lancer = new Lancer(400, 390, 0, 0, 3, 0, 0.05, "", false);
        Weapon lancer2 = new Lancer(400, 390, 0, 0, 3, 0, 0.05, "", false);
        CloseWeapon sword = new Sword(400, 390, 0, 0, 3, 0,  "", false);

        
        woody.setCloseWeapon(sword);
        //chonBota.setWeapon(lancer);


        zeca.setEnemy(true);
        //enemy.setEnemy(true) ;
        environment.setPauseImage("/images/environment/pause.png") ;
        environment.setGameOverImage("/images/environment/gameover.png");   

        level1.getAgents().add(zeca);
        level2.getAgents().add(zeca);
        level3.getAgents().add(zeca);
        //level2.getAgents().add(enemy);


        /* Set up some collectable objects */
        List<Object> objects = new ArrayList<>();
        objects.add(new Object(0, 0, 250, 1280, 0, 0, "", false, false, false, false, true, 0));
        List<Object> objects2 = new ArrayList<>();
        objects2.add(new Object(0, 0, 590, 1280, 0, 0, "", false, false, false, false, true, 0));
        List<Object> objects3 = new ArrayList<>();
        objects3.add(new Object(0, 0, 600, 1280, 0, 0, "", false, false, false, false, true, 0));

        // Register objects into the environment and count total collectibles
        level1.setObjects(objects);
        level1.countCollectibles();
        environment.getLevels().add(level1);
        level2.setObjects(objects2);
        level2.countCollectibles();
        environment.getLevels().add(level2);
        environment.setCurrentLevel(level1);
        level3.setObjects(objects3);
        level3.countCollectibles();
        environment.getLevels().add(level3);
        

        MenuTextManager.getInstance().setText(MainOption.START_GAME, "Iniciar lutas");
        MenuTextManager.getInstance().setText(MainOption.EXIT, "Sair do jogo");
        MenuTextManager.getInstance().setText(PauseOption.RESUME, "Voltar ao jogo");
        MenuTextManager.getInstance().setText(PauseOption.GO_BACK_TO_MENU, "Voltar para o menu");
    }

}
