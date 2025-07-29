package chon.group.game.ui;

import javafx.scene.image.Image;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Menu {

    private List<Image> menuImages;
    private int selectedOptionIndex;

    public Menu() {
        this.menuImages = new ArrayList<>();
        try {
            // NOMES ATUALIZADOS
            String path1 = "/images/ui/Menu1.png";
            String path2 = "/images/ui/Menu2.png";
            String path3 = "/images/ui/Menu3.png";

            System.out.println("[DEBUG MENU] Tentando carregar: " + path1); // <-- LOG ADICIONADO
            menuImages.add(loadImage(path1));
            System.out.println("[DEBUG MENU] Tentando carregar: " + path2); // <-- LOG ADICIONADO
            menuImages.add(loadImage(path2));
            System.out.println("[DEBUG MENU] Tentando carregar: " + path3); // <-- LOG ADICIONADO
            menuImages.add(loadImage(path3));

        } catch (Exception e) {
            System.err.println("ERRO CRÍTICO: Não foi possível carregar as imagens do menu.");
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

    public int getSelectedOptionIndex() { return selectedOptionIndex; }
    public void selectNextOption() { selectedOptionIndex = (selectedOptionIndex + 1) % menuImages.size(); }
    public void selectPreviousOption() { selectedOptionIndex = (selectedOptionIndex - 1 + menuImages.size()) % menuImages.size(); }
}