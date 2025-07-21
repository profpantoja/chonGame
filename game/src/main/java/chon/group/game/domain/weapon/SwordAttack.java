package chon.group.game.domain.weapon;

import chon.group.game.core.weapon.Slash;

public class SwordAttack extends Slash {

    public SwordAttack(int posX, int posY, int height, int width, int speed, int health, String pathImage, boolean flipped,
            int damage, String direction) {
        super(posX, posY, height, width, speed, health, pathImage, flipped, damage, direction);
    }

}
