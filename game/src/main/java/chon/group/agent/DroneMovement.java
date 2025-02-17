package chon.group.agent;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class DroneMovement extends EnemyMovement {

    private Agent spaceship;
    private ImageView imageView;

    public DroneMovement(int positionX, int positionY, int width, int height, String pathImage, boolean isProtagonist, int life, Agent spaceship) {
        super(positionX, positionY, width, height, pathImage, isProtagonist, life);
        this.spaceship = spaceship; // Referência da nave
        
        // Inicializa a imagem do drone
        this.imageView = new ImageView(new Image(getClass().getResourceAsStream(pathImage)));
        this.imageView.setPreserveRatio(true);
        this.imageView.setSmooth(true);
    }

    @Override
    public void move() {
        if (spaceship != null) {
            int spaceshipX = spaceship.getPositionX();
            int spaceshipY = spaceship.getPositionY();


            if (getPositionX() < spaceshipX) {
                setPositionX(getPositionX() + 2);
                imageView.setScaleX(-1); // flip para a esquerda
            } else if (getPositionX() > spaceshipX) {
                setPositionX(getPositionX() - 2);
                imageView.setScaleX(1); // Mantém o padrão
            }


            if (getPositionY() < spaceshipY) {
                setPositionY(getPositionY() + 2);
            } else if (getPositionY() > spaceshipY) {
                setPositionY(getPositionY() - 2);
            }


            imageView.setX(getPositionX());
            imageView.setY(getPositionY());
        }
    }

    public ImageView getImageView() {
        return imageView;
    }
}

