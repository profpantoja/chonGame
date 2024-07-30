package chon.group.enviroment;

import chon.group.agent.Agent;

import java.util.ArrayList;


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
    private ArrayList<Agent> agents = new ArrayList<Agent>();
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

    public void setImage(String pathImage){
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

        // Simplificacao para capturar o primeiro protagonista
        for (Agent agent : agents){
            if(agent.getIsProtagonist()){
                this.protagonist = agent;
                break;
            }
        }

        // this.protagonist = agents.stream()
        //                           .filter(Agent::getIsProtagonist)
        //                           .findFirst()
        //                           .orElseThrow(() -> new RuntimeException("No protagonist found"));

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
            // printStatusPanel(agent);
        }
        printStatusPanel(this.protagonist);
    }

    public void clearRect(){
        gc.clearRect(0, 0, 1280, 780);
    }

    public void printStatusPanel(Agent agent) {
        //Caso ele esteja sendo controlado, a sua coordenada é exposta na tela 

        gc.setFill(Color.BLACK);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);
        Font theFont = Font.font("Verdana", FontWeight.BOLD, 14);
        gc.setFont(theFont);
        gc.fillText("X: " + agent.getPositionX(), agent.getPositionX() + 10, agent.getPositionY() - 25);
        gc.fillText("Y: " + agent.getPositionY(), agent.getPositionX() + 10, agent.getPositionY() - 10);
        
	}

    // Futuramente essa classe deve receber um tipo Object q verificará tanto se personagens ou qlqr coisa estão dentro dos limites do ambiente
    public Boolean limitsApprove(){

        if(this.protagonist.getPositionX() == 0){
            this.protagonist.setPositionX(1);
        }else if(this.width == (this.protagonist.getPositionX() + this.protagonist.getWidth())){
            this.protagonist.setPositionX(this.protagonist.getPositionX() - 1);
        }else if(this.protagonist.getPositionY() == 0){
            this.protagonist.setPositionY(1);
        }else if(this.height == (this.protagonist.getPositionY() + this.protagonist.getHeight())){
            this.protagonist.setPositionY(this.protagonist.getPositionY() - 1);
        }

        return true;
    }
}
