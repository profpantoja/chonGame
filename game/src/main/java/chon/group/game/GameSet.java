package chon.group.game;

import java.util.ArrayList;
import java.util.List;

import chon.group.game.core.agent.Agent;
import chon.group.game.core.agent.Object;
import chon.group.game.core.environment.Environment;
import chon.group.game.core.weapon.Weapon;
import chon.group.game.domain.weapon.Cannon;
import chon.group.game.domain.weapon.Lancer;

public class GameSet {

    private int canvasWidth;
    private int canvasHeight;
    private Environment environment;

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

    private void load() {
        /* Define some size properties for both Canvas and Environment */
        this.canvasWidth = 1280;
        this.canvasHeight = 780;
        /* Initialize the game environment, agents and weapons */
        environment = new Environment(0, 0, 780, 8024,
                this.canvasWidth, "/images/environment/castleLong.png");
        Agent chonBota = new Agent(400, 390, 90, 65, 3, 1000, "/images/agents/chonBota.png", false);
        Weapon cannon = new Cannon(400, 390, 0, 0, 3, 0, 0.05, "", false);
        Weapon lancer = new Lancer(400, 390, 0, 0, 3, 0, 0.05, "", false);

        chonBota.setWeapon(cannon);
        chonBota.setWeapon(lancer);

        Agent chonBot = new Agent(920, 440, 90, 65, 1, 500, "/images/agents/chonBot.png", true);
        environment.setProtagonist(chonBota);
        environment.getAgents().add(chonBot);
        environment.setPauseImage("/images/environment/pause.png");
        environment.setGameOverImage("/images/environment/gameover.png");

        /* Set up some collectable objects */
        List<Object> objects = new ArrayList<>();
        objects.add(new Object(200, 350, 32, 32, "/images/agents/coin.png", true, false));
        objects.add(new Object(400, 380, 32, 32, "/images/agents/coin.png", true, false));
        objects.add(new Object(1000, 600, 32, 32, "/images/agents/coin.png", true, false));
        objects.add(new Object(1400, 380, 32, 32, "/images/agents/coin.png", true, false));
        objects.add(new Object(1800, 650, 32, 32, "/images/agents/coin.png", true, false));
        objects.add(new Object(2000, 580, 32, 32, "/images/agents/coin.png", true, false));
        objects.add(new Object(2300, 380, 32, 32, "/images/agents/coin.png", true, false));
        objects.add(new Object(2600, 500, 32, 32, "/images/agents/coin.png", true, false));
        objects.add(new Object(2900, 380, 32, 32, "/images/agents/coin.png", true, false));
        objects.add(new Object(3200, 400, 32, 32, "/images/agents/coin.png", true, false));
        objects.add(new Object(4100, 500, 32, 32, "/images/agents/coin.png", true, false));
        objects.add(new Object(5000, 380, 32, 32, "/images/agents/coin.png", true, false));
        objects.add(new Object(6200, 400, 32, 32, "/images/agents/coin.png", true, false));

        environment.setObjects(objects);
    }

}
