package chon.group.game.domain.environment;
import chon.group.game.drawer.JavaFxDrawer;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;

public class MainMenu {

    public enum Option {
        START_GAME, EXIT
    }

    private int selectedOption = 0;
    private final Option[] options = Option.values();
    private JavaFxDrawer drawer;
    private Image backgroundImage;

    public MainMenu(JavaFxDrawer drawer, Image backgroundImage) {
        this.drawer = drawer;
        this.backgroundImage = backgroundImage;
    }

    public void draw() {
        String[] labels = {"Start Game", "Exit"};
        drawer.drawMainMenu(backgroundImage, "Chon Game", selectedOption, labels);
    }

    // Retorna a opção escolhida ao pressionar ENTER, ou null caso contrário
    public Option handleInput(KeyCode code) {
        switch (code) {
            case UP:
                selectedOption = (selectedOption - 1 + options.length) % options.length;
                break;
            case DOWN:
                selectedOption = (selectedOption + 1) % options.length;
                break;
            case ENTER:
                return options[selectedOption];
        }
        return null;
    }

    public void reset() {
        this.selectedOption = 0;
    }
}