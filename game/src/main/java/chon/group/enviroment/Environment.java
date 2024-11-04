package chon.group.enviroment;

import java.util.ArrayList;

import chon.group.agent.Agent;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class Environment {
    private int width;
    private int height;
    private int positionX;
    private int positionY;
    private Image image;
    private Agent protagonist;
    private ArrayList<Agent> agents = new ArrayList<>();
    private GraphicsContext gc;

    public Environment() {

    }

    public Environment(int positionX, int positionY, int width, int height, String pathImage, ArrayList<Agent> agents, GraphicsContext gc) {
        this.positionX = positionX;
        this.positionY = positionY;
        this.height = height;
        this.width = width;
        this.gc = gc;
        setImage(pathImage);
        setAgents(agents);
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

    public int getPositionX() {
        return positionX;
    }

    public void setPositionX(int positionX) {
        this.positionX = positionX;
    }

    public int getPositionY() {
        return positionY;
    }

    public void setPositionY(int positionY) {
        this.positionY = positionY;
    }

    public Image getImage() {
        return image;
    }

    public final void setImage(String pathImage){
        this.image = new Image(getClass().getResource(pathImage).toExternalForm());
        this.drawBackground();
    }

    public Agent getProtagonist() {
        return protagonist;
    }

    public ArrayList<Agent> getAgents(){
        return this.agents;
    }

    public void setAgents(ArrayList<Agent> agents){
        this.agents = agents;

        for (Agent agent : agents){
            if(agent.getIsProtagonist()){
                this.protagonist = agent;
                break;
            }
        }

        this.drawAgents(agents);
    }

    public GraphicsContext getGc() {
        return this.gc;
    }

    public void setGc(GraphicsContext gc) {
        this.gc = gc;
    }

    public void drawBackground() {
        gc.drawImage(this.image, this.positionX, this.positionY, this.width, this.height);
    }

    public void drawAgents(ArrayList<Agent> agents){
        for (Agent agent : agents){
            gc.drawImage(agent.getImage(), agent.getPositionX(), agent.getPositionY(), agent.getWidth(), agent.getHeight());

        }
        printStatusPanel(this.protagonist);
        printLifeEnergybar(this.protagonist);
    }

    public void clearRect(){
        gc.clearRect(0, 0, 1180, 780);
    }

    public void printLifeEnergybar(Agent agent) {
        Image lifeBarImage = new Image
        (getClass().getResource("/images/agent/Life_bar_and_energy_bar/barra de vida1.png").toExternalForm());
        gc.drawImage(lifeBarImage, agent.getPositionX() + 10, agent.getPositionY() - 5);
        Image energyBarImage = new Image
        (getClass().getResource("/images/agent/Life_bar_and_energy_bar/barra de energia 1.png").toExternalForm());
        gc.drawImage(energyBarImage, agent.getPositionX() + 10, agent.getPositionY() - -6);
	}

    public void printStatusPanel(Agent agent) {

        gc.setFill(Color.WHITE);
        gc.setStroke(Color.WHITE);
        gc.setLineWidth(2);
        Font theFont = Font.font("Verdana", FontWeight.BOLD, 14);
        gc.setFont(theFont);
        gc.fillText("X: " + agent.getPositionX(), agent.getPositionX() + 10, agent.getPositionY() - 25);
        gc.fillText("Y: " + agent.getPositionY(), agent.getPositionX() + 10, agent.getPositionY() - 10);
        
	}

    public Boolean limitsApprove(){

        if(this.protagonist.getPositionX() <= 0){
            this.protagonist.setPositionX(1);
        }else if(this.width == (this.protagonist.getPositionX() + this.protagonist.getWidth())){
            this.protagonist.setPositionX(this.protagonist.getPositionX() - 1);
        }else if(this.protagonist.getPositionY() <= 0){
            this.protagonist.setPositionY(1);
        }else if(this.height == (this.protagonist.getPositionY() + this.protagonist.getHeight())){
            this.protagonist.setPositionY(this.protagonist.getPositionY() - 1);
        }

        return true;
    }
}
