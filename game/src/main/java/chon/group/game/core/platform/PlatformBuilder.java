package chon.group.game.core.platform;

import chon.group.game.joystick.client.Joystick;

public interface PlatformBuilder {

    void buildJoystick(Joystick joystick);

    void buildDrawer();

    void buildSoundPlayer();

    Platform build();
}