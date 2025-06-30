package chon.group.game.domain.agent;

import java.util.List;

import chon.group.game.messaging.Message;
import javafx.animation.KeyFrame;
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

    private String pathImageHit;

    private String pathImageDeath;

    private boolean blinking = false;

    private Timeline blinkTimeline;

    public void setPathImageDeath(String pathImageDeath) {
        this.pathImageDeath = pathImageDeath;
    }

    public void setPathImageHit(String pathImageHit) {
        this.pathImageHit = pathImageHit;
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
    public Agent(int posX, int posY, int height, int width, int speed, int health, String pathImage) {
        super(new Image(Agent.class.getResource(pathImage).toExternalForm()), posX, posY, height, width, speed, health, pathImage);
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
        super(new Image(Agent.class.getResource(pathImage).toExternalForm()), posX, posY, height, width, speed, health, pathImage, flipped);
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
            this.setlastHitTime(System.currentTimeMillis()); 

            if(this.isDead()){
                this.setWidth(64);
                this.setHeight(64);
                this.setAnimation(this.pathImageDeath, 2, 150);
                System.out.println("Chon bota die!");
            }
            else if(this.pathImageHit != null && !this.pathImageHit.isEmpty()){
                this.setWidth(64); 
                this.setAnimation(this.pathImageHit, 10, 300);
                startBlinking(); 
                System.out.println("Chon bota took damage!");
            }
        }
    }

       private void startBlinking() {
        if (blinkTimeline != null) {
            blinkTimeline.stop();
        }
        blinking = true;
        blinkTimeline = new Timeline(
            new KeyFrame(Duration.millis(0), new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    setOpacity(0.3);
                }
            }),
            new KeyFrame(Duration.millis(100), new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    setOpacity(1.0);
                }
            }),
            new KeyFrame(Duration.millis(200), new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    setOpacity(0.3);
                }
            }),
            new KeyFrame(Duration.millis(300), new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    setOpacity(1.0);
                }
            }),
            new KeyFrame(Duration.millis(400), new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    setOpacity(0.3);
                }
            }),
            new KeyFrame(Duration.millis(500), new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    setOpacity(1.0);
                    blinking = false;
                }
            })
        );
        blinkTimeline.setCycleCount(1);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                blinkTimeline.play();
            }
        });
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