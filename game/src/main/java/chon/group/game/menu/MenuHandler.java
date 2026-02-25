package chon.group.game.menu;

public class MenuHandler {

    private Menu start;
    private Menu pause;
    private Menu skip;
    private Menu gameOver;

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

}
