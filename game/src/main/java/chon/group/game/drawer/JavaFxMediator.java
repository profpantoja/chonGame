package chon.group.game.drawer;

import chon.group.game.domain.agent.Agent;
import chon.group.game.domain.environment.Environment;
import javafx.scene.canvas.GraphicsContext;

/**
 * Mediates the drawing operations between the game environment, agents, and the JavaFX drawer.
 */
public class JavaFxMediator implements EnvironmentDrawer {
    private final Environment environment;
    private final Agent agents;
    private final JavaFxDrawer drawer;
    private final double width;
    private final double height;

    /**
     * Constructs a JavaFxMediator to manage the rendering of the game environment and agents.
     *
     * @param environment The game environment containing the background.
     * @param agents      The agents (paddles and puck) to be drawn.
     * @param gc          The graphics context used for rendering.
     * @param width       The width of the game canvas.
     * @param height      The height of the game canvas.
     */
    public JavaFxMediator(Environment environment, Agent agents, GraphicsContext gc, double width, double height) {
        this.environment = environment;
        this.agents = agents;
        this.drawer = new JavaFxDrawer(gc, this);
        this.width = width;
        this.height = height;
    }

    /**
     * Clears the game environment by wiping the canvas.
     */
    @Override
    public void clearEnvironment() {
        drawer.clearScreen(this.width, this.height);
    }

    /**
     * Draws the game background image.
     */
    @Override
    public void drawBackground() {
        drawer.drawImage(this.environment.getBackgroundImage().getImage(),
                0, 0,
                this.width,
                this.height);
    }

    /**
     * Draws the game agents, including paddles and the puck.
     */
    @Override
    public void drawAgents() {
        // Draw paddle 1
        drawer.drawImage(agents.getPaddle1().getFill(),
                agents.getPaddle1().getTranslateX(),
                agents.getPaddle1().getTranslateY(),
                agents.getPaddle1().getRadius() * 2,
                agents.getPaddle1().getRadius() * 2);
        // Draw paddle 1
        drawer.drawImage(agents.getPaddle2().getFill(),
                agents.getPaddle2().getTranslateX(),
                agents.getPaddle2().getTranslateY(),
                agents.getPaddle2().getRadius() * 2,
                agents.getPaddle2().getRadius() * 2);
        // Draw the puck
        drawer.drawImage(agents.getPuck().getFill(),
                agents.getPuck().getTranslateX(),
                agents.getPuck().getTranslateY(),
                agents.getPuck().getRadius() * 2,
                agents.getPuck().getRadius() * 2);
    }
}
