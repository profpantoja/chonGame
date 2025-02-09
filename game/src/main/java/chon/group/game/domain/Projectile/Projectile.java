package chon.group.game.domain.Projectile;

import chon.group.game.domain.agent.Agent;
import javafx.scene.image.Image;

public class Projectile {
    private int x, y;
    private int speed;
    private boolean active;
    private int damage;
    private String direction;
    /** Image representing the agent. */
    private Image image;

    public Projectile(int x, int y, int speed, int damage, String direction, String pathImage) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.damage = damage;
        this.active = true;
        this.direction = direction;
        this.image = new Image(getClass().getResource(pathImage).toExternalForm());
    }

    public void move() {
        if (direction.equals("RIGHT")) {
            x += speed; // Move para a direita
        } else if (direction.equals("LEFT")) {
            x -= speed; // Move para a esquerda
        }
    }

    public boolean checkCollision(Agent enemy) {
    // Pegando as coordenadas e dimensões do projétil
    double projX = this.getX();
    double projY = this.getY();
    double projWidth = 80;
    double projHeight = 60;
                            
    // Pegando as coordenadas e dimensões do zumbi (inimigo)
    double enemyX = enemy.getPosX();
    double enemyY = enemy.getPosY();
    double enemyWidth = enemy.getWidth();
    double enemyHeight = enemy.getHeight();                        
                            
    // Verifica se o projétil está dentro da área total do zumbi
    boolean isColliding = 
    projX < enemyX + enemyWidth &&  // Verifica se a bala não está à direita do zumbi
    projX + projWidth > enemyX &&   // Verifica se a bala não está à esquerda do zumbi
    projY < enemyY + enemyHeight && // Verifica se a bala não está abaixo do zumbi
    projY + projHeight > enemyY;    // Verifica se a bala não está acima do zumbi
                            
    return isColliding;
    }
                        
        
            /**
     * Gets the agent image.
     *
     * @return the agent image
     */
    public Image getImage() {
        return image;
    }

  

    /**
     * Gets the agent flipped status.
     *
     * @param image the new image
     */
    public void setImage(Image image) {
        this.image = image;
    }

    public int getDamage() {
        return damage;
    }

    public boolean isActive() {
        return active;
    }

    public void deactivate() {
        this.active = false;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }
    
}