package chon.group.game.domain.environment;

import chon.group.game.domain.agent.Agent;
import chon.group.game.domain.agent.Fireball;
import chon.group.game.domain.agent.Weapon;
import javafx.scene.image.Image;

// Tornando a classe final e com construtor privado para caracterizá-la como utilitária.
public final class Setup {

    private Setup() {
        // Construtor privado para impedir a instanciação.
    }

    /**
     * Cria e retorna uma instância do ambiente de jogo com todos os objetos iniciais.
     * @return um objeto Environment totalmente configurado.
     */
    public static Environment createEnvironment() {
        // Dimensões do mundo e da janela
        int worldWidth = 4096;
        int worldHeight = 768;

        Environment environment = new Environment(0, 0, worldWidth, worldHeight, "/images/environment/castle.png");
        
        // Criando o protagonista
        Agent chonBota = new Agent(100, 390, 90, 65, 5, 1000, "/images/agents/chonBota.png", false);
        Weapon fireball = new Fireball(400, 390, 0, 0, 3, 0, "", false); // Posição da arma é relativa ao agente, não precisa ser fixo aqui.
        chonBota.setWeapon(fireball);
        environment.setProtagonist(chonBota);

        // Criando inimigos
        Agent chonBot = new Agent(920, 440, 90, 65, 1, 500, "/images/agents/chonBot.png", true);
        environment.getAgents().add(chonBot);
        
        // Configurando imagens de estado do jogo
        environment.setPauseImage("/images/environment/pause.png");             
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