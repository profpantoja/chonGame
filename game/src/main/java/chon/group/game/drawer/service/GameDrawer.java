package chon.group.game.drawer.service;

import chon.group.game.Game;
import chon.group.game.core.agent.Agent;
import chon.group.game.core.agent.Entity;

/**
 * The {@code EnvironmentDrawer} interface defines methods for rendering
 * various elements in the game environment. It provides essential drawing
 * functionalities such as clearing the environment, rendering agents, and
 * displaying UI elements like life bars and status panels.
 */
public interface GameDrawer {

    void setGame(Game game);

    /** Render the Game */
    void renderGame();

    /**
     * Clears the environment, removing all elements.
     */
    void clearEnvironment();

    /**
     * Draws the background of the environment.
     */
    void drawBackground();

    /**
     * Draws all agents present in the environment.
     */
    void drawAgents();

    /**
     * Draws all the objects on the screen.
     */
    void drawObjects();

    /**
     * Draws the life bar for agents or players.
     */
    void drawLifeBar(Entity entity);

    /**
     * Draws the energy bar for agents or players.
     */
    void drawEnergyBar(Agent agent);

    /**
     * Draws the game panel.
     */
    void drawPanel();

    /**
     * Draws the status panel displaying relevant game information for debugging.
     */
    void drawDebugPanel();

    /**
     * Draws the pause screen when the game is paused.
     */
    void drawPauseScreen();

    /**
     * Draws all the messages on the screen.
     */
    void drawMessages();

    /**
     * Draws the Game Over screen when the protagonist is dead.
     */
    void drawShots();

    /**
     * Draws the Game Over screen when the protagonist is dead.
     */
    void drawGameOver();

    /**
     * Draws the current Menu.
     */
    void drawMenu();

}
