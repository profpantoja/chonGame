package chon.group.game.core.platform;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import chon.group.game.core.platform.config.ControlType;
import chon.group.game.joystick.client.ExternalJoystick;
import chon.group.game.joystick.client.Joystick;

public class PlatformAssemblerTest {

    @Test
    public void hybridKeepsLocalJoystickAndStartsGateway() {
        StubPlatformBuilder builder = new StubPlatformBuilder();

        Platform platform = new PlatformAssembler().construct(builder, ControlType.HYBRID, 8765);

        assertNull(builder.joystick);
        assertNotNull(platform.getGateway());
    }

    @Test
    public void apiUsesExternalJoystickAndStartsGateway() {
        StubPlatformBuilder builder = new StubPlatformBuilder();

        Platform platform = new PlatformAssembler().construct(builder, ControlType.API, 8765);

        assertTrue(builder.joystick instanceof ExternalJoystick);
        assertNotNull(platform.getGateway());
    }

    @Test
    public void localDoesNotStartGateway() {
        StubPlatformBuilder builder = new StubPlatformBuilder();

        Platform platform = new PlatformAssembler().construct(builder, ControlType.LOCAL, 8765);

        assertNull(builder.joystick);
        assertNull(platform.getGateway());
    }

    private static final class StubPlatformBuilder implements PlatformBuilder {
        private final Platform platform = new Platform();
        private Joystick joystick;

        @Override
        public void buildJoystick(Joystick joystick) {
            this.joystick = joystick;
        }

        @Override
        public void buildDrawer() {
        }

        @Override
        public void buildSoundPlayer() {
        }

        @Override
        public Platform build() {
            return platform;
        }
    }
}