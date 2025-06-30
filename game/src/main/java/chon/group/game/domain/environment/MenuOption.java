package chon.group.game.domain.environment;

public class MenuOption {

    public enum Main {
        START_GAME,
        SETTINGS,
        EXIT
    }

    public enum Pause {
        RESUME,
        SETTINGS,
        GO_BACK_TO_MENU
    }

    public enum Settings {
        BACK
    }
}
