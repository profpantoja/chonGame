package chon.group.game.domain.weapon;

import java.util.List;

import chon.group.game.core.agent.Direction;
import chon.group.game.core.weapon.Shot;
import chon.group.game.core.weapon.Weapon;
import chon.group.game.messaging.Message;

public class Lancer extends Weapon {

    public Lancer(int posX, int posY, int height, int width, int speed, int health, double energyCost, String pathImage,
            boolean flipped) {
        super(posX, posY, height, width, speed, health, energyCost, pathImage, flipped);
    }

    @Override
    protected Shot createShot(int posX, int posY, Direction direction) {
        if (direction.equals(Direction.RIGHT))
            posX += 64 + 1;
        else
            posX -= 64 + 1;
        return new Fireball(posX,
                posY + 30,
                42,
                64,
                4,
                0,
                direction,
                "/images/weapons/fireball/fireball001.png",
                false,
                500);
    }

    @Override
    public void takeDamage(int damage, List<Message> messages) {

    }

}
