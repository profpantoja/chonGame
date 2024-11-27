package chon.group.game.domain.environment;

import java.util.ArrayList;
import java.util.List;

import chon.group.game.domain.agent.Agent;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class Environment {

    private int width;
    private int height;
    private int posX;
    private int posY;
    private Image image;
    private Agent protagonist;
    private List<Agent> agents = new ArrayList<Agent>();
    private GraphicsContext gc;

    public Environment() {

    }

    public Environment(int posX, int posY, int width, int height, String pathImage) {
        this.posX = posX;
        this.posY = posY;
        this.height = height;
        this.width = width;
        this.setImage(pathImage);
        this.agents = new ArrayList<Agent>();
    }

    public Environment(int posX, int posY, int width, int height, String pathImage, ArrayList<Agent> agents) {
        this.posX = posX;
        this.posY = posY;
        this.height = height;
        this.width = width;
        this.setImage(pathImage);
        this.setAgents(agents);
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

    public int getPosX() {
        return posX;
    }

    public void setPosX(int positionX) {
        this.posX = positionX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int positionY) {
        this.posY = positionY;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(String pathImage) {
        this.image = new Image(getClass().getResource(pathImage).toExternalForm());
    }

    public void setProtagonist(Agent protagonist) {
        this.protagonist = protagonist;
    }

    public Agent getProtagonist() {
        return protagonist;
    }

    public List<Agent> getAgents() {
        return this.agents;
    }

    public void setAgents(ArrayList<Agent> agents) {
        this.agents = agents;
    }

    public GraphicsContext getGc() {
        return this.gc;
    }

    public void setGc(GraphicsContext gc) {
        this.gc = gc;
    }

    public void drawBackground() {
        gc.drawImage(this.image, this.posX, this.posY, this.width, this.height);
    }

    public void drawAgents() {
        for (Agent agent : this.agents) {
            gc.drawImage(agent.getImage(), agent.getPosX(), agent.getPosY(), agent.getWidth(), agent.getHeight());
        }
        gc.drawImage(this.protagonist.getImage(), this.protagonist.getPosX(), this.protagonist.getPosY(),
                this.protagonist.getWidth(), this.protagonist.getHeight());
        printStatusPanel(this.protagonist);
    }

    public void clearRect() {
        gc.clearRect(0, 0, this.width, this.height);
    }

    public void printStatusPanel(Agent agent) {
        gc.setFill(Color.BLACK);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);
        Font theFont = Font.font("Verdana", FontWeight.BOLD, 14);
        gc.setFont(theFont);
        gc.fillText("X: " + agent.getPosX(), agent.getPosX() + 10, agent.getPosY() - 25);
        gc.fillText("Y: " + agent.getPosY(), agent.getPosX() + 10, agent.getPosY() - 10);
    }

    // Futuramente essa classe deve receber um tipo Object q verificará tanto se
    // personagens ou qlqr coisa estão dentro dos limites do ambiente
    public void checkBorders() {
        if (this.protagonist.getPosX() < 0) {
            this.protagonist.setPosX(0);
        } else if ((this.protagonist.getPosX() + this.protagonist.getWidth()) > this.width) {
            this.protagonist.setPosX(this.width - protagonist.getWidth());
        } else if (this.protagonist.getPosY() < 0) {
            this.protagonist.setPosY(0);
        } else if ((this.protagonist.getPosY() + this.protagonist.getHeight()) > this.height) {
            this.protagonist.setPosY(this.height - this.protagonist.getHeight());
        }
    }
}