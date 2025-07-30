package chon.group.game.domain.weapon;

import java.util.List;

import chon.group.game.core.agent.Agent;
import chon.group.game.core.weapon.Shot;
import chon.group.game.messaging.Message;

public class LancerCell extends Lancer{

    public LancerCell(int posX, int posY, int height, int width, int speed, int health, double energyCost,
            String pathImage, boolean flipped) {
        super(posX, posY, height, width, speed, health, energyCost, pathImage, flipped);
    }

    @Override
    protected Shot createShot(int posX, int posY, String direction, Agent owner) {
        if (direction.equals("RIGHT"))
            posX += 84 + 1;
        else
            posX -= 84 + 1;
        return new Fireball(posX+90,
                posY + 52,
                42,
                64,
                4,
                0,
                "/images/weapons/fireball/shot.png",
                false,
                200,
                direction,
                owner);
    }

    @Override
    public void takeDamage(int damage, List<Message> messages) {

    }
}
