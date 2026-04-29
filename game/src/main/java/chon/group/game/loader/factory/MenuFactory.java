package chon.group.game.loader.factory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import chon.group.game.loader.config.menu.MenuConfig;
import chon.group.game.loader.config.menu.MenuHandlerConfig;
import chon.group.game.loader.config.menu.MenuItemConfig;
import chon.group.game.menu.Action;
import chon.group.game.menu.Item;
import chon.group.game.menu.Menu;
import chon.group.game.menu.MenuHandler;

public class MenuFactory {

    public Map<String, Menu> buildAll(Map<String, MenuConfig> configs) {
        Map<String, Menu> menus = new HashMap<>();

        for (var entry : configs.entrySet()) {
            String menuId = entry.getKey();
            MenuConfig config = entry.getValue();

            menus.put(menuId, build(menuId, config, configs, menus));
        }

        return menus;
    }

    private Menu build(
            String menuId,
            MenuConfig config,
            Map<String, MenuConfig> configs,
            Map<String, Menu> menus) {

        List<Item> items = new ArrayList<>();

        Menu menu = new Menu(
                config.getTitle(),
                items,
                config.getHeightProportion(),
                config.getWidth());

        menus.put(menuId, menu);

        for (MenuItemConfig itemConfig : config.getItems()) {
            items.add(buildItem(itemConfig, configs, menus));
        }

        return menu;
    }

    private Item buildItem(
            MenuItemConfig config,
            Map<String, MenuConfig> configs,
            Map<String, Menu> menus) {

        Action action = Action.valueOf(config.getAction());

        if (config.getTarget() != null) {
            Menu targetMenu = menus.get(config.getTarget());

            if (targetMenu == null) {
                MenuConfig targetConfig = configs.get(config.getTarget());

                if (targetConfig == null) {
                    throw new IllegalArgumentException(
                            "Target menu not found: " + config.getTarget());
                }

                targetMenu = build(config.getTarget(), targetConfig, configs, menus);
            }

            return new Item(
                    config.getTitle(),
                    action,
                    targetMenu);
        }

        return new Item(
                config.getTitle(),
                action);
    }

    public MenuHandler buildMenuHandler(
            Map<String, MenuConfig> menuConfigs,
            MenuHandlerConfig handlerConfig) {

        Map<String, Menu> menus = buildAll(menuConfigs);

        return new MenuHandler(
                getMenu(menus, handlerConfig.getStart()),
                getMenu(menus, handlerConfig.getPause()),
                getMenu(menus, handlerConfig.getSkip()),
                getMenu(menus, handlerConfig.getGameOver()),
                getMenu(menus, handlerConfig.getWinning()));
    }

    private Menu getMenu(Map<String, Menu> menus, String id) {
        Menu menu = menus.get(id);

        if (menu == null) {
            throw new IllegalArgumentException("Menu not found: " + id);
        }

        return menu;
    }
}