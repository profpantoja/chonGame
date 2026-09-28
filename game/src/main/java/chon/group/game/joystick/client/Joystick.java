package chon.group.game.joystick.client;

import chon.group.game.joystick.GameCommand;

public abstract class Joystick {

    public abstract boolean isHeld(GameCommand command);

    public abstract boolean inPress(GameCommand command);

    public abstract void endFrame();

    public abstract void clear();
}