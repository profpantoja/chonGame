package chon.group.game.menu;

public class Item {

    private String title;
    private Action action = Action.NONE;
    private Menu subMenu;

    public Item(String title, Action action) {
        this.title = title;
        this.action = action;
    }

    public Item(String title, Action action, Menu subMenu) {
        this.title = title;
        this.action = action;
        this.subMenu = subMenu;
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

    public Menu getSubMenu() {
        return subMenu;
    }

    public void setSubMenu(Menu subMenu) {
        this.subMenu = subMenu;
    }
    
}
