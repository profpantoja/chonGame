package chon.group.game.core.platform;

public class PlatformAssembler {

    public Platform construct(PlatformBuilder builder) {
        builder.buildDrawer();
        builder.buildJoystick();
        builder.buildSoundPlayer();
        return builder.build();
    }
}