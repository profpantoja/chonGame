package chon.group.game.domain.weapon;

import java.util.List;

import chon.group.game.core.agent.Agent;
import chon.group.game.core.animation.AnimationSpritesheet;
import chon.group.game.core.animation.SimpleAnimationSpritesheet;
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
            posX += 84 + 1;
        else
            posX -= 84 + 1;
            AnimationSpritesheet shotAnimated = new SimpleAnimationSpritesheet(38, 18, 4, 200, "/images/weapons/shots/shot.png");
        Rocket shot = new Rocket(posX,
                posY + 30,
                42,
                64,
                4,
                0,
                "/images/weapons/shots/shot.png",
                false,
                2000,
                direction,
                owner,
                shotAnimated);
            return shot;
    }
    

    @Override
    public void takeDamage(int damage, List<Message> messages) {

    }

}
