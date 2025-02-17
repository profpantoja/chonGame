package chon.group;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import chon.group.agent.Agent;
import chon.group.agent.AsteroidMovement;
import chon.group.agent.DroneMovement;
import chon.group.agent.HeroMovement;
import chon.group.agent.Shot;
import chon.group.enviroment.CollisionDetector;
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
    private final ArrayList<Environment> lifeIcons = new ArrayList<>(); // Lista para armazenar os lifeIcons

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage theStage) {
        try {
            ArrayList<Agent> agents = new ArrayList<>();


            AsteroidMovement asteroid = new AsteroidMovement(920, 420, 85, 55, "/images/agent/Asteroid/asteroide.gif", false, 0);
            agents.add(asteroid); 

            HeroMovement spaceship = new HeroMovement(70, 410, 90, 42, "/images/agent/Spaceship/spaceship_parada ou dando ré1.gif", true, 5);
            agents.add(spaceship); 

            DroneMovement drone1 = new DroneMovement(920, 110, 74, 54, "/images/agent/enemys_space/enemy_drone1.png", false, 0, spaceship);
            agents.add(drone1);

            DroneMovement drone2 = new DroneMovement(920, 540, 74, 54, "/images/agent/enemys_space/enemy_drone1.png", false, 0, spaceship);
            agents.add(drone2);

            if (Agent.numberProtagonist == 1) {
                StackPane root = new StackPane();
                Scene scene = new Scene(root, 1180, 780);
                theStage.setTitle("Chon: The Learning Game");
                theStage.setScene(scene);
                Canvas canvas = new Canvas(1180, 780);

                root.getChildren().add(canvas);
                theStage.show();

                Environment atmosphere = new Environment(0, 0, 1180, 780, "/images/environment/background space.png", agents, canvas.getGraphicsContext2D());

                // Inicializa a lista de lifeIcons com 3 ícones
                for (int i = 0; i < 3; i++) {
                    Environment lifeIcon = new Environment(964 + (i * (56 + 10)), 638, 56, 47, "/images/agent/Spaceship/Life_icon.png", agents, canvas.getGraphicsContext2D());
                    lifeIcons.add(lifeIcon);
                }

                startAnimation(canvas, scene, atmosphere, agents);
                theStage.show();
            } else {
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

    // Método para gerar valores de posições diferentes para spawnar os inimigos
    private int generateRandomPosition(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min + 1) + min;
    }

    public void startAnimation(Canvas canvas, Scene scene, Environment atmosphere, ArrayList<Agent> agents) {

        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            public void handle(KeyEvent e) {
                String code = e.getCode().toString();
                input.clear();
                input.add(code);
    
                if (code.equals("SPACE")) { // Dispara tiro
                    Agent protagonist = atmosphere.getProtagonist();
                    if (protagonist != null) {
                        int shotX = protagonist.getPositionX() + protagonist.getWidth();
                        int shotY = protagonist.getPositionY() + protagonist.getHeight() / 2;
                        Shot shot = new Shot(shotX, shotY, 10, "/images/agent/shot.gif"); // Cria um novo tiro
                        atmosphere.addShot(shot);
                    }
                }
            }
        });
    
        scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
            public void handle(KeyEvent e) {
                String code = e.getCode().toString();
                input.remove(code);
            }
        });
    
        new AnimationTimer() {
            @Override
            public void handle(long arg0) {
                atmosphere.setGc(canvas.getGraphicsContext2D());
    
                if (!input.isEmpty()) {
                    atmosphere.getProtagonist().move(input.get(0));
                    if (atmosphere.limitsApprove()) {
                        atmosphere.drawAgents(agents);
                    }
                }
                atmosphere.clearRect();
                atmosphere.drawBackground();
    
                // Verifica colisões e movimento dos agentes
                if (agents.size() > 1) {
  
                    if (atmosphere.getAgents().get(0) instanceof AsteroidMovement) {
                        AsteroidMovement asteroid = (AsteroidMovement) atmosphere.getAgents().get(0); // Cast seguro
                        HeroMovement spaceship = (HeroMovement) atmosphere.getAgents().get(1); // A nave espacial
    
                        // Verifica colisão entre asteroide e nave
                        if (CollisionDetector.checkCollision(asteroid, spaceship)) {
                            System.out.println("Colisão detectada entre o asteroide e a espaçonave!");
                            spaceship.desacrease_life(1); // Corrigido o nome do método
                            if (spaceship.getLife() <= 0) {
                                spaceship.startExplosion(); // Inicia a explosão da nave
                                System.out.println("A nave foi destruída!");
    
                                if (!lifeIcons.isEmpty()) {
                                    lifeIcons.remove(lifeIcons.size() - 1);
                                }
                            }
                            asteroid.setAlive(false); 
                            asteroid.startExplosion();
    
                            // Gera um novo asteroide em uma posição aleatória
                            int randomX = generateRandomPosition(911, 1071);
                            int randomY = generateRandomPosition(51, 570);
                            Agent newAsteroid = new AsteroidMovement(randomX, randomY, 85, 55, "/images/agent/Asteroid/asteroide.gif", false, 0);
                            atmosphere.getAgents().set(0, newAsteroid); 
                        }
    
                        // Verifica se o asteroide passou de x: 0 e reaparece em uma nova posição
                        if (asteroid.isAlive() && asteroid.getPositionX() < 0) {
                            int randomX = generateRandomPosition(911, 1071);
                            int randomY = generateRandomPosition(51, 570);
                            asteroid.setPositionX(randomX);
                            asteroid.setPositionY(randomY);
                        }
    
                        if (asteroid.isAlive()) {
                            asteroid.move(); 
                        }
    
                        // Move os drones para seguir a nave e verificar colisões
                        for (int i = 2; i < agents.size(); i++) {
                            DroneMovement drone = (DroneMovement) atmosphere.getAgents().get(i);
                        
                            // Verifica se o drone está à direita ou à esquerda da nave
                            /*if (drone.getPositionX() > spaceship.getPositionX()) {
                                // Drone à direita da nave, espelhe a imagem
                                drone.setImage("/images/agent/enemys_space/enemy_drone.gif");
                            } else {
                                // Drone à esquerda da nave, usa o GIF padrão
                                drone.setImage("/images/agent/enemys_space/enemy_drone_flip.gif");
                            }/* */
                        
                            if (CollisionDetector.checkCollision(spaceship, drone)) {
                                System.out.println("Colisão detectada entre a nave e o drone!");
                                spaceship.desacrease_life(1); 
                                if (spaceship.getLife() <= 0) {
                                    spaceship.startExplosion();
                                    System.out.println("A nave foi destruída!");
                                }
                                drone.setAlive(false); 
                                drone.startExplosion();
                        
                                // Gera um novo drone em uma posição aleatória
                                int randomX = generateRandomPosition(911, 1071);
                                int randomY = generateRandomPosition(51, 570);
                                Agent newDrone = new DroneMovement(randomX, randomY, 74, 54, "/images/agent/enemys_space/enemy_drone.gif", false, 0, spaceship);
                                atmosphere.getAgents().set(i, newDrone); 
                            }
                        
                            if (drone.isAlive()) {
                                drone.move(); 
                            }
                        }
                    }
                }
    
                // Atualiza e redesenha os agentes
                for (Iterator<Agent> iterator = agents.iterator(); iterator.hasNext();) {
                    Agent agent = iterator.next();
                    if (!agent.isAlive()) {
                        iterator.remove();
                    }
                }
    
                // Move os tiros e verifica colisões
                for (Iterator<Shot> shotIterator = atmosphere.getShots().iterator(); shotIterator.hasNext();) {
                    Shot shot = shotIterator.next();
                    if (shot.isAlive()) {
                        shot.move(); 
                
                        // Verifica se o tiro saiu da tela
                        if (shot.getPositionX() > atmosphere.getWidth()) {
                            shot.setAlive(false);
                        }
                
                        // Verifica colisões entre o tiro e os asteroides e drones
                        for (int i = 0; i < agents.size(); i++) {
                            Agent enemy = agents.get(i);
                
                            if (enemy.isAlive() && atmosphere.checkCollisionShot(shot, enemy)) {
                                shot.setAlive(false); // O tiro desaparece ao atingir o inimigo
                
                                if (enemy instanceof AsteroidMovement) {
                                    System.out.println("Tiro atingiu o asteroide!");
                
                                    enemy.setAlive(false);
                                    enemy.startExplosion();
                
                                    // Cria um novo asteroide na tela
                                    int randomX = generateRandomPosition(911, 1071);
                                    int randomY = generateRandomPosition(51, 570);
                                    Agent newAsteroid = new AsteroidMovement(randomX, randomY, 85, 55, "/images/agent/Asteroid/asteroide.gif", false, 0);
                                    agents.set(i, newAsteroid);
                                } 
                
                                if (enemy instanceof DroneMovement) {
                                    System.out.println("Tiro atingiu o drone!");
                
                                    enemy.setAlive(false);
                                    enemy.startExplosion();
                
                                    // Cria um novo drone na tela
                                    int randomX = generateRandomPosition(911, 1071);
                                    int randomY = generateRandomPosition(51, 570);
                                    Agent newDrone = new DroneMovement(randomX, randomY, 74, 54, "/images/agent/enemys_space/enemy_drone.gif", false, 2, atmosphere.getProtagonist());
                                    agents.set(i, newDrone);
                                }
                            }
                        }
                    } else {
                        shotIterator.remove(); // Remove o tiro
                    }
                }                
    
           
                atmosphere.drawAgents(agents);
                for (Environment lifeIcon : lifeIcons) {
                    lifeIcon.drawLifeIcon();
                }
            }
        }.start();
    }
}
