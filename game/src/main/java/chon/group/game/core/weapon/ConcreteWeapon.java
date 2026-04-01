package chon.group.game.core.weapon;

import java.util.List;

import chon.group.game.animation.AnimationSet;
import chon.group.game.animation.AnimationType;
import chon.group.game.core.agent.Direction;
import chon.group.game.messaging.Message;
import chon.group.game.sound.Sound;

public class ConcreteWeapon extends Weapon {

    private ConcreteShot shot;

    public ConcreteWeapon(
            int posX,
            int posY,
            int width,
            int height,
            int speed,
            int health,
            double energyCost,
            boolean flipped,
            ConcreteShot shot) {
        super(posX, posY, width, height, speed, health, energyCost, flipped);
        this.shot = shot;
    }

    public ConcreteWeapon(
            int width,
            int height,
            int speed,
            int health,
            double energyCost,
            boolean flipped,
            long cooldown,
            ConcreteShot shot) {
        super(width, height, speed, health, energyCost, flipped, cooldown);
        this.shot = shot;
    }

    public ConcreteShot getShot() {
        return shot;
    }

    public void setShot(ConcreteShot shot) {
        this.shot = shot;
    }

    @Override
    protected Shot createShot(int posX, int posY, int entityWidth, Direction direction) {
        if (direction.equals(Direction.RIGHT))
            posX += entityWidth + 1;
        else
            posX -= entityWidth + 1;
        AnimationSet animationSet = this.shot.getAnimationSet();
        this.shot.getAnimationState().setCurrentAnimation(animationSet.get(AnimationType.IDLE));
        ConcreteShot shot = new ConcreteShot(
                posX,
                posY + 30,
                this.shot.getHeight(),
                this.shot.getWidth(),
                this.shot.getSpeed(),
                this.shot.getHealth(),
                direction,
                false,
                this.shot.getDamage());
        shot.setAnimationSet(animationSet);
        shot.getAnimationState().setCurrentAnimation(animationSet.get(AnimationType.IDLE));
        return shot;
    }

    @Override
    public void takeDamage(int damage, List<Message> messages, List<Sound> sounds) {

    }

}
