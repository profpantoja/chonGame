package chon.group.game.core.agent;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import chon.group.game.messaging.Message;
import javafx.scene.image.Image;

public abstract class Entity {
    protected int posX;
    protected int posY;
    protected int height;
    protected int width;
    protected int speed;
    private boolean flipped = false;
    private int health;
    private int fullHealth;
    
    protected Map<AgentState, Animation> animations;
    protected AgentState currentState;
    protected Animation currentAnimation;

    protected int hitboxWidth;
    protected int hitboxHeight;
    protected int hitboxOffsetX;
    protected int hitboxOffsetY;

    public Entity(int posX, int posY, int height, int width, int speed, int health, String pathImage, boolean flipped, boolean visibleBars) {
        this.posX = posX; this.posY = posY; this.height = height; this.width = width; this.speed = speed; this.health = health; this.fullHealth = health; this.flipped = flipped;
        
        double hitboxScale = 0.7;
        this.hitboxWidth = (int)(width * hitboxScale);
        this.hitboxHeight = (int)(height * hitboxScale);
        this.hitboxOffsetX = (width - this.hitboxWidth) / 2;
        this.hitboxOffsetY = (height - this.hitboxHeight) / 2;

        this.animations = new HashMap<>();
        if (pathImage != null && !pathImage.isEmpty()) {
            List<Image> singleFrame = new ArrayList<>();
            singleFrame.add(loadImage(pathImage));
            Animation staticAnim = new Animation(singleFrame, 1.0, true);
            this.animations.put(AgentState.IDLE, staticAnim);
            setCurrentState(AgentState.IDLE);
        }
    }
    
    public boolean intersects(Entity other) {
        if (other == null) return false;
        int thisHitboxX = this.posX + this.hitboxOffsetX;
        int thisHitboxY = this.posY + this.hitboxOffsetY;
        int otherHitboxX = other.posX + other.hitboxOffsetX;
        int otherHitboxY = other.posY + other.hitboxOffsetY;
        
        return thisHitboxX < otherHitboxX + other.hitboxWidth &&
               thisHitboxX + this.hitboxWidth > otherHitboxX &&
               thisHitboxY < otherHitboxY + other.hitboxHeight &&
               thisHitboxY + this.hitboxHeight > otherHitboxY;
    }
    
    public void chase(int targetX, int targetY) {
        List<String> movements = new ArrayList<>();
        
        int stoppingDistance = 5;

        int distanceX = Math.abs(targetX - this.getPosX());

        if (distanceX > stoppingDistance) {
            if (targetX > this.posX) {
                movements.add("RIGHT");
            } else if (targetX < this.posX) {
                movements.add("LEFT");
            }
        }
        if (targetY > this.posY) {
            movements.add("DOWN");
        } else if (targetY < this.posY) {
            movements.add("UP");
        }
        
        this.move(movements);
    }
    
    public Image getImage() { if (currentAnimation == null) return null; return currentAnimation.getCurrentFrame(); }
    protected void setCurrentState(AgentState state) { if (this.currentState != state && animations.containsKey(state)) { this.currentState = state; this.currentAnimation = animations.get(state); this.currentAnimation.reset(); } }
    public void updateAnimation(double deltaTime) { if (currentAnimation != null) { currentAnimation.update(deltaTime); } }
    protected Image loadImage(String path) { if (path == null || path.isEmpty()) return null; try { URL imageUrl = getClass().getResource(path); if (imageUrl == null) { System.err.println("[ERRO DE RECURSO] Imagem não encontrada: " + path); return null; } return new Image(imageUrl.toExternalForm()); } catch (Exception e) { System.err.println("[ERRO CRÍTICO] Falha ao carregar imagem: " + path); e.printStackTrace(); return null; } }
    public void flipImage() { this.flipped = !this.flipped; }
    public void move(List<String> movements) { if (movements.contains("RIGHT") && isFlipped()) { flipImage(); } else if (movements.contains("LEFT") && !isFlipped()) { flipImage(); } if (movements.contains("RIGHT")) { posX += speed; } else if (movements.contains("LEFT")) { posX -= speed; } if (movements.contains("UP")) { posY -= speed; } else if (movements.contains("DOWN")) { posY += speed; } }
    public int getX() { return posX; }
    public int getY() { return posY; }
    public int getPosX() { return posX; }
    public void setPosX(int posX) { this.posX = posX; }
    public int getPosY() { return posY; }
    public void setPosY(int posY) { this.posY = posY; }
    public int getHeight() { return height; }
    public void setHeight(int height) { this.height = height; }
    public int getWidth() { return width; }
    public void setWidth(int width) { this.width = width; }
    public int getSpeed() { return speed; }
    public void setSpeed(int speed) { this.speed = speed; }
    public boolean isFlipped() { return flipped; }
    public void setFlipped(boolean flipped) { this.flipped = flipped; }
    public int getHealth() { return health; }
    public void setHealth(int health) { this.health = health; }
    public int getFullHealth() { return fullHealth; }
    public void setFullHealth(int fullHealth) { this.fullHealth = fullHealth; }
    public void takeDamage(int damage, List<Message> messages) { if (this.getHealth() > 0) { this.setHealth(this.getHealth() - damage); messages.add(new Message(String.valueOf(damage), this.getPosX(), this.getPosY(), 25)); if (this.getHealth() < 0) this.setHealth(0); } }
}