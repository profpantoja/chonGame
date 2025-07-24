package chon.group.game.drawer;

import chon.group.game.ui.Menu;

/**
 * The {@code EnvironmentDrawer} interface defines methods for rendering
 * various elements in the game environment. It provides essential drawing
 * functionalities such as clearing the environment, rendering agents, and
 * displaying UI elements like life bars and status panels.
 */
public interface EnvironmentDrawer {

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
    void drawSingleLifeBar();

    /**
     * Draws the energy bar for agents or players.
     */
    void drawSingleEnergyBar();

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
     * Draws all the shots on the screen.
     */
    void drawShots();

    /**
     * Draws the Game Over screen when the protagonist is dead.
     */
    void drawGameOver();

    /**
     * Draws the main game menu.
     * @param menu A instância do Menu com as opções e seleção.
     */
    void drawMainMenu(Menu menu);

    /**
     * Draws the Win screen when the player completes the game.
     */
    void drawWinScreen(); // MÉTODO QUE ESTAVA FALTANDO
}