package chon.group;

import java.util.ArrayList;
import java.util.Iterator;

import chon.group.agent.Agent;
import chon.group.agent.HeroMovement;
import chon.group.enviroment.Environment;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Engine extends Application {

	private final ArrayList<String> input = new ArrayList<>();

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage theStage) {
		try {

			ArrayList<Agent> agents = new ArrayList<>();

			Agent agentA = new Agent(920, 420, 134, 80, "/images/agent/Asteroid/asteroide1.png", false, 0);
			agents.add(agentA);

			Agent agentB = new HeroMovement(70, 410, 120, 100, "/images/agent/Spaceship/spaceship_andando1.png", true, 500000);
			agents.add(agentB);

			if (Agent.numberProtagonist == 1) {

				StackPane root = new StackPane();
				Scene scene = new Scene(root, 960, 870);
				theStage.setTitle("Chon: The Learning Game");
				theStage.setScene(scene);
				Canvas canvas = new Canvas(960, 870);

				System.out.println("Working Directory = " + System.getProperty("user.dir"));

				root.getChildren().add(canvas);
				theStage.show();

				Environment atmosphere = new Environment(0, 0, 960, 870, "/images/environment/background space.png",
						agents, canvas.getGraphicsContext2D());

				Environment lifeIcon = new Environment(964, 638, 56, 47, "/images/agent/Spaceship/Life_icon.png",
						agents, canvas.getGraphicsContext2D());

				startAnimation(canvas, scene, atmosphere, lifeIcon, agents);

				theStage.show();
			} else {
				throw new IllegalArgumentException(
						"The number of protagonists didn't reach the minimum number or exceeded it");
			}

		} catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
			Platform.exit();
		} catch (Exception e) {
			e.printStackTrace();
			Platform.exit();
		}
	}

	public void startAnimation(Canvas canvas, Scene scene, Environment atmosphere, Environment lifeIcon,
			ArrayList<Agent> agents) {

		scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent e) {
				String code = e.getCode().toString();
				input.clear();

				System.out.println("Pressed: " + code);
				input.add(code);
			}
		});

		scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent e) {
				String code = e.getCode().toString();
				System.out.println("Released: " + code);
				input.remove(code);
			}
		});

		new AnimationTimer() {

			@Override
			public void handle(long arg0) {
				atmosphere.setGc(canvas.getGraphicsContext2D());

				if (agents.size() > 1)
					atmosphere.getAgents().get(0).move("LEFT");

				if (!input.isEmpty()) {
					atmosphere.getProtagonist().move(input.get(0));
					if (atmosphere.limitsApprove()) {
						atmosphere.drawAgents(agents);
					}
				}
				atmosphere.clearRect();
				atmosphere.drawBackground();

				if (atmosphere.limitsApprove()) {
					atmosphere.drawAgents(agents);
					for (int i = 0; i < 3; i++) {
						lifeIcon.setPositionX(964 + (i * (lifeIcon.getWidth() + 10)));
						lifeIcon.drawLifeIcon();
					}
				}

				if (agents.size() > 1) {
					Agent asteroid = atmosphere.getAgents().get(0); // O asteroide (primeiro agente da lista)
					Agent spaceship = atmosphere.getAgents().get(1); // A espaçonave (segundo agente da lista)
					// Verifica a colisão e inicia a animação de explosão
					if (atmosphere.checkCollision(asteroid, spaceship)) {
						System.out.println("Colisão detectada entre o asteroide e a espaçonave!");
						asteroid.setAlive(false);
						asteroid.startExplosion(); // Inicia a explosão do asteroide

						spaceship.desacrease_life(1); // Reduz a vida em 1
						System.out.println("A nave tem: " + spaceship.getLife() + " de vida");

						if (spaceship.getLife() <= 0) {
							spaceship.startExplosion(); // Inicia a explosão da nave ao chegar em 0 de vida
							System.out.println("A nave foi destruída!");
						}

						// Atualiza a animação da explosão
						asteroid.updateExplosion(); // Agora a explosão será controlada pelo tempo no método
						// updateExplosion()
					}

				} else {
					Agent spaceship = atmosphere.getAgents().get(0);
				}

				// Redesenha os agentes
				for (Iterator<Agent> iterator = agents.iterator(); iterator.hasNext();) {
					Agent agent = iterator.next();
					if (!agent.isAlive()) {
						iterator.remove();
					}

				}

				atmosphere.drawAgents(agents);
			}

		}.start();
	}
}
