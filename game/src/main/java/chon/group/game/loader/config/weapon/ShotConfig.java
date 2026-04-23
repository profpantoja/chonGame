package chon.group.game.loader.config.weapon;

import chon.group.game.loader.config.agent.SizeConfig;

public class ShotConfig {

    private SizeConfig size;

    private double speed;
    private int damage;
    private int range;

    private boolean destructible;

    private String animation;

    public SizeConfig getSize() {
        return size;
    }

    public void setSize(SizeConfig size) {
        this.size = size;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public boolean isDestructible() {
        return destructible;
    }

    public void setDestructible(boolean destructible) {
        this.destructible = destructible;
    }

    public String getAnimation() {
        return animation;
    }

    public void setAnimation(String animation) {
        this.animation = animation;
    }
}