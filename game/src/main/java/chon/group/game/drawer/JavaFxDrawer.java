package chon.group.game.drawer;

import chon.group.game.domain.agent.Agent;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class JavaFxDrawer {

    /** The graphics context used to render the environment. */
    private final GraphicsContext gc;
    private final EnvironmentDrawer mediator;

    /**
     * Constructor to initialize the JavaFx Drawer.
     *
     * @param gc the GraphicsContext instance
     */
    public JavaFxDrawer(GraphicsContext gc, EnvironmentDrawer mediator) {
        this.gc = gc;
        this.mediator = mediator;
    }

    /**
     * Clears the environment area, removing previously drawn elements.
     */
    public void clearEnvironment() {
        this.gc.clearRect(0, 0, mediator.getEnvironmentWidth(), mediator.getEnvironmentHeight());
    }

    /**
     * Renders the environment's background on the graphics context.
     */
    public void drawBackground() {
        this.gc.drawImage(mediator.getEnvironmentImage(),
                mediator.getEnvironmentPosX(),
                mediator.getEnvironmentPosY(),
                mediator.getEnvironmentWidth(),
                mediator.getEnvironmentHeight());
    }

    /**
     * Renders all agents and the protagonist in the environment.
     */
    public void drawAgents() {
        for (Agent agent : mediator.getAgents()) {
            this.gc.drawImage(agent.getImage(), agent.getPosX(), agent.getPosY(), agent.getWidth(), agent.getHeight());
        }
        this.gc.drawImage(mediator.getProtagonist().getImage(),
                mediator.getProtagonist().getPosX(),
                mediator.getProtagonist().getPosY(),
                mediator.getProtagonist().getWidth(),
                mediator.getProtagonist().getHeight());
        this.drawStatusPanel(mediator.getProtagonist());
        this.drawLifeBar();
    }

    /**
     * Renders the Protagonist's Life Bar.
     */
    public void drawLifeBar() {
        /* The border's thickness. */
        int borderThickness = 2;
        /* The bar's height. */
        int barHeight = 5;
        /* The life span proportion calculated based on actual and maximum health. */
        int lifeSpan = Math.round(
                (float) ((mediator.getProtagonist().getHealth() * 100 / mediator.getProtagonist().getFullHealth())
                        * mediator.getProtagonist().getWidth()) / 100);
        /* Int points before the agent's y position. The initial bar's position. */
        int barY = 15;
        /* The outside background of the health bar. */
        this.gc.setFill(Color.BLACK);
        /* The height is a little bit bigger to give a border experience. */
        this.gc.fillRect(mediator.getProtagonist().getPosX(),
                mediator.getProtagonist().getPosY() - barY,
                mediator.getProtagonist().getWidth(),
                barHeight + (borderThickness * 2));
        /**
         * The inside of the health bar. It is the effective life of the agent.
         * The border height plus the thickness multiplied by two (beggining and end at
         * X).
         */
        this.gc.setFill(Color.GREEN);
        /**
         * The initial position considering the border from both X and Y points.
         * The life span less the border thickness multiplied by two (beggining and end
         * at Y).
         */
        this.gc.fillRect(mediator.getProtagonist().getPosX() + borderThickness,
                mediator.getProtagonist().getPosY() - (barY - borderThickness),
                (lifeSpan - (borderThickness * 2)),
                barHeight);
    }

    /**
     * Displays a status panel showing the protagonist's coordinates.
     *
     * @param agent the protagonist whose information will be displayed
     */
    public void drawStatusPanel(Agent agent) {
        Font theFont = Font.font("Verdana", FontWeight.BOLD, 14);
        this.gc.setFont(theFont);
        this.gc.setFill(Color.BLACK);
        this.gc.fillText("X: " + agent.getPosX(), agent.getPosX() + 10, agent.getPosY() - 40);
        this.gc.fillText("Y: " + agent.getPosY(), agent.getPosX() + 10, agent.getPosY() - 25);
    }

    /**
     * Renders the Game Paused Screen.
     */
    public void drawPauseScreen() {
        if (mediator.getPauseImage() != null && this.gc != null) {
            double centerX = (mediator.getEnvironmentWidth() - mediator.getPauseImage().getWidth()) / 2;
            double centerY = (mediator.getEnvironmentHeight() - mediator.getPauseImage().getHeight()) / 2;
            /* Draw image on the center of screen */
            this.gc.drawImage(mediator.getPauseImage(), centerX, centerY);
        } else {
            System.out.println("Pause image not set or GraphicsContext is null.");
        }
    }

}
