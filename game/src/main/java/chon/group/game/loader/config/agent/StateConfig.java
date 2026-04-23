package chon.group.game.loader.config.agent;

public class StateConfig {

    private String direction;
    private boolean flipped;
    private boolean visibleBars;

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public boolean isFlipped() {
        return flipped;
    }

    public void setFlipped(boolean flipped) {
        this.flipped = flipped;
    }

    public boolean isVisibleBars() {
        return visibleBars;
    }

    public void setVisibleBars(boolean visibleBars) {
        this.visibleBars = visibleBars;
    }
}