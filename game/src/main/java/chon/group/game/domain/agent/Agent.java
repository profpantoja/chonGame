package chon.group.game.domain.agent;

import java.util.List;

import chon.group.game.domain.environment.Collision;
import chon.group.game.domain.environment.Environment;
import chon.group.game.messaging.Message;
import javafx.scene.image.Image;

/**
 * Represents an agent in the game, with properties such as position, size,
 * speed, and image.
 * The agent can move in specific directions and chase a target.
 */
public class Agent extends AnimatedEntity {

    /* The time of the last hit taken. */
    private long lastHitTime = 0;
    
    /* Flag to control the invulnerability status of the agent. */
    private boolean invulnerable = false;

    /* Invulnerability (in milliseconds) */
    private final long INVULNERABILITY_COOLDOWN = 3000;
    
    /* The Agent's Weapon */
    private Weapon weapon;

    private boolean isJumping = false;

    /* Velocidade inicial do pulo (negativo = sobe) */
    private int jumpVelocity = -23;

    private int currentVelocityY = 0;

    private final int gravity = 3;

    private final int groundY = 500;

    private boolean isProtagonist;
    
    /* The Agent's Close Weapon */
    private CloseWeapon closeWeapon;

    
    /*Flag to stop the invulnerability status of the agent when on menu */
    private boolean checkMenu = false;
    
    private String pathImageHit;
    
    private String pathImageDeath;
    
    public void setPathImageDeath(String pathImageDeath) {
        this.pathImageDeath = pathImageDeath;
    }
    
    public void setPathImageHit(String pathImageHit) {
        this.pathImageHit = pathImageHit;
    }
    
    public void setCheckMenu(boolean checkMenu){
        this.checkMenu = checkMenu;
    }

    public boolean getCheckMenu(){
        return checkMenu;
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
     * @param isProtagonist the agent's protagonist or not
     */

    public Agent(int posX, int posY, int height, int width, int speed, int health, String pathImage, boolean flipped, boolean isProtagonist) {
        super(new Image(Agent.class.getResource(pathImage).toExternalForm()), posX, posY, height, width, speed, health, pathImage, flipped);
        this.isProtagonist = isProtagonist;
    }

    /**
     * Gets the last hit taken.
     */
    public long getlastHitTime() {
        return lastHitTime;
    }

    /**
     * Sets the last hit taken.
     *
     * @param lastHitTime the new image
     */
    public void setlastHitTime(long lastHitTime) {
        this.lastHitTime = lastHitTime;
    }

    /**
     * Gets invulnerable cooldown time.
     *
     * @return the time is milliseconds
     */
    public long getInvulnerabilityCooldown() {
        return INVULNERABILITY_COOLDOWN;
    }

    /**
     * Gets if the agent is invulnerable.
     *
     * @return if the agent is invulnerable
     */
    public boolean isInvulnerable() {
        return invulnerable;
    }

    /**
     * Sets the agent invulnerable status.
     *
     * @param invulnerable the new invulnerable status
     */
    public void setInvulnerable(boolean invulnerable) {
        this.invulnerable = invulnerable;
    }

    /**
     * Gets the agent's weapon.
     *
     * @return its weapon.
     */
    public Weapon getWeapon() {
        return weapon;
    }

    /**
     * Sets the agent new weapon.
     *
     * @param weapon the new weapon
     */
    public void setWeapon(Weapon weapon) {
        this.weapon = weapon;
    }

    /**
     * Gets the agent's close combat weapon.
     *
     * @return its close combat weapon.
     */
    public CloseWeapon getCloseWeapon() {
        return closeWeapon;
    }

    /**
     * Sets the agent new weapon.
     *
     * @param weapon the new weapon
     */
    public void setCloseWeapon(CloseWeapon closeWeapon) {
        this.closeWeapon = closeWeapon;
    }

    /**
     * Gets if the agent is dead.
     *
     * @return if the agent is dead
     */
    public boolean isDead() {
        return (this.getHealth() <= 0);
    }

    public boolean isProtagonist() {
    return isProtagonist;
}

    public void setProtagonist(boolean isProtagonist) {
    this.isProtagonist = isProtagonist;
}

    /**
     * Makes the agent take damage.
     * If health reaches 0, the game must end.
     *
     * @param damage the amount of damage to be applied
     */
    @Override
    public void takeDamage(int damage, List<Message> messages) {
        this.invulnerable = this.updateInvulnerability();
        if (!this.invulnerable) {
            super.takeDamage(damage, messages); 
            this.lastHitTime = System.currentTimeMillis();
            this.setlastHitTime(System.currentTimeMillis()); 

            if(this.isDead()){
                this.setWidth(64);
                this.setHeight(64);
                this.setAnimation(this.pathImageDeath, 2, 150);
                System.out.println("Chon bota die!");
                //SoundManager.playSound("sounds/gameOver.wav");
            }
            else if(this.pathImageHit != null && !this.pathImageHit.isEmpty()){
                this.setWidth(64); 
                this.setAnimation(this.pathImageHit, 10, 300); 
                System.out.println("Chon bota took damage!");
                //SoundManager.playSound("sounds/takedamage.wav");
            }
        }
    }

    public void checkCollision(Collision collision, Environment environment) {
        if (getPosX() < collision.getX() + collision.getWidth() &&
            getPosX() + getWidth() > collision.getX() &&
            getPosY() < collision.getY() + collision.getHeight() &&
            getPosY() + getHeight() > collision.getY()) {

            // Detecção de colisão
            float dx = (getPosX() + getWidth() / 2.0f) - (collision.getX() + collision.getWidth() / 2.0f);
            float dy = (getPosY() + getHeight() / 2.0f) - (collision.getY() + collision.getHeight() / 2.0f);

            float halfWidths = (getWidth() / 2.0f) + (collision.getWidth() / 2.0f);
            float halfHeights = (getHeight() / 2.0f) + (collision.getHeight() / 2.0f);

            float overlapX = halfWidths - Math.abs(dx);
            float overlapY = halfHeights - Math.abs(dy);

            // 1. Impedir passagem se não for passável
            if (!collision.isPassable()) {
                int fix = (int)Math.ceil(Math.min(overlapX, overlapY));
                if (overlapX < overlapY) {
                    if (dx > 0) {
                        setPosX(getPosX() + fix);
                    } else {
                        setPosX(getPosX() - fix);
                    }
                } else {
                    if (dy > 0) {
                        setPosY(getPosY() + fix);
                    } else {
                        setPosY(getPosY() - fix);
                    }
                }
            }

            // 2. Aplicar dano
            if (collision.getDamage() > 0 && this == environment.getProtagonist()) {
                takeDamage(collision.getDamage(), environment.getMessages());
            }
            else if (collision.getDamage() > 0 && collision.isAgentDamage()) {
                takeDamage(collision.getDamage(), environment.getMessages());
            }

            // 3. Destruir ao contato
            if (collision.isContactDestroy() && this == environment.getProtagonist())
            {
                collision.setDestroy(true);
            }
            else if (collision.isContactDestroy() && collision.isAgentContact()) {
                collision.setDestroy(true);
            }
        }
    }

    /**
     * Method to update the invulnerable status.
     *
     * @return if the agent is still invulnerable
     */
    private boolean updateInvulnerability() {
    if (checkMenu) {
        // Congela o timer de invulnerabilidade enquanto está no menu
        lastHitTime = System.currentTimeMillis();
        return true;
    }
    if (System.currentTimeMillis() - lastHitTime >= INVULNERABILITY_COOLDOWN) {
        return false;
    }
    return true;
}

    /**
     * Método move sobreescrito da classe Entity
     */
    
public void moveGravity(List<String> movements) {
    int speed = this.getSpeed();

    // Movimento lateral
    if (movements.contains("LEFT")) {
        if (!this.isFlipped()) this.flipImage();
        this.setPosX(this.getPosX() - speed);
    }

    if (movements.contains("RIGHT")) {
        if (this.isFlipped()) this.flipImage();
        this.setPosX(this.getPosX() + speed);
    }

    /* PULO: apenas se for protagonista */
    if (isProtagonist) {
        if ((movements.contains("W")) && !isJumping)
 {
            isJumping = true;
            currentVelocityY = jumpVelocity;
        }

        if (isJumping) {
            this.setPosY(this.getPosY() + currentVelocityY);
            currentVelocityY += gravity;

            /* Se está tocando no chão */
            if (this.getPosY() >= groundY) {
                this.setPosY(groundY);
                isJumping = false;
                currentVelocityY = 0;
            }
        } else {
            // Fixa a Chonbota no chão, caso não esteja pulando
            this.setPosY(groundY);
        }
    } else {
        // Chonbot não pula, está sempre no chão
        this.setPosY(groundY);
    }
}
}