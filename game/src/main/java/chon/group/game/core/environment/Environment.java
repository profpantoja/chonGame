package chon.group.game.core.environment;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import chon.group.game.core.agent.Agent;
import chon.group.game.core.agent.Entity;
import chon.group.game.core.agent.Object;
import chon.group.game.core.weapon.Shot;
import chon.group.game.messaging.Messenger;
import chon.group.game.sound.Sound;
import chon.group.game.sound.SoundEvent;
import javafx.scene.image.Image;

/**
 * Represents the game environment, including properties such as dimensions,
 * position, background image, agents, protagonist, objects, shots, messages,
 * and camera.
 * The environment is responsible for updating the state of all elements,
 * detecting collisions, and tracking game progress such as score and
 * collectible status.
 */
public class Environment {

    /** The background image for the pause screen. */
    private Image pauseImage;

    /** The background image for the game over screen. */
    private Image gameOverImage;

    /** The background image for the game over screen. */
    private Image theEndImage;

    /** The protagonist of the environment. */
    private Agent protagonist;

    private List<Level> levels;

    private Level currentLevel;

    /** The messenger system. */
    private Messenger messenger;

    /** List of sounds currently being emitted. */
    private List<Sound> sounds;

    /** The camera used to follow the protagonist. */
    private Camera camera;

    private Panel panel;

    /** Number of objects collected so far. */
    private int collectedCount = 0;

    /** Current score of the player. */
    private int score = 0;

    private boolean debugMode = false;

    /**
     * Constructor to initialize the environment with dimensions, position, and a
     * background image.
     *
     * @param posX        the initial X position of the environment
     * @param posY        the initial Y position of the environment
     * @param width       the width of the environment
     * @param screenWidth the screen width used for camera calculations
     * @param pathImage   the path to the background image
     */
    public Environment(int width, double screenWidth, Panel panel) {
        this.sounds = new ArrayList<Sound>();
        this.levels = new ArrayList<Level>();
        this.camera = new Camera(screenWidth, width, 0.49, 0.51);
        this.messenger = new Messenger();
        this.panel = panel;
    }

    public Image getPauseImage() {
        return pauseImage;
    }

    /**
     * Sets the background image for the pause screen.
     *
     * @param pathImage the path to the image
     */
    public void setPauseImage(String pathImage) {
        this.pauseImage = new Image(getClass().getResource(pathImage).toExternalForm());
    }

    public Image getGameOverImage() {
        return gameOverImage;
    }

    /**
     * Sets the background image for the game over screen.
     *
     * @param pathImage the path to the image
     */
    public void setGameOverImage(String pathImage) {
        this.gameOverImage = new Image(getClass().getResource(pathImage).toExternalForm());
    }

    public Image getTheEndImage() {
        return theEndImage;
    }

    public void setTheEndImage(String pathImage) {
        this.theEndImage = new Image(getClass().getResource(pathImage).toExternalForm());
    }

    public Agent getProtagonist() {
        return protagonist;
    }

    /**
     * Sets the protagonist of the game and attaches the camera to it.
     *
     * @param protagonist the protagonist agent
     */
    public void setProtagonist(Agent protagonist) {
        this.protagonist = protagonist;
        if (camera != null)
            camera.setTarget(protagonist);
    }

    public List<Level> getLevels() {
        return levels;
    }

    public void setLevels(List<Level> levels) {
        this.levels = levels;
    }

    public Level getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(Level currentLevel) {
        this.currentLevel = currentLevel;
    }

    public Messenger getMessenger() {
        return this.messenger;
    }

    public List<Sound> getSounds() {
        return sounds;
    }

    public void setSounds(List<Sound> sounds) {
        this.sounds = sounds;
    }

    public Camera getCamera() {
        return camera;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    public Panel getPanel() {
        return panel;
    }

    public void setPanel(Panel panel) {
        this.panel = panel;
    }

    /**
     * Gets the number of objects collected by the protagonist.
     *
     * @return the number of collected objects
     */
    public int getCollectedCount() {
        return collectedCount;
    }

    public void setCollectedCount(int collectedCount) {
        this.collectedCount = collectedCount;
    }

    /**
     * Gets the current score of the player.
     *
     * @return the current score
     */
    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public boolean isDebugMode() {
        return debugMode;
    }

    public void setDebugMode(boolean debugMode) {
        this.debugMode = debugMode;
    }

    /**
     * Ensures the protagonist stays within the boundaries of the environment.
     */
    public void checkBorders() {
        int bottomY = currentLevel.getBottomY();
        int topY = currentLevel.getTopY();
        if (protagonist.getPosX() < 0)
            protagonist.setPosX(0);
        if ((protagonist.getPosX() + protagonist.getWidth()) > this.currentLevel.getWidth())
            protagonist.setPosX(this.currentLevel.getWidth() - protagonist.getWidth());
        /* It ensures an internal pre-defined boundary in Y. */
        if (protagonist.getPosY() < topY)
            protagonist.setPosY(topY);
        if (protagonist.getPosY() + protagonist.getHeight() > bottomY)
            protagonist.setPosY(bottomY - protagonist.getHeight());
    }

    /**
     * Detects collisions between the protagonist and agents.
     * Applies damage if a collision is detected.
     */
    public void detectCollision() {
        /**
         * It detects if other agents have collided with the protagonist. It also
         * verifies if any agents has collided with non-collectible obstacles.
         */
        for (Agent agent : this.currentLevel.getAgents()) {
            /* It verifies a live agents vs. protagonist. */
            if (!agent.isDead())
                if (protagonist != null && intersect(protagonist, agent)) {
                    int damage = 900;
                    protagonist.takeDamage(damage, messenger.getMessages(), sounds);
                }
            /*
             * It verifies agents vs. obstacles. The collision can only happens with non
             * collectible objects.
             */
            for (Object object : this.currentLevel.getObjects()) {
                if (!object.isCollectible() && !object.isDestroyed()) {
                    this.onCollision(agent, object);
                }
            }
        }
        /**
         * It verifies if the protagonist has collided with non-collectible obstacles.
         * The collision can only happens with non
         * collectible objects.
         */
        for (Object object : this.currentLevel.getObjects()) {
            if (!object.isCollectible() && !object.isDestroyed()) {
                this.onCollision(protagonist, object);
            }
        }
    }

    /**
     * Checks if an agent and an object intersect (collide) based on a direction. If
     * so, it adjusts its position.
     *
     * @param a the agent
     * @param b the object
     *
     */
    public void onCollision(Agent agent, Object object) {
        if (!intersect(agent, object)) {
            return;
        }

        int agentLeft = agent.getPosX();
        int agentRight = agent.getPosX() + agent.getHitbox().getWidth();
        int agentTop = agent.getPosY();
        int agentBottom = agent.getPosY() + agent.getHitbox().getHeight();

        int objectLeft = object.getPosX();
        int objectRight = object.getPosX() + object.getHitbox().getWidth();
        int objectTop = object.getPosY();
        int objectBottom = object.getPosY() + object.getHitbox().getHeight();

        int overlapLeft = agentRight - objectLeft;
        int overlapRight = objectRight - agentLeft;
        int overlapTop = agentBottom - objectTop;
        int overlapBottom = objectBottom - agentTop;

        int minOverlapX = Math.min(overlapLeft, overlapRight);
        int minOverlapY = Math.min(overlapTop, overlapBottom);

        if (minOverlapX < minOverlapY) {
            if (overlapLeft < overlapRight) {
                agent.setPosX(objectLeft - agent.getHitbox().getWidth() - 1);
            } else {
                agent.setPosX(objectRight + 1);
            }
        } else {
            if (overlapTop < overlapBottom) {
                agent.setPosY(objectTop - agent.getHitbox().getHeight() - 1);
            } else {
                agent.setPosY(objectBottom + 1);
            }
        }
    }

    /**
     * Checks if two entities intersect (collide).
     *
     * @param a the first entity
     * @param b the second entity
     * @return true if their areas overlap, false otherwise
     */
    private boolean intersect(Entity a, Entity b) {
        return a.getPosX() < b.getPosX() + b.getHitbox().getWidth() &&
                a.getPosX() + a.getHitbox().getWidth() > b.getPosX() &&
                a.getPosY() < b.getPosY() + b.getHitbox().getHeight() &&
                a.getPosY() + a.getHitbox().getHeight() > b.getPosY();
    }

    /**
     * Updates all collectible objects in the environment.
     * Makes them follow the protagonist and handles collection.
     */
    public void updateObjects() {
        Iterator<Object> iterator = this.currentLevel.getObjects().iterator();
        while (iterator.hasNext()) {
            Object object = iterator.next();
            if (!object.isTerminated()) {
                object.idle();
                if (!object.isCollected() && object.isCollectible()) {
                    object.follow(protagonist);
                    double collectRadius = 20;
                    double dx = object.getPosX() - protagonist.getPosX();
                    double dy = object.getPosY() - protagonist.getPosY();
                    double squaredDistance = dx * dx + dy * dy;
                    /**
                     * If the radius between the agent and the object is less than 20 pxls then it
                     * is collected.
                     */
                    if (squaredDistance < collectRadius * collectRadius) {
                        this.getSounds().add(object.getSoundSet().get(SoundEvent.COLLECT));
                        object.onCollect();
                        collectedCount++;
                        score += 10;
                    }
                }
                /* If it was collected then it is removed. */
                else if (object.isCollected() && object.isCollectible()) {
                    iterator.remove();
                }
            }
        }
    }

    /**
     * Updates the state of all shots in the environment.
     * Handles movement, boundary removal, and collision with agents or protagonist.
     */
    public void updateShots() {
        // It caches the level.
        Level level = this.currentLevel;
        // It gets the list of shots.
        Iterator<Shot> itShot = level.getShots().iterator();
        /*
         * While there is a next available shot. The position of each conditional block
         * defines the collision priority.
         */
        while (itShot.hasNext()) {
            Shot shot = itShot.next();
            // Shots can expire based on their range.
            if (shot.hasExpired()) {
                itShot.remove();
                continue;
            }
            /* if the shot's position went off the level width, it is removed. */
            if (shot.getPosX() > level.getWidth() || shot.getPosX() + shot.getWidth() < 0) {
                itShot.remove();
                continue;
            }
            // Indicates whether the shot was removed.
            boolean removed = false;
            /* If the remaining shots hit (in)destructible and no collectible objects. */
            for (Object object : level.getObjects()) {
                /*
                 * The shot hit an object if it is not terminated. The shot may pass by
                 * terminated objects.
                 */
                if (object.isTerminated()) {
                    continue;
                }
                /* If there is no intersection, the shot must go on. */
                if (!intersect(object, shot)) {
                    continue;
                }
                /* If the object is destructible, it must take some damage. */
                if (object.isDestructible()) {
                    object.takeDamage(shot.getDamage(), messenger.getMessages(), sounds);
                    /* Then the shot is removed. */
                    itShot.remove();
                    removed = true;
                    break;
                }
                /*
                 * If the object is indestructible, it is necessary to verify if it collectible
                 * or not. If it is collectible, the shot must go on. Otherwise it should be
                 * removed.
                 */
                if (!object.isCollectible()) {
                    itShot.remove();
                    removed = true;
                    break;
                }
                /*
                 * The shot must goes on. So, it leaves the object iterator and searches other
                 * conditions.
                 */
                break;
            }
            /* If this shot was removed, then move to the next shot. */
            if (removed) {
                continue;
            }
            /* The same as before but now with all other agents. */
            for (Agent agent : level.getAgents()) {
                // The shot passes by dead agents.
                if (agent.isDead()) {
                    continue;
                }
                // If it hits an agent.
                if (intersect(agent, shot)) {
                    agent.takeDamage(shot.getDamage(), messenger.getMessages(), sounds);
                    itShot.remove();
                    removed = true;
                    break;
                }
            }
            /* If this shot was removed, then move to the next shot. */
            if (removed) {
                continue;
            }
            /**
             * If any shot intersected the protagonist, the damage is taken, the message
             * system is informed, and the shot is removed.
             */
            if (intersect(protagonist, shot)) {
                protagonist.takeDamage(shot.getDamage(), messenger.getMessages(), sounds);
                itShot.remove();
                continue;
            }
            /* The remaining shots move. */
            shot.move(List.of(shot.getDirection()));
        }
    }

    /**
     * Performs a full update cycle of the environment.
     * Includes updating objects, shots, messages, camera,
     * checking collisions, and recovering protagonist energy.
     */
    public void update() {
        updateObjects();
        updateShots();
        // updateMessages();
        detectCollision();
        this.currentLevel.getBehavior().update(this);
        // updateCamera();
        // protagonist.recoverEnergy();
    }

    public void loadNextLevel() {
        if (this.levels == null || this.levels.isEmpty())
            return;
        int levelIndex = this.getLevels().indexOf(this.currentLevel);
        // If the current level do not exist in Levels or it is null.
        if (currentLevel == null || levelIndex < 0) {
            this.currentLevel = levels.get(0);
            this.setupCurrentLevel();
            return;
        }
        // This is the last level. So, we cannot setup the level again!
        if (levelIndex >= this.levels.size() - 1)
            return;
        // Then, there is a next level to play.
        this.currentLevel = this.levels.get(levelIndex + 1);
        this.setupCurrentLevel();
    }

    public void loadNextPlayableLevel() {
        if (this.levels == null || this.levels.isEmpty())
            return;
        int startIndex = 0;
        if (this.currentLevel != null) {
            int currentIndex = this.levels.indexOf(this.currentLevel);
            if (currentIndex >= 0) {
                startIndex = currentIndex + 1;
            }
        }
        Level nextPlayableLevel = this.levels.stream()
                .skip(startIndex)
                .filter(level -> level.getType() == StoryType.PLAYABLE)
                .findFirst()
                .orElse(null);
        if (nextPlayableLevel == null)
            return;
        this.currentLevel = nextPlayableLevel;
        this.setupCurrentLevel();
    }

    private void setupCurrentLevel() {
        this.protagonist.setPosX(10);
        this.protagonist.setPosY(450);
        this.camera.setPosX(0);
        this.camera.setLevelWidth(this.currentLevel.getWidth());
        this.loadSounds();
    }

    private void loadSounds() {
        Sound ambient = this.currentLevel.getSoundSet().get(SoundEvent.AMBIENT);
        Sound background = this.currentLevel.getSoundSet().get(SoundEvent.BACKGROUND);
        if (ambient != null) {
            this.sounds.add(ambient);
        }
        if (background != null) {
            this.sounds.add(background);
        }
    }

}