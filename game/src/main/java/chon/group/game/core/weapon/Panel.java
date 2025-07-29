package chon.group.game.core.weapon;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class Panel {

    private int panelHeight;
    private int panelWidth;
    private int lifeHeight;
    private int lifeWidth;
    private Color lifeColor;
    private int energyHeight;
    private int energyWidth;
    private Color energyColor;

    public Panel(int panelWidth, int panelHeight) {
        this.panelWidth = panelWidth;
        this.panelHeight = panelHeight;
    }
    
    public int getPanelHeight() { return panelHeight; }
    public void setPanelHeight(int panelHeight) { this.panelHeight = panelHeight; }
    public int getPanelWidth() { return panelWidth; }
    public void setPanelWidth(int panelWidth) { this.panelWidth = panelWidth; }
    public int getLifeHeight() { return lifeHeight; }
    public void setLifeHeight(int lifeHeight) { this.lifeHeight = lifeHeight; }
    public int getLifeWidth() { return lifeWidth; }
    public void setLifeWidth(int lifeWidth) { this.lifeWidth = lifeWidth; }
    public Color getLifeColor() { return lifeColor; }
    public void setLifeColor(Color lifeColor) { this.lifeColor = lifeColor; }
    public int getEnergyHeight() { return energyHeight; }
    public void setEnergyHeight(int energyHeight) { this.energyHeight = energyHeight; }
    public int getEnergyWidth() { return energyWidth; }
    public void setEnergyWidth(int energyWidth) { this.energyWidth = energyWidth; }
    public Color getEnergyColor() { return energyColor; }
    public void setEnergyColor(Color energyColor) { this.energyColor = energyColor; }
}