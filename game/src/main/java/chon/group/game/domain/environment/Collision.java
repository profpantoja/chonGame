package chon.group.game.domain.environment;
import javafx.scene.image.Image;

public class Collision {
    private int x;
    private int y;
    private int width;
    private int height;
    private Image image;
    private boolean contactDestroy;
    private boolean projectileDestroy;
    private boolean destroy;
    private int damage;
    private boolean passable;
    private boolean agentDamage;
    private boolean agentContact;

    public Collision(int x, int y, int width, int height, String image, boolean contactDestroy, boolean projectileDestroy, int damage, boolean passable, boolean agentDamage, boolean agentContact) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.image = new Image(getClass().getResource(image).toExternalForm());
        this.contactDestroy = contactDestroy;
        this.projectileDestroy = projectileDestroy;
        this.damage = damage;
        this.passable = passable;
        this.agentDamage = agentDamage;
        this.agentContact = agentContact;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public boolean isDestroy() {
        return destroy;
    }

    public void setDestroy(boolean destroy) {
        this.destroy = destroy;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public boolean isPassable() {
        return passable;
    }

    public void setPassable(boolean passable) {
        this.passable = passable;
    }

    public boolean isContactDestroy() {
        return contactDestroy;
    }

    public void setContactDestroy(boolean contactDestroy) {
        this.contactDestroy = contactDestroy;
    }

    public boolean isProjectileDestroy() {
        return projectileDestroy;
    }

    public void setProjectileDestroy(boolean projectileDestroy) {
        this.projectileDestroy = projectileDestroy;
    }

    public boolean isAgentDamage() {
        return agentDamage;
    }

    public void setAgentDamage(boolean agentDamage) {
        this.agentDamage = agentDamage;
    }

    public boolean isAgentContact() {
        return agentContact;
    }

    public void setAgentContact(boolean agentContact) {
        this.agentContact = agentContact;
    }
}
