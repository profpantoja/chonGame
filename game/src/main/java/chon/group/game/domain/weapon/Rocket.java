package chon.group.game.domain.weapon;

import chon.group.game.core.agent.Direction;
import chon.group.game.core.weapon.Shot;

public class Rocket extends Shot {

    public Rocket(int posX, int posY, int height, int width, int speed, int health, Direction direction,
            String pathImage, boolean flipped,
            int damage) {
        super(posX, posY, height, width, speed, health, direction, flipped, damage);
    }

}
