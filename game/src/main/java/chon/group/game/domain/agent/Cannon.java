package chon.group.game.domain.agent;

import java.util.List;

import chon.group.game.messaging.Message;

public class Cannon extends Weapon {

    public Cannon(int posX, int posY, int height, int width, int speed, int health, String pathImage, boolean flipped, int shotWidth) {
        super(posX, posY, height, width, speed, health, pathImage, flipped, shotWidth);
    }

    @Override
    protected Shot createShot(int posX, int posY, String direction) {
        if (direction.equals("RIGHT"))
            posX += getShotWidth() + 1;
        else
            posX -= getShotWidth() + 1;
        return new Shot(posX,
                posY,
                42,
                getShotWidth(),
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
