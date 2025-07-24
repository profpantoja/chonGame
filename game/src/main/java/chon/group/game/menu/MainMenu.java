package chon.group.game.menu;

import chon.group.game.drawer.JavaFxDrawer;
import javafx.scene.image.Image;

public class MainMenu extends Menu<MainOption> {
    
    public MainMenu(JavaFxDrawer drawer, Image backgroundImage) {
        super(drawer, backgroundImage, MainOption.values());
    }

}