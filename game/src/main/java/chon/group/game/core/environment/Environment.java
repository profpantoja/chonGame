package chon.group.game.core.environment;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import chon.group.game.core.agent.Agent;
import chon.group.game.core.agent.Entity;
import chon.group.game.core.agent.Object;
import chon.group.game.core.weapon.Panel;
import chon.group.game.core.weapon.Shot;
import chon.group.game.messaging.Message;
import javafx.scene.image.Image;

public class Environment {

    private int canvasWidth;
    private int canvasHeight;
    private Image pauseImage;
    private Image gameOverImage;
    private Agent protagonist;
    private List<Level> levels;
    private Level currentLevel;
    private List<Message> messages;
    private Camera camera;
    private Panel panel;
    private int collectedCount = 0; // Mantido mas não usado por enquanto
    private int score = 0; // Mantido para a lógica de inimigos

    public Environment(int height, int width, double screenWidth, Panel panel) {
        this.canvasHeight = height;
        this.canvasWidth = width;
        this.messages = new ArrayList<>();
        this.levels = new ArrayList<>();
        this.camera = new Camera(screenWidth, width, 0.49, 0.51);
        this.panel = panel;
    }

    public int getCanvasWidth() {
        return canvasWidth;
    }

    public int getCanvasHeight() {
        return canvasHeight;
    }

    public double getScrollX() {
        if (camera != null) {
            return camera.getPosX();
        }
        return 0;
    }

    public Image getPauseImage() {
        return pauseImage;
    }

    public void setPauseImage(String pathImage) {
        this.pauseImage = new Image(getClass().getResource(pathImage).toExternalForm());
    }

    public Image getGameOverImage() {
        return gameOverImage;
    }

    public void setGameOverImage(String pathImage) {
        this.gameOverImage = new Image(getClass().getResource(pathImage).toExternalForm());
    }

    public Agent getProtagonist() {
        return protagonist;
    }

    public void setProtagonist(Agent protagonist) {
        this.protagonist = protagonist;
        if (camera != null)
            camera.setTarget(protagonist);
    }

    public List<Level> getLevels() {
        return levels;
    }

    public Level getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(Level currentLevel) {
        this.currentLevel = currentLevel;
        if (camera != null && currentLevel != null) {
            camera.setLevelWidth(currentLevel.getWidth());
        }
    }

    public List<Message> getMessages() {
        return messages;
    }

    public Camera getCamera() {
        return camera;
    }

    public Panel getPanel() {
        return panel;
    }
    
    public int getScore() {
        return score;
    }

    public void checkBorders() {
        if (protagonist == null || currentLevel == null) return;

        if (protagonist.getPosX() < 0)
            protagonist.setPosX(0);
        if ((protagonist.getPosX() + protagonist.getWidth()) > this.currentLevel.getWidth())
            protagonist.setPosX(this.currentLevel.getWidth() - protagonist.getWidth());
        if (protagonist.getPosY() < 0)
            protagonist.setPosY(0);
        if ((protagonist.getPosY() + protagonist.getHeight()) > this.currentLevel.getHeight())
            protagonist.setPosY(this.currentLevel.getHeight() - protagonist.getHeight());
    }

    public void detectCollision() {
        if (protagonist == null || currentLevel == null) return;

        for (Agent agent : this.currentLevel.getAgents()) {
            if (protagonist != null && intersect(protagonist, agent)) {
                int damage = 100;
                protagonist.takeDamage(damage, messages);
            }
        }
    }

    private boolean intersect(Entity a, Entity b) {
        if (a == null || b == null) return false;

        return a.getPosX() < b.getPosX() + b.getWidth() &&
               a.getPosX() + a.getWidth() > b.getPosX() &&
               a.getPosY() < b.getPosY() + b.getHeight() &&
               a.getPosY() + a.getHeight() > b.getPosY();
    }

    // MÉTODO DOS OBJETOS/COLETÁVEIS AGORA VAZIO
    public void updateObjects() {
        // Lógica de coletáveis foi removida.
    }

    public void updateMessages() {
        messages.removeIf(message -> !message.update());
    }

    public void updateShots() {
        if (currentLevel == null || protagonist == null) return;

        Iterator<Shot> itShot = this.currentLevel.getShots().iterator();
        while (itShot.hasNext()) {
            Shot shot = itShot.next();
            if ((shot.getPosX() > this.currentLevel.getWidth()) || ((shot.getPosX() + shot.getWidth()) < 0)) {
                itShot.remove();
            } else {
                if (intersect(protagonist, shot)) {
                    protagonist.takeDamage(shot.getDamage(), messages);
                    itShot.remove();
                } else {
                    Iterator<Agent> itAgent = this.currentLevel.getAgents().iterator();
                    while (itAgent.hasNext()) {
                        Agent agent = itAgent.next();
                        if (intersect(agent, shot)) {
                            agent.takeDamage(shot.getDamage(), messages);
                            if (agent.isDead()) {
                                itAgent.remove();
                                this.score += 10; // Adiciona pontos ao derrotar inimigo
                            }
                            itShot.remove();
                            break;
                        }
                    }
                }
                shot.move(new ArrayList<>(List.of(shot.getDirection())));
            }
        }
    }

    public void updateCamera() {
        if (camera != null)
            camera.update();
    }

    public void updateLevel() {
        if (this.currentLevel == null) return;

        if (this.currentLevel.isCompleted(this)) {
            this.loadNextLevel();
        }
    }

    public void update() {
        if (protagonist == null || currentLevel == null) {
            return;
        }

        // updateObjects(); // CHAMADA REMOVIDA
        updateShots();
        updateMessages();
        updateCamera();
        detectCollision();
        protagonist.recoverEnergy();
        checkBorders();
        updateLevel();
    }

    public void loadNextLevel() {
        if (levels.isEmpty()) {
            System.out.println("No more levels to load!");
            return;
        }
        
        int nextLevelIndex = 0;
        if (currentLevel != null) {
            int currentLevelIndex = levels.indexOf(currentLevel);
            if (currentLevelIndex >= levels.size() - 1) {
                System.out.println("All levels completed!");
                // Aqui podemos setar o status para WIN
                return;
            }
            nextLevelIndex = currentLevelIndex + 1;
        }
        
        this.currentLevel = levels.get(nextLevelIndex);

        if (this.protagonist != null) {
            this.protagonist.setPosX(10);
            this.protagonist.setPosY(600);
        }
        if (this.camera != null) {
            this.camera.setPosX(0);
            this.camera.setLevelWidth(this.currentLevel.getWidth());
            this.camera.setTarget(this.protagonist);
        }
        this.messages.clear();
        // LÓGICA DO PAINEL DE COLETÁVEIS REMOVIDA
    }
}