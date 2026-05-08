package chon.group.game.loader.config.environment;

import java.util.Map;

import chon.group.game.loader.config.environment.camera.CameraConfig;
import chon.group.game.loader.config.environment.ui.DisplayConfig;
import chon.group.game.loader.config.environment.ui.UiConfig;
import chon.group.game.loader.config.environment.menu.MenuConfig;
import chon.group.game.loader.config.environment.menu.MenuHandlerConfig;

public class EnvironmentConfig {

    private String protagonist;
    private DisplayConfig display;
    private UiConfig ui;
    private CameraConfig camera;
    private Map<String, MenuConfig> menus;
    private MenuHandlerConfig menuHandler;

    public EnvironmentConfig() {
    }

    public String getProtagonist() {
        return protagonist;
    }

    public void setProtagonist(String protagonist) {
        this.protagonist = protagonist;
    }

    public DisplayConfig getDisplay() {
        return display;
    }

    public void setDisplay(DisplayConfig display) {
        this.display = display;
    }

    public UiConfig getUi() {
        return ui;
    }

    public void setUi(UiConfig ui) {
        this.ui = ui;
    }

    public CameraConfig getCamera() {
        return camera;
    }

    public void setCamera(CameraConfig camera) {
        this.camera = camera;
    }

    public Map<String, MenuConfig> getMenus() {
        return menus;
    }

    public void setMenus(Map<String, MenuConfig> menus) {
        this.menus = menus;
    }

    public MenuHandlerConfig getMenuHandler() {
        return menuHandler;
    }

    public void setMenuHandler(MenuHandlerConfig menuHandler) {
        this.menuHandler = menuHandler;
    }

}