package chon.group.game.core.agent;

import java.util.List;

import chon.group.game.core.weapon.CloseWeapon;
import chon.group.game.core.animation.AnimationSpritesheet;
import chon.group.game.core.animation.AnimationStatus;
import chon.group.game.core.weapon.Shot;
import chon.group.game.core.weapon.Slash;
import chon.group.game.core.weapon.Weapon;
import chon.group.game.messaging.Message;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

/**
 * Represents an agent in the game, with properties such as position, size,
 * speed, and image.
 * The agent can move in specific directions and chase a target.
 */
public class Agent extends Entity {

    /** The time of the last hit taken. */
    private long lastHitTime = 0;

    /** Flag to control the invulnerability status of the agent. */
    private boolean invulnerable = false;

    /* Invulnerability (in milliseconds) */
    private final long INVULNERABILITY_COOLDOWN = 1000;

    /** The Agent's Weapon */
    private Weapon weapon;

    
    /* The Agent's Close Weapon */
    private CloseWeapon closeWeapon;

    /** The initial agent's energy */
    private double energy;

    /** The maximum agent's energy. */
    private final double fullEnergy;

    /** The agent's energy recovery factor */
    private final double recoveryFactor;
    /** The time of the last shot fired. */
    private long lastShotTime = 0;
    /** The cooldown time between shots in milliseconds. */
    private long shotCooldown = 5000; // 5 segundos em milissegundos
    /** The agent's energy cost for using the weapon. */
    private boolean isEnemy;

    private String pathImageIdle;

    private String pathImageRun;
    
    private String pathImageAttack;

    private String pathImageHit;
    
    private String pathImageDeath;

    private String pathImageJump;

    public void setPathImageDeath(String pathImageDeath) {
        this.pathImageDeath = pathImageDeath;
    }
    
    public void setPathImageHit(String pathImageHit) {
        this.pathImageHit = pathImageHit;
    }
    

    private boolean isJumping = false;

    private boolean canJump = true;

    private boolean isGrounded = false;

    private boolean gravityEffects = true;

    /* Velocidade inicial do pulo (negativo = sobe) */
    private int jumpVelocity = -20;

    private int currentVelocityY = 0;

    private final double gravity = 1;

    private final int groundY = 550;

    private boolean isProtagonist;

    private final int maxFallSpeed = 30;

    
    public boolean isProtagonist() {
        return isProtagonist;
    }

    public void setProtagonist(boolean isProtagonist) {
        this.isProtagonist = isProtagonist;
    }

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

    public Agent(int posX, int posY, int height, int width, int speed, int health, String pathImage, boolean flipped, boolean visibleBars) {
        super(posX, posY, height, width, speed, health, pathImage, flipped, visibleBars);
        this.energy = 1.0;
        this.fullEnergy = 1.0;
        this.recoveryFactor = 0.0002;
    }

    
    @Override
    public Image getImage() {
        Image baseImage;
        if (getAnimationSystem() != null) {
            baseImage = getAnimationSystem().getCurrentFrameImage();
        } else {
            baseImage = image;
        }
        if (isFlipped() && baseImage != null && getAnimationSystem() != null) {
            ImageView view = new ImageView(baseImage);
            view.setScaleX(-1);
            view.setFitWidth(getWidth());
            view.setFitHeight(getHeight());
            SnapshotParameters params = new SnapshotParameters();
            params.setFill(Color.TRANSPARENT);
            return view.snapshot(params, null);
        }
        return baseImage;
    }

    public void syncDimensions() {
        if (getAnimationSystem() != null) {
            AnimationSpritesheet sheet = getAnimationSystem().getGraphics().getSpritesheet(getAnimationSystem().getCurrentStatus());
            if (sheet != null) {
                setWidth(sheet.getFrameWidth());
                setHeight(sheet.getFrameHeight());
            }
        }
    }
  

    /**
     * Gets the last hit taken.
     */
    public long getLastHitTime() {
        return lastHitTime;
    }

    /**
     * Sets the last hit taken.
     *
     * @param lastHitTime the new image
     */
    public void setLastHitTime(long lastHitTime) {
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
     * Gets the current energy level (0.0 to 1.0).
     *
     * @return current energy level
     */
    public double getEnergy() {
        return energy;
    }

    public void setEnergy(double energy) {
        this.energy = energy;
    }

    /**
     * Gets the maximum energy capacity.
     *
     * @return maximum energy
     */
    public double getFullEnergy() {
        return fullEnergy;
    }

    public double getRecoveryFactor() {
        return recoveryFactor;
    }

    public long getLastShotTime() {
        return lastShotTime;
    }
    public void setLastShotTime(long lastShotTime) {
        this.lastShotTime = lastShotTime;
    }

    public long getShotCooldown() {
        return shotCooldown;
    }

    public void setShotCooldown(long shotCooldown) {
        this.shotCooldown = shotCooldown;
    }

    /**
     * Consumes a specified amount of energy.
     *
     * @param amount the amount of energy to consume
     */
    public void consumeEnergy(double amount) {
        this.energy = Math.max(0, this.energy - amount);
    }

    /**
     * Recovers a specified amount of energy.
     *
     * @param amount the amount of energy to recover
     */
    public void recoverEnergy() {
        this.energy = Math.min(this.fullEnergy, (this.energy + this.recoveryFactor));
    }

    /**
     * Checks if energy is empty.
     *
     * @return true if energy is 0 or less
     */
    public boolean isEnergyEmpty() {
        return this.energy <= 0;
    }

    /**
     * Gets if the agent is dead.
     *
     * @return if the agent is dead
     */
    public boolean isDead() {
        return (this.getHealth() <= 0);
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
            // SoundManager.playSound("sounds/hurt.wav"); não há som de quando o agente toma dano
        }
    }

    /**
     * Method to update the invulnerable status.
     *
     * @return if the agent is still invulnerable
     */
    private boolean updateInvulnerability() {
        if (System.currentTimeMillis() - lastHitTime >= INVULNERABILITY_COOLDOWN) {
            return false;
        }
        return true;
    }

    public Shot useWeapon() {
        if (this.getWeapon() == null) return null; 

        String direction = this.isFlipped() ? "LEFT" : "RIGHT";
        if (this.energy >= this.getWeapon().getEnergyCost()) {
            this.consumeEnergy(this.getWeapon().getEnergyCost());
            return this.weapon.fire(this.getPosX(), this.getPosY(), direction, this);
        } else
            return null;
    }

    public Slash useCloseWeapon() {
        if (this.getCloseWeapon() == null) return null;

        String direction = this.isFlipped() ? "LEFT" : "RIGHT";
        if (this.energy >= this.getCloseWeapon().getEnergyCost()) {
            this.consumeEnergy(this.getCloseWeapon().getEnergyCost());
            return this.closeWeapon.slash(this.getPosX(), this.getPosY(), direction, this);
        } else
            return null;
    }

    public boolean isEnemy() {
        return isEnemy;
    }
    
    public void setEnemy(boolean isEnemy) {
        this.isEnemy = isEnemy;
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
    if (movements.contains("UP") && canJump && isGrounded) {
        changeAnimation(AnimationStatus.JUMPING); 
        isJumping = true;
        isGrounded = false;
        canJump = false;
        currentVelocityY = jumpVelocity;
    }

            if (movements.contains("LEFT") || movements.contains("RIGHT")) {
    changeAnimation(AnimationStatus.RUNNING);
} else if (isGrounded && !isJumping) {
    changeAnimation(AnimationStatus.IDLE);
}
        }
        //}
    }

private void changeAnimation(AnimationStatus status) {
    if (getAnimationSystem() != null) {
        getAnimationSystem().setStatus(status);
        syncDimensions(); // Atualiza largura/altura do agente com base no spritesheet atual
    }
}


public void gravityEffect() {
    if (posY < groundY || currentVelocityY < 0) {
        posY += currentVelocityY;
        currentVelocityY += gravity;

        isJumping = true;
        isGrounded = false;

        if (currentVelocityY > maxFallSpeed) {
            currentVelocityY = maxFallSpeed;
        }

        // Evita ultrapassar o chão
        if (posY >= groundY) {
            posY = groundY;
            currentVelocityY = 0;
            isJumping = false;
            isGrounded = true;
            canJump = true;

            if (getAnimationStatus() == AnimationStatus.JUMPING) {
                changeAnimation(AnimationStatus.IDLE);
            }
        }
    }
}


     /**
     * Moves the entity based on the movement commands provided.
     *
     * @param movements a list of movement directions ("RIGHT", "LEFT", "UP", "DOWN")
     *
     */

    public void moveNonGravity(List<String> movements) {
        if (movements.contains("RIGHT")) {
            if (isFlipped()) {
                flipImage();
                setFlipped(false);
            }
            setPosX(getPosX() + getSpeed());
            updateHitboxPosition();
        } else if (movements.contains("LEFT")) {
            if (!isFlipped()) {
                flipImage();
                setFlipped(true);
            }
            setPosX(getPosX() - getSpeed());
            updateHitboxPosition();
        } else if (movements.contains("UP")) {
            setPosY(getPosY() - getSpeed());
            updateHitboxPosition();
        } else if (movements.contains("DOWN")) {
            setPosY(getPosY() + getSpeed());
            updateHitboxPosition();
        }
    }

    @Override
    public void move(List<String> gameInput) {
AnimationStatus status = getAnimationStatus();
if (status == AnimationStatus.HIT || status == AnimationStatus.ATTACKING) return;        if (this.isGravityEffects()) {
            moveGravity(gameInput);
            if (this.isProtagonist) gravityEffect();
        }
        else {
            moveNonGravity(gameInput);
        }
    }

    public String getPathImageIdle() {
        return pathImageIdle;
    }

    public void setPathImageIdle(String pathImageIdle) {
        this.pathImageIdle = pathImageIdle;
    }

    public String getPathImageRun() {
        return pathImageRun;
    }

    public void setPathImageRun(String pathImageRun) {
        this.pathImageRun = pathImageRun;
    }

    public String getPathImageAttack() {
        return pathImageAttack;
    }

    public void setPathImageAttack(String pathImageAttack) {
        this.pathImageAttack = pathImageAttack;
    }

    public String getPathImageHit() {
        return pathImageHit;
    }

    public String getPathImageDeath() {
        return pathImageDeath;
    }

    public boolean isIsJumping() {
        return isJumping;
    }

    public void setIsJumping(boolean isJumping) {
        this.isJumping = isJumping;
    }

    public String getPathImageJump() {
        return pathImageJump;
    }

    public void setPathImageJump(String pathImageJump) {
        this.pathImageJump = pathImageJump;
    }

    public boolean isIsGrounded() {
        return isGrounded;
    }

    public void setIsGrounded(boolean isGrounded) {
        this.isGrounded = isGrounded;
    }

    public boolean isGravityEffects() {
        return gravityEffects;
    }

    public void setGravityEffects(boolean gravityEffects) {
        this.gravityEffects = gravityEffects;
    }

    public int getJumpVelocity() {
        return jumpVelocity;
    }

    public void setJumpVelocity(int jumpVelocity) {
        this.jumpVelocity = jumpVelocity;
    }


}
