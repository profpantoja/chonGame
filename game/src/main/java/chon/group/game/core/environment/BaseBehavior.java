package chon.group.game.core.environment;

//public abstract class BaseBehavior implements EnvironmentBehavior {
public class BaseBehavior implements EnvironmentBehavior {
    @Override
    public final void update(Environment environment) {
        updateWorld(environment);
        environment.getMessenger().update();
        updateCamera(environment);
        recoverEnergy(environment);
    }

    // public abstract void updateWorld(Environment environment);
    public void updateWorld(Environment environment) {
    };

    /**
     * Updates the camera based on the protagonist’s current position.
     */
    public void updateCamera(Environment environment) {
        if (environment.getCamera() != null) {
            environment.getCamera().update();
        }
    }

    public void recoverEnergy(Environment environment) {
        environment.getProtagonist().recoverEnergy();
    }

}
