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

    public Menu getStart() {
        return start;
    }

    public void setStart(Menu start) {
        this.start = start;
    }

    public Menu getPause() {
        return pause;
    }

    public void setPause(Menu pause) {
        this.pause = pause;
    }

    public Menu getSkip() {
        return skip;
    }

    public void setSkip(Menu skip) {
        this.skip = skip;
    }

    public Menu getGameOver() {
        return gameOver;
    }

    public void setGameOver(Menu gameOver) {
        this.gameOver = gameOver;
    }

    public Menu getWin() {
        return win;
    }

    public void setWin(Menu win) {
        this.win = win;
    }

    public Menu getCurrentMenu() {
        return currentMenu;
    }

    public void setCurrentMenu(Menu currentMenu) {
        this.currentMenu = currentMenu;
    }

    public Deque<Menu> getHistory() {
        return history;
    }

    public void setHistory(Deque<Menu> history) {
        this.history = history;
    }

    public void push(Menu menu) {
        history.push(currentMenu);
        currentMenu = menu;
        currentMenu.reset();
    }

    public void pop() {
        if (!history.isEmpty()) {
            currentMenu = history.pop();
        }
    }

}
