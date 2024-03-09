package engine;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import java.util.ArrayList;
import java.util.Random;

public class Agent implements Runnable {

	// ...........................................
	public enum Direction {
		RIGHT, LEFT, DOWN, UP, STOPPED
	}

	private int a_ini_image = 0, a_final_image = 0;// ponto Y inicial e final da
													// entidade
	
	private int reset = 0; // largura final da entidade
	private int reset_col = 0; // altura final da entidade

	private Random rand = new Random();

	@SuppressWarnings("unused")
	private int y, x;

	@SuppressWarnings("rawtypes")
	public ArrayList bullets = new ArrayList();
	private boolean visible = true;

	private Image image;
	public Direction direction = Agent.Direction.RIGHT;
	public Direction gunDirection = Agent.Direction.RIGHT;

	public boolean jumpDone = false;
	public int limite = 0;

	private int jumpHeight = 50; // altura do pulo máximo da entidade
	public boolean jump = false; // condição se o pulo foi feito

	private int p1 = 0; // posição inicial X da entidade
	private int p2 = 0; // posição inicial Y da entidade

	private int width = 0;
	private int height = 0;

	private int l_ini_image = 0, l_final_image = 0; // ponto X inicial e final
													// da entidade
	private int step = 4;

	public int powerAtual = 1; // n° do poder atual

	@SuppressWarnings("rawtypes")
	public ArrayList power = new ArrayList();

	public Agent(int xi, int xf, int yi, int yf, int p1, int p2, int reset, int width, int height) {

		this.a_final_image = yf; // altura final do Ghost
		this.a_ini_image = yi; // altura inicial do Ghost

		this.l_ini_image = xi; // largura inicial do Ghost
		this.l_final_image = xf; // largura final do Ghost

		this.p1 = p1; // ponto X inicial do Ghost
		this.p2 = p2; // ponro Y inicial do Ghost

		this.reset = reset; // largura final do Ghost

		this.width = width;
		this.height = height;

	}

	public int getStep() {
		return step;
	}

	public void setStep(int step) {
		this.step = step;
	}

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	// .............................

	public Direction getGunDirection() {
		return gunDirection;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public void setGunDirection(Direction gunDirection) {
		this.gunDirection = gunDirection;
	}

	// .............................
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

	// ............................................

	public int getP1() {
		return p1;
	}

	public void setP1(double d) {
		this.p1 = (int) d;
	}

	public int getP2() {
		return p2;
	}

	public void setP2(double p2) {
		this.p2 = (int) p2;
	}

	public int getL_ini_image() {
		return l_ini_image;
	}

	public void setL_ini_image(int l_ini_image) {
		this.l_ini_image = l_ini_image;
	}

	public int getL_final_image() {
		return l_final_image;
	}

	public void setL_final_image(int l_final_image) {
		this.l_final_image = l_final_image;
	}

	public int getA_ini_image() {
		return a_ini_image;
	}

	public void setA_ini_image(int a_ini_image) {
		this.a_ini_image = a_ini_image;
	}

	public int getA_final_image() {
		return a_final_image;
	}

	public void setA_final_image(int a_final_image) {
		this.a_final_image = a_final_image;
	}

	public int getReset() {
		return reset;
	}

	public void setReset(int reset) {
		this.reset = reset;
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image; 
	}

	public int getReset_col() {
		return reset_col;
	}

	public void setReset_col(int reset_col) {
		this.reset_col = reset_col;
	}

	@SuppressWarnings("rawtypes")
	public ArrayList getBullet() {
		return bullets;
	}

	// .....................................

	public void draw(GraphicsContext g) {
		//Canvas canvas = new Canvas(150, 150);
		//GraphicsContext g2d = canvas.getGraphicsContext2D();
		//Graphics g2d = (Graphics) g;
		g.drawImage(this.image, this.p1, this.p2, this.height, this.width);
		
		//g2d.drawImage(this.image, this.p1, this.p2, this.p1 + this.width, this.p2 + this.height, this.l_ini_image,
		//		this.a_ini_image, this.l_final_image, this.a_final_image, eng);
	}

	public void moveColumn(int column) {
		if (this.a_final_image != this.reset_col) {

			this.l_ini_image = (this.width * column) - this.width;
			this.l_final_image = this.width * column;

			this.a_ini_image += this.height;
			this.a_final_image += this.height;
		} else {
			this.a_ini_image = 0;
			this.a_final_image = this.height;
		}
	}

	public void moveLine(int line) {
		if (this.l_final_image != this.reset) {
			this.a_ini_image = (this.height * line) - this.height;
			this.a_final_image = this.height * line;

			this.l_ini_image += this.width;
			this.l_final_image += this.width;
		} else {
			this.l_ini_image = 0;
			this.l_final_image = this.width;
		}

	}

	// .........................................

	public void shoot(Agent.Direction direction) {
		if(direction == Agent.Direction.RIGHT){
			Agent ag = (Agent) this.power.get(this.powerAtual);
			ag.setP1(this.p1 + this.width);
			ag.setP2(this.p2 + (this.getHeight() / 2));
			ag.fire(direction, this);
		}
		if(direction == Agent.Direction.LEFT){
			Agent ag = (Agent) this.power.get(this.powerAtual);
			ag.setP1(this.p1 - (this.width + 10));
			ag.setP2(this.p2 + (this.getHeight() / 2));
			ag.fire(direction, this);
		}
	}

	public void moveLine(){}
	
	public void move() {
	}

	public void fire(Agent.Direction dir, Agent ag) {
	}

	public void Random(int altura, int largura) {
		x = rand.nextInt(largura - 100);
		y = rand.nextInt(altura - 200);
	}

	public void jump() {
//		System.out.println("[Ghost]: To pulando maluco!!!");
		if (!jumpDone) {
			this.p2 -= this.step - 2;
		}
		if (this.p2 + this.height <= limite) {
			jumpDone = true;
		}
	}

	public void down() {
//		 System.out.println("dowm");
		if (this.p2 <= 510) { 
			this.direction = Agent.Direction.DOWN;
			this.p2 += this.step - 2;
		}
		if (this.p2 >= 510) {
			reset();
		}
	}

	public void reset() {
//		System.out.println("reset");
		this.jump = false;
		this.jumpDone = false;

	}

	public void run() {
		limite = this.p2 - this.jumpHeight;
		long sleep = 0;
		while (this.jump == true) {
			if (!this.jumpDone)
				jump();
			else
				down();
			sleep = 15;
			try {
				Thread.sleep(sleep);
			} catch (InterruptedException e) {
				System.out.println("interrupted");
			}
		}
		
	}
	
	public void die(){
		this.visible = false;
	}

}
