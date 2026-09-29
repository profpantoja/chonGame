package chon.group.game.joystick.client;

import java.util.EnumSet;

import chon.group.game.joystick.GameCommand;

/** Joystick controlled by commands received from an external client. */
public class ExternalJoystick extends Joystick {

    private final EnumSet<GameCommand> heldCommands =
            EnumSet.noneOf(GameCommand.class);

    private final EnumSet<GameCommand> pressedCommands =
            EnumSet.noneOf(GameCommand.class);

    public synchronized void hold(GameCommand command) {
        heldCommands.add(command);
    }

    public synchronized void release(GameCommand command) {
        heldCommands.remove(command);
    }

    public synchronized void press(GameCommand command) {
        pressedCommands.add(command);
    }

    @Override
    public synchronized boolean isHeld(GameCommand command) {
        return heldCommands.contains(command);
    }

    @Override
    public synchronized boolean inPress(GameCommand command) {
        return pressedCommands.remove(command);
    }

    @Override
    public synchronized void endFrame() {
        pressedCommands.clear();
    }

    @Override
    public synchronized void clear() {
        heldCommands.clear();
        pressedCommands.clear();
    }
}