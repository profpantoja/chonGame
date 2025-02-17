package chon.group.agent;


public class AsteroidMovement extends EnemyMovement {

    public AsteroidMovement(int positionX, int positionY, int width, int height, String pathImage, boolean isProtagonist, int life) {
        super(positionX, positionY, width, height, pathImage, isProtagonist, life);
    }

    @Override
    public void move() {
        // Movimento do asteroide: se move para a esquerda
        setPositionX(getPositionX() - 5); 
    }

}
