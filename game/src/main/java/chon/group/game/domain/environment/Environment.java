package chon.group.game.domain.environment;

import java.util.ArrayList;
import java.util.List;
import chon.group.game.domain.agent.Agent;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class Environment {

    private int posX;
    private int posY;
    private int width;
    private int height;
    private Image image;
    private Agent protagonist;
    private Agent chonBot;
    private List<Agent> agents = new ArrayList<>();
    private GraphicsContext gc;

    public Environment(int posX, int posY, int width, int height, String pathImage) {
        this.posX = posX;
        this.posY = posY;
        this.height = height;
        this.width = width;
        this.setImage(pathImage);
        this.agents = new ArrayList<>();
    }

    //Getters and Setters
    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(String pathImage) {
        this.image = new Image(getClass().getResource(pathImage).toExternalForm());
    }

    public Agent getProtagonist() {
        return protagonist;
    }

    public void setProtagonist(Agent protagonist) {
        this.protagonist = protagonist;
    }

    public List<Agent> getAgents() {
        return agents;
    }

    public void setAgents(List<Agent> agents) {
        this.agents = agents;
    }

    public GraphicsContext getGraphicsContext() {
        return gc;
    }

    public void setGraphicsContext(GraphicsContext gc) {
        this.gc = gc;
    }

    public void render() {
        gc.drawImage(image, posX, posY, width, height);
    
        for (Agent agent : agents) {
            // Draws each agent's image at its respective position and size
            gc.drawImage(agent.getImage(), agent.getPosX(), agent.getPosY(), agent.getWidth(), agent.getHeight());
        }
    
        // Check if the protagonist exists and is not dead before rendering
        if (protagonist != null && !protagonist.isDead()) {

            // Draw the protagonist's image at its current position
            gc.drawImage(protagonist.getImage(), protagonist.getPosX(), protagonist.getPosY(),
                         protagonist.getWidth(), protagonist.getHeight());
    
            // Render the health bar just above the protagonist's image
            gc.setFill(Color.RED);  // Background of the health bar (red)
            gc.fillRect(protagonist.getPosX(), protagonist.getPosY() - 10, protagonist.getWidth(), 5);
            gc.setFill(Color.GREEN);  // Foreground of the health bar (green)
            gc.fillRect(protagonist.getPosX(), protagonist.getPosY() - 10,
                        protagonist.getWidth() * (protagonist.getHealth() / 3.0), 5);
        }

    }
    
    public void detectCollision() {
        // Loop through all agents to check for collisions
        for (Agent agent : agents) {
            // Check if the protagonist is invulnerable; if not, check for collision
            if (protagonist != null && !protagonist.isInvulnerable() && isColliding(agent, protagonist)) {
                // The protagonist takes damage when colliding with an agent
                protagonist.takeDamage();
                // If the protagonist's health is 0 or below, the protagonist is dead
                if (protagonist.isDead()) {
                    System.out.println("The protagonist is dead!");
                }
            }
        }
    }
    
    private boolean isColliding(Agent a, Agent b) {
        // Returns true if there is a collision between two agents
        return a.getPosX() < b.getPosX() + b.getWidth() &&
                a.getPosX() + a.getWidth() > b.getPosX() &&
                a.getPosY() < b.getPosY() + b.getHeight() &&
                a.getPosY() + a.getHeight() > b.getPosY();
    }
    
    public void checkBorders() {
        // Prevent the protagonist from going beyond the top border
        if (protagonist.getPosY() < 0) {
            protagonist.setPosY(0);
        }
    
        if (protagonist.getPosY() + protagonist.getHeight() > height) {
            protagonist.setPosY(height - protagonist.getHeight());
        }
    
        if (protagonist.getPosX() < 0) {
            protagonist.setPosX(0);
        }

        if (protagonist.getPosX() + protagonist.getWidth() > width) {
            protagonist.setPosX(width - protagonist.getWidth());
        }
    }
}    
