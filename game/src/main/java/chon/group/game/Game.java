package chon.group.game;

import java.util.ArrayList;

import chon.group.game.core.agent.Agent;
import chon.group.game.core.agent.Object;
import chon.group.game.core.animation.AnimationStatus;
import chon.group.game.core.environment.Environment;
import chon.group.game.core.menu.MainMenu;
import chon.group.game.core.menu.MainOption;
import chon.group.game.core.menu.PauseMenu;
import chon.group.game.core.menu.PauseOption;
import chon.group.game.core.weapon.Shot;
import chon.group.game.core.weapon.Slash;
import chon.group.game.drawer.EnvironmentDrawer;
import chon.group.game.sound.SoundManager;
import chon.group.game.core.environment.Level;

import javafx.scene.input.KeyEvent;

public class Game {

    private long victoryStartTime = 0;
    private final long VICTORY_DELAY = 3000;
    private final long GAMEOVER_DELAY = 12000;
    private long gameOverStartTime = 0;
    private long attackDuration = 300;


    private MainMenu mainMenu;
    private PauseMenu menuPause;
    private Environment environment;
    private EnvironmentDrawer mediator;
    private ArrayList<String> input;

    private GameStatus status = GameStatus.START;
    private boolean canSlash = true;
    private int weaponDecision = 1;
    private boolean debugMode = true;
    private boolean wantsToStartGame = false;

    private long protagonistAttackEndTime = 0;
    // --- MUDANÇA: Variável não utilizada foi removida ---
    // private long protagonistHitEndTime = 0; 
    private static final long ATTACK_DURATION = 400; // ms 
    private static final long HIT_DURATION = 200;    // ms 

    private boolean gameOverMusicPlayed = false;
    private boolean victoryMusicPlayed = false;
    private boolean wasPaused = false;

    public static final String gameMusic = "/sounds/gameMusic.wav";
    public static final String cellTheme = "/sounds/cellTheme.wav";
    public static final String gameOverMusic = "/sounds/gameOverMusic.wav";
    public static final String menuMusic = "/sounds/menuSound1.wav";
    public static final String winSound = "/sounds/winSound.wav";
    public static final String attack = "/sounds/gohanAttack.wav";
    
    private static final long EVOLUTION_TIMER = 2000;

    public Game(Environment environment, EnvironmentDrawer mediator, ArrayList<String> input) {
        this.environment = environment;
        this.mediator = mediator;
        this.input = input;
    }

    public Environment getEnvironment() { return environment; }
    public void setEnvironment(Environment environment) { this.environment = environment; }

    public EnvironmentDrawer getMediator() { return mediator; }
    public void setMediator(EnvironmentDrawer mediator) { this.mediator = mediator; }

    public ArrayList<String> getInput() { return input; }
    public void setInput(ArrayList<String> input) { this.input = input; }

    public GameStatus getStatus() { return status; }
    public void setStatus(GameStatus status) {
        this.status = status;
        if (status == GameStatus.START) {
            gameOverMusicPlayed = false;
            victoryMusicPlayed = false;
        }
    }
    
    public boolean isDebugMode() { return debugMode; }
    public void setDebugMode(boolean debugMode) { this.debugMode = debugMode; }

    public MainMenu getMainMenu() { return mainMenu; }
    public void setMainMenu(MainMenu mainMenu) { this.mainMenu = mainMenu; }

    public PauseMenu getMenuPause() { return menuPause; }
    public void setMenuPause(PauseMenu menuPause) { this.menuPause = menuPause; }

    public boolean wantsToStartGame() { return wantsToStartGame; }
    public void setWantsToStartGame(boolean wantsToStartGame) { this.wantsToStartGame = wantsToStartGame; }


    public void loop() {
        this.updateControls();

        switch (this.status) {
            case START:
                this.pauseAnimations(false);
                if (!SoundManager.isCurrentMusic(menuMusic)) {
                    SoundManager.playMusic(menuMusic);
                }
                mediator.drawMainMenu(mainMenu);
                break;

            case RUNNING:
                this.pauseAnimations(false);
                this.running();
                break;

            case PAUSED:
                this.pauseAnimations(true);
                this.pause();
                mediator.drawPauseMenu(menuPause);
                break;

            case WIN:
                this.pauseAnimations(true);
                this.win();
                break;

            case GAME_OVER:
                this.pauseAnimations(true);
                this.gameOver();
                break;
        }
    }

    public void pauseAnimations(boolean value) {
        if (environment.getCurrentLevel() == null) return;
        for (Agent agent : environment.getCurrentLevel().getAgents()) {
            if (agent.getAnimationSystem() != null) {
                agent.getAnimationSystem().setPaused(value);
            }
        }
        for (Shot shot : environment.getCurrentLevel().getShots()) {
            if (shot.getAnimationSystem() != null) {
                shot.getAnimationSystem().setPaused(value);
            }
        }
        for (Object object : environment.getCurrentLevel().getObjects()) {
            if (object.getAnimationSystem() != null) {
                object.getAnimationSystem().setPaused(value);
            }
        }
        if (environment.getProtagonist().getAnimationSystem() != null) {
            environment.getProtagonist().getAnimationSystem().setPaused(value);
        }
    }

    public void gameOver() {
        if (!gameOverMusicPlayed) {
            SoundManager.update();
            SoundManager.stopAll();
            SoundManager.playMusic(Game.gameOverMusic); 
            gameOverMusicPlayed = true;
            gameOverStartTime = System.currentTimeMillis(); 
        }

        environment.updateMessages();
        environment.updateShots();
        environment.updateSlashes();
        mediator.renderGame();
        mediator.drawGameOver();

        if (System.currentTimeMillis() - gameOverStartTime >= GAMEOVER_DELAY) {
            this.status = GameStatus.START;
            gameOverMusicPlayed = false;
            gameOverStartTime = 0;
        }
    }

    public void running() {
        String musicToPlay = environment.getCurrentLevel().getBackgroundMusic();
        if (musicToPlay == null || musicToPlay.isEmpty()) {
            musicToPlay = Game.gameMusic;
        }
    
        if (!SoundManager.isCurrentMusic(musicToPlay)) {
            SoundManager.playMusic(musicToPlay);
        }
    
        if (wasPaused) {
            SoundManager.resumeMusic();
            SoundManager.resumeAllSoundEffects();
            wasPaused = false;
        }
    
        long now = System.currentTimeMillis();
        Agent protagonist = environment.getProtagonist();
    
        // --- LÓGICA DE ANIMAÇÃO DO PROTAGONISTA CORRIGIDA ---
        long protagonistHitEnd = protagonist.getLastHitTime() + HIT_DURATION;

        if (now < protagonistHitEnd) {
            protagonist.getAnimationSystem().setStatus(AnimationStatus.HIT);
        } else if (now < protagonistAttackEndTime) {
            protagonist.getAnimationSystem().setStatus(AnimationStatus.ATTACKING);
        } else {
            if (!input.isEmpty()) {
                boolean isMoving = false;
                if (input.contains("SPACE")) {
                    // Lógica de Ataque
                    switch (weaponDecision) {
                        case 1:
                            Shot shot = protagonist.useWeapon();
                            if (shot != null) {
                                SoundManager.playSound(attack);
                                protagonistAttackEndTime = now + ATTACK_DURATION;
                                environment.getCurrentLevel().getShots().add(shot);
                            }
                            break;
                        case 2:
                            if (canSlash) {
                                canSlash = false;
                                Slash slash = protagonist.useCloseWeapon();
                                if (slash != null) {
                                    SoundManager.playSound(attack);
                                    protagonistAttackEndTime = now + ATTACK_DURATION;
                                    environment.getCurrentLevel().getSlashes().add(slash);
                                }
                            }
                            break;
                    }
                    input.remove("SPACE");
                }
                if (input.contains("UP") || input.contains("DOWN") || input.contains("LEFT") || input.contains("RIGHT")) {
                    isMoving = true;
                }
                protagonist.getAnimationSystem().setStatus(isMoving ? AnimationStatus.RUNNING : AnimationStatus.IDLE);
            } else {
                protagonist.getAnimationSystem().setStatus(AnimationStatus.IDLE);
            }
        }
        protagonist.move(input);
        environment.checkBorders();
        if (!canSlash && now >= protagonistAttackEndTime) {
            canSlash = true;
        }
    
        ArrayList<Agent> agentsToRemove = new ArrayList<>();
        ArrayList<Agent> agentsToAdd = new ArrayList<>();

        for (Agent currentAgent : environment.getCurrentLevel().getAgents()) {
            
            if (currentAgent.isDying()) {
    
                // Lógica de evolução para Cell 1 e Cell 2
                if (currentAgent == GameSet.cell1 || currentAgent == GameSet.cell2) {
                    
                    if (currentAgent.getAnimationSystem().getStatus() == AnimationStatus.DYING) {
                        if (now - currentAgent.getStateStartTime() >= EVOLUTION_TIMER) {
                            currentAgent.getAnimationSystem().setStatus(AnimationStatus.CHARGING);
                            currentAgent.setStateStartTime(now);
                            SoundManager.playSound("/sounds/cellTransformation.wav");
                        }
                    }
                    else if (currentAgent.getAnimationSystem().getStatus() == AnimationStatus.CHARGING) {
                        if (now - currentAgent.getStateStartTime() >= EVOLUTION_TIMER) {
                            int x = currentAgent.getPosX();
                            int y = currentAgent.getPosY();
    
                            if (currentAgent == GameSet.cell1) {
                                agentsToRemove.add(GameSet.cell1);
                                GameSet.cell2.setPosX(x);
                                GameSet.cell2.setPosY(y);
                                GameSet.cell2.setHealth(GameSet.cell2.getFullHealth());
                                GameSet.cell2.setDying(false);
                                GameSet.cell2.getAnimationSystem().setStatus(AnimationStatus.RUNNING);
                                agentsToAdd.add(GameSet.cell2);
                            }
                            else if (currentAgent == GameSet.cell2) {
                                agentsToRemove.add(GameSet.cell2);
                                GameSet.cell3.setPosX(x);
                                GameSet.cell3.setPosY(y);
                                GameSet.cell3.setHealth(GameSet.cell3.getFullHealth());
                                GameSet.cell3.setDying(false);
                                GameSet.cell3.getAnimationSystem().setStatus(AnimationStatus.RUNNING);
                                agentsToAdd.add(GameSet.cell3);
                            }
                        }
                    }
                } 
                // Verifica a morte do boss final
                else if (currentAgent == GameSet.cell3) {
                    final long CELL3_DEATH_DURATION = 750;
                    if (now - currentAgent.getStateStartTime() >= CELL3_DEATH_DURATION) {
                        this.status = GameStatus.WIN;
                        break;
                    }
                }
                continue;
            }
    
            // Lógica Padrão do Inimigo
            long agentHitEnd = currentAgent.getLastHitTime() + HIT_DURATION;
            if (now < agentHitEnd) {
                currentAgent.getAnimationSystem().setStatus(AnimationStatus.HIT);
            } else if (currentAgent.isAttacking() && now < currentAgent.getAttackEndTime()) {
                currentAgent.getAnimationSystem().setStatus(AnimationStatus.ATTACKING);
            } else {
                double oldPosX = currentAgent.getPosX();
                currentAgent.chase(protagonist.getPosX(), protagonist.getPosY());
    
                if (currentAgent.getPosX() != oldPosX) {
                    currentAgent.getAnimationSystem().setStatus(AnimationStatus.RUNNING);
                } else {
                    currentAgent.getAnimationSystem().setStatus(AnimationStatus.IDLE);
                }
    
                if (now - currentAgent.getLastShotTime() >= currentAgent.getShotCooldown()) {
                    Shot shot = currentAgent.useWeapon();
                    if (shot != null) {
                        environment.getCurrentLevel().getShots().add(shot);
                        currentAgent.setLastShotTime(now);
                        currentAgent.setAttackEndTime(now + attackDuration);
                    }
                    Slash slash = currentAgent.useCloseWeapon();
                    if (slash != null) {
                        environment.getCurrentLevel().getSlashes().add(slash);
                        currentAgent.setLastShotTime(now);
                        currentAgent.setAttackEndTime(now + attackDuration);
                    }
                }
            }
        }
        
        if (this.status == GameStatus.WIN) {
            // Se o jogo foi ganho, não faz mais nada neste loop.
        } else {
            if (!agentsToRemove.isEmpty()){
                environment.getCurrentLevel().getAgents().removeAll(agentsToRemove);
            }
            if (!agentsToAdd.isEmpty()){
                environment.getCurrentLevel().getAgents().addAll(agentsToAdd);
            }
            
            for (Object object : environment.getCurrentLevel().getObjects()) {
                for (Agent collidingAgent : environment.getCurrentLevel().getAgents()) {
                    object.onCollide(collidingAgent, environment.getMessages());
                }
                for (Shot shot : environment.getCurrentLevel().getShots()) {
                    object.onCollide(shot, environment.getMessages());
                }
                object.onCollide(protagonist, environment.getMessages());
            }
        
            for (Agent agent : environment.getCurrentLevel().getAgents()) {
                agent.syncDimensions();
            }
            protagonist.syncDimensions();
        
            environment.update();
            mediator.renderGame();
        
            if (protagonist.isDead()) {
                this.status = GameStatus.GAME_OVER;
            }
        
            if (!environment.hasNextLevel() && environment.getCurrentLevel().isCompleted(environment)) {
                this.status = GameStatus.WIN;
            }
        }
    }

    public boolean isAttacking() {
        return environment.getProtagonist().getAnimationSystem().getStatus() == AnimationStatus.ATTACKING;
    }

    public boolean isRunning() {
        return environment.getProtagonist().getAnimationSystem().getStatus() == AnimationStatus.RUNNING;
    }

    public boolean isIdle() {
        return environment.getProtagonist().getAnimationSystem().getStatus() == AnimationStatus.IDLE;
    }

    public void pause() {
        if (!wasPaused) {
            SoundManager.pauseMusic();
            SoundManager.pauseAllSoundEffects();
            wasPaused = true;
        }
        environment.updateMessages();
        mediator.renderGame();
        mediator.drawPauseScreen();
    }

    public void init() {
        SoundManager.playMusic(Game.gameMusic);
        this.status = GameStatus.RUNNING;
        mediator.renderGame();
    }

    public void win() {
        if (!victoryMusicPlayed) {
            SoundManager.stopAll();
            SoundManager.playMusic(Game.winSound);
            victoryMusicPlayed = true;
            victoryStartTime = System.currentTimeMillis();
        }

        mediator.renderGame();
        if (System.currentTimeMillis() - victoryStartTime >= VICTORY_DELAY) {
            this.status = GameStatus.START;
            victoryMusicPlayed = false;
            victoryStartTime = 0;
        }
    }

    private void updateControls() {
        if (this.input.contains("P")) {
            if (this.status.equals(GameStatus.RUNNING)) {
                this.status = GameStatus.PAUSED;
            } else if (this.status.equals(GameStatus.PAUSED)) {
                this.status = GameStatus.RUNNING;
            }
            this.input.remove("P");
        }
    }

    public void handleInput(KeyEvent e) {
        switch (status) {
            case START:
                MainOption mainOpt = mainMenu.handleInput(e.getCode());
                if (mainOpt == MainOption.START_GAME) {
                    setStatus(GameStatus.RUNNING);
                    wantsToStartGame = true;
                } else if (mainOpt == MainOption.EXIT) {
                    javafx.application.Platform.exit();
                }
                break;

            case PAUSED:
                PauseOption pauseOpt = menuPause.handleInput(e.getCode());
                if (pauseOpt == PauseOption.RESUME) {
                    setStatus(GameStatus.RUNNING);
                    menuPause.reset();
                } else if (pauseOpt == PauseOption.GO_BACK_TO_MENU) {
                    setStatus(GameStatus.START);
                    menuPause.reset();
                    mainMenu.reset();
                }
                break;

            case RUNNING:
                if (e.getCode().toString().equals("P")) {
                    setStatus(GameStatus.PAUSED);
                    menuPause.reset();
                } else if (!input.contains(e.getCode().toString())) {
                    input.add(e.getCode().toString());
                }
                break;

            default:
                break;
        }
    }

    public void handleKeyReleased(KeyEvent e) {
        String code = e.getCode().toString();
        if (!code.equals("P")) {
            input.remove(code);
        }
    }
}
