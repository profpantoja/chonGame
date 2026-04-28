module chon.group {
    requires transitive javafx.controls;
    requires transitive javafx.graphics;
    requires transitive javafx.media;
    requires java.desktop;
    /* Dependencies for loading the game with Jackson. */
    requires com.fasterxml.jackson.databind;

    opens chon.group.game.loader to com.fasterxml.jackson.databind;
    opens chon.group.game.loader.config.sound to com.fasterxml.jackson.databind;
    opens chon.group.game.loader.config.animation to com.fasterxml.jackson.databind;
    opens chon.group.game.loader.config.agent to com.fasterxml.jackson.databind;
    opens chon.group.game.loader.config.weapon to com.fasterxml.jackson.databind;
    opens chon.group.game.loader.config.level to com.fasterxml.jackson.databind;

    exports chon.group;
}