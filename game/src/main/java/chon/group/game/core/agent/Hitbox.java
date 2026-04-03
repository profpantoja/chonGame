package chon.group.game.core.agent;

public class Hitbox {

    private int width;
    private int height;
    private double ratio = 1;

    public Hitbox(int width, int height, double ratio) {
        this.ratio = ratio;
        if (ratio == 0 || ratio > 1)
            this.ratio = 1;
        this.width = width;
        this.height = (int) (height * this.ratio);
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public double getRatio() {
        return ratio;
    }

    public void setRatio(double ratio) {
        this.ratio = ratio;
    }

}
