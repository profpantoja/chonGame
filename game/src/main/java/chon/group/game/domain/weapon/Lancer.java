package chon.group.game.domain.weapon;

import java.util.List;

import chon.group.game.core.agent.Agent;
import chon.group.game.core.weapon.Shot;
import chon.group.game.core.weapon.Weapon;
import chon.group.game.messaging.Message;

public class Lancer extends Weapon {

    public Lancer(int posX, int posY, int height, int width, int speed, int health, double energyCost, String pathImage,
            boolean flipped) {
        super(posX, posY, height, width, speed, health, energyCost, pathImage, flipped);
    }

    @Override
protected Shot createShot(int posX, int posY, String direction, Agent owner) {
    if (direction.equals("RIGHT"))
        posX += 84 + 1;
    else
        posX -= 84 + 1;

    // Centraliza verticalmente o tiro com base na altura do dono da arma
    int shotY = owner.getPosY() + owner.getHeight() / 2 - 32; // 32 é metade da altura do tiro (64/2)

    return new Fireball(posX,
            shotY,
            42,
            64,
            4,
            0,
            "/images/weapons/fireball/fireball001.png",
            false,
            500,
            direction,
            owner);
}


    @Override
    public void takeDamage(int damage, List<Message> messages) {

    }

}
