package chon.group.game.core.platform;

import chon.group.game.core.platform.config.ControlType;
import chon.group.game.gateway.GameGateway;
import chon.group.game.joystick.client.ExternalJoystick;
import chon.group.game.joystick.client.Joystick;

public class PlatformAssembler {

    public Platform construct(PlatformBuilder builder, ControlType control, int apiPort) {
        builder.buildDrawer();
        Joystick joystick = null;
        if (control == ControlType.API) {
            joystick = new ExternalJoystick();
        }
        builder.buildJoystick(joystick);
        builder.buildSoundPlayer();
        Platform platform = builder.build();
        if (control == ControlType.API || control == ControlType.HYBRID) {
            ExternalJoystick externalJoystick = joystick instanceof ExternalJoystick
                    ? (ExternalJoystick) joystick
                    : null;
            platform.setGateway(
                    new GameGateway(apiPort, externalJoystick, control == ControlType.HYBRID));
        }
        return platform;
    }
}