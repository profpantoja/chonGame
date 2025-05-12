package chon.group.game.domain.collectibles;
 
import chon.group.game.domain.agent.Agent;
import javafx.scene.image.Image;
 
public class Coin {
    private double posX;
    private double posY;
    private Image image;
    private boolean collected = false;
 
    public Coin(int posX, int posY, String imagePath) {
        this.posX = posX;
        this.posY = posY;
        this.image = new Image(getClass().getResource(imagePath).toExternalForm());
    }
    
 
    public double getPosX() {
    return this.posX;
}
 
public double getPosY() {
    return this.posY;
}
 
public void setPosX(double x) {
    this.posX = x;
}
 
public void setPosY(double y) {
    this.posY = y;
}
 
    public Image getImage() {
        return image;
    }
    public boolean isCollected() {
         return collected;
        }
    public void setCollected(boolean collected) { this.collected = collected; }
 
 
public void followAgentIfClose(Agent agent, double radius) {
    if (isCollected()) return;
 
    double dx = agent.getPosX() - this.getPosX();
    double dy = agent.getPosY() - this.getPosY();
    double distance = Math.sqrt(dx * dx + dy * dy);
 
    if (distance < radius) {
        
        double directionX = dx / distance;
        double directionY = dy / distance;
 
        
        double speed = 10.0;
 
        
        this.setPosX(this.getPosX() + directionX * speed);
        this.setPosY(this.getPosY() + directionY * speed);
        
        System.out.println("Moeda seguindo o Harry Potter!");
    }
}
 
}