package chon.group.game.domain.agent;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.image.Image;

public class Agent {

    private int posX;
    private int posY; 
    private int height;  
    private int width;
    private int speed;  
    private Image image;  
    private int health;  
    private int fullHealth;
    private long lastAttackTime = 0;  // The time of the last attack (for cooldown)
    private static final long ATTACK_COOLDOWN = 1000;  // Cooldown duration for attacks (in milliseconds)
    private boolean invulnerable;  // Flag to control the invulnerability status of the agent

    // Constructor to initialize the agent's properties
    public Agent(int posX, int posY, int height, int width, int speed, String pathImage, int health) {
        this.posX = posX;
        this.posY = posY;
        this.height = height;
        this.width = width;
        this.speed = speed;
        this.health = health;
        this.fullHealth = health;
        this.image = new Image(getClass().getResource(pathImage).toExternalForm());  // Load the agent's image
        this.lastAttackTime = 0;  // Initial state with no previous attack
        this.invulnerable = false;  // Initially, the agent is not invulnerable
    }

    // Getters and setters

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

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getFullHealth() {
        return fullHealth;
    }

    public void setFullHealth(int fullHealth) {
        this.fullHealth = fullHealth;
    }

    public boolean isInvulnerable() {
        return invulnerable;
    }

    public void setInvulnerable(boolean invulnerable) {
        this.invulnerable = invulnerable;
    }

    // Method to move the agent based on a list of movement commands
    public void move(List<String> movements) {
        if (movements.contains("RIGHT")) {
            setPosX(posX += speed); 
        } else if (movements.contains("LEFT")) {
            setPosX(posX -= speed);  
        } else if (movements.contains("UP")) {
            setPosY(posY -= speed);  
        } else if (movements.contains("DOWN")) {
            setPosY(posY += speed);  
        }
    }

    // Method for the agent to chase a target (another agent)
    public void chase(int targetX, int targetY) {
        if (targetX > this.posX) {
            this.move(new ArrayList<>(List.of("RIGHT")));  
        } else if (targetX < this.posX) {
            this.move(new ArrayList<>(List.of("LEFT")));  
        }
        if (targetY > this.posY) {
            this.move(new ArrayList<>(List.of("DOWN")));  
        } else if (targetY < this.posY) {
            this.move(new ArrayList<>(List.of("UP")));
        }
    }

    // Method to check if the agent is dead (health <= 0)
    public boolean isDead() {
        return health <= 0;
    }

    // Method to make the agent take damage, if not invulnerable
    public void takeDamage() {
        if (!invulnerable && health > 0) {
            health--;  // Decrease health
            activateInvulnerability(ATTACK_COOLDOWN);  // Activate invulnerability for a cooldown period
        }
    }

    // Method to check if the agent can attack (based on cooldown)
    public boolean canAttack() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastAttackTime >= ATTACK_COOLDOWN) {
            lastAttackTime = currentTime;
            return true;
        }
        return false;
    }

    // Method for the agent to attack another agent (target)
    public void attack(Agent target) {
        if (canAttack()) {
            target.takeDamage();
            System.out.println("Attack successful! Target's remaining health: " + target.getHealth());
        } else {
            System.out.println("Still in cooldown!");
        }
    }
    
    // Method to activate invulnerability for a given duration (in milliseconds)
    public void activateInvulnerability(long duration) {
        setInvulnerable(true); 
        new Thread(() -> {
            try {
                Thread.sleep(duration);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            setInvulnerable(false); 
        }).start();
    }
}
