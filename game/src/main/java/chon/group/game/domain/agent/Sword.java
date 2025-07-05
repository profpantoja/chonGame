package chon.group.game.domain.agent;

import java.util.List;

import chon.group.game.messaging.Message;

public class Sword extends CloseWeapon {
    
    public Sword(int posX, int posY, int height, int width, int speed, int health, String pathImage, boolean flipped, int slashWidth) {
        super(posX, posY, height, width, speed, health, pathImage, flipped, slashWidth);
    }

    @Override
    

    protected Slash createSlash(int posX, int posY, String direction) {
        if (direction.equals("RIGHT"))
            posX += 95 + getSlashWidth();
        else
            posX -= 95 + getSlashWidth();
        return new Slash(posX,
                posY,
                50,
                getSlashWidth(),
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
