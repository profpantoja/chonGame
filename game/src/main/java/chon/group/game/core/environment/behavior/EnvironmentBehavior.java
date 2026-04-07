package chon.group.game.core.environment.behavior;

import chon.group.game.core.environment.Environment;

public interface EnvironmentBehavior {

    void update(Environment environment);

    void updateShots(Environment environment);

    void checkBorders(Environment environment);

}
