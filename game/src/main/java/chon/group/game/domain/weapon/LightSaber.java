package chon.group.game.domain.weapon;

import java.util.List;

import chon.group.game.core.agent.Agent;
import chon.group.game.core.weapon.CloseWeapon;
import chon.group.game.core.weapon.Slash;
import chon.group.game.messaging.Message;

public class LightSaber extends CloseWeapon {

    public LightSaber(int posX, int posY, int height, int width, int speed, int health, String pathImage,
            boolean flipped) {
        super(posX, posY, height, width, speed, health, pathImage, flipped);
    }

    @Override
    protected Slash createSlash(int posX, int posY, String direction, Agent owner) {
        if (direction.equals("RIGHT"))
            posX += 70 + 10;
        else
            posX -= 70 + 10;
        return new SaberSlash(posX,
                posY,
                100,
                150,
                6,
                0,
                "/images/weapons/lightsaber/saberAttackInv.png",
                false,
                400,
                direction,
                owner);
    }
    @Override
    public void takeDamage(int damage, List<Message> messages) {

    }

}
