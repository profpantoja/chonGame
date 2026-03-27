package chon.group.game.domain.weapon;

import java.util.List;

import chon.group.game.animation.Animation;
import chon.group.game.animation.AnimationType;
import chon.group.game.core.agent.Direction;
import chon.group.game.core.weapon.Shot;
import chon.group.game.core.weapon.Weapon;
import chon.group.game.messaging.Message;
import chon.group.game.sound.Sound;
import javafx.scene.image.Image;

public class Lancer extends Weapon {

    public Lancer(int posX, int posY, int height, int width, int speed, int health, double energyCost, String pathImage,
            boolean flipped) {
        super(posX, posY, height, width, speed, health, energyCost, pathImage, flipped);
    }

    @Override
    protected Shot createShot(int posX, int posY, Direction direction) {
        if (direction.equals(Direction.RIGHT))
            posX += 64 + 1;
        else
            posX -= 64 + 1;
        /*
         * Animation frames for fireball. That is not the best solution. It needs to
         * change.
         */
        Animation fireballShot = new Animation();
        fireballShot.getFrames().add(new Image(
                getClass().getResource("/images/weapons/fireball/fireball001.png").toExternalForm()));
        fireballShot.getFrames().add(new Image(
                getClass().getResource("/images/weapons/fireball/fireball002.png").toExternalForm()));
        fireballShot.getFrames().add(new Image(
                getClass().getResource("/images/weapons/fireball/fireball003.png").toExternalForm()));
        Fireball fireball = new Fireball(
                posX,
                posY + 30,
                42,
                64,
                3,
                0,
                direction,
                "/images/weapons/fireball/fireball001.png",
                false,
                500);
        fireball.getAnimationSet().add(AnimationType.IDLE, fireballShot);
        return fireball;
    }

    @Override
    public void takeDamage(int damage, List<Message> messages, List<Sound> sounds) {

    }

}
