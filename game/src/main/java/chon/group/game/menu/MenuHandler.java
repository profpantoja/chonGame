package chon.group.game.menu;

import java.util.ArrayDeque;
import java.util.Deque;

public class MenuHandler {

    private Menu start;
    private Menu pause;
    private Menu skip;
    private Menu gameOver;
    private Menu win;
    private Menu currentMenu;
    private Deque<Menu> history = new ArrayDeque<>();

    public MenuHandler(Menu start, Menu pause, Menu skip, Menu gameOver, Menu win) {
        this.start = start;
        this.pause = pause;
        this.skip = skip;
        this.gameOver = gameOver;
        this.win = win;
    }

    public Menu getCurrentMenu() {
        return currentMenu;
    }

    public void setCurrentMenu(Menu currentMenu) {
        this.currentMenu = currentMenu;
    }

    private void push(Menu menu) {
        history.push(currentMenu);
    }

    public void pop() {
        if (!history.isEmpty()) {
            currentMenu = history.pop();
        }
    }

    public void open(Menu menu) {
        this.currentMenu = menu;
        if (this.currentMenu != null) {
            this.currentMenu.reset();
        }
    }

    public void openStart() {
        open(start);
    }

    public void openPause() {
        open(pause);
    }

    public void openSkip() {
        open(skip);
    }

    public void openGameOver() {
        open(gameOver);
    }

    public void openWin() {
        open(win);
    }

    public void openSubMenu() {
        Item item = this.currentMenu.getSelectedItem();
        if (item == null || item.getSubMenu() == null) {
            return;
        }
        this.push(this.currentMenu);
        this.currentMenu = item.getSubMenu();
    }

}
