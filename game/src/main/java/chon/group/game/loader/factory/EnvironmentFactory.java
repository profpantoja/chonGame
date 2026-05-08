package chon.group.game.loader.factory;

import chon.group.game.core.environment.Environment;
import chon.group.game.core.environment.Level;
import chon.group.game.core.environment.Panel;
import chon.group.game.loader.config.environment.EnvironmentConfig;
import chon.group.game.loader.config.environment.camera.CameraConfig;

public class EnvironmentFactory {

    public Environment build(
            EnvironmentConfig config,
            Level firstLevel,
            Panel panel) {

        CameraConfig camera = config.getCamera();

        return new Environment(
                firstLevel.getWidth(),
                camera.getWidth(),
                panel,
                camera.getLeftBoundaryRate(),
                camera.getRightBoundaryRate());
    }
}