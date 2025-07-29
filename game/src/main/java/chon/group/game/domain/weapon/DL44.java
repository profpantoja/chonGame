package chon.group.game.domain.weapon;

import java.util.List;

import chon.group.game.core.agent.Agent;
import chon.group.game.core.weapon.Shot;
import chon.group.game.core.weapon.Weapon;
import chon.group.game.messaging.Message;

public class DL44 extends Weapon {

    public DL44(int posX, int posY, int height, int width, int speed, int health, double energyCost, String pathImage,
            boolean flipped) {
        super(posX, posY, height, width, speed, health, energyCost, pathImage, flipped);
    }

    public DL44() {
        super(0, 0, 0, 0, 0, 0, 0, null, false);
    }

    @Override
    protected Shot createShot(int posX, int posY, String direction, Agent owner) {
        if (direction.equals("RIGHT"))
            posX += 100 + 1;
        else
            posX -= 100 + 1;
            
            return new Blaster(posX,
                posY + 30,
                20,
                64,
                8,
                0,
                "/images/weapons/DL44/blasterShoot.png",
                false,
                500,
                direction,
                owner);


    }

    @Override
    public void takeDamage(int damage, List<Message> messages) {

    }

}
