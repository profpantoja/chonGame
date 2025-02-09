package chon.group.game.domain.agent;

import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

public class Agent {
    private Circle paddle1;
    private Circle paddle2;
    private Circle puck;

    public Agent(double paddleRadius, double puckRadius) {
        paddle1 = new Circle(paddleRadius, Color.BLUE);
        paddle2 = new Circle(paddleRadius, Color.RED);
        puck = new Circle(puckRadius, Color.BLACK);
    }

    public Circle getPaddle1() {
        return paddle1;
    }

    public Circle getPaddle2() {
        return paddle2;
    }

    public Circle getPuck() {
        return puck;
    }

    public void setPaddle1Image(ImagePattern imagePattern) {
        paddle1.setFill(imagePattern);
    }

    public void setPaddle2Image(ImagePattern imagePattern) {
        paddle2.setFill(imagePattern);
    }

    public void setPuckImage(ImagePattern imagePattern) {
        puck.setFill(imagePattern);
    }

    public void resetPuckPosition(double x, double y) {
        puck.setTranslateX(x);
        puck.setTranslateY(y);
    }
}