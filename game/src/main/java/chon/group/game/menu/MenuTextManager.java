package chon.group.game.menu;

import java.util.HashMap;
import java.util.Map;

public class MenuTextManager {
    private static MenuTextManager instance;
    private final Map<Enum<?>, String> customTexts;

    private MenuTextManager() {
        customTexts = new HashMap<>();
        loadDefaultTexts();
    }

    public static MenuTextManager getInstance() {
        if (instance == null) {
            instance = new MenuTextManager();
        }
        return instance;
    }
    
    private void loadDefaultTexts() {
        setText(MainOption.START_GAME, "Start Game");
        setText(MainOption.EXIT, "Exit");
        setText(PauseOption.RESUME, "Resume");
        setText(PauseOption.GO_BACK_TO_MENU, "Go back to Menu");
    }

    public void setText(Enum<?> option, String text) {
        customTexts.put(option, text);
    }

    public String getText(Enum<?> option) {
        return customTexts.getOrDefault(option, option.name());
    }
}