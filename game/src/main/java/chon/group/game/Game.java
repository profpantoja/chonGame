package chon.group.game;

import java.util.ArrayList;

import chon.group.game.core.agent.Agent;
import chon.group.game.core.environment.Environment;
import chon.group.game.core.weapon.Shot;
import chon.group.game.drawer.EnvironmentDrawer;
import chon.group.game.sound.SoundManager;

public class Game {
    
    private Environment environment;
    private EnvironmentDrawer mediator;
    ArrayList<String> input;
    private GameStatus status = GameStatus.START;

    private boolean gameOverMusicPlayed = false;
    private boolean victoryMusicPlayed = false;
    private boolean wasPaused = false;

    public static final String gameMusic = "/sounds/gameMusic.wav";
    public static final String gameOverMusic = "/sounds/gameOverMusic.wav";
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
    }

    public void loop() {
        this.updateControls();
        switch (this.status) {
            case START:
                this.init();
                break;
            case RUNNING:
                this.running();
                break;
            case PAUSED:
                this.pause();
                break;
            case WIN:
                this.win();
                break;
            case GAME_OVER:
                this.gameOver();
                break;
        }
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
        mediator.renderGame();
        /** Rendering the Game Over Screen */
        mediator.drawGameOver();
    }

    public void running() {
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
            if (input.contains("SPACE")) {
                input.remove("SPACE");
                Shot shot = environment.getProtagonist().useWeapon();
                if (shot != null){
                    // Play the attack sound effect while have stamina for shooting.
                    SoundManager.playSound(attack); 
                    environment.getShots().add(shot);
                }
            }
            /* ChonBota's Movements */
            environment.getProtagonist().move(input);
            environment.checkBorders();
        }
        /* ChonBot's Automatic Movements */
        /* Update the other agents' movements */
        for (Agent agent : environment.getAgents()) {
            agent.chase(environment.getProtagonist().getPosX(),
                    environment.getProtagonist().getPosY());
        }
        /* Render the game environment and agents */
        environment.update();
        mediator.renderGame();
        /* If the agent died in this loop */
        if (environment.getProtagonist().isDead())
            this.status = GameStatus.GAME_OVER;
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
    }

    public void win() {
        if (!victoryMusicPlayed) {
            SoundManager.stopAll();
            SoundManager.playMusic(Game.winSound); 
            victoryMusicPlayed = true;
        }
        this.status = GameStatus.START;
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

}
