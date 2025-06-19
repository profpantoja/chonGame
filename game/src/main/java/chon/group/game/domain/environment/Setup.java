package chon.group.game.domain.environment;

import chon.group.game.domain.agent.Agent;
import chon.group.game.domain.agent.Cannon;
import chon.group.game.domain.agent.Fireball;
import chon.group.game.domain.agent.Weapon;

public class Setup {
     /**
     * Cria e retorna uma instância do ambiente de jogo com todos os objetos iniciais.
     * @return um objeto Environment totalmente configurado.
     */
    public static Environment createEnvironment() {
        Environment environment = new Environment(0, 0, 4096, 768, "/images/environment/castle.png");

        // Criando o protagonista
        Agent chonBota = new Agent(100, 390, 90, 65, 5, 1000, "/images/agents/chonBota.png", false);
        Weapon cannon = new Cannon(400, 390, 0, 0, 5, 0, "", false);
        Weapon fireball = new Fireball(400, 390, 0, 0, 3, 0, "", false);
        chonBota.setWeapon(fireball);
        environment.setProtagonist(chonBota);

        // Criando inimigos
        Agent chonBot = new Agent(920, 440, 90, 65, 1, 500, "/images/agents/chonBot.png", true);
        environment.getAgents().add(chonBot);

        // Configurando imagens de estado do jogo
        environment.setPauseImage("/images/environment/pause.png");
        environment.setGameOverImage("/images/environment/gameover.png");
        environment.setWinImage("/images/environment/win.png"); // Use uma imagem de vitória real

        return environment;
    }
}
