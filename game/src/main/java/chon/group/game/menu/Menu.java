package chon.group.game.menu;
import chon.group.game.drawer.JavaFxDrawer;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;

public class Menu {

    private int selectedOption = 0;
    private final MenuOption.Main[] options = MenuOption.Main.values();
    private JavaFxDrawer drawer;
    private Image backgroundImage;

    public Menu(JavaFxDrawer drawer, Image backgroundImage) {
        this.drawer = drawer;
        this.backgroundImage = backgroundImage;
    }

    public void draw() {
        String[] labels = {"Start Game", "Exit"};
        drawer.drawMainMenu(backgroundImage, "Chon Game", selectedOption, labels);
    }

    // Retorna a opção escolhida ao pressionar ENTER, ou null caso contrário
    public MenuOption.Main handleInput(KeyCode code) {
    if (code == KeyCode.UP) {
        selectedOption = (selectedOption - 1 + options.length) % options.length;
    } else if (code == KeyCode.DOWN) {
        selectedOption = (selectedOption + 1) % options.length;
    } else if (code == KeyCode.ENTER) {
        return options[selectedOption];
    }
    return null;
}

    public void reset() {
        this.selectedOption = 0;
    }
}