package com.example;

import java.util.ArrayList;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;


public class Engine extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage theStage) {
        try {
            StackPane root = new StackPane();
            Scene scene = new Scene(root, 1280, 780);
            theStage.setTitle("Chon: The Learning Game");
            theStage.setScene(scene);

            int wCanvas = 1280;
            int hCanvas = 780;
            Canvas canvas = new Canvas(wCanvas, hCanvas);

            GraphicsContext gc = canvas.getGraphicsContext2D();

            System.out.println("Working Directory = " + System.getProperty("user.dir"));
            Image background = new Image(getClass().getResource("/images/environment/castle.png").toExternalForm());
            Image chonBota = new Image(getClass().getResource("/images/agents/chonBota.png").toExternalForm());
            Image chonBot = new Image(getClass().getResource("/images/agents/chonBot.png").toExternalForm());

            int wImageBot = 65;
            int hImageBot = 90;
            gc.drawImage(background, 0, 0, 1280, 780);
            gc.drawImage(chonBota, 400, 390, wImageBot, hImageBot);
            gc.drawImage(chonBot, 920, 440, wImageBot, hImageBot);

            Font theFont = Font.font("Verdana", FontWeight.BOLD, 14);
            gc.setFont(theFont);

            gc.setFill(Color.BLACK);
            gc.setStroke(Color.BLACK);
            gc.setLineWidth(2);
            gc.fillText("Hey, I'm ChonBota...", 365, 380);

            root.getChildren().add(canvas);
            theStage.show();

            ArrayList<String> input = new ArrayList<>();
            ArrayList<Projectile> projectiles = new ArrayList<>();

            scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
				public void handle(KeyEvent e) {
					String code = e.getCode().toString();
					if (!input.contains(code)) {
						input.add(code); // Adiciona a tecla pressionada, se ainda não estiver na lista
					}
				}
			});
			
			scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
				public void handle(KeyEvent e) {
					String code = e.getCode().toString();
					input.remove(code); // Remove a tecla quando liberada
				}
			});
			

			boolean isSprinting = false; // Variável para rastrear se o jogador está sprintando

			new AnimationTimer() {
				int xImage = 400;
				int yImage = 390;
			
				int xEnemy = 920;
				int yEnemy = 440;
			
				long lastShotTime = 0; // Tempo do último disparo
				final long SHOT_COOLDOWN = 200_000_000; // 200ms em nanosegundos
			
				long lastSprintTime = 0; // Tempo do último sprint
				final long SPRINT_TIME = 500_000_000; // Duração do sprint (1s)
				final long SPRINT_COOLDOWN = 2_000_000_000; // Cooldown do sprint (2s)
				int movespeed = 5;

				boolean isSprinting = false;
				boolean playerFacingRight = true;
				boolean enemyFacingRight = true;
			
				@Override
				public void handle(long now) {
					// Limpa e redesenha o fundo do canvas
					gc.clearRect(0, 0, 1280, 780);
					gc.drawImage(background, 0, 0, canvas.getWidth(), canvas.getHeight());
			
					// Controle de sprint
					if (isSprinting) {
						if (now - lastSprintTime >= SPRINT_TIME) {
							isSprinting = false; // Termina o sprint após o tempo
							movespeed = 5; // Retorna à velocidade normal
						}
					}
			
					// Verifica inputs e atualiza a posição do personagem
					if (!input.isEmpty()) {
						if (input.contains("LEFT")) {
							if (xImage - 1 > 0) {
								xImage -= movespeed;
								playerFacingRight = false;
							} else {
								xImage = 0;
							}
						}
						if (input.contains("RIGHT")) {
							if (xImage + 1 < 828) {
								xImage += movespeed;
								playerFacingRight = true;
							} else {
								xImage = 828;
							}
						}
						if (input.contains("UP")) {
							if (yImage - 1 > 268)
								yImage -= movespeed;
							else
								yImage = 268;
						}
						if (input.contains("DOWN")) {
							if ((yImage + hImageBot) + 1 < hCanvas)
								yImage += movespeed;
							else
								yImage = (hCanvas - hImageBot);
						}
						if (input.contains("SPACE")) {
							if (now - lastShotTime >= SHOT_COOLDOWN) {
								if (playerFacingRight) {
									projectiles.add(new Projectile(xImage + 30, yImage, playerFacingRight));
								} else {
									projectiles.add(new Projectile(xImage - 30, yImage, playerFacingRight));
								}
								lastShotTime = now;
							}
						}
						if (input.contains("C")) {
							if (!isSprinting && now - lastSprintTime >= SPRINT_COOLDOWN) {
								isSprinting = true;
								movespeed = 10; // Aumenta a velocidade
								lastSprintTime = now; // Atualiza o tempo do início do sprint
							}
						}
					}
			
					// Renderiza o personagem principal
					gc.save();
					if (!playerFacingRight) {
						gc.translate(xImage + wImageBot, 0);
						gc.scale(-1, 1);
						gc.drawImage(chonBota, 0, yImage, wImageBot, hImageBot);
					} else {
						gc.drawImage(chonBota, xImage, yImage, wImageBot, hImageBot);
					}
					gc.restore();
			
					// Atualiza e renderiza os projéteis
					for (int i = 0; i < projectiles.size(); i++) {
						Projectile p = projectiles.get(i);
						p.update();
						p.render(gc);
			
						if (p.isOffScreenRight(wCanvas) || p.isOffScreenLeft()) {
							projectiles.remove(i);
							i--;
						}
					}
			
					// Atualiza posição e direção do inimigo
					if (xEnemy != xImage) {
						if (xEnemy < xImage) {
							xEnemy += 2;
							enemyFacingRight = false;
						} else if (xEnemy > xImage) {
							xEnemy -= 2;
							enemyFacingRight = true;
						}
					}
					if (yEnemy != yImage) {
						if (yEnemy < yImage) {
							yEnemy += 2;
						} else if (yEnemy > yImage) {
							yEnemy -= 2;
						}
					}
			
					// Renderiza o inimigo
					gc.save();
					if (!enemyFacingRight) {
						gc.translate(xEnemy + 65, 0);
						gc.scale(-1, 1);
						gc.drawImage(chonBot, 0, yEnemy, 65, 90);
					} else {
						gc.drawImage(chonBot, xEnemy, yEnemy, 65, 90);
					}
					gc.restore();
			
					// Renderiza a HUD
					printStatusPanel(gc, xImage, yImage);
				}
			}.start();
			
			
			

            theStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void printStatusPanel(GraphicsContext gc, int xImage, int yImage) {
        gc.setFill(Color.ROSYBROWN);
        gc.fillRoundRect(100, 100, 185, 70, 50, 50);
        gc.setFill(Color.NAVY);
        gc.fillRoundRect(900, 100, 270, 70, 50, 50);
        gc.setFill(Color.BLACK);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(4);
        Font theFont = Font.font("Verdana", FontWeight.BOLD, 14);
        gc.setFont(theFont);
        gc.fillText("X: " + xImage, xImage + 10, yImage - 25);
        gc.fillText("Y: " + yImage, xImage + 10, yImage - 10);
        gc.setFill(Color.WHITE);
        gc.fillText("Life: 100/100", 140, 140);
        gc.fillText("Boss Health: 100/100", 950, 140);
    }
}

// Classe Projectile
class Projectile {
    double x, y;
    double width = 80, height = 80;
	boolean movingRight;

    public Projectile(double x, double y, boolean movingRight) {
        this.x = x;
        this.y = y;
		this.movingRight = movingRight;
    }

    public void update() {
		if(movingRight){
			x += 10; // Velocidade do projétil
		} else {
			x -= 10;
		}

    }

    public void render(GraphicsContext gc) {
		Image bomb = new Image(getClass().getResource("/images/agents/Bomb_C_01.png").toExternalForm());
		gc.drawImage(bomb, x, y, width, height);
    }

    public boolean isOffScreenRight(double screenWidth) {
        return x > screenWidth;
    }
	public boolean isOffScreenLeft(){
		return x + 80 < 0;
	}
}
