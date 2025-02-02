package chon.group.game.drawer;

import java.util.List;

import chon.group.game.domain.agent.Agent;
import chon.group.game.domain.environment.Environment;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class JavaFxMediator implements EnvironmentDrawer {

    private final Environment environment;
    private final JavaFxDrawer drawer;

    public JavaFxMediator(Environment environment, GraphicsContext gc) {
        this.environment = environment;
        this.drawer = new JavaFxDrawer(gc, this);
    }

    @Override
    public void clearEnvironment() {
        drawer.clearEnvironment();
    }

    @Override
    public void drawBackground() {
        drawer.drawBackground();
    }

    @Override
    public void drawAgents() {
        drawer.drawAgents();
    }

    @Override
    public void drawLifeBar() {
        drawer.drawLifeBar();
    }

    @Override
    public void drawStatusPanel() {
        drawer.drawStatusPanel(null);
    }

    @Override
    public void drawPauseScreen() {
        drawer.drawPauseScreen();
    }

    @Override
    public int getEnvironmentWidth() {
        return environment.getWidth();
    }

    @Override
    public int getEnvironmentHeight() {
        return environment.getHeight();
    }

    @Override
    public int getEnvironmentPosX() {
        return environment.getPosX();
    }

    @Override
    public int getEnvironmentPosY() {
        return environment.getPosY();
    }

    @Override
    public Image getEnvironmentImage() {
        return environment.getImage();
    }

    @Override
    public Image getPauseImage() {
        return environment.getPauseImage();
    }

    @Override
    public List<Agent> getAgents() {
        return environment.getAgents();
    }

    @Override
    public Agent getProtagonist() {
        return environment.getProtagonist();
    }

}
