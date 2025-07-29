package chon.group.game.domain.weapon;

import chon.group.game.core.weapon.Shot;
import chon.group.game.core.weapon.Weapon;

public class MageStaff extends Weapon {

    public MageStaff(double energyCost, long cooldown) {
        super(0, 0, 0, 0, 0, 0, energyCost, "", false, cooldown);
    }

    @Override
    protected Shot createShot(int posX, int posY, String direction) {
        return new EssenceOrb(posX, posY, direction);
    }
}