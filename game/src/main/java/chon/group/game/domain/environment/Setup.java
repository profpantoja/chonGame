package chon.group.game.domain.environment;

import chon.group.game.domain.agent.Agent;
import chon.group.game.domain.agent.Fireball;
import chon.group.game.domain.agent.Weapon;
import javafx.scene.image.Image;

public class Setup {

    private Setup() {
    }

    /**
     * Cria e retorna uma instância do ambiente de jogo com todos os objetos iniciais.
     * @return um objeto Environment totalmente configurado.
     */
    public static Environment createEnvironment() {
        
        int worldWidth = 4096;
        int worldHeight = 768;

        Environment environment = new Environment(0, 0, worldWidth, worldHeight, "/images/environment/castle.png");
        
        // adding protagonist agent
        Agent chonBota = new Agent(100, 390, 90, 65, 5, 1000, "/images/agents/chonBota.png", false);
        Weapon fireball = new Fireball(400, 390, 0, 0, 3, 0, "", false); 
        chonBota.setWeapon(fireball);
        environment.setProtagonist(chonBota);

        // adding enemy agents
        Agent chonBot = new Agent(920, 440, 90, 65, 1, 500, "/images/agents/chonBot.png", true);
        environment.getAgents().add(chonBot);

        environment.createGround(worldWidth, 238, "/images/environment/brick.png");
        
        Collision platform1 = new Collision(350, 550, 192, 64, "/images/environment/brick.png", false, false, 0, false, false, false);
        environment.getCollisions().add(platform1);

        Collision platform2 = new Collision(600, 450, 192, 64, "/images/environment/brick.png", false, true, 0, false, false, true);
        environment.getCollisions().add(platform2);

        Collision wall1 = new Collision(1000, 300, 64, 256, "/images/environment/brick.png", false, true, 0, false, true, false);
        environment.getCollisions().add(wall1);

        Collision breakableBlock = new Collision(1200, 400, 64, 64, "/images/environment/brick.png", true, false, 20, false, false, false);
        environment.getCollisions().add(breakableBlock);

        Collision passableBlock = new Collision(1500, 280, 64, 64, "/images/environment/brick.png", false, false, 0, true, false, false);
        environment.getCollisions().add(passableBlock);
        
        environment.setPauseImage("");             
        environment.setGameOverImage("/images/environment/gameover.png");
        environment.setWinImage("/images/environment/gameover.png");

        return environment;
    }

    /**
     * Carrega uma imagem a partir de um caminho no classpath.
     * @param path O caminho para o recurso da imagem.
     * @return um objeto Image.
     */
    public static Image loadImage(String path) {
        try {
            return new Image(Setup.class.getResource(path).toExternalForm());
        } catch (Exception e) {
            System.err.println("Falha ao carregar imagem: " + path);
            return null; // Retorna nulo se a imagem não for encontrada
        }
        
    }    
}