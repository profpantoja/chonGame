package chon.group.test;

import chon.group.game.domain.agent.Agent;
import chon.group.game.domain.environment.Environment;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class GameCameraTest {

    private static final double WINDOW_WIDTH = 1000.0;

    private Environment environment;
    private Agent protagonist;
    private GameCameraLogic cameraLogic;

    static class GameCameraLogic {
        private final Environment environment;
        private final double windowWidth;

        public GameCameraLogic(Environment environment, double windowWidth) {
            this.environment = environment;
            this.windowWidth = windowWidth;
        }

        public void updateCameraPosition() {
            double posX = environment.getProtagonist().getPosX();
            double speed = environment.getProtagonist().getSpeed();
            double cameraX = environment.getCameraX();

            double rightBoundary = cameraX + windowWidth * 0.80;
            double leftBoundary = cameraX + windowWidth * 0.15;

            if (posX > rightBoundary) {
                double newCameraX = cameraX + speed;
                double maxX = environment.getWidth() - windowWidth;
                environment.setCameraX(Math.min(newCameraX, maxX));
            } else if (posX < leftBoundary) {
                double newCameraX = cameraX - speed;
                environment.setCameraX(Math.max(newCameraX, 0));
            }
        }
    }

    @Before
    public void setUp() {
        environment = new Environment(); 
        environment.setWidth(3000);
        
        protagonist = new Agent();
        protagonist.setPosX(0);
        protagonist.setSpeed(10);
        environment.setProtagonist(protagonist);
        
        cameraLogic = new GameCameraLogic(environment, WINDOW_WIDTH);
    }

    @Test
    public void cameraShouldMoveRightWhenProtagonistCrossesRightBoundary() {
        environment.setCameraX(100.0);
        protagonist.setPosX(901); 
        cameraLogic.updateCameraPosition();
        System.out.println("Camera move right => CameraX: " + environment.getCameraX());
        assertEquals(110.0, environment.getCameraX(), 0.0);
    }

    @Test
    public void cameraShouldMoveLeftWhenProtagonistCrossesLeftBoundary() {
        environment.setCameraX(500.0);
        protagonist.setPosX(649); 
        cameraLogic.updateCameraPosition();
        System.out.println("Camera move left => CameraX: " + environment.getCameraX());
        assertEquals(490.0, environment.getCameraX(), 0.0);
    }

    @Test
    public void cameraShouldNotMoveWhenProtagonistIsInCenter() {
        environment.setCameraX(100.0);
        protagonist.setPosX(500); 
        cameraLogic.updateCameraPosition();
        System.out.println("Camera stop when protagonist betwen 80% and 15% of camera => CameraX: " + environment.getCameraX());
        assertEquals(100.0, environment.getCameraX(), 0.0);
    }

    @Test
    public void cameraShouldStopAtMaximumBoundary() {
        environment.setCameraX(1995.0);
        protagonist.setPosX(2800);
        cameraLogic.updateCameraPosition();
        System.out.println("Camera stop in maximum screen boundary => CameraX: " + environment.getCameraX());
        assertEquals(2000.0, environment.getCameraX(), 0.0);
    }

    @Test
    public void cameraShouldStopAtMinimumBoundary() {
        environment.setCameraX(5.0);
        protagonist.setPosX(100);
        cameraLogic.updateCameraPosition();
        System.out.println("Camera stop in minimum screen boundary => CameraX: " + environment.getCameraX());
        assertEquals(0.0, environment.getCameraX(), 0.0);
    }
}
