package chon.group.game.domain.weapon;

import chon.group.game.core.agent.Agent;
import chon.group.game.core.weapon.Slash;

public class SaberSlash extends Slash {

    public SaberSlash(int posX, int posY, int height, int width, int speed, int health, String pathImage, boolean flipped,
            int damage, String direction, Agent owner) {
        super(posX, posY, height, width, speed, health, pathImage, flipped, damage, direction, owner);
    }

}
