package chon.group.game.menu;

import java.util.List;

public class Menu {

    private int index = 0;
    private String title;
    private List<Item> items;

    public Menu(String title, List<Item> items) {
        this.title = title;
        this.items = items;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public Action handleInput(List<String> input) {
        if (input.contains("ENTER")) {
            input.clear();
            return items.get(this.index).getAction();
        }
        if (input.contains("UP"))
            this.index = (this.index - 1 + items.size()) % items.size();
        if (input.contains("DOWN"))
            this.index = (this.index + 1) % items.size();
        input.clear();
        return Action.NONE;
    }

    public void reset() {
        this.index = 0;
    }
}