package chon.group.game.drawer;

import chon.group.game.domain.agent.Agent;
import chon.group.game.domain.agent.Shot;
import chon.group.game.domain.environment.Environment;
import chon.group.game.messaging.Message;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * The {@code JavaFxMediator} class serves as an intermediary for rendering the
 * game environment and its elements using JavaFX.
 */
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

    @Override
    public void drawAgents() {
        // Desenha todos os agentes
        for (Agent agent : this.environment.getAgents()) {
            drawer.drawImage(agent.getImage(),
                    agent.getPosX(),
                    agent.getPosY(),
                    agent.getWidth(),
                    agent.getHeight());
            drawer.drawEnergyBar(agent.getEnergy(),
                    agent.getFullEnergy(),
                    agent.getWidth(),
                    agent.getPosX(),
                    agent.getPosY(),
                    Color.BLUE);
            drawer.drawLifeBar(agent.getHealth(),
                    agent.getFullHealth(),
                    agent.getWidth(),
                    agent.getPosX(),
                    agent.getPosY(),
                    Color.GREEN);
        }

        // Desenha o protagonista
        Agent protagonist = this.environment.getProtagonist();
        if (protagonist != null) {
            drawer.drawImage(protagonist.getImage(),
                    protagonist.getPosX(),
                    protagonist.getPosY(),
                    protagonist.getWidth(),
                    protagonist.getHeight());
            this.drawLifeBar();
            this.drawEnergyBar();
            this.drawStatusPanel();
        }
    }

    @Override
    public void drawLifeBar() {
        Agent protagonist = this.environment.getProtagonist();
        if (protagonist != null) {
            drawer.drawLifeBar(
                    protagonist.getHealth(),
                    protagonist.getFullHealth(),
                    protagonist.getWidth(),
                    protagonist.getPosX(),
                    protagonist.getPosY(),
                    Color.GREEN);
        }
    }

    @Override
    public void drawEnergyBar() {
        Agent protagonist = this.environment.getProtagonist();
        if (protagonist != null) {
            drawer.drawEnergyBar(
                    protagonist.getEnergy(),
                    protagonist.getFullEnergy(),
                    protagonist.getWidth(),
                    protagonist.getPosX(),
                    protagonist.getPosY(),
                    Color.BLUE);
        }
    }

    @Override
    public void drawStatusPanel() {
        Agent protagonist = this.environment.getProtagonist();
        if (protagonist != null) {
            drawer.drawStatusPanel(protagonist.getPosX(),
                    protagonist.getPosY());
        }
    }

    @Override
    public void drawPauseScreen() {
        drawer.drawScreen(this.environment.getPauseImage(),
                (int) this.environment.getPauseImage().getWidth(),
                (int) this.environment.getPauseImage().getHeight(),
                this.environment.getWidth(),
                this.environment.getHeight());
    }

    @Override
    public void drawGameOver() {
        drawer.drawScreen(this.environment.getGameOverImage(),
                (int) this.environment.getGameOverImage().getWidth(),
                (int) this.environment.getGameOverImage().getHeight(),
                this.environment.getWidth(),
                this.environment.getHeight());
    }

    @Override
    public void drawMessages() {
        for (Message message : this.environment.getMessages()) {
            drawer.drawMessages(message.getSize(),
                    message.getOpacity(),
                    Color.BLACK,
                    Color.WHEAT,
                    String.valueOf(message.getMessage()),
                    message.getPosX(),
                    message.getPosY());
        }
    }

    @Override
    public void drawShots() {
        for (Shot shot : this.environment.getShots()) {
            drawer.drawImage(shot.getImage(),
                    shot.getPosX(),
                    shot.getPosY(),
                    shot.getWidth(),
                    shot.getHeight());
        }
    }
}