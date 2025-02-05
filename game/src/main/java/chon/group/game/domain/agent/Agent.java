package chon.group.game.domain.agent;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.SnapshotParameters;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

/**
 * Represents an agent in the game, with properties such as position, size,
 * speed, and image.
 * The agent can move in specific directions and chase a target.
 */
public class Agent {

    private int posX;
    private int posY;
    private int height;
    private int width;
    private int speed;
    private Image image;
    private boolean flipped = false;
    private int health;
    private int fullHealth;
    private boolean gameOver = false;  // New field to track the game state
    private long lastAttackTime = 0;  // The time of the last attack (for cooldown)
    private static final long ATTACK_COOLDOWN = 2000;  // Cooldown duration for attacks (in milliseconds)
    private boolean invulnerable;  // Flag to control the invulnerability status of the agent

    /**
     * Constructor to initialize the agent properties.
     *
     * @param posX      the agent's initial X (horizontal) position
     * @param posY      the agent's initial Y (vertical) position
     * @param height    the agent's height
     * @param width     the agent's width
     * @param speed     the agent's speed
     * @param health    the agent's health
     * @param pathImage the path to the agent's image
     */
    public Agent(int posX, int posY, int height, int width, int speed, int health, String pathImage) {
        this.posX = posX;
        this.posY = posY;
        this.height = height;
        this.width = width;
        this.speed = speed;
        this.health = health;
        this.fullHealth = health;
        this.image = new Image(getClass().getResource(pathImage).toExternalForm());
        this.lastAttackTime = 0;  // Initial state with no previous attack
        this.invulnerable = false;  // Initially, the agent is not invulnerable
    }

    /**
     * Constructor to initialize the agent properties including its direction.
     *
     * @param posX      the agent's initial X (horizontal) position
     * @param posY      the agent's initial Y (vertical) position
     * @param height    the agent's height
     * @param width     the agent's width
     * @param speed     the agent's speed
     * @param health    the agent's health
     * @param pathImage the path to the agent's image
     * @param flipped   the agent's direction (RIGHT=0 or LEFT=1)
     */
    public Agent(int posX, int posY, int height, int width, int speed, int health, String pathImage, boolean flipped) {
        this.posX = posX;
        this.posY = posY;
        this.height = height;
        this.width = width;
        this.speed = speed;
        this.health = health;
        this.fullHealth = health;
        this.image = new Image(getClass().getResource(pathImage).toExternalForm());
        this.flipped = flipped;
    }

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

    public boolean isFlipped() {
        return flipped;
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

    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public long getLastAttackTime() {
        return lastAttackTime;
    }

    public void setLastAttackTime(long lastAttackTime) {
        this.lastAttackTime = lastAttackTime;
    }

    public static long getAttackCooldown() {
        return ATTACK_COOLDOWN;
    }

    public boolean isInvulnerable() {
        return invulnerable;
    }

    public void setInvulnerable(boolean invulnerable) {
        this.invulnerable = invulnerable;
    }

    /**
     * Checks if the agent is alive.
     *
     * @return true if the agent's health is greater than 0, false otherwise.
     */
    public boolean isAlive() {
        return health > 0;
    }

    /**
     * Sets the agent flipped status.
     *
     * @param flipped the new flipped status
     */
    public void setFlipped(boolean flipped) {
        this.flipped = flipped;
    }

    /**
     * Flips the Image horizontally.
     */
    private void flipImage() {
        ImageView flippedImage = new ImageView(image);
        flippedImage.setScaleX(-1);
        SnapshotParameters params = new SnapshotParameters();
        params.setFill(Color.TRANSPARENT);
        this.flipped = !this.flipped;
        this.image = flippedImage.snapshot(params, null);
    }

    /**
     * Moves the agent based on the movement commands provided.
     *
     * @param movements a list of movement directions ("RIGHT", "LEFT", "UP",
     *                  "DOWN")
     */
    public void move(List<String> movements) {
        if (gameOver) {
            System.out.println("Game Over! Agent cannot move.");
            return; // Prevent movement if the game is over
        }

        if (movements.contains("RIGHT")) {
            if (flipped)
                this.flipImage();
            setPosX(posX += speed);
        } else if (movements.contains("LEFT")) {
            if (!flipped)
                this.flipImage();
            setPosX(posX -= speed);
        } else if (movements.contains("UP")) {
            setPosY(posY -= speed);
        } else if (movements.contains("DOWN")) {
            setPosY(posY += speed);
        }
    }

    /**
     * Makes the agent chase a target based on its coordinates.
     *
     * @param targetX the target's X (horizontal) position
     * @param targetY the target's Y (vertical) position
     */
    public void chase(int targetX, int targetY) {
        if (gameOver) {
            System.out.println("Game Over! Agent cannot chase.");
            return; // Prevent chasing if the game is over
        }

        if (targetX > this.posX) {
            this.move(new ArrayList<String>(List.of("RIGHT")));
        } else if (targetX < this.posX) {
            this.move(new ArrayList<String>(List.of("LEFT")));
        }
        if (targetY > this.posY) {
            this.move(new ArrayList<String>(List.of("DOWN")));
        } else if (targetY < this.posY) {
            this.move(new ArrayList<String>(List.of("UP")));
        }
    }

    /**
     * Makes the agent take damage.
     * If health reaches 0, the game ends.
     *
     * @param damage the amount of damage to be applied
     */
    // Method to make the agent take damage, if not invulnerable
    public void takeDamage() {
        if (!invulnerable && health > 0) {
            health -= 20;  // Decrease health
            if (health <= 0) {
                setGameOver(true);  // Trigger game over when health reaches zero
            }
            activateInvulnerability(ATTACK_COOLDOWN);  // Activate invulnerability for a cooldown period
        }
    }

    // Method to check if the agent is dead (health <= 0)
    public boolean isDead() {
        return health <= 0;
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