package chon.group.game.domain.environment;
import chon.group.game.drawer.JavaFxDrawer;
import javafx.scene.image.Image;

public class MenuPause {

    

    public MenuPause() {
    }
    
    private Image backgroundImage = Setup.createEnvironment().getPauseImage();
    private int selectedOptionIndex = 0;
    private String[] options = {"Continuar", "Opções", "Sair"};
    JavaFxDrawer drawer = new JavaFxDrawer(null, null);

  

}


