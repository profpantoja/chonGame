package application;

import java.util.ArrayList;

import engine.BulletOne;
import engine.BulletTwo;
import javafx.geometry.Rectangle2D;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class Character {

	public enum Direction {
		RIGHT, LEFT, DOWN, UP, STOPPED
	}

	private ArrayList<Image> images;
	private int imageIndex;
	private int positionX;
	private int positionY;
	private int velocityX;
	private int velocityY;
	private int width;
	private int height;
	public Direction direction = Character.Direction.RIGHT;
	private ArrayList<Character> weapons;
	private int weapon = 0;
	private boolean jumping = true;
	
	
	public boolean isJumping() {
		return jumping;
	}

	public void setJumping(boolean jumping) {
		this.jumping = jumping;
	}

	public ArrayList<Character> getWeapons() {
		return weapons;
	}

	public void setWeapons(ArrayList<Character> weapons) {
		this.weapons = weapons;
	}

	public int getWeapon() {
		return weapon;
	}

	public void setWeapon(int weapon) {
		this.weapon = weapon;
	}

	public ArrayList<Image> getImages() {
		return images;
	}

	public void setImages(ArrayList<Image> images) {
		this.images = images;
	}

	public int getImageIndex() {
		return imageIndex;
	}

	public void setImageIndex(int imageIndex) {
		this.imageIndex = imageIndex;
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

	public int getVelocityX() {
		return velocityX;
	}

	public void setVelocityX(int velocityX) {
		this.velocityX = velocityX;
	}

	public int getVelocityY() {
		return velocityY;
	}

	public void setVelocityY(int velocityY) {
		this.velocityY = velocityY;
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

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	public Character(int positionX, int positionY, int velocityX, int velocityY, int width, int height) {
		this.positionX = positionX;
		this.positionY = positionY;
		this.velocityX = velocityX;
		this.velocityY = velocityY;
		this.width = width;
		this.height = height;
		this.imageIndex = 0;
	}

	public void drawPanel(GraphicsContext gc) {
		gc.setFill(Color.BLACK);
		gc.setStroke(Color.BLACK);
		gc.setLineWidth(2);
		Font theFont = Font.font("Verdana", FontWeight.BOLD, 10);
		gc.setFont(theFont);
		gc.fillText("Xb: " + (this.positionX + this.width), this.positionX, positionY - 5);
		gc.fillText("X: " + this.positionX, this.positionX, positionY - 18);
		gc.fillText("Yb: " + (this.positionY + this.height), this.positionX, positionY - 31);
		gc.fillText("Y: " + this.positionY, this.positionX, positionY - 44);
		gc.fillText("Dir: " + this.direction, this.positionX, positionY - 57);
	}

	private Image flip(Image image) {
		ImageView iv = new ImageView(image);
		iv.setScaleX(-1);
		SnapshotParameters params = new SnapshotParameters();
		params.setFill(Color.TRANSPARENT);
		return iv.snapshot(params, null);
	}

	private Image nextMotion() {
		if (this.imageIndex < (this.images.size() - 1)) {
			this.imageIndex += 1;
			return this.images.get(this.imageIndex);
		} else {
			this.imageIndex = 0;
			return this.images.get(this.imageIndex);
		}
	}

	private void render(Image image, GraphicsContext gc) {
		gc.drawImage(image, this.positionX, this.positionY);
		this.drawPanel(gc);
	}

	public void moveRight(GraphicsContext gc) {
		this.positionX += this.velocityX;
		this.direction = Character.Direction.RIGHT;
		this.render(this.nextMotion(), gc);

	}

	public void moveUp(GraphicsContext gc) {
		this.positionY -= this.velocityY;
		this.direction = Character.Direction.UP;
		this.render(this.images.get(0), gc);
	}

	public void moveDown(GraphicsContext gc) {
		this.positionY += this.velocityY;
		this.direction = Character.Direction.DOWN;
		this.render(this.images.get(0), gc);
	}

	public void moveLeft(GraphicsContext gc) {
		this.positionX -= this.velocityX;
		this.direction = Character.Direction.LEFT;
		this.render(this.flip(this.nextMotion()), gc);
	}

	public void stopped(GraphicsContext gc) {
		switch (this.direction) {
		case LEFT:
			this.render(this.flip(this.images.get(0)), gc);
			break;
		default:
			this.render(this.images.get(0), gc);
			break;
		}
	}

	public void jumping(GraphicsContext gc) {
		this.direction = Character.Direction.UP;
		this.render(this.images.get(0), gc);
	}

	public void falling(GraphicsContext gc) {
		this.positionY += this.velocityY;
		if (this.direction == Character.Direction.LEFT)
			this.render(this.flip(this.images.get(0)), gc);
		// this.direction = Character.Direction.DOWN;
		else
			this.render(this.images.get(0), gc);
		this.direction = Character.Direction.DOWN;
	}

	public void draw(GraphicsContext gc) {
		if (this.direction == Character.Direction.LEFT)
			gc.drawImage(this.flip(this.images.get(0)), this.positionX, this.positionY);
		else
			gc.drawImage(this.images.get(0), this.positionX, this.positionY);
	}

	public Rectangle2D getBoundary() {
		return new Rectangle2D(positionX, positionY, width, height);
	}

	public boolean intersects(Character a) {
		boolean flag = false;
		if (a.getBoundary().intersects(this.getBoundary())) {
			flag = true;

			// Colidiu na Esquerda Somente
			if (this.positionX <= a.getBoundary().getMaxX()
					&& ((this.positionX + this.width) > a.getBoundary().getMaxX())) {
				if (this.direction == Character.Direction.LEFT)
					this.positionX = (int) (a.getBoundary().getMaxX() + 1);
			}

			// Colidiu na Direita Somente
			if ((this.positionX + this.width) >= a.getBoundary().getMinX()
					&& this.positionX < a.getBoundary().getMinX()) {
				if (this.direction == Character.Direction.RIGHT)
					this.positionX = (int) a.getBoundary().getMinX() - (this.width) - 1;
			}

			// Colidiu em Cima Somente
			if ((this.positionY + this.height) >= a.getBoundary().getMinY()
					&& this.positionY < a.getBoundary().getMinY()) {
				if (this.direction == Character.Direction.DOWN) {
					this.positionY = (int) a.getBoundary().getMinY() - (this.height) - 1;
					this.jumping = false;
				}
			}

			// Colidiu em Baixo Somente
			if (this.positionY <= a.getBoundary().getMaxY()) {
				// && ((this.positionY + this.height) > this.getBoundary().getMaxY())) {
				if (this.direction == Character.Direction.UP)
					this.positionY = (int) a.getBoundary().getMaxY() + 1;
			}
		}
		return flag;
		// return a.getBoundary().intersects(this.getBoundary());
	}

	public Character fire(GraphicsContext gc) {
		Character bullet = null;
		if (this.direction == Character.Direction.RIGHT || this.direction == Character.Direction.DOWN
				|| this.direction == Character.Direction.UP) {
			bullet = this.getWeapons().get(weapon);
			bullet.setPositionX(this.positionX + this.width + 1);
			bullet.setPositionY(positionY);
			// bullet = new BulletTwo(this.positionX + this.width + 1, this.positionY, 6, 6,
			// 64, 42);
			// bullet = new BulletOne(this.positionX + this.width + 1, this.positionY, 6, 6,
			// 75, 47);
			bullet.setDirection(Character.Direction.RIGHT);
			bullet.render(bullet.getImages().get(bullet.imageIndex), gc);
			this.render(this.getImages().get(this.imageIndex), gc);
		}
		if (this.direction == Character.Direction.LEFT) {
			bullet = this.getWeapons().get(weapon);
			bullet.setPositionX(this.positionX - this.width - 1);
			bullet.setPositionY(positionY);
			// bullet = new BulletTwo(this.positionX - this.width - 1, this.positionY, 6, 6,
			// 64, 42);
			// bullet = new BulletOne(this.positionX - 75 - 1, this.positionY, 6, 6, 75,
			// 47);
			bullet.setDirection(Character.Direction.LEFT);
			System.out.println("-------------------------------------------> "
					+ bullet.getImages().get(bullet.imageIndex).getWidth());
			bullet.render(this.flip(bullet.getImages().get(bullet.imageIndex)), gc);
			this.render(this.flip(this.getImages().get(this.imageIndex)), gc);
		}
		return bullet;
	}

	public void changeWeapon() {
		if (this.weapon < (this.weapons.size() - 1)) {
			this.weapon += 1;
		} else {
			this.weapon = 0;
		}
	}

}