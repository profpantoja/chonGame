package chon.group.game.loader.config.environment;

import java.util.Map;

import chon.group.game.loader.config.environment.menu.MenuConfig;
import chon.group.game.loader.config.environment.menu.MenuHandlerConfig;

public class EnvironmentConfig {

    private String protagonist;

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