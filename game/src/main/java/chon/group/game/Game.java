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
import javafx.scene.input.KeyEvent;
import chon.group.game.sound.SoundManager;



public class Game {
    

    private long victoryStartTime = 0;
    private final long VICTORY_DELAY = 3000; 
    private MainMenu mainMenu;
    private PauseMenu menuPause;
    private Environment environment;
    private EnvironmentDrawer mediator;
    ArrayList<String> input;
    private GameStatus status = GameStatus.START;
    /* If the game is paused or not. */
    private boolean isPaused = false;
    /* If the player can slash again or not. */
    private boolean canSlash = true;
    /* If the player has made a decision about the weapon to use. */
    private int weaponDecision = 1; // 0  = both, 1 = fireball, 2 = sword
    private boolean debugMode = true;
    private boolean wantsToStartGame = false;

    private boolean gameOverMusicPlayed = false;
    private boolean victoryMusicPlayed = false;
    private boolean wasPaused = false;
    
    public static final String gameMusic = "/sounds/gameMusic.wav";
    public static final String gameOverMusic = "/sounds/gameOverMusic.wav";
    public static final String menuMusic = "/sounds/menuSound.wav";
    public static final String winSound = "/sounds/winSound.wav";
    public static final String attack = "/sounds/attackFX.wav";

    public Game(Environment environment, EnvironmentDrawer mediator, ArrayList<String> input) {
        this.environment = environment;
        this.mediator = mediator;
        this.input = input;
    }
    
    public Environment getEnvironment() {
        return environment;
    }
    
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
    
    public EnvironmentDrawer getMediator() {
        return mediator;
    }
    
    public void setMediator(EnvironmentDrawer mediator) {
        this.mediator = mediator;
    }
    
    public ArrayList<String> getInput() {
        return input;
    }
    
    public void setInput(ArrayList<String> input) {
        this.input = input;
    }
    
    public GameStatus getStatus() {
        return status;
    }
    
    public void setStatus(GameStatus status) {
        this.status = status;
        // Reset music flags when the game status changes to START.
        if (status == GameStatus.START) {
                gameOverMusicPlayed = false;
                victoryMusicPlayed = false;
            }
    }
    
    public boolean isDebugMode() {
        return debugMode;
    }
    
    public void setDebugMode(boolean debugMode) {
        this.debugMode = debugMode;
    }
    
    public MainMenu getMainMenu() { 
        return mainMenu;
    }
    
    public void setMainMenu(MainMenu mainMenu) {
        this.mainMenu = mainMenu;
    }
    
    public PauseMenu getMenuPause() { 
        return menuPause;
    }
    
    public void setMenuPause(PauseMenu menuPause) {
        this.menuPause = menuPause;
    }
    
    public boolean wantsToStartGame() {
        return wantsToStartGame;
    }
    
    public void setWantsToStartGame(boolean wantsToStartGame) {
        this.wantsToStartGame = wantsToStartGame;
    }    

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
        for (Agent agent : environment.getCurrentLevel().getAgents()) {
            if (agent.getAnimationSystem() != null) agent.getAnimationSystem().setPaused(value);
        }
        for (Shot shot : environment.getCurrentLevel().getShots()) {
            if (shot.getAnimationSystem() != null) shot.getAnimationSystem().setPaused(value);
        }
        for (Object object : environment.getCurrentLevel().getObjects()) {
            if (object.getAnimationSystem() != null) object.getAnimationSystem().setPaused(value);
        }
        if (environment.getProtagonist().getAnimationSystem() != null) environment.getProtagonist().getAnimationSystem().setPaused(value);
    }

    public void gameOver() {
        if (!gameOverMusicPlayed) {
            SoundManager.update();
            SoundManager.stopAll();
            SoundManager.playMusic(Game.gameOverMusic);
            gameOverMusicPlayed = true;
        }
        environment.updateMessages();
        environment.updateShots();
        environment.updateSlashes();
        mediator.renderGame();
        

        /** Rendering the Game Over Screen */
        mediator.drawGameOver();
    }

    public void running() {
        String musicToPlay = environment.getCurrentLevel().getBackgroundMusic();
        if (musicToPlay == null || musicToPlay.isEmpty()) {
            musicToPlay = Game.gameMusic; // fallback para música padrão
        }

        if (!SoundManager.isCurrentMusic(musicToPlay)) {
            SoundManager.playMusic(musicToPlay);
        }
         // If the game was paused, return the sounds and musics.
        if (wasPaused) {
            SoundManager.resumeMusic();
            SoundManager.resumeAllSoundEffects();
            wasPaused = false;
        }
        

        /** ChonBota Only Moves if the Player Press Something */
        /** Update the protagonist's movements if input exists */
        if (!input.isEmpty()) {
            /** ChonBota Shoots Somebody Who Outdrew You */
            /** But only if she has enough energy */


              /* chonbota Only Moves if the Player Press Something */
            /* Update the protagonist's movements if input exists */
                if (!input.isEmpty()) {
                    switch(weaponDecision){
                        case 1:// Fireball weapon available
                            if (input.contains("SPACE")) {
                                input.remove("SPACE");
                                Shot shot = environment.getProtagonist().useWeapon();
                                if (shot != null){
                                    SoundManager.playSound(attack); 
                                    environment.getCurrentLevel().getShots().add(shot);
                                }
                            }
                        break;

                        case 2: // Sword weapon available
                            if (input.contains("SPACE") && canSlash) {
                                input.remove("SPACE");
                                canSlash = true; // prevents multiple slashes in a row
                                Slash slash = environment.getProtagonist().useCloseWeapon();
                                
                                if (slash != null){
                                SoundManager.playSound(attack); 
                                environment.getCurrentLevel().getSlashes().add(slash);
                                }
                                
                            }

                        break;

                     }

             }

            /* ChonBota's Movements */
            environment.getProtagonist().move(input);
            environment.checkBorders();
        } else {
            /** If no input, ChonBota stops running */
            environment.getProtagonist().getAnimationSystem().setStatus(AnimationStatus.IDLE);
        }
        /* ChonBot's Automatic Movements */
        /* Update the other agents' movements */
        for (Agent agent : environment.getCurrentLevel().getAgents()) {
            agent.chase(environment.getProtagonist().getPosX(),
                    environment.getProtagonist().getPosY());
        }
        /* ChonBot's Automatic Movements */
        /* Update the other agents' movements */
        for (Agent agent : environment.getCurrentLevel().getAgents()) {
            agent.chase(environment.getProtagonist().getPosX(),
                    environment.getProtagonist().getPosY());
        }
        /* Update the collisions between agents and shots */
        for (Object object : environment.getCurrentLevel().getObjects()) {
            for (Agent agent : environment.getCurrentLevel().getAgents()) {
                object.onCollide(agent, environment.getMessages());
            }
            for (Shot shot : environment.getCurrentLevel().getShots()) {
                object.onCollide(shot, environment.getMessages());
            }
            object.onCollide(environment.getProtagonist(), environment.getMessages());
        }
        for (Agent agent: environment.getCurrentLevel().getAgents()) {
            agent.syncDimensions();
        }
        environment.getProtagonist().syncDimensions();
        /* Render the game environment and agents */
        environment.update();
        mediator.renderGame();
        /* If the agent died in this loop */
        if (environment.getProtagonist().isDead()){
            this.status = GameStatus.GAME_OVER;
        
        if (!environment.hasNextLevel() && environment.getCurrentLevel().isCompleted(environment)) {
            this.status = GameStatus.WIN;
        }



        /* Enemies Shooting */
        long currentTime = System.currentTimeMillis();

        for (Agent agent : environment.getCurrentLevel().getAgents()) {

                if (currentTime - agent.getLastShotTime() >= agent.getShotCooldown()) {
                    Shot shot = agent.useWeapon();
                    if (shot != null) {
                        environment.getCurrentLevel().getShots().add(shot);
                        agent.setLastShotTime(currentTime);
                    }
                }

        }

        for (Agent agent : environment.getCurrentLevel().getAgents()) {

                if (currentTime - agent.getLastShotTime() >= agent.getShotCooldown()) {
                    Slash slash = agent.useCloseWeapon();
                    if (slash != null) {
                        environment.getCurrentLevel().getSlashes().add(slash);
                        agent.setLastShotTime(currentTime);
                    }
                }

        }

    }

    public void pause() {
        // Just pause the music when the game is paused.
        if (!wasPaused) {
            SoundManager.pauseMusic();
            SoundManager.pauseAllSoundEffects();
            wasPaused = true;
        }
        this.environment.updateMessages();
        mediator.renderGame();
        /** Rendering the Pause Screen */
        mediator.drawPauseScreen();
    }

    public void init() {
        // Initialize the game environment and start the music.
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
            } else {
                if (this.status.equals(GameStatus.PAUSED))
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
            default: break;
        }
    }

    public void handleKeyReleased(KeyEvent e) {
        String code = e.getCode().toString();
        if (!code.equals("P")) {
            input.remove(code);
        }
    }


}
