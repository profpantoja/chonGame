package chon.group.game.drawer;

import java.util.Iterator;

import chon.group.game.domain.agent.Agent;
import chon.group.game.domain.agent.Shot;
import chon.group.game.domain.environment.Collision;
import chon.group.game.domain.agent.Slash;
import chon.group.game.domain.environment.Environment;
import chon.group.game.messaging.Message;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * The {@code JavaFxMediator} class serves as an intermediary for rendering the
 * game environment
 * and its elements using JavaFX. It coordinates the interaction between the
 * {@link Environment}
 * and the {@link JavaFxDrawer} to manage graphical rendering.
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
     * Clears the environment by erasing all drawn elements on the screen.
     */
    @Override
    public void clearEnvironment() {
        drawer.clearScreen(this.environment.getWidth(), this.environment.getHeight());
    }

    /**
     * Clears the environment by erasing all drawn elements on the screen.
     */
    @Override
    public void clearEnvironmentSideScrolling() {
        drawer.clearScreen((int)drawer.getCanvasWidth(), (int)drawer.getCanvasHeight());
    }

    /**
     * Draws the background image of the environment.
     */
    @Override
    public void drawBackground() {
        drawer.drawImage(this.environment.getImage(),
                this.environment.getPosX(),
                this.environment.getPosY(),
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
            // Use animated frame if available
            drawer.drawImage(agent.getCurrentFrameImage(),
                    agent.getPosX(),
                    agent.getPosY(),
                    agent.getWidth(),
                    agent.getHeight());
            drawer.drawLifeBar(agent.getHealth(),
                    agent.getFullHealth(),
                    agent.getWidth(),
                    agent.getPosX(),
                    agent.getPosY(),
                    Color.DARKRED);
        }
        // Use animated frame for protagonist
        drawer.drawImage(this.environment.getProtagonist().getCurrentFrameImage(),
                this.environment.getProtagonist().getPosX(),
                this.environment.getProtagonist().getPosY(),
                this.environment.getProtagonist().getWidth(),
                this.environment.getProtagonist().getHeight());
        drawer.drawLifeBar(this.environment.getProtagonist().getHealth(),
                this.environment.getProtagonist().getFullHealth(),
                this.environment.getProtagonist().getWidth(),
                this.environment.getProtagonist().getPosX(),
                this.environment.getProtagonist().getPosY(),
                Color.GREEN);
        drawer.drawStatusPanel(this.environment.getProtagonist().getPosX(),
                this.environment.getProtagonist().getPosY());
        drawer.drawStatusPanel(this.environment.getProtagonist().getPosX(),
                this.environment.getProtagonist().getPosY());
    }
    
    /**
     * Renders all collisions within the environment,
     */
    @Override
    public void drawCollisions() {
        for (Collision collision : this.environment.getCollisions()) {
            drawer.drawImage(collision.getImage(),
                    collision.getX(),
                    collision.getY(),
                    collision.getWidth(),
                    collision.getHeight());
        }
    }

    /**
     * Draws the protagonist's life bar on the screen.
     */
    @Override
    public void drawLifeBar() {
        drawer.drawLifeBar(
            this.environment.getProtagonist().getHealth(),
            this.environment.getProtagonist().getFullHealth(),
            this.environment.getProtagonist().getWidth(),
            this.environment.getProtagonist().getPosX(),
            this.environment.getProtagonist().getPosY(),
            Color.GREEN);
        }
        
        /**
         * Draws the protagonist's status panel on the screen.
         */
        @Override
        public void drawStatusPanel() {
            drawer.drawStatusPanel(this.environment.getProtagonist().getPosX(),
            this.environment.getProtagonist().getPosY());
        }
        
        // Now this method use canvas width and height, with this we can use
        // the same method for side-scrolling and top-down environments.
        @Override
        public void drawGameOver() {
            drawer.drawScreen(this.environment.getGameOverImage(),
                (int) this.environment.getGameOverImage().getWidth(),
                (int) this.environment.getGameOverImage().getHeight(),
                (int) drawer.getCanvasWidth(),   
                (int) drawer.getCanvasHeight());
        }

        // Now this method use canvas width and height, with this we can use
        // the same method for side-scrolling and top-down environments.
        @Override
        public void drawWinScreen(){
            drawer.drawScreen(this.environment.getWinImage(),
                (int) this.environment.getWinImage().getWidth(),
                (int) this.environment.getWinImage().getHeight(),
                (int) drawer.getCanvasWidth(),   
                (int) drawer.getCanvasHeight());
        }
        
        /**
         * Draws damage messaages that appear when agents take damage.
         * The message float upward and fade out over time.
         */
        @Override
        public void drawMessages() {
            Iterator<Message> iterator = this.environment.getMessages().iterator();
            while (iterator.hasNext()) {
                Message message = iterator.next();
                drawer.drawMessages(message.getSize(),
                message.getOpacity(),
                Color.BLACK,
                Color.WHEAT,
                String.valueOf(message.getMessage()),
                message.getPosX(),
                message.getPosY());
            }
        }
        
        @Override
        public void drawShots() {
            Iterator<Shot> iterator = this.environment.getShots().iterator();
            while (iterator.hasNext()) {
                Shot shot = iterator.next();          
                drawer.drawImage(shot.getImage(),
                shot.getPosX(),
                shot.getPosY(),
                shot.getWidth(),
                shot.getHeight());
            }
        }

        /**
         * Draws the protagonist's status panel on the screen.
         */
        @Override
        public void drawStatusPanelSideScrolling() {
            Agent protagonist = this.environment.getProtagonist();
            double cameraX = this.environment.getCameraX();

            int screenX = (int) (protagonist.getPosX() - cameraX);
            int screenY = protagonist.getPosY();

            drawer.drawStatusPanelSideScrolling((int) protagonist.getPosX(),protagonist.getPosY(),screenX,screenY);
        }
        

        /**
         * Draws the background for side-scrolling environments, adjusting the
         * position based on the camera's X coordinate.
         */
        @Override
        public void drawBackgroundSideScrolling() {
            drawer.drawImage(this.environment.getImage(),
                    (int) (this.environment.getPosX() - this.environment.getCameraX()), // Posição X ajustada
                    this.environment.getPosY(),
                    this.environment.getWidth(),
                    this.environment.getHeight());
        }
    
        /**
         * Draws agents and the protagonist in a side-scrolling environment,
         * including their health bars and status panels.
         */
        @Override
        public void drawAgentsSideScrolling() {
            double cameraX = this.environment.getCameraX();
    
            for (Agent agent : this.environment.getAgents()) {
                int screenX = (int) (agent.getPosX() - cameraX);
                drawer.drawImage(agent.getCurrentFrameImage(),
                        screenX,
                        agent.getPosY(),
                        agent.getWidth(),
                        agent.getHeight());
                drawer.drawLifeBar(agent.getHealth(),
                        agent.getFullHealth(),
                        agent.getWidth(),
                        screenX,
                        agent.getPosY(),
                        Color.DARKRED);
            }

            for (Collision collision : this.environment.getCollisions()) {
                int screenX = (int) (collision.getX() - cameraX);
                drawer.drawImage(collision.getImage(),
                        screenX,
                        collision.getY(),
                        collision.getWidth(),
                        collision.getHeight());
            }
    
            Agent protagonist = this.environment.getProtagonist();
            int protagonistScreenX = (int) (protagonist.getPosX() - cameraX);
    
            drawer.drawImage(protagonist.getCurrentFrameImage(),
                    protagonistScreenX,
                    protagonist.getPosY(),
                    protagonist.getWidth(),
                    protagonist.getHeight());
            drawer.drawLifeBar(protagonist.getHealth(),
                    protagonist.getFullHealth(),
                    protagonist.getWidth(),
                    protagonistScreenX,
                    protagonist.getPosY(),
                    Color.GREEN);
            
            drawer.drawStatusPanelSideScrolling( (int) protagonist.getPosX(),protagonist.getPosY(),protagonistScreenX,protagonist.getPosY());
        }
    
        /**
         * Draws shots in a side-scrolling environment, adjusting their positions
         * based on the camera's X coordinate.
         */
        @Override
        public void drawShotsSideScrolling() {
            double cameraX = this.environment.getCameraX();
            Iterator<Shot> iterator = this.environment.getShots().iterator();
            while (iterator.hasNext()) {
                Shot shot = iterator.next();
                int screenX = (int) (shot.getPosX() - cameraX);          
                drawer.drawImage(shot.getImage(),screenX,shot.getPosY(),shot.getWidth(),shot.getHeight());
            }
        }

        /**
         * Draws damage messaages that appear when agents take damage.
         * The message float upward and fade out over time.
         */
        @Override
        public void drawMessagesSideScrolling() {
            Iterator<Message> iterator = this.environment.getMessages().iterator();
            while (iterator.hasNext()) {
                Message message = iterator.next();

                double screenX = message.getPosX() - this.environment.getCameraX();
                drawer.drawMessages(message.getSize(),
                message.getOpacity(),
                Color.BLACK,
                Color.WHEAT,
                String.valueOf(message.getMessage()),
                screenX,
                message.getPosY());
            }
        }
        @Override
    public void drawSlashes() {
        for (Slash slash : environment.getSlashes()) {
            drawer.drawImage(slash.getImage(),
                     slash.getPosX(),
                     slash.getPosY(),
                     slash.getWidth(), 
                     slash.getHeight());
        }
    }

}
    