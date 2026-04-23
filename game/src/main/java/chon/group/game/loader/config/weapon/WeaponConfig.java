package chon.group.game.loader.config.weapon;

public class WeaponConfig {

    private int offsetY;
    private double energyCost;
    private long cooldown;
    private String shot;

    public int getOffsetY() {
        return offsetY;
    }

    public void setOffsetY(int offsetY) {
        this.offsetY = offsetY;
    }

    public double getEnergyCost() {
        return energyCost;
    }

    public void setEnergyCost(double energyCost) {
        this.energyCost = energyCost;
    }

    public long getCooldown() {
        return cooldown;
    }

    public void setCooldown(long cooldown) {
        this.cooldown = cooldown;
    }

    public String getShot() {
        return shot;
    }

    public void setShot(String shot) {
        this.shot = shot;
    }
}