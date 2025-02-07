package chon.group.game.domain.item;

public class FallingItem {
    private double posX;
    private double posY;
    private int width;
    private int height;
    private double speed;
    private String imagePath;
    private boolean isBomb;

    public FallingItem(double posX, int width, int height, double speed, String imagePath, boolean isBomb) {
        this.posX = posX;
        this.posY = 0;
        this.width = width;
        this.height = height;
        this.speed = speed;
        this.imagePath = imagePath;
        this.isBomb = isBomb;
    }

    public double getPosX() { return posX; }
    public void setPosX(double posX) { this.posX = posX; }
    public double getPosY() { return posY; }
    public void setPosY(double posY) { this.posY = posY; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public String getImagePath() { return imagePath; }
    public boolean isBomb() { return isBomb; }

    public void fall() {
        posY += speed;
    }
}