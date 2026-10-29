package chon.group.game.core.environment;

import chon.group.game.core.agent.Entity;

public class Camera {

    /** X position (horizontal) of the camera considering the environment width. */
    private double posX;
    /** The Screen width. */
    private double screenWidth;
    /** The level full width. */
    private double levelWidth;
    /** The left boundary. */
    private double leftBoundary;
    /** The right boundary. */
    private double rightBoundary;
    /** The Entity which the camera is following. */
    private Entity target;

    public Camera(double screenWidth, double levelWidth, double leftBoundaryRate, double rightBoundaryRate) {
        this.posX = 0;
        this.screenWidth = screenWidth;
        this.levelWidth = levelWidth;
        this.leftBoundary = screenWidth * leftBoundaryRate;
        this.rightBoundary = screenWidth * rightBoundaryRate;
    }

    public double getPosX() {
        return this.posX;
    }

    public void setPosX(double posX) {
        this.posX = posX;
    }

    public double getLevelWidth() {
        return levelWidth;
    }

    public void setLevelWidth(double levelWidth) {
        this.levelWidth = levelWidth;
    }

    public double getScreenWidth() {
        return screenWidth;
    }

    public void setScreenWidth(double screenWidth) {
        this.screenWidth = screenWidth;
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
        if (target != null) {
            /* It calculates the relative target X position considering the screen size. */
            double targetRelativePosX = target.getPosX() - this.posX;
            /*
             * If the agent passes after the left boundary, the camera position moves to the
             * left. Otherwise, it moves to the right.
             */
            if (targetRelativePosX > rightBoundary)
                this.posX += target.getSpeed();
            else if (targetRelativePosX < leftBoundary)
                this.posX -= target.getSpeed();
            /*
             * If the camera position is out of bounds, it must be adjusted. If it is behind
             * the beginning, it should point to the initial spot (zero). Otherwise, it
             * should point to the end of the environment minus the screen size.
             */
            if (this.posX < 0)
                this.posX = 0;
            if (this.posX > levelWidth - screenWidth)
                this.posX = levelWidth - screenWidth;
        }
    }

    public double updateEntity(Entity entity) {
        return entity.getPosX() - this.posX;
    }

}