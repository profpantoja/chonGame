package chon.group.game.domain.agent;

import java.util.List;

import chon.group.game.messaging.Message;

public class Fireball extends Weapon {

    public Fireball(int posX, int posY, int height, int width, int speed, int health, String pathImage,
            boolean flipped, int shotWidth) {
        super(posX, posY, height, width, speed, health, pathImage, flipped, shotWidth);
    }

    @Override
    protected Shot createShot(int posX, int posY, String direction) {
        int offset = getWidth(); // player's width
        if (direction.equals("RIGHT"))
            posX += getShotWidth() + 100;
        else
            posX -= getShotWidth() + 100;
        return new Shot(posX,
                posY,
                47,
                getShotWidth(),
                3,
                0,
                "/images/weapons/fireball/fireball001.png",
                false,
                100,
                direction);
    }

    @Override
    public void takeDamage(int damage, List<Message> messages) {

    }

}
