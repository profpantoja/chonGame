package chon.group.game.drawer.service;

import java.util.Iterator;

import chon.group.game.Game;
import chon.group.game.core.agent.Agent;
import chon.group.game.core.agent.Object;
import chon.group.game.core.environment.Environment;
import chon.group.game.core.weapon.Shot;
import chon.group.game.drawer.client.Drawer;
import chon.group.game.drawer.client.JavaFxDrawer;
import chon.group.game.menu.Item;
import chon.group.game.menu.Menu;
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
        int posX = (int) (this.game.getEnvironment().getCamera().getPosX() * -1);
        drawer.drawImage(this.game.getEnvironment().getCurrentLevel().getAnimationState().getCurrentImage(),
                posX, this.game.getEnvironment().getCurrentLevel().getPosY(),
                this.game.getEnvironment().getCurrentLevel().getWidth(),
                this.game.getEnvironment().getCurrentLevel().getHeight());
    }

    /**
     * Renders all agents and the protagonist within the environment,
     * including their health bars and status panels.
     */
    @Override
    public void drawAgents() {
        for (Agent agent : this.game.getEnvironment().getCurrentLevel().getAgents()) {
            int newPosX = (int) this.game.getEnvironment().getCamera().updateEntity(agent);
            drawer.drawImage(
                    agent.getAnimationState().getCurrentImage(),
                    newPosX,
                    agent.getPosY(),
                    agent.getFlippedWidth(),
                    agent.getHeight());
            if (agent.isVisibleBars()) {
                drawer.drawEnergyBar(
                        agent.getEnergy(),
                        agent.getFullEnergy(),
                        agent.getWidthOffset(),
                        (int) this.game.getEnvironment().getCamera().updateBar(agent),
                        agent.getPosY(),
                        Color.BLUE);
                drawer.drawLifeBar(
                        agent.getHealth(),
                        agent.getFullHealth(),
                        agent.getWidthOffset(),
                        (int) this.game.getEnvironment().getCamera().updateBar(agent),
                        agent.getPosY(),
                        Color.GREEN);
            }
            if (this.game.getEnvironment().isDebugMode()) {
                drawer.drawEntityPanel(
                        agent.getPosX(),
                        agent.getPosY(),
                        (int) this.game.getEnvironment().getCamera().getPosX(),
                        agent.getHeight(),
                        agent.getDirection().toString(),
                        agent.getStatus().toString(),
                        agent.getAnimationState().isBlocked(),
                        agent.getAnimationState().isFinished());
            }
        }
        Agent protagonist = this.game.getEnvironment().getProtagonist();
        int newPosX = (int) this.game.getEnvironment().getCamera().updateEntity(protagonist);
        drawer.drawImage(
                protagonist.getAnimationState().getCurrentImage(),
                newPosX,
                protagonist.getPosY(),
                protagonist.getFlippedWidth(),
                protagonist.getHeight());
        if (protagonist.isVisibleBars()) {
            this.drawSingleLifeBar();
            this.drawSingleEnergyBar();
        }
        if (this.game.getEnvironment().isDebugMode()) {
            drawer.drawEntityPanel(
                    protagonist.getPosX(),
                    protagonist.getPosY(),
                    (int) this.game.getEnvironment().getCamera().getPosX(),
                    protagonist.getHeight(),
                    protagonist.getDirection().toString(),
                    protagonist.getStatus().toString(),
                    protagonist.getAnimationState().isBlocked(),
                    protagonist.getAnimationState().isFinished());
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
            drawer.drawImage(object.getAnimationState().getCurrentImage(),
                    (int) this.game.getEnvironment().getCamera().updateEntity(object),
                    object.getPosY(),
                    object.getFlippedWidth(),
                    object.getHeight());
            if (object.isVisibleBars())
                drawer.drawLifeBar(
                        object.getHealth(),
                        object.getFullHealth(),
                        object.getWidth(),
                        (int) this.game.getEnvironment().getCamera().updateBar(object),
                        object.getPosY(),
                        Color.GREEN);
            if (this.game.getEnvironment().isDebugMode())
                drawer.drawEntityPanel(
                        object.getPosX(),
                        object.getPosY(),
                        (int) this.game.getEnvironment().getCamera().getPosX(),
                        object.getHeight(),
                        object.getDirection().toString(),
                        object.getStatus().toString(),
                        object.getAnimationState().isBlocked(),
                        object.getAnimationState().isFinished());
        }
    }

    /**
     * Draws the protagonist's status panel with stats like score, life, energy, and
     * collected items.
     */
    @Override
    public void drawDebugPanel() {
        drawer.drawDebugPanel(
                240,
                85,
                this.game.getEnvironment().getCamera().getPosX(),
                this.game.getEnvironment().getMessages().size(),
                this.game.getEnvironment().getCurrentLevel().getShots().size());
    }

    /**
     * Draws the protagonist's life bar on the screen.
     */
    @Override
    public void drawSingleLifeBar() {
        Agent protagonist = this.game.getEnvironment().getProtagonist();
        if (protagonist != null) {
            drawer.drawLifeBar(
                    protagonist.getHealth(),
                    protagonist.getFullHealth(),
                    protagonist.getWidthOffset(),
                    (int) this.game.getEnvironment().getCamera().updateBar(protagonist),
                    protagonist.getPosY(),
                    Color.GREEN);
        }
    }

    /**
     * Draws the protagonist's energy bar on the screen.
     */
    @Override
    public void drawSingleEnergyBar() {
        Agent protagonist = this.game.getEnvironment().getProtagonist();
        if (protagonist != null) {
            drawer.drawEnergyBar(
                    protagonist.getEnergy(),
                    protagonist.getFullEnergy(),
                    protagonist.getWidthOffset(),
                    (int) this.game.getEnvironment().getCamera().updateBar(protagonist),
                    protagonist.getPosY(),
                    Color.BLUE);
        }
    }

    @Override
    public void drawPanel() {
        Agent protagonist = this.game.getEnvironment().getProtagonist();
        int collected = this.game.getEnvironment().getCollectedCount();
        int total = this.game.getEnvironment().getCurrentLevel().getTotalCollectibleCount();
        int score = this.game.getEnvironment().getScore();
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
                this.game.getEnvironment().getPanel().getLifeIcon(),
                this.game.getEnvironment().getPanel().getEnergyIcon(),
                this.game.getEnvironment().getPanel().getItemIcon(),
                this.game.getEnvironment().getPanel().getScoreIcon(),
                this.game.getEnvironment().getPanel().getPanelWidth(),
                this.game.getEnvironment().getPanel().getPanelHeight());
    }

    /**
     * Draws the pause screen overlay, displaying a pause image centered within the
     * environment.
     */
    @Override
    public void drawPauseScreen() {
        drawer.drawScreen(this.game.getEnvironment().getPauseImage(),
                (int) this.game.getEnvironment().getPauseImage().getWidth(),
                (int) this.game.getEnvironment().getPauseImage().getHeight(),
                (int) this.game.getEnvironment().getCamera().getScreenWidth(),
                this.game.getEnvironment().getCurrentLevel().getHeight());
    }

    /**
     * Draws the game over screen overlay, displaying a game over image centered
     * within the environment.
     */
    @Override
    public void drawGameOver() {
        drawer.drawScreen(this.game.getEnvironment().getGameOverImage(),
                (int) this.game.getEnvironment().getGameOverImage().getWidth(),
                (int) this.game.getEnvironment().getGameOverImage().getHeight(),
                (int) this.game.getEnvironment().getCamera().getScreenWidth(),
                this.game.getEnvironment().getCurrentLevel().getHeight());
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
        Iterator<Shot> iterator = this.game.getEnvironment().getCurrentLevel().getShots().iterator();
        while (iterator.hasNext()) {
            Shot shot = iterator.next();
            drawer.drawImage(shot.getAnimationState().getCurrentImage(),
                    (int) this.game.getEnvironment().getCamera().updateEntity(shot),
                    shot.getPosY(),
                    shot.getFlippedWidth(),
                    shot.getHeight());
            if (this.game.getEnvironment().isDebugMode())
                this.drawer.drawEntityPanel(
                        shot.getPosX(),
                        shot.getPosY(),
                        (int) this.game.getEnvironment().getCamera().getPosX(),
                        shot.getHeight(),
                        shot.getDirection().toString(),
                        shot.getStatus().toString(),
                        shot.getAnimationState().isBlocked(),
                        shot.getAnimationState().isFinished());
        }
    }

    /**
     * Renders the current Menu.
     */
    @Override
    public void drawMenu() {
        Menu currentMenu = this.game.getMenu().getCurrentMenu();
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

}
