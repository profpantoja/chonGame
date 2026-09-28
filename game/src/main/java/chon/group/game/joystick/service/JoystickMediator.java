package chon.group.game.joystick.service;

import java.util.Objects;

import chon.group.game.joystick.GameCommand;
import chon.group.game.joystick.client.Joystick;

public final class JoystickMediator implements GameJoystick {

    private final Joystick joystick;

    public JoystickMediator(Joystick joystick) {
        this.joystick = Objects.requireNonNull(joystick);
    }

    @Override
    public boolean isHeld(GameCommand command) {
        return joystick.isHeld(command);
    }

    @Override
    public boolean press(GameCommand command) {
        return joystick.inPress(command);
    }

    @Override
    public void endFrame() {
        joystick.endFrame();
    }

    @Override
    public void clear() {
        joystick.clear();
    }
}