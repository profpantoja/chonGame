package chon.group.game.domain.agent;

import java.util.ArrayList;
import java.util.List;

import chon.group.game.domain.environment.Collision;
import chon.group.game.domain.environment.Environment;
import chon.group.game.messaging.Message;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

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

    private final int groundY = 800;

    private boolean isProtagonist;
    
    /* The Agent's Close Weapon */
    private CloseWeapon closeWeapon;

    private AnimationStatus animationStatus = null;
    
    /*Flag to stop the invulnerability status of the agent when on menu */
    private boolean checkMenu = false;
    
    private String pathImageIdle;

    private String pathImageRun;
    
    private String pathImageAttack;

    private String pathImageHit;
    
    private String pathImageDeath;

    private boolean blinking = false;

    private PauseTransition damageStatusTimer;

    private Timeline blinkTimeline;
    
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

    public Agent(int posX, int posY, int height, int width, int speed, int health, String pathImage, boolean flipped, boolean isProtagonist, String imagePathAttack, String imagePathIdle, String imagePathHit, String imagePathDeath, String imagePathRun) {
        super(new Image(Agent.class.getResource(pathImage).toExternalForm()), posX, posY, height, width, speed, health, pathImage, flipped);
        this.isProtagonist = isProtagonist;
        this.pathImageAttack = imagePathAttack;
        this.pathImageIdle = imagePathIdle;
        this.pathImageHit = imagePathHit;
        this.pathImageDeath = imagePathDeath;
        this.pathImageRun = imagePathRun;
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
                if (this.isProtagonist()) {
                    this.setWidth(256);
                    changeAnimation(AnimationStatus.DEAD);
                    System.out.println("Chon bota die!");
                } else {
                    // Enemy death animation (optional)
                    System.out.println("Enemy died!");
                }
            }
            else if(this.pathImageHit != null && !this.pathImageHit.isEmpty()){
                this.setWidth(256); 
                setDamageStatusForDuration(250);
                startBlinking(); 
                System.out.println("Chon bota took damage!");
            }
        }
    }

    public void checkCollision(Collision collision, Environment environment) {
        // Use hitbox if available, otherwise use entity bounds
        int ax, ay, aw, ah;
        if (getHitbox() != null) {
            ax = getPosX() + getHitbox().getOffsetX();
            ay = getPosY() + getHitbox().getOffsetY();
            aw = getHitbox().getWidth();
            ah = getHitbox().getHeight();
        } else {
            ax = getPosX();
            ay = getPosY();
            aw = getWidth();
            ah = getHeight();
        }

        int bx = collision.getX();
        int by = collision.getY();
        int bw = collision.getWidth();
        int bh = collision.getHeight();

        if (ax < bx + bw &&
            ax + aw > bx &&
            ay < by + bh &&
            ay + ah > by) {

            // Detecção de colisão (usando centro do hitbox ou entidade)
            float centerAx = ax + aw / 2.0f;
            float centerAy = ay + ah / 2.0f;
            float centerBx = bx + bw / 2.0f;
            float centerBy = by + bh / 2.0f;

            float dx = centerAx - centerBx;
            float dy = centerAy - centerBy;

            float halfWidths = (aw / 2.0f) + (bw / 2.0f);
            float halfHeights = (ah / 2.0f) + (bh / 2.0f);

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
                updateHitboxPosition(); // Atualiza a posição do hitbox se necessário
            }

            // 2. Aplicar dano
            if (collision.getDamage() > 0 && this == environment.getProtagonist()) {
                takeDamage(collision.getDamage(), environment.getMessages());
            }
            else if (collision.getDamage() > 0 && collision.isAgentDamage()) {
                takeDamage(collision.getDamage(), environment.getMessages());
            }

            // 3. Destruir ao contato
            if (collision.isContactDestroy() && this == environment.getProtagonist()) {
                collision.setDestroy(true);
            }
            else if (collision.isContactDestroy() && collision.isAgentContact()) {
                collision.setDestroy(true);
            }
        }
    }

       private void startBlinking() {
        if (blinkTimeline != null) {
            blinkTimeline.stop();
        }
        blinking = true;

        List<KeyFrame> frames = new ArrayList<>();
        int duration = (int) INVULNERABILITY_COOLDOWN - 100; // total duration in ms
        int interval = 100; // interval in ms

        for (int t = 0; t <= duration; t += interval) {
            final boolean visible = (t / interval) % 2 == 1;
            if (t < duration) {
                frames.add(new KeyFrame(Duration.millis(t), e -> setOpacity(visible ? 1.0 : 0.3)));
            } else {
                // Last frame: ensure visible and stop blinking
                frames.add(new KeyFrame(Duration.millis(t), e -> {
                    setOpacity(1.0);
                    blinking = false;
                }));
            }
        }

        blinkTimeline = new Timeline(frames.toArray(new KeyFrame[0]));
        blinkTimeline.setCycleCount(1);
        Platform.runLater(() -> blinkTimeline.play());
    }

    public void setDamageStatusForDuration(long millis) {
        changeAnimation(AnimationStatus.DAMAGE);
        if (damageStatusTimer != null) {
            damageStatusTimer.stop();
        }
        damageStatusTimer = new PauseTransition(Duration.millis(millis));
        damageStatusTimer.setOnFinished(e -> {
            System.out.println("A");
            changeAnimation(AnimationStatus.IDLE);
        });
        damageStatusTimer.play();
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
            if ((movements.contains("UP")) && !isJumping)
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

    @Override
     /**
     * Moves the entity based on the movement commands provided.
     *
     * @param movements a list of movement directions ("RIGHT", "LEFT", "UP", "DOWN")
     *
     */

    public void move(List<String> movements) {
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

    public void changeAnimation(AnimationStatus newStatus) {
        AnimationStatus oldStatus = getAnimationStatus();
        if (oldStatus == newStatus) return;
        setAnimationStatus(newStatus);
        AnimationStatus currentStatus = getAnimationStatus();

        if (currentStatus == AnimationStatus.IDLE) {
            setAnimation(getPathImageIdle(), 4, 150);
        }
        if (currentStatus == AnimationStatus.DAMAGE) {
            setAnimation(getPathImageHit(), 10, 1000);
        }
        if (currentStatus == AnimationStatus.RUN) {
            setAnimation(getPathImageRun(), 6, 75);
        }  
        if (currentStatus == AnimationStatus.DEAD) {
            setAnimation(getPathImageDeath(), 2, 150);
        } 
        if (currentStatus == AnimationStatus.ATTACK) {
            setAnimation(getPathImageAttack(), 4, 75);
        }
    }

    public AnimationStatus getAnimationStatus() {
        return animationStatus;
    }

    public void setAnimationStatus(AnimationStatus animationStatus) {
        this.animationStatus = animationStatus;
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
}


/*  private void startBlinking() {
        if (blinkTimeline != null) {
            blinkTimeline.stop();
        }
        blinking = true;
        blinkTimeline = new Timeline(
            new KeyFrame(Duration.millis(0), e -> setOpacity(0.3)),
            new KeyFrame(Duration.millis(100), e -> setOpacity(1.0)),
            new KeyFrame(Duration.millis(200), e -> setOpacity(0.3)),
            new KeyFrame(Duration.millis(300), e -> setOpacity(1.0)),
            new KeyFrame(Duration.millis(400), e -> setOpacity(0.3)),
            new KeyFrame(Duration.millis(500), e -> {
                setOpacity(1.0);
                blinking = false;
            })
        );
        blinkTimeline.setCycleCount(1);
        Platform.runLater(() -> blinkTimeline.play());
    }
 */