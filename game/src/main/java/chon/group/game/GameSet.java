package chon.group.game;

import java.util.ArrayList;
import java.util.List;

import chon.group.game.core.agent.Agent;
import chon.group.game.core.agent.Object;
import chon.group.game.core.environment.Environment;
import chon.group.game.core.environment.Level;
import chon.group.game.core.weapon.Panel;
import chon.group.game.core.weapon.Weapon;
import chon.group.game.domain.weapon.Cannon;
import chon.group.game.domain.weapon.Lancer;

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
                3000,
                "/images/environment/bg1.png");

        Level level2 = new Level(
                0,
                0,
                canvasHeight,
                3000,
                "/images/environment/bg2.png");

        Level level3 = new Level(
                0,
                0,
                canvasHeight,
                3000,
                "/images/environment/bg3.png");
                

        environment = new Environment(  
                this.canvasHeight,
                level1.getWidth(),
                this.canvasWidth,
                panel);

        Agent darkMage = new Agent(300, 390, 180, 150, 3, 1000, "/images/agents/darkMage.png", false, false);
        Weapon cannon = new Cannon(400, 460, 0, 0, 3, 0, 0.05, "", false);
        Weapon lancer = new Lancer(400, 390, 0, 0, 3, 0, 0.05, "", false);

        darkMage.setWeapon(cannon);
        darkMage.setWeapon(lancer);

        Agent chonBot = new Agent(920, 440, 90, 65, 1,  500, "/images/agents/chonBot.png", true, true);
        environment.setProtagonist(darkMage);
        environment.setPauseImage("/images/environment/pause.png");
        environment.setGameOverImage("/images/environment/gameover.png");

        Agent chonBot1 = new Agent(600, 440, 90, 65, 1,  500, "/images/agents/chonBot.png", true, true);
        environment.setProtagonist(darkMage);
        environment.setPauseImage("/images/environment/pause.png");
        environment.setGameOverImage("/images/environment/gameover.png");

        level1.getAgents().add(chonBot);
        level1.getAgents().add(chonBot1);
        level2.getAgents().add(chonBot);
        level2.getAgents().add(chonBot1);

        /* Set up some collectable objects */
        List<Object> objects = new ArrayList<>();
        objects.add(new Object(200, 350, 32, 32, 0, 0, "/images/agents/coin.png", false, false, true, false));
        objects.add(new Object(400, 380, 32, 32, 0, 0, "/images/agents/coin.png", false, false, true, false));
        objects.add(new Object(1000, 600, 32, 32, 0, 0, "/images/agents/coin.png", false, false, true, false));
        objects.add(new Object(1400, 380, 32, 32, 0, 0, "/images/agents/coin.png", false, false, true, false));
        objects.add(new Object(1800, 650, 32, 32, 0, 0, "/images/agents/coin.png", false, false, true, false));
        objects.add(new Object(2000, 580, 32, 32, 0, 0, "/images/agents/coin.png", false, false, true, false));
        objects.add(new Object(2300, 380, 32, 32, 0, 0, "/images/agents/coin.png", false, false, true, false));
        objects.add(new Object(2600, 500, 32, 32, 0, 0, "/images/agents/coin.png", false, false, true, false));
        objects.add(new Object(2900, 380, 32, 32, 0, 0, "/images/agents/coin.png", false, false, true, false));
        objects.add(new Object(3200, 400, 32, 32, 0, 0, "/images/agents/coin.png", false, false, true, false));
        objects.add(new Object(4100, 500, 32, 32, 0, 0, "/images/agents/coin.png", false, false, true, false));
        objects.add(new Object(5000, 380, 32, 32, 0, 0, "/images/agents/coin.png", false, false, true, false));
        objects.add(new Object(6200, 400, 32, 32, 0, 0, "/images/agents/coin.png", false, false, true, false));

        // Register objects into the environment and count total collectibles
        level1.setObjects(objects);
        level1.countCollectibles();
        environment.getLevels().add(level1);
        environment.getLevels().add(level2);
        environment.getLevels().add(level3);
        environment.setCurrentLevel(level1);
    }

}
