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
import chon.group.game.ui.Menu;

public class GameSet {

    private int canvasWidth;
    private int canvasHeight;
    private Environment environment;
    private Panel panel;
    private Menu mainMenu;

    public GameSet() {
        this.load();
    }

    public int getCanvasWidth() {
        return canvasWidth;
    }

    public int getCanvasHeight() {
        return canvasHeight;
    }

    public Environment getEnvironment() {
        return environment;
    }

    public Panel getPanel() {
        return panel;
    }

    public Menu getMainMenu() {
        return mainMenu;
    }

    private void load() {
        this.canvasWidth = 1280;
        this.canvasHeight = 780;

        panel = new Panel(240, 110);

        Level level1 = new Level(0, 0, canvasHeight, 3000, "/images/environment/bg1.png");
        Level level2 = new Level(0, 0, canvasHeight, 3000, "/images/environment/bg2.png");
        Level level3 = new Level(0, 0, canvasHeight, 3000, "/images/environment/bg3.png");

        environment = new Environment(this.canvasHeight, level1.getWidth(), this.canvasWidth, panel);

        Agent darkMage = new Agent(300, 390, 180, 150, 3, 1000, "/images/agents/darkMage.png", false, false);
        Weapon cannon = new Cannon(400, 460, 0, 0, 3, 0, 0.05, "", false);
        darkMage.setWeapon(cannon);

        environment.setProtagonist(darkMage);
        environment.setPauseImage("/images/environment/pause.png");
        environment.setGameOverImage("/images/environment/gameover.png");

        // Adiciona inimigos ao Nível 1
        level1.getAgents().add(new Agent(920, 440, 90, 65, 1, 500, "/images/agents/chonBot.png", true, true));
        level1.getAgents().add(new Agent(600, 440, 90, 65, 1, 500, "/images/agents/chonBot.png", true, true));

        // Adiciona NOVOS inimigos ao Nível 2 (CORRIGIDO)
        level2.getAgents().add(new Agent(920, 440, 90, 65, 1, 500, "/images/agents/chonBot.png", true, true));
        level2.getAgents().add(new Agent(600, 440, 90, 65, 1, 500, "/images/agents/chonBot.png", true, true));
        
        environment.getLevels().add(level1);
        environment.getLevels().add(level2);
        environment.getLevels().add(level3);
        environment.setCurrentLevel(level1);

        mainMenu = new Menu();
    }
}