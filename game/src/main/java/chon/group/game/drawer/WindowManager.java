package chon.group.game.drawer;

import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;

/**
 * Manages the game window properties and scaling.
 * Handles window resizing while maintaining aspect ratio.
 */
public class WindowManager {
    private static final double ASPECT_RATIO_WIDTH = 1280.0;
    private static final double ASPECT_RATIO_HEIGHT = 780.0;
    
    private final double initialWidth;
    private final double initialHeight;
    private final double minWidth;
    private final double minHeight;
    
    /**
     * Constructor with default values.
     */
    public WindowManager() {
        this.initialWidth = 0;
        this.initialHeight = 0;
        this.minWidth = 0;
        this.minHeight = 0;
    }
    
    /**
     * Constructor that allows setting initial dimensions.
     * 
     * @param initialWidth initial window width
     * @param initialHeight initial window height
     */
    public WindowManager(double initialWidth, double initialHeight) {
        this.initialWidth = initialWidth;
        this.initialHeight = initialHeight;
        this.minWidth = 640;
        this.minHeight = 390;
    }
    
    /**
     * Constructor that allows setting all dimensions.
     * 
     * @param initialWidth initial window width
     * @param initialHeight initial window height
     * @param minWidth minimum window width
     * @param minHeight minimum window height
     */
    public WindowManager(double initialWidth, double initialHeight, double minWidth, double minHeight) {
        this.initialWidth = initialWidth;
        this.initialHeight = initialHeight;
        this.minWidth = minWidth;
        this.minHeight = minHeight;
    }
    
    /**
     * Sets up the window properties and scaling behavior.
     * Maintains 16:10 aspect ratio and handles window resizing.
     * 
     * @param stage the primary stage of the application
     * @param scene the main scene containing game content
     * @param root the root StackPane of the scene
     */
    public void setupWindow(Stage stage, Scene scene, StackPane root) {
        // Mantém proporção 16:10
        stage.widthProperty().addListener((obs, oldVal, newVal) -> {
            double newHeight = newVal.doubleValue() * (ASPECT_RATIO_HEIGHT / ASPECT_RATIO_WIDTH);
            stage.setHeight(newHeight);
        });

        stage.heightProperty().addListener((obs, oldVal, newVal) -> {
            double newWidth = newVal.doubleValue() * (ASPECT_RATIO_WIDTH / ASPECT_RATIO_HEIGHT);
            stage.setWidth(newWidth);
        });

        // Configuração de escala
        root.scaleXProperty().bind(scene.widthProperty().divide(ASPECT_RATIO_WIDTH));
        root.scaleYProperty().bind(scene.heightProperty().divide(ASPECT_RATIO_HEIGHT));

        // Configurações da janela
        stage.setTitle("Chon: The Learning Game");
        stage.setWidth(initialWidth);
        stage.setHeight(initialHeight);
        stage.setMinWidth(minWidth);
        stage.setMinHeight(minHeight);
    }
}