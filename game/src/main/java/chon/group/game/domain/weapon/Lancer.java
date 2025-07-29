package chon.group.game.domain.weapon;

import chon.group.game.core.weapon.Shot;
import chon.group.game.core.weapon.Weapon;

public class Lancer extends Weapon {

    public Lancer(int posX, int posY, int height, int width, int speed, int health, double energyCost, String pathImage, boolean flipped) {
        super(posX, posY, height, width, speed, health, energyCost, pathImage, flipped, 1000);
    }

    @Override
    protected Shot createShot(int posX, int posY, String direction) {
        return null;
    }
}