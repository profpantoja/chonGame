package chon.group;

import java.util.ArrayList;

import chon.group.agent.Agent;
import chon.group.enviroment.Environment;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.event.EventHandler;


public class Engine extends Application {

	private ArrayList<String> input = new ArrayList<String>();

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage theStage) {
		try {

			// ajuste no hoisting do código, para antes de qualquer coisa , saber se há a quantidade ideal de protagonistas.

			ArrayList<Agent> agents = new ArrayList<Agent>();
			
			Agent agentA = new Agent(920, 440, 90, 65, "/images/agent/chonBot.png", true);
			agents.add(agentA);
			
			Agent agentB = new Agent(400, 390, 90, 65, "/images/agent/chonBota.png", false);
			agents.add(agentB);

			if(Agent.qtdProtagonist == 1){

				StackPane root = new StackPane();
				Scene scene = new Scene(root, 1280, 780);
				theStage.setTitle("Chon: The Learning Game");
				theStage.setScene(scene);
				Canvas canvas = new Canvas(1280, 780);

				System.out.println("Working Directory = " + System.getProperty("user.dir"));

				root.getChildren().add(canvas);
				theStage.show();
				
				Environment ambiente = new Environment(0, 0, 1280, 780, "/images/environment/castle.png", agents, canvas.getGraphicsContext2D());
				
				startAnimation(canvas, scene, ambiente, agents);
	
				theStage.show();
			}else{
				throw new IllegalArgumentException("A quantidade de protagonista não alcançou sua quantidade mínima ou à excedeu (1)");
			}
		} catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            Platform.exit();
        } catch (Exception e) {
            e.printStackTrace();
            Platform.exit();
        }
	}

	static WritableImage flip(Image image) {
		ImageView iv = new ImageView(image);
		iv.setScaleX(-1);
		SnapshotParameters params = new SnapshotParameters();
		params.setFill(Color.TRANSPARENT);
		return iv.snapshot(params, null);
	}
	
	public void startAnimation(Canvas canvas, Scene scene, Environment ambiente, ArrayList<Agent> agents) {

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
				ambiente.setGc(canvas.getGraphicsContext2D());
				if (!input.isEmpty()) {
					ambiente.clearRect();
					ambiente.drawBackground();
					
					//Pega o protagonista do ambiente e movimenta o mesmo, após isso, printa todos os agentes do ambiente.
					
					

					ambiente.getProtagonist().movimentar(input);
					if(ambiente.limitsApprove()){
						ambiente.drawAgents(agents);				

					}
				}
			}
		
		}.start();
		
	}
}


//NOTAS:

//Reparei que ele fica um pouco negrito os status de cima quando começamos a andar com o personagem, talvez seja o caso dos eventos estarem sendo disparados duas vezes, por algum motivo...
