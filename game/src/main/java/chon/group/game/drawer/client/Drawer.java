package chon.group.game.drawer.client;

/* These imports must change to decouple the mediator. */
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public abstract class Drawer {

        /**
         * Clears the canvas area, removing previously drawn elements.
         *
         * @param width  The width of the area to clear.
         * @param height The height of the area to clear.
         */
        public abstract void clearScreen(
                        int width,
                        int height);

        /**
         * Renders an image at the specified position and dimensions.
         *
         * @param image  The image to be drawn.
         * @param posX   The x-coordinate position.
         * @param posY   The y-coordinate position.
         * @param width  The width of the image.
         * @param height The height of the image.
         */
        public abstract void drawImage(
                        Image image,
                        int posX,
                        int posY,
                        int width,
                        int height);

        /**
         * Renders the protagonist's life bar.
         *
         * @param health     The current health value.
         * @param fullHealth The maximum health value.
         * @param width      The width of the life bar.
         * @param posX       The x-coordinate position.
         * @param posY       The y-coordinate position.
         * @param color      The color of the life bar.
         */
        public abstract void drawLifeBar(
                        int health,
                        int fullHealth,
                        int width,
                        int posX,
                        int posY,
                        Color color);

        /**
         * Renders the agent's energy bar.
         *
         * @param energy     The current energy value (0.0 to 1.0).
         * @param fullEnergy The maximum energy value (typically 1.0).
         * @param width      The width of the energy bar.
         * @param posX       The x-coordinate position.
         * @param posY       The y-coordinate position.
         * @param color      The base color of the energy bar.
         */
        public abstract void drawEnergyBar(
                        double energy,
                        double fullEnergy,
                        int width,
                        int posX,
                        int posY,
                        Color color);

        /**
         * Draws the status panel showing health, energy, score, and collectibles.
         *
         * @param life      current life value
         * @param maxLife   maximum life value
         * @param collected number of collected items
         * @param total     total collectible items
         * @param score     current score
         * @param energy    current energy value
         * @param maxEnergy maximum energy value
         */
        public abstract void drawPanel(
                        int life,
                        int maxLife,
                        int collected,
                        int total,
                        int score,
                        double energy,
                        double maxEnergy,
                        Font pixelFont,
                        Image lifeIcon,
                        Image energyIcon,
                        Image itemIcon,
                        Image scoreIcon,
                        int panelWidth,
                        int panelHeight);

        public abstract void drawDebugPanel(
                        int panelWidth,
                        int panelHeight,
                        double camX,
                        int messages,
                        int shots);

        /**
         * Displays a status panel showing the protagonist's coordinates.
         *
         * @param posX The x-coordinate of the protagonist.
         * @param posY The y-coordinate of the protagonist.
         */

        public abstract void drawEntityPanel(
                        int posX,
                        int posY,
                        int camX,
                        int height,
                        String direction,
                        String entityStatus,
                        boolean blocked,
                        boolean finished);

        /**
         * Renders the pause screen, centering the pause image within the environment.
         *
         * @param image       The image representing the pause screen.
         * @param imageWidth  The width of the pause image.
         * @param imageHeight The height of the pause image.
         * @param width       The total width of the environment.
         * @param height      The total height of the environment.
         */
        public abstract void drawScreen(
                        Image image,
                        int imageWidth,
                        int imageHeight,
                        int width,
                        int height);

        /**
         * Draws damage numbers that appear when agents take damage.
         * The numbers float upward and fade out over time.
         * 
         * @param fontSize    The font size to be printed.
         * @param opacity     The opacity value from 0 to 1.
         * @param borderColor The border color.
         * @param fillColor   The inside color.
         * @param message     The message to be printed.
         * @param posX        The x-coordinate of the protagonist.
         * @param posY        The y-coordinate of the protagonist.
         */
        public abstract void drawMessages(
                        int fontSize,
                        double opacity,
                        Color borderColor,
                        Color fillColor,
                        String message,
                        double posX,
                        double posY);

        /**
         * Desenha um highlight (retângulo ou borda) para destacar a seleção atual em
         * menus.
         *
         * @param x      Posição X do highlight.
         * @param y      Posição Y do highlight.
         * @param width  Largura do highlight.
         * @param height Altura do highlight.
         */
        public abstract void drawHighlight(
                        double x,
                        double y,
                        double width,
                        double height);

        /**
         * Design the game's main menu, with a background that fills the entire screen.
         *
         * @param backgroundImage The image to fill the entire screen. If it is null,
         *                        use a black background.
         * @param title           The center menu title.
         * @param selectedIndex   The index of the currently selected option.
         * @param options         The text options for the central menu.
         */
        public abstract void drawMenu(
                        String title,
                        int selectedIndex,
                        double width,
                        double span,
                        double screenWidth,
                        double levelHeight,
                        String[] options);

}
