package chon.group.game.drawer;

import java.util.Iterator;

import chon.group.game.GamePanel;
import chon.group.game.core.agent.Agent;
import chon.group.game.core.agent.Object;
import chon.group.game.core.environment.Environment;
import chon.group.game.core.weapon.Shot;
import chon.group.game.messaging.Message;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * The {@code JavaFxMediator} class serves as an intermediary for rendering the
 * game environment and its elements using JavaFX. It coordinates the interaction
 * between the {@link Environment} and the {@link JavaFxDrawer} to manage graphical rendering.
 */
public class JavaFxMediator implements EnvironmentDrawer {

    private final Environment environment;
    private final JavaFxDrawer drawer;
    private final GamePanel gamePanel;

    /**
     * Constructs a JavaFxMediator with the specified environment and graphics context.
     *
     * @param environment The game environment containing agents and the protagonist.
     * @param gc          The {@link GraphicsContext} used for rendering.
     * @param gamePanel   The UI panel used for rendering game statistics and status.
     * @param pixelFont   The font used in the UI panel.
     */
    public JavaFxMediator(Environment environment, GraphicsContext gc, GamePanel gamePanel, Font pixelFont) {
        this.environment = environment;
        this.drawer = new JavaFxDrawer(gc, this);
        this.gamePanel = new GamePanel(gc, pixelFont);
    }

    /**
     * Renders the entire game environment, including background, agents, objects,
     * projectiles, and messages.
     */
    @Override
    public void renderGame() {
        this.drawBackground();
        this.drawAgents();
        this.drawObjects();
        this.drawShots();
        this.drawMessages();
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
            drawer.drawEnergyBar(agent.getEnergy(),
                    agent.getFullEnergy(),
                    agent.getWidth(),
                    newPosX,
                    agent.getPosY(),
                    Color.BLUE);
            drawer.drawLifeBar(agent.getHealth(),
                    agent.getFullHealth(),
                    agent.getWidth(),
                    newPosX,
                    agent.getPosY(),
                    Color.GREEN);
        }

        Agent protagonist = this.environment.getProtagonist();
        int newPosX = (int) this.environment.getCamera().updateEntity(protagonist);
        drawer.drawImage(protagonist.getImage(),
                newPosX,
                protagonist.getPosY(),
                protagonist.getWidth(),
                protagonist.getHeight());

        this.drawLifeBar();
        this.drawEnergyBar();
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
        if (protagonist != null) {
            drawer.drawLifeBar(
                    protagonist.getHealth(),
                    protagonist.getFullHealth(),
                    protagonist.getWidth(),
                    (int) this.environment.getCamera().updateEntity(protagonist),
                    protagonist.getPosY(),
                    Color.GREEN);
        }
    }

    /**
     * Draws the protagonist's energy bar on the screen.
     */
    @Override
    public void drawEnergyBar() {
        Agent protagonist = this.environment.getProtagonist();
        if (protagonist != null) {
            drawer.drawEnergyBar(
                    protagonist.getEnergy(),
                    protagonist.getFullEnergy(),
                    protagonist.getWidth(),
                    (int) this.environment.getCamera().updateEntity(protagonist),
                    protagonist.getPosY(),
                    Color.BLUE);
        }
    }

    /**
     * Draws the protagonist's status panel with stats like score, life, energy, and collected items.
     */
    @Override
    public void drawStatusPanel() {
        Agent protagonist = this.environment.getProtagonist();
        drawer.drawStatusPanel(protagonist.getPosX(),
                protagonist.getPosY(),
                (int) this.environment.getCamera().getPosX());

        int collected = environment.getCollectedCount();
        int total = environment.getTotalCollectibleCount();
        int score = environment.getScore();
        int life = protagonist.getHealth();
        int maxLife = protagonist.getFullHealth();
        double energy = protagonist.getEnergy();
        double maxEnergy = protagonist.getFullEnergy();

        gamePanel.drawPanel(life, maxLife, collected, total, score, energy, maxEnergy);
    }

    /**
     * Draws the pause screen overlay, displaying a pause image centered within the environment.
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
     * Draws the game over screen overlay, displaying a game over image centered within the environment.
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
     * Draws damage messages that appear when agents take damage.
     * The messages float upward and fade out over time.
     */
    @Override
    public void drawMessages() {
        for (Message message : this.environment.getMessages()) {
            drawer.drawMessages(message.getSize(),
                    message.getOpacity(),
                    Color.BLACK,
                    Color.WHEAT,
                    String.valueOf(message.getMessage()),
                    message.getPosX() - this.environment.getCamera().getPosX(),
                    message.getPosY());
        }
    }

    /**
     * Renders all active shots (projectiles) currently in the environment.
     */
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
