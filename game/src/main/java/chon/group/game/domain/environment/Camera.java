package chon.group.game.domain.environment;

import chon.group.game.core.agent.Entity;

public class Camera {

    private double x;
    private final double screenWidth, worldWidth;
    private double leftBoundary, rightBoundary;
    private Entity target;

    public Camera(double screenWidth, double worldWidth, double leftBoundary, double rightBoundary) {
        this.x = 0;
        this.screenWidth = screenWidth;
        this.worldWidth = worldWidth;
        this.leftBoundary = screenWidth * leftBoundary;
        this.rightBoundary = screenWidth * rightBoundary;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getWorldWidth() {
        return worldWidth;
    }

    public double getLeftBoundary() {
        return leftBoundary;
    }

    public void setLeftBoundary(double leftBoundary) {
        this.leftBoundary = leftBoundary;
    }

    public double getRightBoundary() {
        return rightBoundary;
    }

    public void setRightBoundary(double rightBoundary) {
        this.rightBoundary = rightBoundary;
    }

    public Entity getTarget() {
        return target;
    }

    public void setTarget(Entity target) {
        this.target = target;
    }

    public void update() {
        if (target == null)
            return;
        double targetScreenX = target.getPosX() - x;
        if (targetScreenX > rightBoundary)
            x += target.getSpeed();
        else if (targetScreenX < leftBoundary)
            x -= target.getSpeed();
        if (x < 0)
            x = 0;
        if (x > worldWidth - screenWidth)
            x = worldWidth - screenWidth;
    }

    public double worldToScreenX(double worldX) {
        return worldX - x;
    }

    public double getScreenWidth() {
        return screenWidth;
    }
}