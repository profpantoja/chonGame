package chon.group.game;

import java.util.ArrayList;
import chon.group.game.core.agent.Agent;
import chon.group.game.core.environment.Environment;
import chon.group.game.core.weapon.ExplosionEffect;
import chon.group.game.core.weapon.Shot;
import chon.group.game.drawer.EnvironmentDrawer;
import chon.group.game.sound.SoundManager;
import chon.group.game.ui.Menu;
import javafx.application.Platform;

public class Game {
    private Environment environment;
    private EnvironmentDrawer drawer;
    private ArrayList<String> input;
    private GameStatus currentStatus;
    private Menu mainMenu;
    private boolean enterReleased = true;
    private boolean upDownReleased = true;
    private boolean spaceReleased = true;
    private boolean qKeyReleased = true;
    private long levelClearTime = 0;
    private long bossWarningTime = 0;
    private long gameWinTime = 0;
    private long lastFrameTime = 0;

    public Game(GameSet gameSet, EnvironmentDrawer drawer, ArrayList<String> input) {
        this.environment = gameSet.getEnvironment(); this.drawer = drawer; this.input = input; this.mainMenu = gameSet.getMainMenu(); this.currentStatus = GameStatus.START;
    }

    public void loop() {
        long currentTime = System.nanoTime();
        if (lastFrameTime == 0) { lastFrameTime = currentTime; return; }
        double deltaTime = (currentTime - lastFrameTime) / 1_000_000_000.0;
        lastFrameTime = currentTime;
        update(deltaTime);
        render();
    }

    private void update(double deltaTime) {
        switch (currentStatus) {
            case START:
                SoundManager.playMusic("/sounds/MenuMusic.mp3");
                handleMenuInput();
                break;
            case RUNNING:
                manageLevelMusic();
                if (input.contains("P") && enterReleased) { currentStatus = GameStatus.PAUSED; enterReleased = false; return; } else if (!input.contains("P")) { enterReleased = true; }
                
                environment.setPlayerInput(input); // Passa o input do jogador para o Environment
                
                if (input.contains("SPACE") && spaceReleased) { if (environment.getProtagonist() != null) { Shot newShot = environment.getProtagonist().useWeapon(); if (newShot != null) { SoundManager.playSound("/sounds/EssenceOrbSound.mp3"); newShot.setTag("PLAYER_SHOT"); environment.getCurrentLevel().getShots().add(newShot); } } spaceReleased = false; } else if (!input.contains("SPACE")) { spaceReleased = true; }
                if (input.contains("Q") && qKeyReleased) { if (environment.getProtagonist() != null) { ExplosionEffect newEffect = environment.getProtagonist().useAreaAbility(); if (newEffect != null) { environment.getAreaEffects().add(newEffect); } } qKeyReleased = false; } else if (!input.contains("Q")) { qKeyReleased = true; }
                
                environment.update(deltaTime);
                
                if (environment.isBossSpawnTriggered()) { environment.resetBossSpawnTrigger(); currentStatus = GameStatus.BOSS_WARNING; bossWarningTime = System.currentTimeMillis(); SoundManager.playMusic("/sounds/Minotaur.mp3"); return; }
                
                Agent protagonist = environment.getProtagonist();
                if (protagonist != null && protagonist.isDead()) {
                    if (protagonist.isDyingAnimationFinished()) {
                        currentStatus = GameStatus.GAME_OVER;
                    }
                } else if (environment.getCurrentLevel() != null && environment.getCurrentLevel().isCompleted(environment)) {
                    int currentLevelIndex = environment.getLevels().indexOf(environment.getCurrentLevel());
                    if (currentLevelIndex >= environment.getLevels().size() - 1) {
                        currentStatus = GameStatus.WIN;
                        gameWinTime = System.currentTimeMillis();
                    } else {
                        currentStatus = GameStatus.LEVEL_CLEAR;
                        levelClearTime = System.currentTimeMillis();
                    }
                }
                break;
            case PAUSED: SoundManager.stopMusic(); handlePauseMenuInput(); break;
            case LEVEL_CLEAR: SoundManager.stopMusic(); if (System.currentTimeMillis() - levelClearTime > 3000) { environment.loadNextLevel(); currentStatus = GameStatus.RUNNING; } break;
            case BOSS_WARNING: if (System.currentTimeMillis() - bossWarningTime > 2000) { environment.spawnBoss(); currentStatus = GameStatus.RUNNING; } break;
            case WIN: SoundManager.playMusic("/sounds/MenuBeholdTheMage.mp3"); if (System.currentTimeMillis() - gameWinTime > 5000) { handleWinScreenInput(); } break;
            case GAME_OVER: SoundManager.stopMusic(); handleGameOverMenuInput(); break;
        }
    }

    private void render() {
        drawer.clearEnvironment();
        switch (currentStatus) {
            case START: drawer.drawMainMenu(mainMenu); break;
            case RUNNING: drawer.renderGame(); break;
            case PAUSED: drawer.renderGame(); drawer.drawPauseScreen(); break;
            case LEVEL_CLEAR: drawer.renderGame(); drawer.drawLevelClearScreen(); break;
            case BOSS_WARNING: drawer.renderGame(); drawer.drawBossWarningScreen(); break;
            case WIN: drawer.drawWinScreen(); break;
            case GAME_OVER: drawer.drawGameOver(); break;
        }
    }
    
    private void handleWinScreenInput() { if (!input.isEmpty() && (input.contains("ENTER") || input.contains("SPACE") || input.contains("UP") || input.contains("DOWN") || input.contains("LEFT") || input.contains("RIGHT"))) { if (enterReleased) { currentStatus = GameStatus.START; resetGame(); input.clear(); enterReleased = false; } } else { enterReleased = true; } }
    private void handleMenuInput() { if (input.contains("UP") && upDownReleased) { mainMenu.selectPreviousOption(); SoundManager.playSound("/sounds/MenuNavigate.mp3"); upDownReleased = false; } else if (input.contains("DOWN") && upDownReleased) { mainMenu.selectNextOption(); SoundManager.playSound("/sounds/MenuNavigate.mp3"); upDownReleased = false; } else if (!input.contains("UP") && !input.contains("DOWN")) { upDownReleased = true; } if (input.contains("ENTER") && enterReleased) { SoundManager.playSound("/sounds/MenuSelect.mp3"); switch (mainMenu.getSelectedOptionIndex()) { case 0: case 1: resetGame(); currentStatus = GameStatus.RUNNING; input.clear(); break; case 2: Platform.exit(); break; } enterReleased = false; } else if (!input.contains("ENTER")) { enterReleased = true; } }
    private void handlePauseMenuInput() { if (input.contains("UP") && upDownReleased) { environment.selectPreviousPauseOption(); SoundManager.playSound("/sounds/MenuNavigate.mp3"); upDownReleased = false; } else if (input.contains("DOWN") && upDownReleased) { environment.selectNextPauseOption(); SoundManager.playSound("/sounds/MenuNavigate.mp3"); upDownReleased = false; } else if (!input.contains("UP") && !input.contains("DOWN")) { upDownReleased = true; } if (input.contains("ENTER") && enterReleased) { SoundManager.playSound("/sounds/MenuSelect.mp3"); switch (environment.getCurrentPauseOptionIndex()) { case 0: currentStatus = GameStatus.RUNNING; input.clear(); break; case 1: currentStatus = GameStatus.START; resetGame(); input.clear(); break; case 2: Platform.exit(); break; } enterReleased = false; } else if (!input.contains("ENTER")) { enterReleased = true; } }
    private void handleGameOverMenuInput() { if (input.contains("UP") && upDownReleased) { environment.getGameOverMenu().selectPreviousOption(); SoundManager.playSound("/sounds/MenuNavigate.mp3"); upDownReleased = false; } else if (input.contains("DOWN") && upDownReleased) { environment.getGameOverMenu().selectNextOption(); SoundManager.playSound("/sounds/MenuNavigate.mp3"); upDownReleased = false; } else if (!input.contains("UP") && !input.contains("DOWN")) { upDownReleased = true; } if (input.contains("ENTER") && enterReleased) { SoundManager.playSound("/sounds/MenuSelect.mp3"); switch (environment.getGameOverMenu().getSelectedOptionIndex()) { case 0: resetGame(); currentStatus = GameStatus.RUNNING; input.clear(); break; case 1: resetGame(); currentStatus = GameStatus.START; input.clear(); break; case 2: Platform.exit(); break; } enterReleased = false; } else if (!input.contains("ENTER")) { enterReleased = true; } }
    
    private void resetGame() { 
        SoundManager.stopMusic();
        environment.startOrResetGame(); 
    }
    
    private void manageLevelMusic() {
        if (environment.getCurrentLevel() == null) {
            SoundManager.stopMusic();
            return;
        }
        int levelIndex = environment.getLevels().indexOf(environment.getCurrentLevel());
        String musicPath = "";
        switch (levelIndex) {
            case 0: musicPath = "/sounds/StageSound1.mp3"; break;
            case 1: musicPath = "/sounds/StageSound2.mp3"; break;
            case 2: musicPath = "/sounds/StageSound3.mp3"; break;
        }
        if (!musicPath.isEmpty()) {
            SoundManager.playMusic(musicPath);
        }
    }
}