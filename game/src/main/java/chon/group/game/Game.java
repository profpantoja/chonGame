package chon.group.game;

import java.util.ArrayList;
import java.util.List;

import chon.group.game.core.agent.Agent;
import chon.group.game.core.environment.Environment;
import chon.group.game.core.environment.SoundManager;
import chon.group.game.drawer.EnvironmentDrawer;
import javafx.scene.image.Image;

public class Game {

    private Environment environment;
    private EnvironmentDrawer mediator;
    private ArrayList<String> input;
    private GameStatus status = GameStatus.START;
    private boolean debugMode = true;
    private boolean canThrowRod = true;
    private boolean throwRod = false;
    private boolean findFish = false;
    private boolean rodReturning = false;
    private long findFishEndTime = 0;
    private String currentChosenKey = null;
    private long keyChoiceEndTime = 0;
    private boolean waitingForKeyChoice = false;

    private String[] keys = {"A", "M", "V", "E", "DOWN", "LEFT", "RIGHT", "UP"};
    private List<String> keySequence = new ArrayList<>();
    private int currentKeyIndex = 0;

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

    public boolean isDebugMode() {
        return debugMode;
    }

    public void setDebugMode(boolean debugMode) {
        this.debugMode = debugMode;
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
        environment.updateMessages();
        environment.updateShots();
        mediator.renderGame();
        /** Rendering the Game Over Screen */
        mediator.drawGameOver();
    }

    public void running() {
        /** ChonBota Only Moves if the Player Press Something */
        /** Update the protagonist's movements if input exists */
        throwRod();
        if (!input.isEmpty() && canThrowRod) {
            /** ChonBota Shoots Somebody Who Outdrew You */
            /** But only if she has enough energy */
            if (input.contains("SPACE") && canThrowRod) {
                input.remove("SPACE");
                //Shot shot = environment.getProtagonist().useWeapon();
                //if (shot != null)
                //    environment.getCurrentLevel().getShots().add(shot);
                canThrowRod = false;
                throwRod = true;
                SoundManager.playSound("/sounds/powerUp.mp3");
            }
            /* ChonBota's Movements */
            if (input.contains("RIGHT") || input.contains("LEFT")) environment.getProtagonist().move(input);
            Agent fishAgent = environment.getFishAgent();
            fishAgent.setPosX(-400);
            fishAgent.setPosY(-400);
            environment.checkBorders();
        }
        /* ChonBot's Automatic Movements */
        /* Update the other agents' movements */
        //for (Agent agent : environment.getCurrentLevel().getAgents()) {
        //    agent.chase(environment.getProtagonist().getPosX(),
        //            environment.getProtagonist().getPosY());
        //}
        /* ChonBot's Automatic Movements */
        /* Update the other agents' movements */
        //for (Agent agent : environment.getCurrentLevel().getAgents()) {
        //    agent.chase(environment.getProtagonist().getPosX(),
        //            environment.getProtagonist().getPosY());
        //}
        /* Render the game environment and agents */
        findFish();
        handleKeyChoice();
        returnRodUp();
        environment.update();
        mediator.renderGame();
        /* If the agent died in this loop */
        if (environment.getProtagonist().isDead())
            this.status = GameStatus.GAME_OVER;
    }

    public void pause() {
        this.environment.updateMessages();
        mediator.renderGame();
        /** Rendering the Pause Screen */
        mediator.drawPauseScreen();
    }

    public void init() {
        this.status = GameStatus.RUNNING;
        mediator.renderGame();
    }

    public void win() {
        this.status = GameStatus.START;
        mediator.renderGame();
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

    private void throwRod() {
        if (throwRod) {
            List<String> input = new ArrayList<String>();
            input.add("DOWN");
            if (environment.getProtagonist().getPosY() < 0) {
                environment.getProtagonist().move(input);
            }
            else {
                throwRod = false;
                findFish = true;
            }
        }
    }

    private void findFish() {
        if (findFish) {
            if (findFishEndTime == 0) {
                int waitMillis = 2000 + (int)(Math.random() * 2000);
                findFishEndTime = System.currentTimeMillis() + waitMillis;
            }
            if (System.currentTimeMillis() >= findFishEndTime) {
                findFish = false;
                findFishEndTime = 0;
                chooseKeys();
            }
        }
    }

    private void chooseKeys() {
        int collected = environment.getCollectedCount();
        // Example: min increases slowly, max increases faster, both capped at 8
        int minKeys = Math.min(1 + collected / 3, 8);
        int maxKeys = Math.min(2 + collected, 8);
        int sequenceLength = minKeys + (int)(Math.random() * (maxKeys - minKeys + 1));

        // Generate random sequence
        keySequence.clear();
        List<String> keyPool = new ArrayList<>(List.of(keys));
        for (int i = 0; i < sequenceLength; i++) {
            int idx = (int)(Math.random() * keyPool.size());
            keySequence.add(keyPool.get(idx));
        }
        currentKeyIndex = 0;
        showCurrentKey();
    }

    private void showCurrentKey() {
        if (currentKeyIndex < keySequence.size()) {
            currentChosenKey = keySequence.get(currentKeyIndex);
            keyChoiceEndTime = System.currentTimeMillis() + 2000; // 2 seconds to press
            waitingForKeyChoice = true;

            int centerX = environment.getCurrentLevel().getWidth() / 2;
            int centerY = environment.getCurrentLevel().getHeight() / 2;

            Agent keyAgent = environment.getKeyAgent();
            keyAgent.setPosX(centerX - keyAgent.getWidth() / 2);
            keyAgent.setPosY(centerY - keyAgent.getHeight() / 2);
            keyAgent.setImage(new Image(getClass().getResource("/images/agents/" + currentChosenKey + "Key.png").toExternalForm()));
        } else {
            SoundManager.playSound("/sounds/happy.mp3");
            environment.setCollectedCount(environment.getCollectedCount() + 1);
            if (environment.getCollectedCount() > environment.getScore()) environment.setScore(environment.getCollectedCount());
            waitingForKeyChoice = false;
            currentChosenKey = null;
            rodReturning = true;
            Agent keyAgent = environment.getKeyAgent();
            Agent fishAgent = environment.getFishAgent();
            Agent protagonist = environment.getProtagonist();
            keyAgent.setPosX(-400);
            keyAgent.setPosY(-400);
            fishAgent.setPosX(protagonist.getPosX() - 10);
            fishAgent.setPosY(protagonist.getPosY() + protagonist.getHeight() - 26);
        }
    }

    private void handleKeyChoice() {
        if (waitingForKeyChoice) {
            if (input.contains(currentChosenKey)) {
                SoundManager.playSound("/sounds/pickupCoin.mp3");
                input.remove(currentChosenKey);
                waitingForKeyChoice = false;
                currentChosenKey = null;
                currentKeyIndex++;
                showCurrentKey();
            } else {
                Agent keyAgent = environment.getKeyAgent();
                Agent fishAgent = environment.getKeyAgent();
                for (String key : input) {
                    if (!key.equals(currentChosenKey)) {
                        SoundManager.playSound("/sounds/synth.mp3");
                        if (environment.getCollectedCount() > environment.getScore()) environment.setScore(environment.getCollectedCount());
                        environment.setCollectedCount(0);
                        input.remove(key);
                        waitingForKeyChoice = false;
                        currentChosenKey = null;
                        rodReturning = true;
                        keySequence.clear();
                        currentKeyIndex = 0;
                        keyAgent.setPosX(-400);
                        keyAgent.setPosY(-400);
                        fishAgent.setPosX(-400);
                        fishAgent.setPosY(-400);
                        return;
                    }
                }
                if (System.currentTimeMillis() >= keyChoiceEndTime) {
                    SoundManager.playSound("/sounds/synth.mp3");
                    if (environment.getCollectedCount() > environment.getScore()) environment.setScore(environment.getCollectedCount());
                    environment.setCollectedCount(0);
                    waitingForKeyChoice = false;
                    currentChosenKey = null;
                    rodReturning = true;
                    keySequence.clear();
                    currentKeyIndex = 0;
                    keyAgent.setPosX(-400);
                    keyAgent.setPosY(-400);
                    fishAgent.setPosX(-400);
                    fishAgent.setPosY(-400);
                }
            }
        }
    }

    private void returnRodUp() {
        if (rodReturning) {
            Agent protagonist = environment.getProtagonist();
            Agent fishAgent = environment.getFishAgent();
            if (protagonist.getPosY() > -297) {
                List<String> input = new ArrayList<>();
                input.add("UP");
                protagonist.move(input);
                fishAgent.move(input);
            } else {
                rodReturning = false;
                canThrowRod = true;
            }
        }
    }
}
