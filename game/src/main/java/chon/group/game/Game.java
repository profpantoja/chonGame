package chon.group.game;

import java.util.ArrayList;
import java.util.Iterator;

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
import javafx.scene.input.KeyEvent;

public class Game {

    private long victoryStartTime = 0;
    private final long VICTORY_DELAY = 3000;
    private final long GAMEOVER_DELAY = 12000;
    private long gameOverStartTime = 0;

    private MainMenu mainMenu;
    private PauseMenu menuPause;
    private Environment environment;
    private EnvironmentDrawer mediator;
    private ArrayList<String> input;

    private GameStatus status = GameStatus.START;
    private boolean canSlash = true;
    private static final int weaponDecision = GameSet.getWeaponDecision();
    private boolean debugMode = true;
    private boolean wantsToStartGame = false;

    private long protagonistAttackEndTime = 0;
    private long protagonistHitEndTime = 0;
        
    private static final long ATTACK_DURATION = 400; // ms (aumentado para garantir visibilidade)
    private static final long HIT_DURATION = 400;    // ms (aumentado também)

    private boolean gameOverMusicPlayed = false;
    private boolean victoryMusicPlayed = false;
    private boolean wasPaused = false;

    public static final String gameMusic = "/sounds/starWarsThemed.mp3";
    public static final String BossMusic = "/sounds/starWarsDarthVader'sTheme.mp3";
    public static final String gameOverMusic = "/sounds/gameOverMusic.wav";
    public static final String menuMusic = "/sounds/starWarsThemed.mp3";
    public static final String winSound = "/sounds/starWarsWin.mp3";
    public static final String attack = "/sounds/attackFX.wav";
    public static final String saberSwing = "/sounds/saberSwing.wav";
    private static final String blasterSFX = "/sounds/blaster.wav";



    public Game(Environment environment, EnvironmentDrawer mediator, ArrayList<String> input) {
        this.environment = environment;
        this.mediator = mediator;
        this.input = input;
        }
        /*
        public void createHanSolo(){
        Agent scout = new Agent(125, 530, 100, 85, 5, 1500, "/images/agents/chonBot.png", false, false);
            AnimationSpritesheet idleScout = new SimpleAnimationSpritesheet(70, 120, 4, 1000, "/images/agents/scout/scoutIdleFlipped.png");
            AnimationSpritesheet runScout = new SimpleAnimationSpritesheet(70, 120, 4, 1000, "/images/agents/scout/scoutRunningFlipped.png");
            AnimationSpritesheet attackScout = new SimpleAnimationSpritesheet(100, 120, 6, 1000, "/images/agents/scout/scoutShootFlipped.png");
            AnimationSpritesheet hitScout = new SimpleAnimationSpritesheet(90, 120, 4, 1000, "/images/agents/scout/scoutHitFlipped.png");

            Weapon blaster = new DL44(400, 390, 0, 0, 3, 0, 0.05, "", false);
            scout.setWeapon(blaster);
            AnimationGraphics scoutGraphics = new AnimationGraphics();
            scoutGraphics.addSpritesheet(AnimationStatus.IDLE, idleScout);
            scoutGraphics.addSpritesheet(AnimationStatus.RUNNING, runScout);
            scoutGraphics.addSpritesheet(AnimationStatus.ATTACKING, attackScout);
            scoutGraphics.addSpritesheet(AnimationStatus.HIT, hitScout);
            AnimationSystem scoutSystem = new AnimationSystem(scoutGraphics);
            scout.setAnimationSystem(scoutSystem);
            environment.setProtagonist(scout);

            
        }
        
         * 
        */
    /*
    public void createLuke(){
           Agent luke = new Agent(125, 530, 120, 85, 5, 2000, "/images/agents/luke.png", false, false);
            AnimationSpritesheet idleLuke = new SimpleAnimationSpritesheet(90, 170, 6, 1000, "/images/agents/luke/lukeIdle.png");
            AnimationSpritesheet runLuke = new SimpleAnimationSpritesheet(90, 170, 7, 1000, "/images/agents/luke/lukeRunning.png");
            AnimationSpritesheet attackLuke = new SimpleAnimationSpritesheet(160, 170, 10, 300, "/images/agents/luke/lukeAttack.png");
            AnimationSpritesheet hitLuke = new SimpleAnimationSpritesheet(90, 170, 4, 1000, "/images/agents/luke/lukeHit.png");
            AnimationSpritesheet dyingLuke = new SimpleAnimationSpritesheet(100, 170, 2, 1000, "/images/agents/luke/lukeDead.png");
            CloseWeapon lightSaber = new LightSaber(400, 390, 0, 0, 3, 0, "", false);
            luke.setCloseWeapon(lightSaber);
            environment.setProtagonist(luke);
            AnimationGraphics lukeGraphics = new AnimationGraphics();
            lukeGraphics.addSpritesheet(AnimationStatus.IDLE, idleLuke);
            lukeGraphics.addSpritesheet(AnimationStatus.RUNNING, runLuke);
            lukeGraphics.addSpritesheet(AnimationStatus.ATTACKING, attackLuke);
            lukeGraphics.addSpritesheet(AnimationStatus.HIT, hitLuke);
            lukeGraphics.addSpritesheet(AnimationStatus.DYING, dyingLuke);
            AnimationSystem lukeSystem = new AnimationSystem(lukeGraphics);
            luke.setAnimationSystem(lukeSystem);
            environment.setProtagonist(luke);
        }
         * 
         */

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

        if (environment.getProtagonist().getLastHitTime() > 0 &&
            now - environment.getProtagonist().getLastHitTime() < HIT_DURATION) {
            protagonistHitEndTime = environment.getProtagonist().getLastHitTime() + HIT_DURATION;
        }

        if (now < protagonistHitEndTime) {
            environment.getProtagonist().getAnimationSystem().setStatus(AnimationStatus.HIT);
        } else if (now < protagonistAttackEndTime) {
            environment.getProtagonist().getAnimationSystem().setStatus(AnimationStatus.ATTACKING);
        } else {
            // --- Ataque ou movimento
            if (!input.isEmpty()) {
                switch (weaponDecision) {
                    case 1:
                        if (input.contains("SPACE")) {
                            input.remove("SPACE");
                            Shot shot = environment.getProtagonist().useWeapon();
                            if (shot != null) {
                                SoundManager.playSound(blasterSFX);
                                environment.getProtagonist().getAnimationSystem().setStatus(AnimationStatus.ATTACKING);
                                protagonistAttackEndTime = now + ATTACK_DURATION;
                                environment.getCurrentLevel().getShots().add(shot);
                            }
                        }
                        break;

                    case 2:
                        if (input.contains("SPACE") && canSlash) {
                            input.remove("SPACE");
                            canSlash = false;
                            Slash slash = environment.getProtagonist().useCloseWeapon();
                            if (slash != null) {
                                SoundManager.playSound(saberSwing);
                                environment.getProtagonist().getAnimationSystem().setStatus(AnimationStatus.ATTACKING);
                                protagonistAttackEndTime = now + ATTACK_DURATION;
                                environment.getCurrentLevel().getSlashes().add(slash);
                            }
                        }
                        break;
                }

                if (!canSlash && now >= protagonistAttackEndTime) {
                    canSlash = true;
                }

                environment.getProtagonist().move(input);
                environment.getProtagonist().getAnimationSystem().setStatus(AnimationStatus.RUNNING);
                environment.checkBorders();
            } else {
                environment.getProtagonist().move(input);
                environment.getProtagonist().getAnimationSystem().setStatus(AnimationStatus.IDLE);
                environment.checkBorders();
            }
        }

        for (Agent agent : environment.getCurrentLevel().getAgents()) {
            long agentHitEnd = agent.getLastHitTime() + HIT_DURATION;
            if (now < agentHitEnd) {
                agent.getAnimationSystem().setStatus(AnimationStatus.HIT);
            } else if (agent.isAttacking() && now < agent.getAttackEndTime()) {
                agent.getAnimationSystem().setStatus(AnimationStatus.ATTACKING);
            } else {
                agent.chase(environment.getProtagonist().getPosX(), environment.getProtagonist().getPosY());
                agent.getAnimationSystem().setStatus(AnimationStatus.RUNNING);
            }
        }

        for (Object object : environment.getCurrentLevel().getObjects()) {
            for (Agent agent : environment.getCurrentLevel().getAgents()) {
                object.onCollide(agent, environment.getMessages());
            }
            for (Shot shot : environment.getCurrentLevel().getShots()) {
                object.onCollide(shot, environment.getMessages());
            }
            object.onCollide(environment.getProtagonist(), environment.getMessages());
        }

        for (Agent agent : environment.getCurrentLevel().getAgents()) {
            agent.syncDimensions();
        }
        environment.getProtagonist().syncDimensions();

        environment.update();
        mediator.renderGame();

        if (environment.getProtagonist().isDead()) {
            this.status = GameStatus.GAME_OVER;
        }

        if (!environment.hasNextLevel() && environment.getCurrentLevel().isCompleted(environment)) {
            this.status = GameStatus.WIN;
        }


        for (Agent agent : environment.getCurrentLevel().getAgents()) {
        if (now - agent.getLastShotTime() >= agent.getShotCooldown()) {

            Shot shot = agent.useWeapon();
            if (shot != null) {
                environment.getCurrentLevel().getShots().add(shot);
                agent.setLastShotTime(now);
                agent.setAttacking(true);
                agent.setAttackEndTime(now + ATTACK_DURATION); // você pode usar uma constante ou criar outra para inimigos
                SoundManager.playSound(blasterSFX);

            }
            /* */
           Slash slash = agent.useCloseWeapon();

                    if (environment.getProtagonist() != null && environment.intersect(environment.getProtagonist(), agent)) {

                        if (agent.getCloseWeapon() != null) {
                            if (slash != null) {
                                environment.getCurrentLevel().getSlashes().add(slash);
                                agent.setLastShotTime(now);
                            agent.setAttacking(true);
                            agent.setAttackEndTime(now + ATTACK_DURATION);

                        }
                    }
                }
        }
        }
        
        Iterator<Shot> shotIterator = environment.getCurrentLevel().getShots().iterator();

        while (shotIterator.hasNext()) {
            Shot shot = shotIterator.next();

            for (Slash slash : environment.getCurrentLevel().getSlashes()) {
                if (environment.intersect(slash, shot)) {
                    shotIterator.remove(); 
                    shot.setOwner(environment.getProtagonist());
                    break; 
                }
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
