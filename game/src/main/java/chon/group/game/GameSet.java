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
import chon.group.game.domain.weapon.LancerCell;
import chon.group.game.domain.weapon.LancerCellJr;
import chon.group.game.domain.weapon.LancerGohan;
import chon.group.game.core.menu.MenuTextManager;

public class GameSet {

    
    
    public static Agent cell1;
    public static Agent cell2;
    public static Agent cell3;
    
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
    
    
    public static Agent getCell1() {
        return cell1;
    }

    public static Agent getCell2() {
        return cell2;
    }

    public static Agent getCell3() {
        return cell3;
    }


    private void load() {
        /* Define some size properties for both Canvas and Environment */
        this.canvasWidth = 1280;
        this.canvasHeight = 780;

        /** Define a general panel for life, energy, points, and objects. */
        panel = new Panel(240, 110);

        /* Initialize the game environment, levels, agents and weapons */
        Level level1 = new Level(0, 0, canvasHeight, 3000, "/images/environment/bg2.png");
        Level level2 = new Level(0, 0, canvasHeight, 3000, "/images/environment/bg2.png");
        Level level3 = new Level(0, 0, canvasHeight, 3000, "/images/environment/bg3.png");

        /* Set background music for each level */
        level1.setBackgroundMusic(Game.gameMusic);
        level2.setBackgroundMusic(Game.gameMusic);
        level3.setBackgroundMusic(Game.gameMusic);

        environment = new Environment(this.canvasHeight, level1.getWidth(), this.canvasWidth, panel);

        
        Agent gohan = new Agent(400, 390, 90, 65, 5, 1000, "/images/agents/chonBota.png", false, false);
        AnimationSpritesheet idleGohan = new SimpleAnimationSpritesheet(105, 129, 2, 750,
                "/images/agents/Gohan/idle_Gohan.png");
        AnimationSpritesheet runGohan = new SimpleAnimationSpritesheet(99, 126, 2, 750,
                "/images/agents/Gohan/run_Gohan.png");
        AnimationSpritesheet attackGohan = new SimpleAnimationSpritesheet(276, 144, 3, 300,
                "/images/agents/Gohan/attack_Gohan.png");
        AnimationSpritesheet hurtGohan = new SimpleAnimationSpritesheet(108, 153, 4, 750,
                "/images/agents/Gohan/hurt_Gohan.png");
        AnimationSpritesheet deathGohan = new SimpleAnimationSpritesheet(150, 153, 5, 750,
                "/images/agents/Gohan/death_Gohan.png");

        AnimationGraphics gohanGraphics = new AnimationGraphics();
        gohanGraphics.addSpritesheet(AnimationStatus.IDLE, idleGohan);
        gohanGraphics.addSpritesheet(AnimationStatus.RUNNING, runGohan);
        gohanGraphics.addSpritesheet(AnimationStatus.ATTACKING, attackGohan);
        gohanGraphics.addSpritesheet(AnimationStatus.HIT, hurtGohan);
        gohanGraphics.addSpritesheet(AnimationStatus.DYING, deathGohan);
        AnimationSystem gohanSystem = new AnimationSystem(gohanGraphics);
        gohan.setAnimationSystem(gohanSystem);

        Weapon lancer = new LancerGohan(400, 390, 0, 0, 3, 0, 0.05, "", false);
        Weapon lancer3 = new LancerCell(400, 390, 0, 0, 3, 0, 0.05, "", false);
        gohan.setWeapon(lancer);
        environment.setProtagonist(gohan); 

        

        int[][] cellJrPositions = {
                { 920, 240 },
                { 1800, 750 },
        };
        Weapon lancer2 = new LancerCellJr(400, 390, 0, 0, 3, 0, 0.05, "", false);

        
        for (int i = 0; i < cellJrPositions.length; i++) {
            Agent cellJr = new Agent(cellJrPositions[i][0], cellJrPositions[i][1], 90, 65, 1, 400,
                    "/images/agents/chonBot.png", true, false);
            AnimationSpritesheet runCellJr = new SimpleAnimationSpritesheet(105, 120, 2, 750,
                    "/images/agents/CellJr/walk_cellJR.png");
            AnimationSpritesheet attackCellJr = new SimpleAnimationSpritesheet(285, 126, 3, 300,
                    "/images/agents/CellJr/attack_cellJR.png");
            AnimationSpritesheet hurtCellJr = new SimpleAnimationSpritesheet(105, 153, 4, 750,
                    "/images/agents/CellJr/hurt_cellJR.png");
            AnimationSpritesheet deathCellJr = new SimpleAnimationSpritesheet(135, 153, 5, 750,
                    "/images/agents/CellJr/death_cellJR.png");

            AnimationGraphics cellJrGraphics = new AnimationGraphics();
            cellJrGraphics.addSpritesheet(AnimationStatus.IDLE, runCellJr);
            cellJrGraphics.addSpritesheet(AnimationStatus.RUNNING, runCellJr);
            cellJrGraphics.addSpritesheet(AnimationStatus.ATTACKING, attackCellJr);
            cellJrGraphics.addSpritesheet(AnimationStatus.HIT, hurtCellJr);
            cellJrGraphics.addSpritesheet(AnimationStatus.DYING, deathCellJr);
            AnimationSystem cellJrSystem = new AnimationSystem(cellJrGraphics);
            cellJr.setAnimationSystem(cellJrSystem);
            cellJr.setWeapon(lancer2);
            cellJr.setEnemy(true);
            level1.getAgents().add(cellJr); 
        }

        
        for (int i = 0; i < cellJrPositions.length; i++) {
            Agent cellJr = new Agent(cellJrPositions[i][0], cellJrPositions[i][1], 90, 65, 1, 400,
                    "/images/agents/chonBot.png", true, false);
            AnimationSpritesheet runCellJr = new SimpleAnimationSpritesheet(105, 120, 2, 750,
                    "/images/agents/CellJr/walk_cellJR.png");
            AnimationSpritesheet attackCellJr = new SimpleAnimationSpritesheet(285, 126, 3, 300,
                    "/images/agents/CellJr/attack_cellJR.png");
            AnimationSpritesheet hurtCellJr = new SimpleAnimationSpritesheet(105, 153, 4, 750,
                    "/images/agents/CellJr/hurt_cellJR.png");
            AnimationSpritesheet deathCellJr = new SimpleAnimationSpritesheet(135, 153, 5, 750,
                    "/images/agents/CellJr/death_cellJR.png");

            AnimationGraphics cellJrGraphics = new AnimationGraphics();
            cellJrGraphics.addSpritesheet(AnimationStatus.IDLE, runCellJr);
            cellJrGraphics.addSpritesheet(AnimationStatus.RUNNING, runCellJr);
            cellJrGraphics.addSpritesheet(AnimationStatus.ATTACKING, attackCellJr);
            cellJrGraphics.addSpritesheet(AnimationStatus.HIT, hurtCellJr);
            cellJrGraphics.addSpritesheet(AnimationStatus.DYING, deathCellJr);
            AnimationSystem cellJrSystem = new AnimationSystem(cellJrGraphics);
            cellJr.setAnimationSystem(cellJrSystem);
            cellJr.setWeapon(lancer2);
            cellJr.setEnemy(true);
            level2.getAgents().add(cellJr); 
        }

        
        cell1 = new Agent(920, 440, 90, 65, 1, 600, "/images/agents/chonBot.png", true, false);
        AnimationSpritesheet runCell1 = new SimpleAnimationSpritesheet(96, 132, 2, 750,
                "/images/agents/Cell1/cell1_run.png");
        AnimationSpritesheet attackCell1 = new SimpleAnimationSpritesheet(279, 129, 3, 300,
                "/images/agents/Cell1/cell1_attack.png");
        AnimationSpritesheet hurtCell1 = new SimpleAnimationSpritesheet(141, 144, 4, 750,
                "/images/agents/Cell1/cell1_hurt.png");
        AnimationSpritesheet chargingCell1 = new SimpleAnimationSpritesheet(192, 168, 3, 750,
                "/images/agents/Cell1/cell1_charging.png");

        AnimationGraphics cell1Graphics = new AnimationGraphics();
        cell1Graphics.addSpritesheet(AnimationStatus.IDLE, runCell1);
        cell1Graphics.addSpritesheet(AnimationStatus.RUNNING, runCell1);
        cell1Graphics.addSpritesheet(AnimationStatus.ATTACKING, attackCell1);
        cell1Graphics.addSpritesheet(AnimationStatus.HIT, hurtCell1);
        cell1Graphics.addSpritesheet(AnimationStatus.DYING, hurtCell1); 
        cell1Graphics.addSpritesheet(AnimationStatus.CHARGING, chargingCell1);
        AnimationSystem cell1System = new AnimationSystem(cell1Graphics);
        cell1.setAnimationSystem(cell1System);
        cell1.setWeapon(lancer3);
        cell1.setEnemy(true);

        
        cell2 = new Agent(920, 440, 90, 65, 1, 800, "/images/agents/chonBot.png", true, false);
        AnimationSpritesheet runCell2 = new SimpleAnimationSpritesheet(95, 132, 2, 750,
                "/images/agents/Cell2/cell2_run.png");
        AnimationSpritesheet attackCell2 = new SimpleAnimationSpritesheet(279, 129, 3, 300,
                "/images/agents/Cell2/cell2_attack.png");
        AnimationSpritesheet hurtCell2 = new SimpleAnimationSpritesheet(140, 144, 4, 750,
                "/images/agents/Cell2/cell2_hurt.png");
        AnimationSpritesheet chargingCell2 = new SimpleAnimationSpritesheet(192, 168, 3, 750,
                "/images/agents/Cell2/cell2_charging.png");

        AnimationGraphics cell2Graphics = new AnimationGraphics();
        cell2Graphics.addSpritesheet(AnimationStatus.IDLE, runCell2);
        cell2Graphics.addSpritesheet(AnimationStatus.RUNNING, runCell2);
        cell2Graphics.addSpritesheet(AnimationStatus.ATTACKING, attackCell2);
        cell2Graphics.addSpritesheet(AnimationStatus.HIT, hurtCell2);
        cell2Graphics.addSpritesheet(AnimationStatus.DYING, hurtCell2); 
        cell2Graphics.addSpritesheet(AnimationStatus.CHARGING, chargingCell2);
        AnimationSystem cell2System = new AnimationSystem(cell2Graphics);
        cell2.setAnimationSystem(cell2System);
        cell2.setWeapon(lancer3);
        cell2.setEnemy(true);

        
        cell3 = new Agent(920, 440, 90, 65, 1, 1000, "/images/agents/chonBot.png", true, false);
        AnimationSpritesheet runCell3 = new SimpleAnimationSpritesheet(95, 132, 2, 750,
                "/images/agents/Cell3/cell3_run.png");
        AnimationSpritesheet attackCell3 = new SimpleAnimationSpritesheet(279, 129, 3, 300,
                "/images/agents/Cell3/cell3_attack.png");
        AnimationSpritesheet hurtCell3 = new SimpleAnimationSpritesheet(141, 144, 4, 750,
                "/images/agents/Cell3/cell3_hurt.png");
        AnimationSpritesheet deathCell3 = new SimpleAnimationSpritesheet(140, 144, 5, 750,
                "/images/agents/Cell3/cell3_death.png");
        AnimationSpritesheet chargingCell3 = new SimpleAnimationSpritesheet(192, 168, 3, 750,
                "/images/agents/Cell3/cell3_charging.png");

        AnimationGraphics cell3Graphics = new AnimationGraphics();
        cell3Graphics.addSpritesheet(AnimationStatus.IDLE, runCell3);
        cell3Graphics.addSpritesheet(AnimationStatus.RUNNING, runCell3);
        cell3Graphics.addSpritesheet(AnimationStatus.ATTACKING, attackCell3);
        cell3Graphics.addSpritesheet(AnimationStatus.HIT, hurtCell3);
        cell3Graphics.addSpritesheet(AnimationStatus.DYING, deathCell3);
        cell3Graphics.addSpritesheet(AnimationStatus.CHARGING, chargingCell3);
        AnimationSystem cell3System = new AnimationSystem(cell3Graphics);
        cell3.setAnimationSystem(cell3System);
        cell3.setWeapon(lancer3);
        cell3.setEnemy(true);

        level3.getAgents().add(cell1);

        environment.setPauseImage("/images/environment/pause.png");
        environment.setGameOverImage("/images/environment/gameover.png");

        
        List<Object> objects = new ArrayList<>();
        objects.add(new Object(1000, 350, 32, 32, 0, 0, "/images/agents/esfera4.png", false, false, true, false,
                false, 0));
        objects.add(new Object(2000, 50, 80, 90, 0, 0, "/images/agents/semente.png", false, false, true, false, false,
                0));
        objects.add(new Object(400, 680, 80, 90, 0, 0, "/images/agents/semente.png", false, false, true, false, false,
                0));
        objects.add(new Object(1300, 400, 32, 32, 0, 0, "/images/agents/esfera4.png", false, false, true, false,
                false, 0));

        List<Object> objects2 = new ArrayList<>();
        objects2.add(new Object(1000, 350, 80, 90, 0, 0, "/images/agents/semente.png", false, false, true, false,
                false, 0));
        objects2.add(
                new Object(2000, 50, 32, 32, 0, 0, "/images/agents/esfera4.png", false, false, true, false, false, 0));
        objects2.add(
                new Object(400, 680, 32, 32, 0, 0, "/images/agents/esfera4.png", false, false, true, false, false, 0));
        objects2.add(new Object(1300, 400, 80, 90, 0, 0, "/images/agents/semente.png", false, false, true, false,
                false, 0));

        level1.setObjects(objects);
        level1.countCollectibles();
        level2.setObjects(objects2);
        level2.countCollectibles();

        environment.getLevels().add(level1);
        environment.getLevels().add(level2);
        environment.getLevels().add(level3);
        environment.setCurrentLevel(level1);

        MenuTextManager.getInstance().setText(MainOption.START_GAME, "Start game");
        MenuTextManager.getInstance().setText(MainOption.EXIT, "Exit");
        MenuTextManager.getInstance().setText(PauseOption.RESUME, "Resume");
        MenuTextManager.getInstance().setText(PauseOption.GO_BACK_TO_MENU, "Go back to menu");
    }
}