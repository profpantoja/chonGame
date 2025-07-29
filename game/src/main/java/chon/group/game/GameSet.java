package chon.group.game;

import chon.group.game.core.agent.Agent;
import chon.group.game.core.environment.Environment;
import chon.group.game.core.environment.Level;
import chon.group.game.core.weapon.Panel;
import chon.group.game.core.weapon.Weapon;
import chon.group.game.domain.weapon.MageStaff;
import chon.group.game.ui.Menu;
import java.util.Arrays;
import java.util.List;

public class GameSet {

    private int canvasWidth;
    private int canvasHeight;
    private Environment environment;
    private Panel panel;
    private Menu mainMenu;

    public GameSet() { this.load(); }
    public int getCanvasWidth() { return canvasWidth; }
    public int getCanvasHeight() { return canvasHeight; }
    public Environment getEnvironment() { return environment; }
    public Panel getPanel() { return panel; }
    public Menu getMainMenu() { return mainMenu; }

    private void load() {
        this.canvasWidth = 1280;
        this.canvasHeight = 780;
        panel = new Panel(240, 110);

        Level level1 = new Level(0, 0, canvasHeight, 3000, "/images/environment/bg1.png");
        Level level2 = new Level(0, 0, canvasHeight, 4000, "/images/environment/bg2.png");
        Level level3 = new Level(0, 0, canvasHeight, 5000, "/images/environment/bg3.png");
        
        // --- CONTAGEM DE INIMIGOS ATUALIZADA ---
        level1.setTotalEnemiesToSpawn(10);
        level2.setTotalEnemiesToSpawn(10);
        level3.setTotalEnemiesToSpawn(10); // Horda antes do chefe

        environment = new Environment(this.canvasHeight, this.canvasWidth, this.canvasWidth, panel);
        
        Agent darkMage = new Agent(300, 390, 180, 150, 3, 1000, false, 0);
        
        Weapon mageStaff = new MageStaff(0.15, 500);
        darkMage.setWeapon(mageStaff);
        environment.setProtagonist(darkMage);
        
        List<String> pauseImagePaths = Arrays.asList("/images/ui/Pause1.png", "/images/ui/Pause2.png", "/images/ui/Pause3.png");
        environment.setPauseImages(pauseImagePaths);
        environment.setWinImage("/images/ui/VoceVenceu.png");
        environment.setBossWarningImage("/images/ui/Boss.png");

        environment.getLevels().add(level1);
        environment.getLevels().add(level2);
        environment.getLevels().add(level3);
        
        mainMenu = new Menu();
    }
}