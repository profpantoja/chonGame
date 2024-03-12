package engine;

import java.applet.Applet;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.Timer;

public class Engine extends Applet implements ActionListener, KeyListener {

	private static final long serialVersionUID = 1L;

	private Environment env = new Environment();
	Image image;

	Thread animator;
	int estado_anterior = 0;
	int kcode;
	int tela_largura, tela_altura;
	Timer time;

	public void init() {

		setSize(this.env.getLargura(), this.env.getAltura());
		addKeyListener(this);
		time = new Timer(40, this);
		time.start();
		
	}

	public void stop() {
		time = null;
	}

	public void keyPressed(KeyEvent k) {
		estado_anterior = kcode;
		//kcode = k.getKeyCode();
		//if (kcode == KeyEvent.VK_LEFT) {
			//this.env.getGost().direction = Agent.Direction.LEFT;
			//this.env.getGost().gunDirection = Agent.Direction.LEFT;
			//this.env.getGost().moveLine(2);
			//this.env.getGost().setP1(this.env.getGost().getP1() - this.env.getGost().getStep());
		//}
		//if (kcode == KeyEvent.VK_RIGHT) {
			//this.env.getGost().direction = Agent.Direction.RIGHT;
			//this.env.getGost().gunDirection = Agent.Direction.RIGHT;
			//this.env.getGost().moveLine(3);
			//this.env.getGost().setP1(this.env.getGost().getP1() + this.env.getGost().getStep());
//}
		//if (kcode == KeyEvent.VK_DOWN) {
			//this.env.getGost().direction = Agent.Direction.DOWN;
			//this.env.getGost().moveLine(1);
			// this.env.getGost().setP2(this.env.getGost().getP2() +
			// this.env.getGost().getStep());
		//}
		//if (kcode == KeyEvent.VK_UP) {
			//this.env.getGost().direction = Agent.Direction.UP;
			//if (!this.env.getGost().jump)
				//this.env.getGost().jumpDone = false;
				//this.env.getGost().moveLine(4);
		//}
		//if (kcode == KeyEvent.VK_SPACE) {
			//this.env.getGost().shoot(this.env.getGost().gunDirection);
		//}
		//if (kcode == KeyEvent.VK_F) {
			//if (this.env.getGost().powerAtual != this.env.getGost().power.size() - 1) {
				//this.env.getGost().powerAtual++;
		//	} else {
				//this.env.getGost().powerAtual = 0;
		//	}
		//}
		//if (kcode == KeyEvent.VK_G) {
			//if (this.env.getGost().powerAtual != 0) {
			//	this.env.getGost().powerAtual--;
			//} else {
			//	this.env.getGost().powerAtual = this.env.getGost().power.size() - 1;
			//}
		//}
	}

	public void keyReleased(KeyEvent k) {}
	public void keyTyped(KeyEvent k) {}

	@SuppressWarnings("rawtypes")
	public void paint(Graphics g) {
		super.paint(g);
		//this.env.draw(g, this);
		
		//.............Box image............
		
		ArrayList boxes = this.env.obstacles;
		for (int w = 0; w < boxes.size(); w++) {
			Agent m = (Agent) boxes.get(w);
			//m.draw(g, this);
		}
		
		//..................................
		
		//this.env.getGost().draw(g, this);
		if (!this.env.getEnemy().isVisible())
			this.env.getEnemy().die();
		//else
			//this.env.getEnemy().draw(g, this);

		//if (this.env.getGost().direction == Agent.Direction.UP && this.env.getGost().jump == false) {
		//	this.env.getGost().jump = true;
		//	animator = new Thread(this.env.getGost());
		//	animator.start();
		//}

		//g.drawString("Posi��o de Y do Gost � " + (this.env.getGost().getP2() + this.env.getGost().getHeight()),
		//		this.env.getGost().getP1() - this.env.getGost().getA_final_image(), this.env.getGost().getP2());
		
		//ArrayList bullets = this.env.getGost().bullets;
		//for (int w = 0; w < bullets.size(); w++) {
		//	Agent m = (Agent) bullets.get(w);
		//	m.moveLine();
		//	//m.draw(g, this);
		//}

	}

	public void actionPerformed(ActionEvent a) {

		//@SuppressWarnings("rawtypes")
		//ArrayList bullets = this.env.getGost().bullets;
		//for (int w = 0; w < bullets.size(); w++) {
		//	Agent m = (Agent) bullets.get(w);
		//	if (m.isVisible()) {
		//		m.move();
		//		this.env.BulletColision(m);
		//	}else {
		//		bullets.remove(w);
		//	}
		//}
		this.env.colision();
		if(!this.env.colision()){
		//	this.env.getGost().down();
		}
		
		repaint();
	}
}