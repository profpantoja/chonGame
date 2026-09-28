package chon.group.game.menu;

import java.util.List;

import chon.group.game.joystick.GameCommand;
import chon.group.game.joystick.service.GameJoystick;

public class Menu {

    private int index = 0;
    private String title;
    private List<Item> items;
    private double heightProportion = 0.7;
    private double width = 400;
    private double span = 45;

    public Menu(String title, List<Item> items, double proportion, double width) {
        this.title = title;
        this.items = items;
        this.heightProportion = proportion;
        this.width = width;
    }

    public Menu(
            int index,
            String title,
            List<Item> items,
            double proportion,
            double width,
            double span) {
        this.index = index;
        this.title = title;
        this.items = items;
        this.heightProportion = proportion;
        this.width = width;
        this.span = span;
    }

    public int getIndex() {
        return this.index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Item> getItems() {
        return this.items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public double getHeightProportion() {
        return this.heightProportion;
    }

    public void setHeightProportion(double heightProportion) {
        this.heightProportion = heightProportion;
    }

    public double getWidth() {
        return this.width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getSpan() {
        return this.span;
    }

    public void setSpan(double span) {
        this.span = span;
    }

    public Action handleAction(GameJoystick joystick) {
        if (joystick.press(GameCommand.CONFIRM)) {
            return this.items.get(this.index).getAction();
        }

        if (joystick.press(GameCommand.UP)) {
            this.index = (this.index - 1 + this.items.size())
                    % this.items.size();

            return Action.NONE;
        }

        if (joystick.press(GameCommand.DOWN)) {
            this.index = (this.index + 1) % this.items.size();

            return Action.NONE;
        }

        Action selectedAction = this.items.get(this.index).getAction();

        if (selectedAction == Action.VOLUME
                && (joystick.isHeld(GameCommand.LEFT)
                        || joystick.isHeld(GameCommand.RIGHT))) {
            return Action.VOLUME;
        }

        return Action.NONE;
    }

    public Item getSelectedItem() {
        return this.items.get(this.index);
    }

    public void reset() {
        this.index = 0;
    }
}