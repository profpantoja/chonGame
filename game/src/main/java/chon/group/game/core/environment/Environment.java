package chon.group.game.core.environment;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import chon.group.game.core.agent.Agent;
import chon.group.game.core.agent.Entity;
import chon.group.game.core.agent.Object;
import chon.group.game.core.weapon.Panel;
import chon.group.game.core.weapon.Shot;
import chon.group.game.core.weapon.Slash;
import chon.group.game.messaging.Message;
import chon.group.game.sound.SoundManager;
import javafx.scene.image.Image;

public class Environment {

    private Image pauseImage;
    private Image gameOverImage;
    private Agent protagonist;
    private List<Level> levels;
    private Level currentLevel;
    private List<Message> messages;
    private Camera camera;
    private Panel panel;
    private int collectedCount = 0;
    private int score = 0;

    public Environment(int height, int width, double screenWidth, Panel panel) {
        this.messages = new ArrayList<Message>();
        this.levels = new ArrayList<Level>();
        this.camera = new Camera(screenWidth, width, 0.49, 0.51);
        this.panel = panel;
    }

    // ... todos os seus getters e setters continuam iguais ...
    public Image getPauseImage() { return pauseImage; }
    public void setPauseImage(String pathImage) { this.pauseImage = new Image(getClass().getResource(pathImage).toExternalForm()); }
    public Image getGameOverImage() { return gameOverImage; }
    public void setGameOverImage(String pathImage) { this.gameOverImage = new Image(getClass().getResource(pathImage).toExternalForm()); }
    public Agent getProtagonist() { return protagonist; }
    public void setProtagonist(Agent protagonist) { this.protagonist = protagonist; if (camera != null) camera.setTarget(protagonist); }
    public List<Level> getLevels() { return levels; }
    public void setLevels(List<Level> levels) { this.levels = levels; }
    public Level getCurrentLevel() { return currentLevel; }
    public void setCurrentLevel(Level currentLevel) { this.currentLevel = currentLevel; }
    public List<Message> getMessages() { return messages; }
    public void setMessages(List<Message> messages) { this.messages = messages; }
    public Camera getCamera() { return camera; }
    public void setCamera(Camera camera) { this.camera = camera; }
    public Panel getPanel() { return panel; }
    public void setPanel(Panel panel) { this.panel = panel; }
    public int getCollectedCount() { return collectedCount; }
    public int getScore() { return score; }


    public void checkBorders() {
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
        for (Agent agent : this.currentLevel.getAgents()) {
            // Se o agente estiver morrendo, ele não causa dano por contato
            if (agent.isDying()) {
                continue;
            }
            if (protagonist != null && intersect(protagonist, agent)) {
                int damage = 100;
                protagonist.takeDamage(damage, messages);
            }
        }
    }

    private boolean intersect(Entity a, Entity b) {
        return a.getPosX() < b.getPosX() + b.getWidth() &&
                a.getPosX() + a.getWidth() > b.getPosX() &&
                a.getPosY() < b.getPosY() + b.getHeight() &&
                a.getPosY() + a.getHeight() > b.getPosY();
    }

    public void updateObjects() {
        Iterator<Object> iterator = this.currentLevel.getObjects().iterator();
        while (iterator.hasNext()) {
            Object object = iterator.next();
            if (!object.isCollected() && object.isCollectible()) {
                object.follow(protagonist, 200, 5);
                double dx = object.getPosX() - protagonist.getPosX();
                double dy = object.getPosY() - protagonist.getPosY();
                double distance = Math.sqrt(dx * dx + dy * dy);
                if (distance < 20) {
                    object.onCollect();
                    collectedCount++;
                    score += 10;
                }
            } else if ((object.isCollected() && object.isCollectible()) || 
            (object.isDestroyed() && object.isDestructible())) {
                iterator.remove();
            }
        }
    }

    public void updateMessages() {
        Iterator<Message> iterator = messages.iterator();
        while (iterator.hasNext()) {
            Message message = iterator.next();
            if (!message.update()) {
                iterator.remove();
            }
        }
    }

    public void updateShots() {
        Iterator<Shot> itShot = this.currentLevel.getShots().iterator();
        while (itShot.hasNext()) {
            Shot shot = itShot.next();

            if ((shot.getPosX() > this.currentLevel.getWidth()) || ((shot.getPosX() + shot.getWidth()) < 0)) {
                itShot.remove();
                continue;
            }

            Agent owner = shot.getOwner(); 

            if (intersect(protagonist, shot)) {
                if (owner != protagonist && owner.isEnemy() != protagonist.isEnemy()) {
                    protagonist.takeDamage(shot.getDamage(), messages);
                    itShot.remove();
                    continue;
                }
            }

            Iterator<Agent> itAgent = this.currentLevel.getAgents().iterator();
            while (itAgent.hasNext()) {
                Agent agent = itAgent.next();
                
                if (owner == agent) continue;
                if (owner != null && owner.isEnemy() == agent.isEnemy()) continue;

                if (intersect(agent, shot)) {
                    agent.takeDamage(shot.getDamage(), messages);
                    // <<< LINHAS PROBLEMÁTICAS REMOVIDAS DAQUI >>>
                    // if (agent.isDead()) {
                    //     itAgent.remove();
                    // }
                    itShot.remove();
                    break;
                }
            }
            shot.move(new ArrayList<>(List.of(shot.getDirection())));
        }
    }

    public void updateSlashes() {
        Iterator<Slash> itSlash = this.currentLevel.getSlashes().iterator();
        while (itSlash.hasNext()) {
            Slash slash = itSlash.next();
            boolean hit = false;
            Agent owner = slash.getOwner();

            Iterator<Agent> itAgent = this.currentLevel.getAgents().iterator();
            while (itAgent.hasNext()) {
                Agent agent = itAgent.next();

                if (owner == agent) continue;
                if (owner != null && owner.isEnemy() == agent.isEnemy()) continue;

                if (intersect(agent, slash)) {
                    agent.takeDamage(slash.getDamage(), messages);
                    // <<< LINHAS PROBLEMÁTICAS REMOVIDAS DAQUI >>>
                    // if (agent.isDead()) {
                    //     itAgent.remove();
                    // }
                    hit = true;
                    // O 'break' aqui faria com que o golpe acertasse apenas um inimigo.
                    // Removi para que um golpe possa acertar múltiplos inimigos se eles estiverem juntos.
                }
            }

            if (!hit && intersect(protagonist, slash)) {
                if (owner != protagonist && (owner == null || owner.isEnemy() != protagonist.isEnemy())) {
                    protagonist.takeDamage(slash.getDamage(), messages);
                    hit = true;
                }
            }

            if (slash.shouldRemove()) { // A própria animação do slash define quando ele deve sumir
                itSlash.remove();
            }
        }
    }

    public void updateCamera() {
        if (camera != null)
            camera.update();
    }

    public void updateLevel() {
        if (this.currentLevel.isCompleted(this))
            this.loadNextLevel();
    }

    public void update() {
        updateObjects();
        updateShots();
        updateSlashes();
        updateMessages();
        updateCamera();
        detectCollision();
        protagonist.recoverEnergy();
        updateLevel();
    }

    public boolean hasNextLevel() {
        int currentIndex = levels.indexOf(currentLevel);
        return currentIndex < levels.size() - 1;
    }

    public void loadNextLevel() {
        if (currentLevel == null)
            this.currentLevel = levels.get(0);
        else {
            int levelIndex = this.getLevels().indexOf(this.currentLevel);
            if (levelIndex >= this.getLevels().size() - 1) {
                levelIndex = this.getLevels().size() - 1;
                this.currentLevel = this.getLevels().get(levelIndex);
            } else {
                this.currentLevel = this.getLevels().get(levelIndex + 1);
                this.protagonist.setPosX(10);
                this.protagonist.setPosY(600);
                this.camera.setPosX(0);
                this.camera.setLevelWidth(this.currentLevel.getWidth());
            }
        }
        if (this.currentLevel.getBackgroundMusic() != null) {
            String musicPath = this.currentLevel.getBackgroundMusic();
            if (!SoundManager.isCurrentMusic(musicPath)) {
                SoundManager.playMusic(musicPath);
            }
        }
    }
}