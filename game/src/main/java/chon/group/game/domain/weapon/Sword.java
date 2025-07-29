package chon.group.game.domain.weapon;

import java.util.List;

import chon.group.game.core.agent.Agent;
import chon.group.game.core.weapon.CloseWeapon;
import chon.group.game.core.weapon.Slash;
import chon.group.game.messaging.Message;

public class Sword extends CloseWeapon {

    public Sword(int posX, int posY, int height, int width, int speed, int health, String pathImage,
            boolean flipped) {
        super(posX, posY, height, width, speed, health, pathImage, flipped);
    }

    @Override
    

    protected Slash createSlash(int posX, int posY, String direction, Agent owner) {
        if (direction.equals("RIGHT"))
            posX += 75 + 10;
        else
            posX -= 75 + 10;
        return new SwordAttack(posX,
                posY,
                47,
                80,
                3,
                0,
                "/images/weapons/slash/SlashTeste.png",
                false,
                1000,
                direction,
                owner);
    }

    @Override
    public void takeDamage(int damage, List<Message> messages) {

    }

}
