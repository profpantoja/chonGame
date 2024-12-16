// Classe Agent

package chon.group.agent;
import java.util.ArrayList;
import javafx.scene.image.Image;

/**
 * Classe que representa um Agente (Agent) no jogo. 
 * Cada agente possui uma posição, tamanho, imagem e pode se mover em diferentes direções.
 */
public class Agent {
    // Atributos de posição
    private int positionX; // Posição horizontal (eixo X) do agente
    private int positionY; // Posição vertical (eixo Y) do agente
    
    // Atributos de tamanho
    private int height; // Altura do agente
    private int width; // Largura do agente
    
    // Imagem do agente
    private Image image; // Imagem do agente usada para renderização
    
    // Indica se o agente é o protagonista
    private Boolean isProtagonist; // Define se o agente é o protagonista
    public static int numberProtagonist = 0; // Contador estático de protagonistas criados

    // Lista de tiros ativos do agente
    private ArrayList<Bullet> bullets; // Lista que contém os tiros disparados pelo agente

    /**
     * Construtor da classe Agent.
     * 
     * @param positionX Posição inicial no eixo X
     * @param positionY Posição inicial no eixo Y
     * @param width Largura do agente
     * @param height Altura do agente
     * @param pathImage Caminho para a imagem do agente
     * @param isProtagonist Indica se o agente é o protagonista
     */
    public Agent(int positionX, int positionY, int width, int height, String pathImage, boolean isProtagonist ) {        
        this.positionX = positionX;
        this.positionY = positionY;
        this.width = width;
        this.height = height;
		this.image = new Image(getClass().getResource(pathImage).toExternalForm()); // Carregamento da imagem do agente
        this.isProtagonist = isProtagonist;
        this.bullets = new ArrayList<>(); // Inicializa a lista de tiros vazia
        
        // Incrementa o número de protagonistas se o agente for o protagonista
        if(isProtagonist){
            numberProtagonist++;
        }
    }

    // Getters e Setters
    public int getPositionX() { return positionX; }
    public void setPositionX(int positionX) { this.positionX = positionX; }
    public int getPositionY() { return positionY; }
    public void setPositionY(int positionY) { this.positionY = positionY; }
    public int getHeight() { return height; }
    public void setHeight(int height) { this.height = height; }
    public int getWidth() { return width; }
    public void setWidth(int width) { this.width = width; }
    public Boolean getIsProtagonist() { return isProtagonist; }
    public void setIsProtagonist(Boolean isProtagonist) { this.isProtagonist = isProtagonist; }
    public Image getImage() { return image; }
    public ArrayList<Bullet> getBullets() { return bullets; } // Retorna a lista de tiros ativos do agente

    /**
     * Move o agente com base nos comandos recebidos.
     * 
     * @param input Lista de entradas de comando (direções "RIGHT", "LEFT", "UP", "DOWN")
     */
    public void move(ArrayList<String> input){
		if (input.contains("RIGHT")) {
			setPositionX(positionX + 1); // Move o agente para a direita
		} 
		if (input.contains("LEFT")) {
			setPositionX(positionX - 1); // Move o agente para a esquerda
		} 
		if (input.contains("UP")) {
			setPositionY(positionY - 1); // Move o agente para cima
		} 
		if (input.contains("DOWN")) {
			setPositionY(positionY + 1); // Move o agente para baixo
		}
		if (input.contains("SPACE")) {
			shoot(); // Dispara um tiro quando a tecla ESPAÇO é pressionada
		}
	}

    /**
     * Dispara um tiro a partir da posição atual do agente.
     * O tiro é adicionado à lista de tiros ativos.
     */
    public void shoot() {
        Bullet bullet = new Bullet(this.positionX + this.width, this.positionY + this.height / 2); // Cria o tiro no centro vertical e à direita do agente
        bullets.add(bullet); // Adiciona o tiro à lista de tiros
    }

    /**
     * Atualiza a posição de todos os tiros disparados pelo agente.
     * Remove os tiros que saíram da tela.
     */
    public void updateBullets() {
        bullets.removeIf(bullet -> bullet.getPositionX() > 1180); // Remove o tiro se ele ultrapassar o limite direito da tela
        for (Bullet bullet : bullets) {
            bullet.updatePosition(); // Atualiza a posição do tiro
        }
    }

    /**
     * Verifica se o agente é o protagonista.
     * @return true se o agente for o protagonista, false caso contrário
     */
    public boolean isProtagonist() {
        return isProtagonist;
    }
}

/**
 * Classe Bullet que representa um tiro disparado pelo agente.
 */
class Bullet {
    private int positionX; // Posição X do tiro
    private int positionY; // Posição Y do tiro
    private int speed = 5; // Velocidade do tiro (movimento para a direita)
    private Image image; // Imagem do tiro

    /**
     * Construtor da classe Bullet.
     * 
     * @param positionX Posição inicial no eixo X
     * @param positionY Posição inicial no eixo Y
     */
    public Bullet(int positionX, int positionY) {
        this.positionX = positionX;
        this.positionY = positionY;
        this.image = new Image(getClass().getResource("/images/bullet.png").toExternalForm()); // Imagem do tiro
    }

    /**
     * Atualiza a posição do tiro para a direita com base na sua velocidade.
     */
    public void updatePosition() {
        this.positionX += speed;
    }

    public int getPositionX() { return positionX; }
    public int getPositionY() { return positionY; }
    public void setPositionX(int positionX) { this.positionX = positionX; }
    public void setPositionY(int positionY) { this.positionY = positionY; }
    public Image getImage() { return image; } // Retorna a imagem do tiro
}