package chon.group.game.core.menu;

import chon.group.game.drawer.JavaFxDrawer;
import javafx.scene.image.Image;

public class SelectMenu extends Menu<SelectOption> {
    public SelectMenu(JavaFxDrawer drawer, Image backgroundImage) {
        super(drawer, backgroundImage, SelectOption.values());
    }

    @Override
    public String[] getLabels() {
        return new String[]{"Han Solo", "Luke Skywalker", "Exit"};
    }
}