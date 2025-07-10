package chon.group.game.drawer;

/**
 * The {@code EnvironmentDrawer} interface defines methods for rendering
 * various elements in the game environment. It provides essential drawing
 * functionalities such as clearing the environment, rendering agents, and
 * displaying UI elements like life bars and status panels.
 */
public interface EnvironmentDrawer {

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
     * Draws the life bar for agents or players.
     */
    void drawLifeBar();

    /**
     * Draws the status panel displaying relevant game information.
     */
    void drawStatusPanel();



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

    void drawObjects();

    void drawObjectCounter();


}
