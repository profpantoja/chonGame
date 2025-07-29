package chon.group.game.core.agent;

import java.util.ArrayList;
import java.util.List;
import chon.group.game.core.weapon.ExplosionEffect;
import chon.group.game.core.weapon.Shot;
import chon.group.game.core.weapon.Weapon;
import chon.group.game.messaging.Message;
import javafx.scene.image.Image;

public class Agent extends Entity {

    private long lastHitTime = 0;
    private final long INVULNERABILITY_COOLDOWN = 1500; // 1.5 segundos de invulnerabilidade

    private Weapon weapon;
    private double energy;
    private final double fullEnergy;
    private final double baseRecoveryFactor;
    private final double idleRecoveryFactor;
    private int collisionDamage;
    private long lastMoveTime = 0;

    // Construtor para o Mago (que carrega suas próprias animações)
    public Agent(int posX, int posY, int height, int width, int speed, int health, boolean flipped, int collisionDamage) {
        super(posX, posY, height, width, speed, health, null, flipped, false);
        this.energy = 1.0;
        this.fullEnergy = 1.0;
        this.collisionDamage = collisionDamage;
        this.baseRecoveryFactor = 0.0009;
        this.idleRecoveryFactor = 0.0030;
        
        if (this.collisionDamage == 0) { // Identifica o Mago pelo dano de colisão 0
            loadMageAnimations();
        }
    }
    
    // Construtor para Inimigos (que usam uma imagem estática)
    public Agent(int posX, int posY, int height, int width, int speed, int health, String pathImage, boolean flipped, boolean visibleBars, int collisionDamage) {
        super(posX, posY, height, width, speed, health, pathImage, flipped, visibleBars);
        this.energy = 1.0;
        this.fullEnergy = 1.0;
        this.collisionDamage = collisionDamage;
        this.baseRecoveryFactor = 0.0009;
        this.idleRecoveryFactor = 0.0030;
    }

    private void loadMageAnimations() {
        List<Image> idleFrames = new ArrayList<>();
        for (int i = 1; i <= 18; i++) { idleFrames.add(loadImage("/images/agents/Mage/Idle/MageIdle" + i + ".png")); }
        animations.put(AgentState.IDLE, new Animation(idleFrames, 0.1, true));

        List<Image> walkFrames = new ArrayList<>();
        for (int i = 1; i <= 15; i++) { walkFrames.add(loadImage("/images/agents/Mage/Walk/MageWalk" + i + ".png")); }
        animations.put(AgentState.WALK, new Animation(walkFrames, 0.05, true));
        
        List<Image> attackFrames = new ArrayList<>();
        for (int i = 1; i <= 12; i++) { attackFrames.add(loadImage("/images/agents/Mage/Attack/MageAttack" + i + ".png")); }
        animations.put(AgentState.ATTACK, new Animation(attackFrames, 0.03, false));

        List<Image> damageFrames = new ArrayList<>();
        for (int i = 1; i <= 3; i++) { damageFrames.add(loadImage("/images/agents/Mage/TakeDamage/MageTakeDamage" + i + ".png")); }
        animations.put(AgentState.TAKE_DAMAGE, new Animation(damageFrames, 0.1, false));
        
        List<Image> dyingFrames = new ArrayList<>();
        for (int i = 1; i <= 12; i++) { dyingFrames.add(loadImage("/images/agents/Mage/Dying/MageDying" + i + ".png")); }
        animations.put(AgentState.DYING, new Animation(dyingFrames, 0.1, false));

        setCurrentState(AgentState.IDLE);
    }
    
    public void update(List<String> input, double deltaTime) {
        updateAnimation(deltaTime);
        if (currentState == AgentState.DYING) return;

        boolean isMoving = input.contains("UP") || input.contains("DOWN") || input.contains("LEFT") || input.contains("RIGHT");
        
        if (currentAnimation == null || currentAnimation.isFinished() || currentAnimation.isLooping()) {
            if (isDead()) { setCurrentState(AgentState.DYING); }
            else if (isMoving) { setCurrentState(AgentState.WALK); } 
            else { setCurrentState(AgentState.IDLE); }
        }
        
        if (currentState == AgentState.IDLE || currentState == AgentState.WALK) {
            this.move(input);
        }
        
        if (currentState != AgentState.ATTACK) {
             if (isMoving) { recoverEnergy(this.baseRecoveryFactor); lastMoveTime = System.currentTimeMillis(); } else { if (System.currentTimeMillis() - lastMoveTime > 1000) { recoverEnergy(this.idleRecoveryFactor); } else { recoverEnergy(this.baseRecoveryFactor); } }
        }
    }

    public Shot useWeapon() {
        if (this.getWeapon() == null || (this.currentState == AgentState.ATTACK && !this.currentAnimation.isFinished()) || isDead()) return null;
        if (this.energy >= this.getWeapon().getEnergyCost()) {
            this.consumeEnergy(this.getWeapon().getEnergyCost());
            setCurrentState(AgentState.ATTACK);
            int shotHeight = 100; int shotWidth = 100; int handsY = this.getPosY() + 75; int shotStartY = handsY - (shotHeight / 2); int shotStartX; String direction;
            if (this.isFlipped()) { direction = "LEFT"; shotStartX = this.getPosX() - (shotWidth / 2) - 30; } else { direction = "RIGHT"; shotStartX = this.getPosX() + this.getWidth() - (shotWidth / 2) + 30; }
            return this.weapon.fire(shotStartX, shotStartY, direction);
        } else {
            return null;
        }
    }
    
    public ExplosionEffect useAreaAbility() {
        if ((this.currentState == AgentState.ATTACK && !this.currentAnimation.isFinished()) || isDead()) return null;
        double cost = 0.30;
        if (this.energy < cost) return null;
        this.consumeEnergy(cost);
        setCurrentState(AgentState.ATTACK);
        int offsetX = isFlipped() ? -250 : 100;
        int effectX = this.getPosX() + offsetX;
        int effectY = this.getPosY() + this.getHeight() - 250;
        return new ExplosionEffect(effectX, effectY);
    }
    
    @Override
    public void takeDamage(int damage, List<Message> messages) {
        if (isInvulnerable() || isDead()) return; // Se estiver invulnerável ou morto, não faz nada

        super.takeDamage(damage, messages);
        this.lastHitTime = System.currentTimeMillis(); // Atualiza o tempo do último hit
        
        if (isDead()) {
            setCurrentState(AgentState.DYING);
        } else {
            setCurrentState(AgentState.TAKE_DAMAGE);
        }
    }
    
    public boolean isInvulnerable() { 
        return System.currentTimeMillis() - lastHitTime < INVULNERABILITY_COOLDOWN; 
    }

    public boolean isDead() { return (this.getHealth() <= 0); }
    public boolean isDyingAnimationFinished() { return currentState == AgentState.DYING && currentAnimation != null && currentAnimation.isFinished(); }
    public void recoverEnergy(double r) { this.energy = Math.min(this.fullEnergy, (this.energy + r)); }
    public void consumeEnergy(double a) { this.energy = Math.max(0, this.energy - a); }
    public int getCollisionDamage() { return collisionDamage; }
    public Weapon getWeapon() { return weapon; }
    public void setWeapon(Weapon weapon) { this.weapon = weapon; }
    public double getEnergy() { return energy; }
}