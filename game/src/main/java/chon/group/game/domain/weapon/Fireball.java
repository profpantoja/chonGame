package chon.group.game.domain.weapon;

import chon.group.game.core.weapon.Shot;

public class Fireball extends Shot{

    public Fireball(int posX, int posY, int height, int width, int speed, int health, String pathImage, boolean flipped,
            int damage, String direction) {
        super(posX, posY, height, width, speed, health, pathImage, flipped, damage, direction);
    }

}
