package chon.group.agent;

import java.util.ArrayList;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;

public class Agent {
    private int positionX;
    private int positionY;
    private int height;
    private int width;
    private Image image;
    private Boolean isProtagonist;
    public static int numberProtagonist = 0;
    private ArrayList<Image> explosionImages; // ArrayList de imagens para a explosão
    private int explosionFrame = 0; // Controlador de frames da explosão
    private boolean isExploding = false;
    private long explosionStartTime = 0; // Marca o tempo que a explosão começa
    private int life;
    private boolean alive = true;


    public Agent(int positionX, int positionY, int width, int height, String pathImage, boolean isProtagonist,
            int life) {
        this.positionX = positionX;
        this.positionY = positionY;
        this.width = width;
        this.height = height;
        this.image = new Image(getClass().getResource(pathImage).toExternalForm());
        this.isProtagonist = isProtagonist;
        this.life = life;
        System.out.println("Vida inicial: " + this.life); // Verifique o valor aqui!
        this.explosionImages = new ArrayList<>();
        this.explosionFrame = 0;
        this.isExploding = false;

        for (int i = 1; i <= 8; i++) {
            explosionImages.add(new Image(
                    getClass().getResource("/images/agent/Death_animation/explosao" + i + ".png").toExternalForm()));
        }

        if (isProtagonist) {
            numberProtagonist++;
        }
    }

    

    public int getPositionX() {
        return positionX;
    }

    public void setPositionX(int positionX) {
        this.positionX = positionX;
    }

    public int getPositionY() {
        return positionY;
    }

    public void setPositionY(int positionY) {
        this.positionY = positionY;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public Boolean getIsProtagonist() {
        return isProtagonist;
    }

    public void setIsProtagonist(Boolean isProtagonist) {
        this.isProtagonist = isProtagonist;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(String pathImage) {
        this.image = new Image(getClass().getResource(pathImage).toExternalForm());
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public void move(String direction) {
        if (direction.equals("LEFT")) {
            this.positionX -= 10;
        } else if (direction.equals("RIGHT")) {
            this.positionX += 10;
        } else if (direction.equals("UP")) {
            this.positionY -= 10;
        } else if (direction.equals("DOWN")) {
            this.positionY += 10;
        }
    }
    


    // Método para iniciar a explosão
    public void startExplosion() {
        this.isExploding = true;
        this.explosionFrame = 0; // Reinicia a animação
        this.explosionStartTime = System.currentTimeMillis(); // Marca o tempo em que a explosão começa
        updateExplosion(); // Atualiza a explosão imediatamente
        this.image = explosionImages.get(explosionFrame); // A primeira imagem da explosão é imediatamente exibida
    }

    // Método para atualizar a animação da explosão
    public void updateExplosion() {
        if (isExploding) {
            // Se a explosão foi iniciada, verifica o tempo que passou desde o início
            if (explosionFrame == 0) {
                explosionStartTime = System.currentTimeMillis(); // Registra o tempo de início da explosão
            }

            long elapsedTime = System.currentTimeMillis() - explosionStartTime; // Tempo decorrido desde o início da
                                                                                // explosão
            int frameDuration = 100; // Duração de cada frame da explosão em milissegundos

            // Avança os frames da explosão conforme o tempo decorrido
            if (elapsedTime >= frameDuration * explosionFrame && explosionFrame < explosionImages.size()) {
                this.image = explosionImages.get(explosionFrame);
                explosionFrame++; // Avança para o próximo frame
            }

            // Se todos os frames da explosão foram exibidos, finaliza a animação da
            // explosão
            if (explosionFrame >= explosionImages.size()) {
                isExploding = false; // Finaliza a explosão
            }
        } else
            this.alive = false;
    }

    // Método para verificar se está em explosão
    public boolean isExploding() {
        return isExploding;
    }

    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
    }

    public void desacrease_life(int amount) {
        this.life = this.life - amount;
        if (this.life <= 0) {
            this.life = 0;
        }
        System.out.println(this.life);
    }

    public Rectangle2D getBounds() {
        return new Rectangle2D(positionX, positionY, width, height);
    }

}
