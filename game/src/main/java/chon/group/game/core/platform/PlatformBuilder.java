package chon.group.game.core.platform;

public interface PlatformBuilder {

    void buildJoystick();

    void buildDrawer();

    void buildSoundPlayer();

    Platform build();
}