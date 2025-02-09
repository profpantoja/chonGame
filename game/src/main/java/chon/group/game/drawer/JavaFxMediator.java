package chon.group.game.drawer;

import chon.group.game.domain.agent.Agent;
import chon.group.game.domain.environment.Environment;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class JavaFxMediator implements EnvironmentDrawer {

    private final Environment environment;
    private final JavaFxDrawer drawer;

    public JavaFxMediator(Environment environment, GraphicsContext gc) {
        this.environment = environment;
        this.drawer = new JavaFxDrawer(gc, this);
    }

    @Override
    public void clearEnvironment() {
        drawer.clearScreen(this.environment.getWidth(), this.environment.getHeight());
    }

    @Override
    public void drawBackground() {
        drawer.drawImage(this.environment.getImage(),
                this.environment.getPosX(),
                this.environment.getPosY(),
                this.environment.getWidth(),
                this.environment.getHeight());
    }

    /**
     * Renders all agents and the protagonist in the environment.
     */
    @Override
    public void drawAgents() {
        for (Agent agent : this.environment.getAgents()) {
            drawer.drawImage(agent.getImage(),
                    agent.getPosX(),
                    agent.getPosY(),
                    agent.getWidth(),
                    agent.getHeight());
            drawer.drawLifeBar(agent.getHealth(),
                    agent.getFullHealth(),
                    agent.getWidth(),
                    agent.getPosX(),
                    agent.getPosY(),
                    Color.DARKRED);
        }
        drawer.drawImage(this.environment.getProtagonist().getImage(),
                this.environment.getProtagonist().getPosX(),
                this.environment.getProtagonist().getPosY(),
                this.environment.getProtagonist().getWidth(),
                this.environment.getProtagonist().getHeight());
        drawer.drawLifeBar(this.environment.getProtagonist().getHealth(),
                this.environment.getProtagonist().getFullHealth(),
                this.environment.getProtagonist().getWidth(),
                this.environment.getProtagonist().getPosX(),
                this.environment.getProtagonist().getPosY(),
                Color.GREEN);
        drawer.drawStatusPanel(this.environment.getProtagonist().getPosX(),
                this.environment.getProtagonist().getPosY());
        drawer.drawStatusPanel(this.environment.getProtagonist().getPosX(),
                this.environment.getProtagonist().getPosY());
        /* Render floating damage numbers that appear when agents take damage */
        drawer.drawDamageNumbers(this.environment.getDamageNumbers());

    }

    @Override
    public void drawLifeBar() {
        drawer.drawLifeBar(
                this.environment.getProtagonist().getHealth(),
                this.environment.getProtagonist().getFullHealth(),
                this.environment.getProtagonist().getWidth(),
                this.environment.getProtagonist().getPosX(),
                this.environment.getProtagonist().getPosY(),
                Color.GREEN);
    }

    @Override
    public void drawStatusPanel() {
        drawer.drawStatusPanel(this.environment.getProtagonist().getPosX(),
                this.environment.getProtagonist().getPosY());
    }

    @Override
    public void drawPauseScreen() {
        drawer.drawPauseScreen(this.environment.getPauseImage(),
                (int) this.environment.getPauseImage().getWidth(),
                (int) this.environment.getPauseImage().getHeight(),
                this.environment.getWidth(),
                this.environment.getHeight());
    }

    @Override
    public void drawDamageNumbers() {
        drawer.drawDamageNumbers(this.environment.getDamageNumbers());
    }

}
