package chon.group.game.drawer;

import chon.group.game.domain.environment.Environment;
import javafx.scene.canvas.GraphicsContext;

public class JavaFxMediator implements EnvironmentDrawer {

    private final Environment environment;
    private final JavaFxDrawer drawer;

    public JavaFxMediator(Environment environment, GraphicsContext gc) {
        this.environment = environment;
        this.drawer = new JavaFxDrawer(gc);
    }

    @Override
    public void clearEnvironment() {
        drawer.clearEnvironment(environment);
    }

    @Override
    public void drawBackground() {
        drawer.drawBackground(environment);
    }

    @Override
    public void drawAgents() {
        drawer.drawAgents(environment);
    }

    @Override
    public void drawLifeBar() {
        drawer.drawLifeBar(environment);
    }

    @Override
    public void drawStatusPanel() {
        drawer.drawStatusPanel(null);
    }

    @Override
    public void drawPauseScreen() {
        drawer.drawPauseScreen(environment);
    }

}
