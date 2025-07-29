package chon.group.game.core.environment;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import chon.group.game.core.agent.Agent;
import chon.group.game.core.agent.EnemyType;
import chon.group.game.core.agent.Entity;
import chon.group.game.core.agent.Object;
import chon.group.game.core.weapon.ExplosionEffect;
import chon.group.game.core.weapon.Panel;
import chon.group.game.core.weapon.Shot;
import chon.group.game.messaging.Message;
import chon.group.game.ui.GameOverMenu;
import javafx.scene.image.Image;

public class Environment {
    private int canvasWidth, canvasHeight, score, enemiesSpawnedCount;
    private List<Image> pauseImages;
    private int currentPauseOptionIndex;
    private GameOverMenu gameOverMenu;
    private Image winImage, bossWarningImage;
    private Agent protagonist;
    private List<Level> levels;
    private Level currentLevel;
    private List<Message> messages;
    private List<ExplosionEffect> areaEffects;
    private Camera camera;
    private Panel panel;
    private long lastSpawnTime = 0;
    private final long SPAWN_INTERVAL = 1000;
    private final int MAX_ENEMIES_ON_SCREEN = 5;
    private boolean bossSpawnTriggered = false;
    private Random random = new Random();
    private List<String> playerInput = new ArrayList<>();

    public Environment(int height, int width, double screenWidth, Panel panel) {
        this.canvasHeight = height; this.canvasWidth = width; this.messages = new ArrayList<>(); this.levels = new ArrayList<>(); this.camera = new Camera(screenWidth, width, 0.49, 0.51); this.panel = panel; this.pauseImages = new ArrayList<>(); this.currentPauseOptionIndex = 0; this.gameOverMenu = new GameOverMenu(); this.areaEffects = new ArrayList<>();
    }
    
    public void setPlayerInput(List<String> input) { this.playerInput = input; }
    public void update(double deltaTime) { if (protagonist == null || currentLevel == null) return; updateAgents(deltaTime); updateSpawning(); updateShots(deltaTime); updateAreaEffects(deltaTime); updateMessages(); updateCamera(); checkBorders(); }

    private void updateAgents(double deltaTime) {
        if (protagonist == null) return;
        protagonist.update(playerInput, deltaTime);
        if (currentLevel == null) return;
        List<Agent> agents = currentLevel.getAgents();
        
        for (int i = 0; i < agents.size(); i++) {
            Agent agent1 = agents.get(i);
            agent1.chase(protagonist.getPosX(), protagonist.getPosY());
            agent1.updateAnimation(deltaTime);

            for (int j = i + 1; j < agents.size(); j++) {
                Agent agent2 = agents.get(j);
                if (agent1.intersects(agent2)) {
                    int pushBack = 1;
                    if (agent1.getPosX() < agent2.getPosX()) { agent1.setPosX(agent1.getPosX() - pushBack); } else { agent1.setPosX(agent1.getPosX() + pushBack); }
                }
            }
            if (protagonist.intersects(agent1)) {
                protagonist.takeDamage(agent1.getCollisionDamage(), messages);
                int pushBack = 1;
                if (protagonist.getPosX() < agent1.getPosX()) { protagonist.setPosX(protagonist.getPosX() - pushBack); } else { protagonist.setPosX(protagonist.getPosX() + pushBack); }
            }
        }
    }
    
    private void updateAreaEffects(double deltaTime) {
        Iterator<ExplosionEffect> iterator = new ArrayList<>(areaEffects).iterator();
        while (iterator.hasNext()) {
            ExplosionEffect effect = iterator.next();
            effect.update(deltaTime);
            if(currentLevel != null) {
                for (Agent agent : new ArrayList<>(currentLevel.getAgents())) {
                    if (agent.intersects(effect) && !effect.hasHit(agent)) {
                        agent.takeDamage(effect.getDamage(), messages);
                        effect.registerHit(agent);
                    }
                }
            }
            if (effect.isExpired()) {
                areaEffects.remove(effect);
            }
        }
    }
    
    private void updateShots(double deltaTime) {
        if (currentLevel == null) return;
        Iterator<Shot> itShot = this.currentLevel.getShots().iterator();
        while (itShot.hasNext()) {
            Shot shot = itShot.next();
            shot.update(deltaTime);
            if (shot.shouldBeRemoved() || (shot.getPosX() > camera.getPosX() + canvasWidth) || ((shot.getPosX() + shot.getWidth()) < camera.getPosX())) { itShot.remove(); continue; }
            if ("PLAYER_SHOT".equals(shot.getTag())) {
                for(Agent agent : new ArrayList<>(this.currentLevel.getAgents())) {
                    if (shot.intersects(agent)) {
                        agent.takeDamage(shot.getDamage(), messages);
                        if (agent.isDead()) {
                            this.currentLevel.getAgents().remove(agent);
                            this.score += 10;
                            currentLevel.incrementEnemiesKilled();
                            int levelIndex = levels.indexOf(currentLevel);
                            if (levelIndex == 2 && !currentLevel.isBossFightActive() && currentLevel.getEnemiesKilledCount() >= currentLevel.getTotalEnemiesToSpawn()) {
                                triggerBossSpawn();
                            }
                        }
                        itShot.remove();
                        break;
                    }
                }
            }
        }
    }
    
    public void updateSpawning() {
        if (currentLevel != null && !currentLevel.isBossFightActive() && enemiesSpawnedCount < currentLevel.getTotalEnemiesToSpawn() &&
            System.currentTimeMillis() - lastSpawnTime > SPAWN_INTERVAL && currentLevel.getAgents().size() < MAX_ENEMIES_ON_SCREEN) {
            
            int spawnY = 580;
            int spawnX = (Math.random() < 0.5) ? ((int)camera.getPosX() - 200) : ((int)camera.getPosX() + canvasWidth + 120);
            EnemyType randomEnemy = EnemyType.values()[random.nextInt(EnemyType.values().length)];
            int enemyDamage = 1;
            
            currentLevel.getAgents().add(new Agent(spawnX, spawnY, 200, 170, 2, 100, randomEnemy.getImagePath(), true, true, enemyDamage));
            
            enemiesSpawnedCount++;
            lastSpawnTime = System.currentTimeMillis();
        }
    }
    
    public void spawnBoss() { 
        if (currentLevel == null) return; 
        currentLevel.getAgents().clear(); 

        int bossHealth = 100 * 10; 
        int bossWidth = 270; 
        int bossHeight = 300; 
        int bossSpeed = 1; 
        int bossDamage = 5; 
        int spawnX = (int)camera.getPosX() + canvasWidth - bossWidth; 
        int spawnY = this.canvasHeight - bossHeight - 95;
        currentLevel.getAgents().add(new Agent(spawnX, spawnY, bossHeight, bossWidth, bossSpeed, bossHealth, "/images/agents/Boss/minotaur.png", true, true, bossDamage)); 
        currentLevel.setBossFightActive(true); 
    }

    private Image loadImage(String path) { if (path == null || path.isEmpty()) return null; try { URL imageUrl = getClass().getResource(path); if (imageUrl == null) { System.err.println("[ERRO DE RECURSO] Imagem não encontrada: " + path); return null; } return new Image(imageUrl.toExternalForm()); } catch (Exception e) { System.err.println("[ERRO CRÍTICO] Falha ao carregar imagem: " + path); e.printStackTrace(); return null; } }
    public void setPauseImages(List<String> paths) { this.pauseImages.clear(); for (String path : paths) { Image img = loadImage(path); if (img != null) { this.pauseImages.add(img); } } this.currentPauseOptionIndex = 0; }
    public void selectNextPauseOption() { if (!pauseImages.isEmpty()) { currentPauseOptionIndex = (currentPauseOptionIndex + 1) % pauseImages.size(); } }
    public void selectPreviousPauseOption() { if (!pauseImages.isEmpty()) { currentPauseOptionIndex = (currentPauseOptionIndex - 1 + pauseImages.size()) % pauseImages.size(); } }
    public Image getCurrentPauseImage() { if (pauseImages.isEmpty() || currentPauseOptionIndex >= pauseImages.size()) { return null; } return pauseImages.get(currentPauseOptionIndex); }
    public int getCurrentPauseOptionIndex() { return currentPauseOptionIndex; }
    public void setWinImage(String path) { this.winImage = loadImage(path); }
    public Image getBossWarningImage() { return bossWarningImage; }
    public void setBossWarningImage(String path) { this.bossWarningImage = loadImage(path); }
    public void triggerBossSpawn() { this.bossSpawnTriggered = true; }
    public boolean isBossSpawnTriggered() { return bossSpawnTriggered; }
    public void resetBossSpawnTrigger() { this.bossSpawnTriggered = false; }
    public void updateMessages() { messages.removeIf(message -> !message.update()); }
    public void updateCamera() { if (camera != null) camera.update(); }
    public void checkBorders() { if (protagonist == null || currentLevel == null) return; if (protagonist.getPosX() < 0) protagonist.setPosX(0); if ((protagonist.getPosX() + protagonist.getWidth()) > this.currentLevel.getWidth()) protagonist.setPosX(this.currentLevel.getWidth() - protagonist.getWidth()); if (protagonist.getPosY() < 0) protagonist.setPosY(0); if ((protagonist.getPosY() + protagonist.getHeight()) > this.canvasHeight) protagonist.setPosY(this.canvasHeight - protagonist.getHeight()); }
    public void loadNextLevel() { int nextLevelIndex = 0; if (currentLevel != null) { nextLevelIndex = levels.indexOf(currentLevel) + 1; } if (nextLevelIndex >= levels.size()) return; this.currentLevel = levels.get(nextLevelIndex); this.currentLevel.reset(); this.camera.setLevelWidth(this.currentLevel.getWidth()); this.enemiesSpawnedCount = 0; this.lastSpawnTime = System.currentTimeMillis(); if (this.protagonist != null) { this.protagonist.setPosX(100); this.protagonist.setPosY(600); } if (this.camera != null) { this.camera.setPosX(0); this.camera.setTarget(this.protagonist); } this.messages.clear(); }
    public void startOrResetGame() { this.score = 0; this.currentLevel = null; if (this.gameOverMenu != null) { this.gameOverMenu = new GameOverMenu(); } if (protagonist != null) { protagonist.setHealth(protagonist.getFullHealth()); } this.loadNextLevel(); }
    public int getCanvasWidth() { return canvasWidth; }
    public int getCanvasHeight() { return canvasHeight; }
    public double getScrollX() { return (camera != null) ? camera.getPosX() : 0; }
    public GameOverMenu getGameOverMenu() { return gameOverMenu; }
    public Image getWinImage() { return winImage; }
    public Agent getProtagonist() { return protagonist; }
    public void setProtagonist(Agent p) { this.protagonist = p; if (camera != null) camera.setTarget(p); }
    public List<Level> getLevels() { return levels; }
    public Level getCurrentLevel() { return currentLevel; }
    public void setCurrentLevel(Level l) { this.currentLevel = l; if (camera != null) camera.setLevelWidth(l.getWidth()); }
    public List<Message> getMessages() { return messages; }
    public List<ExplosionEffect> getAreaEffects() { return areaEffects; }
    public Camera getCamera() { return camera; }
    public Panel getPanel() { return panel; }
    public int getScore() { return score; }
}