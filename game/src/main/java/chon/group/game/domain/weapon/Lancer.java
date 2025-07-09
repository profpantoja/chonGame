package chon.group.game.domain.weapon;

import chon.group.game.core.weapon.Shot;
import chon.group.game.core.weapon.Weapon;

public class Lancer extends Weapon {

    public Lancer(int posX, int posY, int height, int width, int speed, int health, String pathImage, boolean flipped) {
        super(posX, posY, height, width, speed, health, pathImage, flipped);
    }

    @Override
    protected Shot createShot(int posX, int posY, String direction) {
        if (direction.equals("RIGHT"))
            posX += 64 + 1;
        else
            posX -= 64 + 1;
        return new Fireball(posX,
                posY,
                42,
                64,
                3,
                0,
                "/images/weapons/fireball/fireball001.png",
                false,
                100,
                direction);
    }

    @Override
    public void takeDamage(int damage) {

    }

}
