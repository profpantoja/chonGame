package chon.group.game.loader.config.menu;

import java.util.List;

public class MenuConfig {

    private String title;
    private double heightProportion;
    private int width;
    private List<MenuItemConfig> items;

    public MenuConfig() {

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getHeightProportion() {
        return heightProportion;
    }

    public void setHeightProportion(double heightProportion) {
        this.heightProportion = heightProportion;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public List<MenuItemConfig> getItems() {
        return items;
    }

    public void setItems(List<MenuItemConfig> items) {
        this.items = items;
    }
}