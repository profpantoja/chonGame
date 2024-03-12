package engine;

import java.util.ArrayList;

import application.Character;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Environment {

	private int pX = 0, pY = 0;

	private Image image;
	public boolean colision = false;
	private int largura = 1250;
	private int altura = 600;
	public Colision co = new Colision();
	//private Hero gost = new Hero(200, 305, 3, 3);
	//private Box box = new Box(300, 350);
	//private BulletTwo bulletTwo = new BulletTwo(0, 0);
	//private BulletOne bulletOne = new BulletOne(0, 0);
	private Enemy enemy = new Enemy(700, 490);
	public ArrayList<Agent> agents = new ArrayList<>();
	public ArrayList<Agent> obstacles = new ArrayList<>();

	public boolean colision() {
		/*
		if (this.co.listColision(this.getGost(), this.agents) || this.co.listColision(this.getGost(), this.obstacles)) {
//			this.colision = true;
//			System.out.println("colision");
			return true;
		} else {
//			System.out.println("No! colision");
			return false;
		}
		*/
		return false;
	}

	public boolean BulletColision(Agent bullet) {

		if (this.co.listColision(bullet, this.agents)) {
			this.colision = true;
			return true;
		} else {
			return false;
		}
	}

	/*
	public BulletTwo getBullet() {
		return bulletTwo;
	}

	public void setBullet(BulletTwo bulletTwo) {
		this.bulletTwo = bulletTwo;
	}
	*/

	@SuppressWarnings("unchecked")
	public Environment() {
		super();
		this.load();

		// ...........Lista de Bullets...........................
		/*
		this.bullet.setP1(this.gost.getP1() + this.bullet.getWidth());
		this.bullet.setP2(this.gost.getP2() + (this.gost.getHeight() / 2));
		this.gost.power.add(this.bullet);
		this.bullet2.setP1(this.gost.getP1() + this.bullet2.getWidth());
		this.bullet2.setP2(this.gost.getP2() + (this.gost.getHeight() / 2));
		this.gost.power.add(this.bullet2);
		*/
		// ...........Lista de Enemy.............................

		agents.add(enemy);

		// ...........Lista de Barreiras.........................

//		Agent abs = new Agent(0, this.largura, 550, this.altura + 4, 0, 554, this.largura, this.largura, this.altura);
//		abs.setVisible(true);


//		obstacles.add(abs);

		/*
		agents.add(box1);
		agents.add(box2);
		agents.add(box3);
		agents.add(box4);
		agents.add(box5);
		*/
		// ......................................................
	}

	public Enemy getEnemy() {
		return enemy;
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public int getLargura() {
		return largura;
	}

	public void setLargura(int largura) {
		this.largura = largura;
	}

	public int getAltura() {
		return altura;
	}

	public void setAltura(int altura) {
		this.altura = altura;
	}

	// public void draw(GraphicsContext g, Engine eng) {
	public void draw(GraphicsContext g) {
		// g.drawImage(this.image, this.pX, this.pY, this.largura, this.altura, eng);
		g.drawImage(this.image, 0, 0);
	}

	public void load() {
		try {
			//this.image = new Image("file:images/environment3.jpg");
			this.image = new Image(getClass().getResource("/images/environment3.jpg").toExternalForm());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// ..............................................

}
