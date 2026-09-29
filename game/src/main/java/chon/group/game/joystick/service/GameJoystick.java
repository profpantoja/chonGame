package chon.group.game.joystick.service;

import chon.group.game.joystick.GameCommand;

public interface GameJoystick {

    /**
     * Returns true if the command is currently held down.
     * Suitable for continuous movement.
     */
    boolean isHeld(GameCommand command);

    /**
     * Returns true only once per press.
     * Suitable for confirm, pause, and attack actions.
     */
    boolean press(GameCommand command);

    /**
     * Discards unconsumed single-frame commands at the end of the frame.
     * Does not remove held commands.
     */
    void endFrame();

    /**
     * Removes all commands; used in reset and stage change.
     */
    void clear();
}