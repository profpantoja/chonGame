package chon.group.game.core.agent;

import java.util.List;

import chon.group.game.core.weapon.Shot;
import chon.group.game.core.weapon.Weapon;
import chon.group.game.messaging.Message;

public class Agent extends Entity {

    private long lastHitTime = 0;
    private boolean invulnerable = false;
    private final long INVULNERABILITY_COOLDOWN = 1000;
    private Weapon weapon;
    private double energy;
    private final double fullEnergy;
    private final double recoveryFactor;

    public Agent(int posX, int posY, int height, int width, int speed, int health, String pathImage, boolean flipped, boolean visibleBars) {
        super(posX, posY, height, width, speed, health, pathImage, flipped, visibleBars);
        this.energy = 1.0;
        this.fullEnergy = 1.0;
        this.recoveryFactor = 0.0002;
    }
    
    // MÉTODO ADICIONADO PARA COMPATIBILIDADE
    public void update(List<String> input) {
        this.move(input);
    }
    // FIM DO MÉTODO ADICIONADO

    public long getLastHitTime() {
        return lastHitTime;
    }

    public void setLastHitTime(long lastHitTime) {
        this.lastHitTime = lastHitTime;
    }

    public long getInvulnerabilityCooldown() {
        return INVULNERABILITY_COOLDOWN;
    }

    public boolean isInvulnerable() {
        return invulnerable;
    }

    public void setInvulnerable(boolean invulnerable) {
        this.invulnerable = invulnerable;
    }

    public Weapon getWeapon() {
        return weapon;
    }

    public void setWeapon(Weapon weapon) {
        this.weapon = weapon;
    }

    public double getEnergy() {
        return energy;
    }

    public void setEnergy(double energy) {
        this.energy = energy;
    }

    public double getFullEnergy() {
        return fullEnergy;
    }

    public double getRecoveryFactor() {
        return recoveryFactor;
    }

    public void consumeEnergy(double amount) {
        this.energy = Math.max(0, this.energy - amount);
    }

    public void recoverEnergy() {
        this.energy = Math.min(this.fullEnergy, (this.energy + this.recoveryFactor));
    }

    public boolean isEnergyEmpty() {
        return this.energy <= 0;
    }

    public boolean isDead() {
        return (this.getHealth() <= 0);
    }

    @Override
    public void takeDamage(int damage, List<Message> messages) {
        this.invulnerable = this.updateInvulnerability();
        if (!this.invulnerable) {
            super.takeDamage(damage, messages);
            this.lastHitTime = System.currentTimeMillis();
        }
    }

    private boolean updateInvulnerability() {
        if (System.currentTimeMillis() - lastHitTime >= INVULNERABILITY_COOLDOWN) {
            return false;
        }
        return true;
    }

    public Shot useWeapon() {
        String direction = this.isFlipped() ? "LEFT" : "RIGHT";
        if (this.energy >= this.getWeapon().getEnergyCost()) {
            this.consumeEnergy(this.getWeapon().getEnergyCost());
            return this.weapon.fire(this.getPosX(), this.getPosY(), direction);
        } else
            return null;
    }
}