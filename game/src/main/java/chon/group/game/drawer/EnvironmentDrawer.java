package chon.group.game.drawer;

/**
 * Interface for drawing the game environment.
 */
public interface EnvironmentDrawer {
    /**
     * Clears the current environment, removing all drawn elements.
     */
    void clearEnvironment();

    /**
     * Draws the background of the environment.
     */
    void drawBackground();

    /**
     * Draws the agents (players and puck) in the environment.
     */
    void drawAgents();
}