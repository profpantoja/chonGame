package chon.group.launcher;

import java.util.ArrayList;

import chon.group.agent.Agent;
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

	// Limites de movimento dos agentes
	private static final int UPPER_LIMIT = 100; // Limite superior
	private static final int LOWER_LIMIT = 680; // Limite inferior
	private static final int LEFT_LIMIT = 0; // Limite esquerdo
	private static final int RIGHT_LIMIT = 1080; // Limite direito

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage theStage) {
		try {

			ArrayList<Agent> agents = new ArrayList<>();
			
			Agent agentA = new Agent(920, 420, 110, 100, "/images/agent/Asteroid/asteroide1.png", false);
			agents.add(agentA);
			
			Agent agentB = new Agent(70, 410, 120, 100 , "/images/agent/Spaceship/spaceship_andando1.png", true );
			agents.add(agentB);

			if(Agent.numberProtagonist == 1){

				StackPane root = new StackPane();
				Scene scene = new Scene(root, 1180, 780);
				theStage.setTitle("Chon: The Learning Game");
				theStage.setScene(scene);
				Canvas canvas = new Canvas(1180, 780);

				System.out.println("Working Directory = " + System.getProperty("user.dir"));

				root.getChildren().add(canvas);
				theStage.show();
				
				Environment atmosphere = new Environment(0, 0, 1180, 780, "/images/environment/background space.png", agents, canvas.getGraphicsContext2D());
				
				startAnimation(canvas, scene, atmosphere, agents);
	
				theStage.show();
			}else{
				throw new IllegalArgumentException("The number of protagonists didn't reach the minimum number or exceeded it");
			}

		} catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            Platform.exit();
        } catch (Exception e) {
            e.printStackTrace();
            Platform.exit();
        }
	}

	public void startAnimation(Canvas canvas, Scene scene, Environment atmosphere, ArrayList<Agent> agents) {

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
				if (!input.isEmpty()) {
					atmosphere.clearRect();
					atmosphere.drawBackground();					
					
					atmosphere.getProtagonist().move(input);
					
					for (Agent agent : agents) {
						// Limite superior
						if(agent.getPositionY() < UPPER_LIMIT) {
							agent.setPositionY(UPPER_LIMIT);
						}
						// Limite inferior
						if(agent.getPositionY() > LOWER_LIMIT) {
							agent.setPositionY(LOWER_LIMIT);
						}
						// Limite esquerdo
						if(agent.getPositionX() < LEFT_LIMIT) {
							agent.setPositionX(LEFT_LIMIT);
						}
						// Limite direito
						if(agent.getPositionX() > RIGHT_LIMIT) {
							agent.setPositionX(RIGHT_LIMIT);
						}
					}
					
					if(atmosphere.limitsApprove()){
						atmosphere.drawAgents(agents);				
					}
				}
			}
	
			 /*static WritableImage flip(Image image) {
		ImageView iv = new ImageView(image);
		iv.setScaleX(-1);
		SnapshotParameters params = new SnapshotParameters();
		params.setFill(Color.TRANSPARENT);
		return iv.snapshot(params, null);
	}*/	
		}.start();
		
	}
}
