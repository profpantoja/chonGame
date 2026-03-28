package chon.group.game.menu;

import java.util.ArrayDeque;
import java.util.Deque;

public class MenuHandler {

    private Menu start;
    private Menu pause;
    private Menu skip;
    private Menu gameOver;
    private Deque<Menu> history = new ArrayDeque<>();

    public MenuHandler(Menu start, Menu pause, Menu skip, Menu gameOver) {
        this.start = start;
        this.pause = pause;
        this.skip = skip;
        this.gameOver = gameOver;
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

    public Deque<Menu> getHistory() {
        return history;
    }

    public void setHistory(Deque<Menu> history) {
        this.history = history;
    }

}
