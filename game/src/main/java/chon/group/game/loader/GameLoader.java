package chon.group.game.loader;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import chon.group.game.animation.Animation;
import chon.group.game.core.agent.Agent;
import chon.group.game.core.environment.Level;
import chon.group.game.core.weapon.Shot;
import chon.group.game.core.weapon.Weapon;
import chon.group.game.loader.config.entity.agent.AgentConfig;
import chon.group.game.loader.config.level.LevelConfig;
import chon.group.game.loader.factory.AgentFactory;
import chon.group.game.loader.factory.AnimationFactory;
import chon.group.game.loader.factory.LevelFactory;
import chon.group.game.loader.factory.MenuFactory;
import chon.group.game.loader.factory.ObjectFactory;
import chon.group.game.loader.factory.ShotFactory;
import chon.group.game.loader.factory.SoundFactory;
import chon.group.game.loader.factory.WeaponFactory;
import chon.group.game.menu.MenuHandler;
import chon.group.game.sound.Sound;

public class GameLoader {

    private final ObjectMapper objectMapper;
    private final GameConfig game;
    private Map<String, Animation> animations;
    private Map<String, Sound> sounds;
    private Map<String, Shot> shots;
    private Map<String, Weapon> weapons;

    public GameLoader(String resourcePath) {
        this.objectMapper = new ObjectMapper();
        this.game = this.load(resourcePath);
        this.loadComponents();
    }

    public GameConfig load(String resourcePath) {
        try (InputStream inputStream = getClass().getResourceAsStream(resourcePath)) {
            if (inputStream == null) {
                throw new IllegalArgumentException(
                        "JSON config file not found: " + resourcePath);
            }
            return objectMapper.readValue(inputStream, GameConfig.class);
        } catch (Exception e) {
            throw new RuntimeException(
                    "Could not load game config from: " + resourcePath, e);
        }
    }

    private void loadComponents() {
        this.loadMedia();
        this.loadWeapons();
    }

    private void loadMedia() {
        this.animations = new AnimationFactory().buildAll(this.game.getMedia().getAnimations());
        this.sounds = new SoundFactory().buildAll(this.game.getMedia().getSounds());
    }

    private void loadWeapons() {
        this.shots = new ShotFactory().buildAll(this.game.getEntities().getShots(), this.animations);
        this.weapons = new WeaponFactory().buildAll(this.game.getEntities().getWeapons(), this.shots);
    }

    public Agent createProtagonist() {
        String protagonistId = this.game.getEnvironment().getProtagonist();
        AgentConfig agentConfig = this.game.getEntities().getAgents().get(protagonistId);
        AgentFactory agentFactory = new AgentFactory(animations, sounds, weapons);
        return agentFactory.create(protagonistId, agentConfig);
    }

    public MenuHandler createMenuHandler() {
        return new MenuFactory().buildMenuHandler(
                this.game.getEnvironment().getMenus(),
                this.game.getEnvironment().getMenuHandler());
    }

    public Level createLevel(int index) {
        List<LevelConfig> levelConfigs = this.game.getLevels();
        if (index < 0 || index >= levelConfigs.size()) {
            throw new IllegalArgumentException("Invalid level index: " + index);
        }
        LevelFactory factory = new LevelFactory(
                this.animations,
                this.sounds,
                game.getEntities().getAgents(),
                new AgentFactory(
                        this.animations,
                        this.sounds,
                        this.weapons),
                game.getEntities().getObjects(),
                new ObjectFactory(this.animations, this.sounds));
        return factory.build(levelConfigs.get(index));
    }

    public List<Level> createLevels() {
        return new LevelFactory(
                this.animations,
                this.sounds,
                game.getEntities().getAgents(),
                new AgentFactory(
                        this.animations,
                        this.sounds,
                        this.weapons),
                game.getEntities().getObjects(),
                new ObjectFactory(this.animations, this.sounds))
                .buildAll(this.game.getLevels());
    }

}
