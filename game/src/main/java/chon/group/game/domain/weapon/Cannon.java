package chon.group.game.domain.weapon;

import java.util.List;

import chon.group.game.core.agent.Agent;
import chon.group.game.core.weapon.Shot;
import chon.group.game.core.weapon.Weapon;
import chon.group.game.messaging.Message;

public class Cannon extends Weapon {

    public Cannon(int posX, int posY, int height, int width, int speed, int health, double energyCost, String pathImage,
            boolean flipped) {
        super(posX, posY, height, width, speed, health, energyCost, pathImage, flipped);
    }

    @Override
    protected Shot createShot(int posX, int posY, String direction, Agent owner) {
        if (direction.equals("RIGHT"))
            posX += 64 + 1;
        else
            posX -= 64 + 1;
        return new Rocket(posX,
                posY,
                42,
                64,
                3,
                0,
                "/images/weapons/missile/missile001.png",
                false,
                1500,
                direction,
                owner);
    }

    @Override
    public void takeDamage(int damage, List<Message> messages) {

    }

}
