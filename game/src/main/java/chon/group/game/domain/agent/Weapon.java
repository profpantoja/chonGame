package chon.group.game.domain.agent;

import chon.group.game.core.Entity;

public abstract class Weapon extends Entity {

    private long lastShootTime = 0;
    private final long SHOOT_COOLDOWN = 400; // Você pode tornar isso personalizável se quiser

    private int shotWidth;


    public int getShotWidth() {
        return shotWidth;
    }

    public void setShotWidth(int shotWidth) {
        this.shotWidth = shotWidth;
    }



    public Weapon(int posX, int posY, int height, int width, int speed, int health, String pathImage, boolean flipped, int shotWidth) {
        super(posX, posY, height, width, speed, health, pathImage, flipped);
        this.shotWidth = shotWidth;
    }

    protected abstract Shot createShot(int posX, int posY, String direction);

    /**
     * Dispara um tiro, caso o tempo de recarga tenha passado.
     * @return o objeto Shot ou null se não puder atirar agora
     */
    public Shot fire(int posX, int posY, String direction) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastShootTime >= SHOOT_COOLDOWN) {
            lastShootTime = currentTime;
            return createShot(posX, posY, direction);
        }
        return null;
    }

    public long getShootCooldown() {
        return SHOOT_COOLDOWN;
    }

    public boolean canShoot() {
        return System.currentTimeMillis() - lastShootTime >= SHOOT_COOLDOWN;
    }
}
