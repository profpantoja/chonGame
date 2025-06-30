package chon.group.game.domain.agent;

import java.util.List;

import chon.group.game.messaging.Message;

public class Sword extends CloseWeapon {

    public Sword(int posX, int posY, int height, int width, int speed, int health, String pathImage,
            boolean flipped) {
        super(posX, posY, height, width, speed, health, pathImage, flipped);
    }

    @Override
    

    protected Slash createSlash(int posX, int posY, String direction) {
        if (direction.equals("RIGHT"))
            posX += 95;
        else
            posX -= 95;
        return new Slash(posX,
                posY,
                50,
                140,
                3,
                0,
                "/images/weapons/slash/SlashTeste1.png",
                false,
                100,
                direction);
    }

    @Override
    public void takeDamage(int damage, List<Message> messages) {
    }
}
