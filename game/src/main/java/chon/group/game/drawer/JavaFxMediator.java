package chon.group.game.drawer;

import chon.group.game.domain.agent.Agent;
import chon.group.game.domain.environment.Environment;
import javafx.scene.canvas.GraphicsContext;

public class JavaFxMediator implements EnvironmentDrawer {
    private final Environment environment;
    private final Agent agents;
    private final JavaFxDrawer drawer;
    private final double width;
    private final double height;

    public JavaFxMediator(Environment environment, Agent agents, GraphicsContext gc, double width, double height) {
        this.environment = environment;
        this.agents = agents;
        this.drawer = new JavaFxDrawer(gc, this);
        this.width = width;
        this.height = height;
    }

    @Override
    public void clearEnvironment() {
        drawer.clearScreen(this.width, this.height);
    }

    @Override
    public void drawBackground() {
        drawer.drawImage(this.environment.getBackgroundImage().getImage(),
                0, 0,
                this.width,
                this.height);
    }

    @Override
    public void drawAgents() {
        drawer.drawImage(agents.getPaddle1().getFill(),
                agents.getPaddle1().getTranslateX(),
                agents.getPaddle1().getTranslateY(),
                agents.getPaddle1().getRadius() * 2,
                agents.getPaddle1().getRadius() * 2);
        drawer.drawImage(agents.getPaddle2().getFill(),
                agents.getPaddle2().getTranslateX(),
                agents.getPaddle2().getTranslateY(),
                agents.getPaddle2().getRadius() * 2,
                agents.getPaddle2().getRadius() * 2);
        drawer.drawImage(agents.getPuck().getFill(),
                agents.getPuck().getTranslateX(),
                agents.getPuck().getTranslateY(),
                agents.getPuck().getRadius() * 2,
                agents.getPuck().getRadius() * 2);
    }
}
