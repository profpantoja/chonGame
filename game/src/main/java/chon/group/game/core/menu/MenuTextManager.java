package chon.group.game.core.menu;

import java.util.HashMap;
import java.util.Map;

public class MenuTextManager {
    private static MenuTextManager instance;
    private final Map<Enum<?>, String> customTexts;

    private MenuTextManager() {
        customTexts = new HashMap<>();
    }

    public static MenuTextManager getInstance() {
        if (instance == null) {
            instance = new MenuTextManager();
        }
        return instance;
    }

    public void setText(Enum<?> option, String text) {
        customTexts.put(option, text);
    }

    public String getText(Enum<?> option) {
        return customTexts.getOrDefault(option, option.name());
    }
}