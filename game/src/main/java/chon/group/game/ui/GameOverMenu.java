package chon.group.game.ui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.image.Image;

public class GameOverMenu {

    private List<Image> menuImages;
    private int selectedOptionIndex;

    public GameOverMenu() {
        this.menuImages = new ArrayList<>();
        try {
            // Carrega as 3 imagens do menu de Game Over
            menuImages.add(loadImage("/images/ui/GameOver1.png")); // Índice 0: New Game
            menuImages.add(loadImage("/images/ui/GameOver2.png")); // Índice 1: Menu
            menuImages.add(loadImage("/images/ui/GameOver3.png")); // Índice 2: Exit
        } catch (Exception e) {
            System.err.println("ERRO CRÍTICO: Não foi possível carregar as imagens do Game Over.");
            e.printStackTrace();
        }
        this.selectedOptionIndex = 0;
    }

    private Image loadImage(String path) {
        URL imageUrl = getClass().getResource(path);
        if (imageUrl == null) {
            throw new NullPointerException("Não foi possível encontrar o recurso no caminho: " + path);
        }
        return new Image(imageUrl.toExternalForm());
    }

    public Image getCurrentImage() {
        if (menuImages.isEmpty() || selectedOptionIndex >= menuImages.size()) {
            return null;
        }
        return menuImages.get(selectedOptionIndex);
    }

    public int getSelectedOptionIndex() {
        return selectedOptionIndex;
    }

    public void selectNextOption() {
        selectedOptionIndex = (selectedOptionIndex + 1) % menuImages.size();
    }

    public void selectPreviousOption() {
        selectedOptionIndex = (selectedOptionIndex - 1 + menuImages.size()) % menuImages.size();
    }
}