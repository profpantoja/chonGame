package chon.group.game.loader.config.environment.camera;

public class CameraConfig {

    private int width;
    private int height;
    private double leftBoundaryRate;
    private double rightBoundaryRate;

    public CameraConfig() {}

    public int getWidth() {
        return width;
    }

    public void setWidth(int screenWidth) {
        this.width = screenWidth;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int screenHeight) {
        this.height = screenHeight;
    }

    public double getLeftBoundaryRate() {
        return leftBoundaryRate;
    }

    public void setLeftBoundaryRate(double leftBoundaryRate) {
        this.leftBoundaryRate = leftBoundaryRate;
    }

    public double getRightBoundaryRate() {
        return rightBoundaryRate;
    }

    public void setRightBoundaryRate(double rightBoundaryRate) {
        this.rightBoundaryRate = rightBoundaryRate;
    }
}