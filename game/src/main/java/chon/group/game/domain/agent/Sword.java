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
        // Adjust the position based on the direction
            posX += 60+ getSlashWidth();
        else
        // Adjust the position based on the direction flipped
            posX -= -90 + getSlashWidth();
        return new Slash(posX,
                posY,
                60,
                getSlashWidth(),
                3,
                0,
                "/images/weapons/slash/HitboxAtaque.png",
                false,
                100,
                direction);
    }

    @Override
    public void takeDamage(int damage, List<Message> messages) {
    }
}
