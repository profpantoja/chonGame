package chon.group.game.domain.environment;

import chon.group.game.core.agent.Agent;

public class Camera {

    private double x;
    private final double screenWidth, worldWidth;
    private double leftScrollBoundary, rightScrollBoundary;
    private Agent target;

    public Camera(double screenWidth, double worldWidth, double leftBoundary, double rightBoundary) {
        this.x = 0;
        this.screenWidth = screenWidth;
        this.worldWidth = worldWidth;
        this.leftScrollBoundary = screenWidth * leftBoundary;
        this.rightScrollBoundary = screenWidth * rightBoundary;
    }

    public void setTarget(Agent target) {
        this.target = target;
    }

    public void update() {
        if (target == null)
            return;
        double targetScreenX = target.getPosX() - x;
        if (targetScreenX > rightScrollBoundary)
            x += target.getSpeed();
        else if (targetScreenX < leftScrollBoundary)
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