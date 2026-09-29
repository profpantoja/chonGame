package chon.group.game.joystick.client;

import java.util.EnumSet;

import chon.group.game.joystick.GameCommand;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class JavaFxJoystick extends Joystick {

    private final EnumSet<GameCommand> heldCommands =
            EnumSet.noneOf(GameCommand.class);

    private final EnumSet<GameCommand> pressedCommands =
            EnumSet.noneOf(GameCommand.class);

    public JavaFxJoystick(Scene scene) {
        scene.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            GameCommand command = map(event.getCode());

            if (command != null && heldCommands.add(command)) {
                pressedCommands.add(command);
            }
        });

        scene.addEventHandler(KeyEvent.KEY_RELEASED, event -> {
            GameCommand command = map(event.getCode());

            if (command != null) {
                heldCommands.remove(command);
            }
        });
    }

    @Override
    public boolean isHeld(GameCommand command) {
        return heldCommands.contains(command);
    }

    @Override
    public boolean inPress(GameCommand command) {
        return pressedCommands.remove(command);
    }

    @Override
    public void endFrame() {
        pressedCommands.clear();
    }

    @Override
    public void clear() {
        heldCommands.clear();
        pressedCommands.clear();
    }

    private GameCommand map(KeyCode keyCode) {
        return switch (keyCode) {
            case UP -> GameCommand.UP;
            case DOWN -> GameCommand.DOWN;
            case LEFT -> GameCommand.LEFT;
            case RIGHT -> GameCommand.RIGHT;
            case ENTER -> GameCommand.CONFIRM;
            case P -> GameCommand.PAUSE;
            case SPACE -> GameCommand.ATTACK;
            default -> null;
        };
    }
}