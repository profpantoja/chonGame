package chon.group.game.menu;

import java.util.List;

public class Menu {

    private int index = 0;
    private String title;
    private List<String> items;
    private MenuType type;

    public Menu(String title, List<String> items, MenuType type) {
        this.title = title;
        this.items = items;
        this.type = type;
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

    public List<String> getItems() {
        return items;
    }

    public void setItems(List<String> items) {
        this.items = items;
    }

    public MenuType getType() {
        return type;
    }

    public void setType(MenuType type) {
        this.type = type;
    }

    // Retorna a opção escolhida ao pressionar ENTER, ou null caso contrário
    public void handleInput(List<String> input) {
        if (input.contains("UP"))
            this.index = (this.index - 1 + items.size()) % items.size();
        if (input.contains("DOWN"))
            this.index = (this.index + 1) % items.size();
        input.clear();
    }

    public void reset() {
        this.index = 0;
    }
}