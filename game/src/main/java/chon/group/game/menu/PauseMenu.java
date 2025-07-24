package chon.group.game.menu;

import chon.group.game.drawer.JavaFxDrawer;
import javafx.scene.image.Image;

public class PauseMenu extends Menu<PauseOption> {
 
    public PauseMenu(JavaFxDrawer drawer, Image backgroundImage) {
        super(drawer, backgroundImage, PauseOption.values());
    }

}