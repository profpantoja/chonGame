package chon.group.game.core.environment;

import java.util.Iterator;

import chon.group.game.messaging.Message;

public abstract class BaseBehavior implements EnvironmentBehavior {

    @Override
    public final void update(Environment environment) {
        updateWorld(environment);
        updateMessages(environment);
        updateCamera(environment);
        recoverEnergy(environment);
    }

    public abstract void updateWorld(Environment environment);

    /**
     * Updates and removes expired messages from the environment.
     */
    public void updateMessages(Environment environment) {
        Iterator<Message> iterator = environment.getMessages().iterator();
        while (iterator.hasNext()) {
            Message message = iterator.next();
            if (!message.update()) {
                iterator.remove();
            }
        }
    }

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
