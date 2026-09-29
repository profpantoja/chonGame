package chon.group.game.core.platform;

import chon.group.game.core.platform.config.ControlType;
import chon.group.game.joystick.client.ExternalJoystick;
import chon.group.game.joystick.client.Joystick;

public class PlatformAssembler {

    public Platform construct(PlatformBuilder builder, ControlType control) {
        builder.buildDrawer();
        Joystick joystick = null;
        if (control == ControlType.API) {
            joystick = new ExternalJoystick();
        }
        builder.buildJoystick(joystick);
        builder.buildSoundPlayer();
        return builder.build();
    }
}