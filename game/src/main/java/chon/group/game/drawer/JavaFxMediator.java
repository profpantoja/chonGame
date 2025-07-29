package chon.group.game.drawer;

import java.util.Iterator;

import chon.group.game.core.agent.Agent;
import chon.group.game.core.agent.Object;
import chon.group.game.core.environment.Environment;
import chon.group.game.core.weapon.Shot;
import chon.group.game.messaging.Message;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * The {@code JavaFxMediator} class serves as an intermediary for rendering the
 * game environment and its elements using JavaFX. It coordinates the
 * interaction
 * between the {@link Environment} and the {@link JavaFxDrawer} to manage
 * graphical rendering.
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
     * Renders the entire game environment, including background, agents, objects,
     * projectiles, and messages.
     */
    @Override
    public void renderGame() {
        this.drawBackground();
        this.drawAgents();
        this.drawObjects();
        this.drawShots();
        this.drawForeground();
        this.drawMessages();
    }

    /**
     * Clears the environment by erasing all drawn elements on the screen.
     */
    @Override
    public void clearEnvironment() {
        drawer.clearScreen((int) this.environment.getCamera().getScreenWidth(),
                this.environment.getCurrentLevel().getHeight());
    }

    /**
     * Draws the background image of the environment.
     */
    @Override
    public void drawBackground() {
        int posX = (int) (this.environment.getCamera().getPosX() * -1);
        drawer.drawImage(this.environment.getCurrentLevel().getImage(),
                posX, this.environment.getCurrentLevel().getPosY(),
                this.environment.getCurrentLevel().getWidth(),
                this.environment.getCurrentLevel().getHeight());
    }

    public void drawForeground() {
        int posX = (int) (this.environment.getCamera().getPosX() * -1);
        drawer.drawImage(this.environment.getCurrentLevel().getForeground(),
                posX, 384,
                640,
                176);
    }

    /**
     * Renders all agents and the protagonist within the environment,
     * including their health bars and status panels.
     */
    @Override
    public void drawAgents() {
        for (Agent agent : this.environment.getCurrentLevel().getAgents()) {
            int newPosX = (int) this.environment.getCamera().updateEntity(agent);
            drawer.drawImage(agent.getImage(),
                    newPosX,
                    agent.getPosY(),
                    agent.getWidth(),
                    agent.getHeight());

            if (agent.isVisibleBars())
                drawer.drawEnergyBar(agent.getEnergy(),
                        agent.getFullEnergy(),
                        agent.getWidth(),
                        newPosX,
                        agent.getPosY(),
                        Color.BLUE);
            if (agent.isVisibleBars())
                drawer.drawLifeBar(agent.getHealth(),
                        agent.getFullHealth(),
                        agent.getWidth(),
                        newPosX,
                        agent.getPosY(),
                        Color.GREEN);
        }

        Agent protagonist = this.environment.getProtagonist();
        Agent keyAgent = this.environment.getKeyAgent();
        Agent fishAgent = this.environment.getFishAgent();
        int newPosX = (int) this.environment.getCamera().updateEntity(protagonist);
        int newKeyAgentPosX = (int) this.environment.getCamera().updateEntity(keyAgent);
        int newFishAgentPosX = (int) this.environment.getCamera().updateEntity(fishAgent);
        drawer.drawImage(protagonist.getImage(),
                newPosX,
                protagonist.getPosY(),
                protagonist.getWidth(),
                protagonist.getHeight());
        drawer.drawImage(keyAgent.getImage(),
                newKeyAgentPosX,
                keyAgent.getPosY(),
                keyAgent.getWidth(),
                keyAgent.getHeight());
        drawer.drawImage(fishAgent.getImage(),
                newFishAgentPosX,
                fishAgent.getPosY(),
                fishAgent.getWidth(),
                fishAgent.getHeight());

        if (protagonist.isVisibleBars()) {
            this.drawSingleLifeBar();
            this.drawSingleEnergyBar();
        }
        this.drawDebugPanel();
        this.drawPanel();
    }

    /**
     * Renders all objects within the environment.
     */
    @Override
    public void drawObjects() {
        for (Object object : environment.getCurrentLevel().getObjects()) {
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
    public void drawSingleLifeBar() {
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
    public void drawSingleEnergyBar() {
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

    @Override
    public void drawPanel() {
        Agent protagonist = this.environment.getProtagonist();
        int collected = environment.getCollectedCount();
        int total = environment.getCurrentLevel().getTotalCollectibleCount();
        int score = environment.getScore();
        int life = protagonist.getHealth();
        int maxLife = protagonist.getFullHealth();
        double energy = protagonist.getEnergy();
        double maxEnergy = protagonist.getFullEnergy();
        Font.loadFont(getClass().getResourceAsStream("/fonts/Daydream.ttf"), 14);
        Font theFont = Font.font("Daydream", FontWeight.NORMAL, 14);
        drawer.drawPanel(life,
                maxLife,
                collected,
                total,
                score,
                energy,
                maxEnergy,
                theFont,
                this.environment.getPanel().getLifeIcon(),
                this.environment.getPanel().getEnergyIcon(),
                this.environment.getPanel().getItemIcon(),
                this.environment.getPanel().getScoreIcon(),
                this.environment.getPanel().getPanelWidth(),
                this.environment.getPanel().getPanelHeight());
    }

    /**
     * Draws the protagonist's status panel with stats like score, life, energy, and
     * collected items.
     */
    @Override
    public void drawDebugPanel() {
        Agent protagonist = this.environment.getProtagonist();
        drawer.drawDebugPanel(protagonist.getPosX(),
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
                this.environment.getCurrentLevel().getHeight());
    }

    /**
     * Draws the game over screen overlay, displaying a game over image centered
     * within the environment.
     */
    @Override
    public void drawGameOver() {
        drawer.drawScreen(this.environment.getGameOverImage(),
                (int) this.environment.getPauseImage().getWidth(),
                (int) this.environment.getPauseImage().getHeight(),
                (int) this.environment.getCamera().getScreenWidth(),
                this.environment.getCurrentLevel().getHeight());
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
        Iterator<Shot> iterator = this.environment.getCurrentLevel().getShots().iterator();
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
