package chon.group.game.domain.environment;

import java.util.ArrayList;
import java.util.List; // Import List

import chon.group.game.domain.agent.Agent;
import chon.group.game.domain.agent.Cannon;
import chon.group.game.domain.agent.CloseWeapon;
import chon.group.game.domain.agent.Fireball;
import chon.group.game.domain.agent.Hitbox;
import chon.group.game.domain.agent.Sword;
import chon.group.game.domain.agent.Weapon;
import javafx.scene.image.Image;

public class Game {

    private Game() {
    }

    public static String gameMusic = "/sounds/gameMusic.wav"; //Put the gameMusic path 
    public static String gameOverMusic = "/sounds/gameOveMusic.wav"; //Put the gameOverMusic path 
    public static String menuSound = "/sounds/menuSound.wav"; //Put the menuSound path 
    public static String attackFX = "/sounds/attackFX.wav"; //Put the attackFX path 
    public static String winSound = "/sounds/winSound.wav"; //Put the winSound path 

    /**
     * O método agora cria e retorna uma LISTA de ambientes (níveis).
     * Toda a lógica de troca de nível foi removida desta classe.
     * @return um objeto List<Environment> contendo todos os níveis do jogo.
     */
    public static List<Environment> createLevels() {
        List<Environment> environments = new ArrayList<>();
        
        int worldWidth = 4096;
        int worldHeight = 768;
        
        // --- Protagonista ---
        // O protagonista é criado uma única vez para que seu estado (vida, etc.)
        // seja mantido entre os níveis.
        Agent chonBota = new Agent(100, 628, 96, 256, 5, 300, "/images/agents/chonBota.png", false, true,
        "/images/agents/Link_Attack.png",
        "/images/agents/Link_Standing.png",
        "/images/agents/Link_Running.png",
        "/images/agents/Link_Damage.png",
        "/images/agents/Link_Death.png");
        chonBota.setHitbox(new Hitbox(114, 4, 30, 90));
        Weapon fireball = new Fireball(400, 390, 0, 0, 3, 0, "", false,75); 

        Weapon cannon = new Cannon(400, 390, 0, 0, 3, 0, "", false,64);
        CloseWeapon sword = new Sword(400, 390, 0, 0, 3, 0, "", false, 100); // Slash width set to 100
        chonBota.setWeapon(fireball);
        chonBota.setCloseWeapon(sword);
        chonBota.setPathImageAttack("/images/agents/Link_Attack.png");
        chonBota.setPathImageIdle("/images/agents/Link_Standing.png");
        chonBota.setPathImageRun("/images/agents/Link_Running.png");
        chonBota.setPathImageHit("/images/agents/Link_Damage.png");
        chonBota.setPathImageDeath("/images/agents/Link_Death.png");
        
        // --- Nível 1 ---
        Environment level1 = new Environment(0, 0, worldWidth, worldHeight, "/images/environment/castle.png");
        level1.setProtagonist(chonBota); // Adiciona o protagonista ao nível 1
        
        // Inimigos e colisões do nível 1
        Agent chonBot = new Agent(920, 628, 90, 65, 1, 500, "/images/agents/chonBot.png", true,false);
        chonBot.setHitbox(new Hitbox(17, 23, 33, 45));
        level1.getAgents().add(chonBot);
        
        level1.createGround(worldWidth, 50, "/images/environment/brick.png");
        //level1.getCollisions().add(new Collision(350, 570, 192, 64, "/images/environment/brick.png", false, false, 0, false, false, false));
        //level1.getCollisions().add(new Collision(600, 450, 192, 64, "/images/environment/brick.png", false, true, 0, false, false, true));
        //level1.getCollisions().add(new Collision(1000, 300, 64, 256, "/images/environment/brick.png", false, true, 0, false, true, false));
        //level1.getCollisions().add(new Collision(1200, 400, 64, 64, "/images/environment/brick.png", true, false, 20, false, false, false));
        //level1.getCollisions().add(new Collision(1500, 280, 64, 64, "/images/environment/brick.png", false, false, 0, true, false, false));
        
        level1.setPauseImage("");             
        level1.setGameOverImage("/images/environment/gameover.png");
        level1.setWinImage("/images/environment/gameover.png");
        
        environments.add(level1);
        
        // --- Nível 2 ---
        Environment level2 = new Environment(0, 0, 4096, 768, "/images/environment/mountain.png");
        level2.setProtagonist(chonBota); // Adiciona a MESMA instância do protagonista ao nível 2
        
        level2.setPauseImage("");             
        level2.setGameOverImage("/images/environment/gameover.png");
        level2.setWinImage("/images/environment/gameover.png");
        
        Agent chonBoss = new Agent(920, 440,100, 75, 1, 1, "/images/agents/chonBot.png", true,false);
        chonBoss.setHitbox(new Hitbox(20, 15, 60, 60)); 
        level2.getAgents().add(chonBoss);
        level2.createGround(worldWidth, 50, "/images/environment/brick.png"); // Adiciona chão ao nível 2 também
        
        environments.add(level2);

        // --- Nível 3 ---

        Environment lvl3 = new Environment(0, 0, 4096, 768, "/images/environment/mountain.png");
        lvl3.setProtagonist(chonBota); // Adiciona a MESMA instância do protagonista ao nível 3
        
        lvl3.setPauseImage("");             
        lvl3.setGameOverImage("/images/environment/gameover.png");
        lvl3.setWinImage("/images/environment/gameover.png");
        
        Agent chonMegaBoss = new Agent(920, 440,100, 75, 1, 1, "/images/agents/chonBota.png", true,false);
        chonMegaBoss.setHitbox(new Hitbox(20, 15, 60, 60)); 
        lvl3.getAgents().add(chonMegaBoss);
        lvl3.createGround(worldWidth, 50, "/images/environment/brick.png"); // Adiciona chão ao nível 3 também
        
        environments.add(lvl3);


        return environments;
    }

    /**
     * Carrega uma imagem a partir de um caminho no classpath.
     * @param path O caminho para o recurso da imagem.
     * @return um objeto Image.
     */
    public static Image loadImage(String path) {
        try {
            return new Image(Game.class.getResource(path).toExternalForm());
        } catch (Exception e) {
            System.err.println("Falha ao carregar imagem: " + path);
            return null;
        }
    }    
}