package chon.group.game.domain.agent;

import java.util.List;

import chon.group.game.domain.environment.KeyboardKey;
import chon.group.game.messaging.Message;

public class Cannon extends Weapon {

    public Cannon(int posX, int posY, int height, int width, int speed, int health, String pathImage, boolean flipped) {
        super(posX, posY, height, width, speed, health, pathImage, flipped);
    }

    @Override
    protected Shot createShot(int posX, int posY, KeyboardKey direction) {
        if (direction == KeyboardKey.RIGHT)
            posX += 64 + 1;
        else
            posX -= 64 + 1;
        return new Shot(posX,
                posY,
                42,
                64,
                3,
                0,
                "/images/weapons/missile/missile001.png",
                false,
                100,
                direction);
    }

    @Override
    public void takeDamage(int damage, List<Message> messages) {

    }

}
