package chon.group.game.drawer.service;

import chon.group.game.Game;
import chon.group.game.core.agent.Agent;
import chon.group.game.core.agent.Entity;
import chon.group.game.core.agent.Object;
import chon.group.game.core.environment.Environment;
import chon.group.game.core.weapon.Shot;
import chon.group.game.drawer.client.Drawer;
import chon.group.game.drawer.client.JavaFxDrawer;
import chon.group.game.menu.Item;
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
public class GameMediator implements GameDrawer {

    // private Environment environment;
    private Game game;
    private final Drawer drawer;

    /**
     * Constructs a JavaFxMediator with the specified environment and graphics
     * context.
     *
     * @param game The game environment containing agents and the
     *             protagonist.
     * @param gc   The {@link GraphicsContext} used for rendering.
     */
    public GameMediator(Drawer drawer) {
        this.drawer = drawer;
    }

    public void setGame(Game game) {
        this.game = game;
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
        drawer.clearScreen((int) this.game.getEnvironment().getCamera().getScreenWidth(),
                this.game.getEnvironment().getCurrentLevel().getHeight());
    }

    /**
     * Draws the background image of the environment.
     */
    @Override
    public void drawBackground() {
        var environment = this.game.getEnvironment();
        var level = environment.getCurrentLevel();
        drawer.drawImage(level.getAnimationState().getCurrentImage(),
                (int) (environment.getCamera().getPosX() * -1),
                level.getPosY(),
                level.getWidth(),
                level.getHeight());
    }

    /**
     * Renders all agents and the protagonist within the environment,
     * including their health bars and status panels.
     */
    @Override
    public void drawAgents() {
        for (Agent agent : this.game.getEnvironment().getCurrentLevel().getAgents()) {
            this.drawEntity(agent);
            if (agent.isVisibleBars()) {
                this.drawEnergyBar(agent);
                this.drawLifeBar(agent);
            }
            if (this.game.getEnvironment().isDebugMode())
                this.drawEntityDebugPanel(agent);
        }
        var protagonist = this.game.getEnvironment().getProtagonist();
        this.drawEntity(protagonist);
        if (protagonist.isVisibleBars()) {
            this.drawLifeBar(protagonist);
            this.drawEnergyBar(protagonist);
        }
        if (this.game.getEnvironment().isDebugMode()) {
            this.drawEntityDebugPanel(protagonist);
            this.drawDebugPanel();
        }
        this.drawPanel();
    }

    /**
     * Renders all objects within the environment.
     */
    @Override
    public void drawObjects() {
        for (Object object : this.game.getEnvironment().getCurrentLevel().getObjects()) {
            this.drawEntity(object);
            if (object.isVisibleBars())
                this.drawLifeBar(object);
            if (this.game.getEnvironment().isDebugMode())
                this.drawEntityDebugPanel(object);
        }
    }

    /**
     * Draws the protagonist's status panel with stats like score, life, energy, and
     * collected items.
     */
    @Override
    public void drawDebugPanel() {
        var environment = this.game.getEnvironment();
        drawer.drawDebugPanel(
                240,
                85,
                environment.getCamera().getPosX(),
                environment.getMessages().size(),
                environment.getCurrentLevel().getShots().size());
    }

    @Override
    public void drawPanel() {
        var environment = this.game.getEnvironment();
        var protagonist = environment.getProtagonist();
        var panel = environment.getPanel();
        int collected = environment.getCollectedCount();
        int total = environment.getCurrentLevel().getTotalCollectibleCount();
        int score = environment.getScore();
        int life = protagonist.getHealth();
        int maxLife = protagonist.getFullHealth();
        double energy = protagonist.getEnergy();
        double maxEnergy = protagonist.getFullEnergy();
        drawer.drawPanel(life,
                maxLife,
                collected,
                total,
                score,
                energy,
                maxEnergy,
                Font.font("Verdana", FontWeight.BOLD, 18),
                panel.getLifeIcon(),
                panel.getEnergyIcon(),
                panel.getItemIcon(),
                panel.getScoreIcon(),
                panel.getPanelWidth(),
                panel.getPanelHeight());
    }

    /**
     * Draws the pause screen overlay, displaying a pause image centered within the
     * environment.
     */
    @Override
    public void drawPauseScreen() {
        var environment = this.game.getEnvironment();
        drawer.drawScreen(environment.getPauseImage(),
                (int) environment.getPauseImage().getWidth(),
                (int) environment.getPauseImage().getHeight(),
                (int) environment.getCamera().getScreenWidth(),
                environment.getCurrentLevel().getHeight());
    }

    /**
     * Draws the game over screen overlay, displaying a game over image centered
     * within the environment.
     */
    @Override
    public void drawGameOver() {
        var environment = this.game.getEnvironment();
        drawer.drawScreen(environment.getGameOverImage(),
                (int) environment.getGameOverImage().getWidth(),
                (int) environment.getGameOverImage().getHeight(),
                (int) environment.getCamera().getScreenWidth(),
                environment.getCurrentLevel().getHeight());
    }

    /**
     * Draws damage messages that appear when agents take damage.
     * The messages float upward and fade out over time.
     */
    @Override
    public void drawMessages() {
        for (Message message : this.game.getEnvironment().getMessages()) {
            drawer.drawMessages(message.getSize(),
                    message.getOpacity(),
                    Color.BLACK,
                    Color.WHEAT,
                    String.valueOf(message.getMessage()),
                    message.getPosX() - this.game.getEnvironment().getCamera().getPosX(),
                    message.getPosY());
        }
    }

    /**
     * Renders all active shots (projectiles) currently in the environment.
     */
    @Override
    public void drawShots() {
        for (Shot shot : this.game.getEnvironment().getCurrentLevel().getShots()) {
            if (shot.isVisible()) {
                this.drawEntity(shot);
                if (this.game.getEnvironment().isDebugMode()) {
                    this.drawEntityDebugPanel(shot);
                }
            }
        }
    }

    /**
     * Renders the current Menu.
     */
    @Override
    public void drawMenu() {
        var currentMenu = this.game.getMenu().getCurrentMenu();
        drawer.drawMenu(
                currentMenu.getTitle(),
                currentMenu.getIndex(),
                currentMenu.getHeightProportion(),
                currentMenu.getWidth(),
                currentMenu.getSpan(),
                this.game.getEnvironment().getCamera().getScreenWidth(),
                this.game.getEnvironment().getCurrentLevel().getHeight(),
                currentMenu.getItems().stream()
                        .map(Item::getTitle)
                        .toArray(String[]::new));
    }

    private void drawEntityDebugPanel(Entity entity) {
        drawer.drawEntityPanel(
                entity.getPosX(),
                entity.getPosY(),
                (int) this.game.getEnvironment().getCamera().getPosX(),
                entity.getHeight(),
                entity.getDirection().toString(),
                entity.getStatus().toString(),
                entity.getAnimationState().isBlocked(),
                entity.getAnimationState().isFinished());
    }

    private void drawEntity(Entity entity) {
        drawer.drawImage(
                entity.getAnimationState().getCurrentImage(),
                (int) this.game.getEnvironment().getCamera().updateEntity(entity),
                entity.getPosY(),
                entity.getFlippedWidth(),
                entity.getHeight());
    }

    /**
     * Draws the protagonist's life bar on the screen.
     */
    @Override
    public void drawLifeBar(Entity entity) {
        drawer.drawLifeBar(
                entity.getHealth(),
                entity.getFullHealth(),
                entity.getWidth(),
                (int) this.game.getEnvironment().getCamera().updateBar(entity),
                entity.getPosY(),
                Color.GREEN);
    }

    /**
     * Draws the protagonist's energy bar on the screen.
     */
    @Override
    public void drawEnergyBar(Agent agent) {
        drawer.drawEnergyBar(
                agent.getEnergy(),
                agent.getFullEnergy(),
                agent.getWidthOffset(),
                (int) this.game.getEnvironment().getCamera().updateBar(agent),
                agent.getPosY(),
                Color.BLUE);
    }

}