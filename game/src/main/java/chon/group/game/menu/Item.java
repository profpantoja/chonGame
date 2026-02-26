package chon.group.game.menu;

public class Item {

    private String title;
    private Action action = Action.NONE;

    public Item(String title, Action action) {
        this.title = title;
        this.action = action;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

}
