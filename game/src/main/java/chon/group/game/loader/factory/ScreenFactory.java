package chon.group.game.loader.factory;

import chon.group.game.core.environment.Environment;
import chon.group.game.loader.config.media.screen.ScreenConfig;

public class ScreenFactory {

    public void apply(Environment environment, ScreenConfig config) {
        if (config == null) {
            return;
        }

        if (config.getPause() != null) {
            environment.setPauseImage(config.getPause());
        }

        if (config.getGameOver() != null) {
            environment.setGameOverImage(config.getGameOver());
        }

        if (config.getTheEnd() != null) {
            environment.setTheEndImage(config.getTheEnd());
        }
    }
    
}