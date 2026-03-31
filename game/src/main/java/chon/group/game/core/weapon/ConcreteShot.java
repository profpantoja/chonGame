package chon.group.game.core.weapon;

import chon.group.game.core.agent.Direction;

public class ConcreteShot extends Shot {

    public ConcreteShot(
            int posX,
            int posY,
            int height,
            int width,
            int speed,
            int health,
            Direction direction,
            boolean flipped,
            int damage) {
        super(posX, posY, height, width, speed, health, direction, flipped, damage);
    }

    public ConcreteShot(
            int height,
            int width,
            int speed,
            int health,
            boolean flipped,
            int damage) {
        super(0, 0, height, width, speed, health, Direction.RIGHT, flipped, damage);
    }

}
