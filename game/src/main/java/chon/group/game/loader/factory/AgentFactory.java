package chon.group.game.loader.factory;

import java.util.Map;

import chon.group.game.core.agent.Agent;
import chon.group.game.core.agent.Direction;
import chon.group.game.core.weapon.Weapon;
import chon.group.game.loader.config.agent.AgentConfig;
import chon.group.game.animation.Animation;
import chon.group.game.animation.AnimationType;
import chon.group.game.sound.Sound;
import chon.group.game.sound.SoundEvent;

public class AgentFactory {

    private final Map<String, Animation> animations;
    private final Map<String, Sound> sounds;
    private final Map<String, Weapon> weapons;

    public AgentFactory(
            Map<String, Animation> animations,
            Map<String, Sound> sounds,
            Map<String, Weapon> weapons) {
        this.animations = animations;
        this.sounds = sounds;
        this.weapons = weapons;
    }

    public Agent create(String id, AgentConfig config) {

        Agent agent = new Agent(
                config.getPosition().getX(),
                config.getPosition().getY(),
                config.getSize().getWidth(),
                config.getSize().getHeight(),
                config.getSize().getHitboxRatio(),
                config.getStats().getSpeed(),
                config.getStats().getHealth(),
                Direction.valueOf(config.getState().getDirection()),
                config.getState().isFlipped(),
                config.getState().isVisibleBars());

        // 🎞️ animations
        for (var entry : config.getAnimations().entrySet()) {
            AnimationType type = AnimationType.valueOf(entry.getKey());
            Animation animation = animations.get(entry.getValue());
            agent.getAnimationSet().add(type, animation);
        }

        // 🔊 sounds
        for (var entry : config.getSounds().entrySet()) {
            SoundEvent event = SoundEvent.valueOf(entry.getKey());
            Sound sound = sounds.get(entry.getValue());
            agent.getSoundSet().add(event, sound);
        }

        // 🔫 weapons
        for (String weaponId : config.getWeapons()) {
            agent.setWeapon(weapons.get(weaponId));
        }

        // default weapon (optional if your Agent supports it)
        // agent.setCurrentWeapon(weapons.get(config.getDefaultWeapon()));

        return agent;
    }
}