package chon.group.game;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;

/**
 * The {@code GamePanel} class is responsible for rendering the UI panel that
 * shows
 * the player's health, energy, score, and collectible statistics.
 * It uses JavaFX to draw custom UI elements with visual effects.
 */
public class Panel {

    private final Image lifeIcon;
    private final Image energyIcon;
    private final Image itemIcon;
    private final Image scoreIcon;
    private int panelHeight;
    private int panelWidth;
    private int lifeHeight;
    private int lifeWidth;
    private Color lifeColor;
    private int energyHeight;
    private int energyWidth;
    private Color energyColor;

    /**
     * Constructs a new GamePanel with the specified graphics context and font.
     *
     * @param gc        The {@code GraphicsContext} used for rendering.
     * @param pixelFont The pixel-style {@code Font} used for UI text.
     */
    public Panel(int panelWidth, int panelHeight) {
        this.panelWidth = panelWidth;
        this.panelHeight = panelHeight;
        this.lifeIcon = loadImage("/images/environment/heart.png");
        this.energyIcon = loadImage("/images/environment/energy.png");
        this.itemIcon = loadImage("/images/agents/coin.png");
        this.scoreIcon = loadImage("/images/environment/score.png");
    }

    /**
     * Loads an image from the specified path in the resource directory.
     *
     * @param path the relative path to the image resource
     * @return the loaded {@code Image}
     * @throws RuntimeException if the image is not found
     */
    private Image loadImage(String path) {
        var url = getClass().getResource(path);
        if (url == null) {
            throw new RuntimeException("Image not found: " + path);
        }
        return new Image(url.toExternalForm());
    }

    public Image getLifeIcon() {
        return lifeIcon;
    }

    public Image getEnergyIcon() {
        return energyIcon;
    }

    public Image getItemIcon() {
        return itemIcon;
    }

    public Image getScoreIcon() {
        return scoreIcon;
    }

    public int getPanelHeight() {
        return panelHeight;
    }

    public void setPanelHeight(int panelHeight) {
        this.panelHeight = panelHeight;
    }

    public int getPanelWidth() {
        return panelWidth;
    }

    public void setPanelWidth(int panelWidth) {
        this.panelWidth = panelWidth;
    }

    public int getLifeHeight() {
        return lifeHeight;
    }

    public void setLifeHeight(int lifeHeight) {
        this.lifeHeight = lifeHeight;
    }

    public int getLifeWidth() {
        return lifeWidth;
    }

    public void setLifeWidth(int lifeWidth) {
        this.lifeWidth = lifeWidth;
    }

    public Color getLifeColor() {
        return lifeColor;
    }

    public void setLifeColor(Color lifeColor) {
        this.lifeColor = lifeColor;
    }

    public int getEnergyHeight() {
        return energyHeight;
    }

    public void setEnergyHeight(int energyHeight) {
        this.energyHeight = energyHeight;
    }

    public int getEnergyWidth() {
        return energyWidth;
    }

    public void setEnergyWidth(int energyWidth) {
        this.energyWidth = energyWidth;
    }

    public Color getEnergyColor() {
        return energyColor;
    }

    public void setEnergyColor(Color energyColor) {
        this.energyColor = energyColor;
    }

}
