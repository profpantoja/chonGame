package chon.group.game.drawer;

import java.util.Iterator;

import chon.group.game.core.agent.Agent;
import chon.group.game.core.agent.Object;
import chon.group.game.core.weapon.Shot;
import chon.group.game.domain.environment.Environment;
import chon.group.game.messaging.Message;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * The {@code JavaFxMediator} class serves as an intermediary for rendering the
 * game environment
 * and its elements using JavaFX. It coordinates the interaction between the
 * {@link Environment}
 * and the {@link JavaFxDrawer} to manage graphical rendering.
 */
public class JavaFxMediator implements EnvironmentDrawer {

    private final Environment environment;
    private final JavaFxDrawer drawer;

    /**
     * Constructs a JavaFxMediator with the specified environment and graphics
     * context.
     *
     * @param environment The game environment containing agents and the
     *                    protagonist.
     * @param gc          The {@link GraphicsContext} used for rendering.
     */
    public JavaFxMediator(Environment environment, GraphicsContext gc) {
        this.environment = environment;
        this.drawer = new JavaFxDrawer(gc, this);
    }

    /**
     * Clears the environment by erasing all drawn elements on the screen.
     */
    @Override
    public void clearEnvironment() {
        drawer.clearScreen((int) this.environment.getCamera().getScreenWidth(), this.environment.getHeight());
    }

    /**
     * Draws the background image of the environment.
     */
    @Override
    public void drawBackground() {
        int posX = (int) (this.environment.getCamera().getPosX() * -1);
        drawer.drawImage(this.environment.getImage(),
                posX, this.environment.getPosY(),
                this.environment.getWidth(),
                this.environment.getHeight());
    }

    /**
     * Renders all agents and the protagonist within the environment,
     * including their health bars and status panels.
     */
    @Override
    public void drawAgents() {
        for (Agent agent : this.environment.getAgents()) {
            int newPosX = (int) this.environment.getCamera().updateEntity(agent);
            drawer.drawImage(agent.getImage(),
                    newPosX,
                    agent.getPosY(),
                    agent.getWidth(),
                    agent.getHeight());
            drawer.drawLifeBar(agent.getHealth(),
                    agent.getFullHealth(),
                    agent.getWidth(),
                    newPosX,
                    agent.getPosY(),
                    Color.DARKRED);
        }

        Agent protagonist = this.environment.getProtagonist();
        int newPosX = (int) this.environment.getCamera().updateEntity(protagonist);
        drawer.drawImage(protagonist.getImage(),
                newPosX,
                protagonist.getPosY(),
                protagonist.getWidth(),
                protagonist.getHeight());

        this.drawLifeBar();
        this.drawStatusPanel();
    }

    /**
     * Renders all objects within the environment.
     */
    @Override
    public void drawObjects() {
        for (Object object : environment.getObjects()) {
            drawer.drawImage(object.getImage(),
                    (int) this.environment.getCamera().updateEntity(object),
                    object.getPosY(),
                    object.getWidth(),
                    object.getHeight());
        }
    }

    /**
     * Draws the protagonist's life bar on the screen.
     */
    @Override
    public void drawLifeBar() {
        Agent protagonist = this.environment.getProtagonist();
        drawer.drawLifeBar(
                protagonist.getHealth(),
                protagonist.getFullHealth(),
                protagonist.getWidth(),
                (int) this.environment.getCamera().updateEntity(protagonist),
                protagonist.getPosY(),
                Color.GREEN);
    }

    /**
     * Draws the protagonist's status panel on the screen.
     */
    @Override
    public void drawStatusPanel() {
        Agent protagonist = this.environment.getProtagonist();
        drawer.drawStatusPanel(protagonist.getPosX(),
                protagonist.getPosY(),
                (int) this.environment.getCamera().getPosX());
    }

    /**
     * Draws the pause screen overlay, displaying a pause image centered within the
     * environment.
     */
    @Override
    public void drawPauseScreen() {
        drawer.drawScreen(this.environment.getPauseImage(),
                (int) this.environment.getPauseImage().getWidth(),
                (int) this.environment.getPauseImage().getHeight(),
                (int) this.environment.getCamera().getScreenWidth(),
                this.environment.getHeight());
    }

    /**
     * Draws the pause screen overlay, displaying a pause image centered within the
     * environment.
     */
    @Override
    public void drawGameOver() {
        drawer.drawScreen(this.environment.getGameOverImage(),
                (int) this.environment.getPauseImage().getWidth(),
                (int) this.environment.getPauseImage().getHeight(),
                (int) this.environment.getCamera().getScreenWidth(),
                this.environment.getHeight());
    }

    /**
     * Draws damage messaages that appear when agents take damage.
     * The message float upward and fade out over time.
     */
    @Override
    public void drawMessages() {
        Iterator<Message> iterator = this.environment.getMessages().iterator();
        while (iterator.hasNext()) {
            Message message = iterator.next();
            drawer.drawMessages(message.getSize(),
                    message.getOpacity(),
                    Color.BLACK,
                    Color.WHEAT,
                    String.valueOf(message.getMessage()),
                    message.getPosX() - this.environment.getCamera().getPosX(),
                    message.getPosY());
        }
    }

    @Override
    public void drawShots() {
        Iterator<Shot> iterator = this.environment.getShots().iterator();
        while (iterator.hasNext()) {
            Shot shot = iterator.next();
            drawer.drawImage(shot.getImage(),
                    (int) this.environment.getCamera().updateEntity(shot),
                    shot.getPosY(),
                    shot.getWidth(),
                    shot.getHeight());
        }
    }

}
