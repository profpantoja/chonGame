package chon.group.game.core.agent;

import java.net.URL; // Importe a classe URL
import java.util.ArrayList;
import java.util.List;

import chon.group.game.messaging.Message;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

public abstract class Entity {

    // ... (atributos continuam os mesmos)
    protected int posX;
    protected int posY;
    protected int height;
    protected int width;
    protected int speed;
    protected Image image;
    private boolean flipped = false;
    private int health;
    private int fullHealth;
    private boolean visibleBars = false;

    // CONSTRUTOR MODIFICADO PARA SER À PROVA DE FALHAS
    public Entity(int posX, int posY, int height, int width, int speed, int health, String pathImage, boolean flipped, boolean visibleBars) {
        this.posX = posX;
        this.posY = posY;
        this.height = height;
        this.width = width;
        this.speed = speed;
        this.health = health;
        this.fullHealth = health;
        this.flipped = flipped;
        this.visibleBars = visibleBars;

        // Lógica de carregamento de imagem mais segura
        if (pathImage != null && !pathImage.isEmpty()) {
            try {
                URL imageUrl = getClass().getResource(pathImage);
                if (imageUrl == null) {
                    System.err.println("[ERRO] Imagem não encontrada no caminho: " + pathImage);
                    this.image = null; // Define como nulo para não quebrar
                } else {
                    this.image = new Image(imageUrl.toExternalForm());
                }
            } catch (Exception e) {
                System.err.println("[ERRO CRÍTICO] Falha ao tentar carregar a imagem: " + pathImage);
                e.printStackTrace();
                this.image = null;
            }
        } else {
            this.image = null;
        }
    }

    // --- O RESTO DA CLASSE CONTINUA IGUAL ---

    public int getX() { return posX; }
    public int getY() { return posY; }
    public int getPosX() { return posX; }
    public void setPosX(int posX) { this.posX = posX; }
    public int getPosY() { return posY; }
    public void setPosY(int posY) { this.posY = posY; }
    public int getHeight() { return height; }
    public void setHeight(int height) { this.height = height; }
    public int getWidth() { return width; }
    public void setWidth(int width) { this.width = width; }
    public int getSpeed() { return speed; }
    public void setSpeed(int speed) { this.speed = speed; }
    public Image getImage() { return image; }
    public void setImage(Image image) { this.image = image; }
    public boolean isFlipped() { return flipped; }
    public void setFlipped(boolean flipped) { this.flipped = flipped; }
    public int getHealth() { return health; }
    public void setHealth(int health) { this.health = health; }
    public int getFullHealth() { return fullHealth; }
    public void setFullHealth(int fullHealth) { this.fullHealth = fullHealth; }
    public boolean isVisibleBars() { return visibleBars; }
    public void setVisibleBars(boolean visible) { this.visibleBars = visible; }

    public void flipImage() { /* ...código sem alteração... */ }
    public void move(List<String> movements) { /* ...código sem alteração... */ }
    public void chase(int targetX, int targetY) { /* ...código sem alteração... */ }

    public void takeDamage(int damage, List<Message> messages) {
        if (this.getHealth() > 0) {
            this.setHealth(this.getHealth() - damage);
            messages.add(new Message(String.valueOf(damage), this.getPosX(), this.getPosY(), 25));
            if (this.getHealth() < 0) this.setHealth(0);
        }
    }
}