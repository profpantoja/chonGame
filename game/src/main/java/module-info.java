module chon.group {
    requires transitive javafx.controls;
    requires transitive javafx.graphics;
    requires transitive javafx.media;
    requires java.desktop;
    /* Dependencies for loading the game with Jackson. */
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.annotation;

    opens chon.group.game.loader to com.fasterxml.jackson.databind;
    opens chon.group.game.loader.config.media to com.fasterxml.jackson.databind;
    opens chon.group.game.loader.config.media.sound to com.fasterxml.jackson.databind;
    opens chon.group.game.loader.config.media.animation to com.fasterxml.jackson.databind;
    opens chon.group.game.loader.config.entity to com.fasterxml.jackson.databind;
    opens chon.group.game.loader.config.entity.agent to com.fasterxml.jackson.databind;
    opens chon.group.game.loader.config.entity.object to com.fasterxml.jackson.databind;
    opens chon.group.game.loader.config.entity.weapon to com.fasterxml.jackson.databind;
    opens chon.group.game.loader.config.environment to com.fasterxml.jackson.databind;
    opens chon.group.game.loader.config.environment.menu to com.fasterxml.jackson.databind;
    opens chon.group.game.loader.config.level to com.fasterxml.jackson.databind;

    exports chon.group;
}