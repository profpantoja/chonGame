package chon.group.game.core.agent;

/**
 * Representa um objeto genérico do jogo que pode ser coletável e/ou
 * destrutível.
 */
public class Object extends Entity {
    private boolean collected = false;
    private boolean destructible;
    private boolean collectible;

    public Object(int posX, int posY, int height, int width, String pathImage,
            boolean collectible, boolean destructible) {
        super(posX, posY, height, width, pathImage);
        this.collectible = collectible;
        this.destructible = destructible;
    }

    public Object(int posX, int posY, int height, int width, String imagePath) {
        super(posX, posY, height, width, imagePath);
        this.collectible = true;
        this.destructible = false;
    }

    public boolean isCollected() {
        return collected;
    }

    public void setCollected(boolean collected) {
        this.collected = collected;
    }

    public boolean isCollectible() {
        return collectible;
    }

    public void setCollectible(boolean collectible) {
        this.collectible = collectible;
    }

    public boolean isDestructible() {
        return destructible;
    }

    public void setDestructible(boolean destructible) {
        this.destructible = destructible;
    }

    /**
     * Define o comportamento de coleta quando próximo ao agente.
     * Pode ser sobrescrito em subclasses.
     */
    public void onCollect() {
        this.collected = true;
    }

    /**
     * Define o comportamento de destruição.
     * Pode ser sobrescrito em subclasses.
     */
    public void onDestroy() {
        this.collected = true; 
    }

    public void follow(Entity target, double attractionRadius, double speed) {
        double dx = target.getPosX() - this.getPosX();
        double dy = target.getPosY() - this.getPosY();
        double distance = Math.sqrt(dx * dx + dy * dy);

        if (distance < attractionRadius) {
            // Normaliza o vetor direção
            double directionX = dx / distance;
            double directionY = dy / distance;

            // Move um pouco na direção do protagonista
            this.setPosX((int) (this.getPosX() + directionX * speed));
            this.setPosY((int) (this.getPosY() + directionY * speed));
        }
    }

}