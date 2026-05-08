package chon.group.game.loader.config.media;

import java.util.Map;

import chon.group.game.loader.config.media.animation.AnimationConfig;
import chon.group.game.loader.config.media.screen.ScreenConfig;
import chon.group.game.loader.config.media.sound.SoundConfig;

public class MediaConfig {

    private ScreenConfig screens;
    private Map<String, AnimationConfig> animations;
    private Map<String, SoundConfig> sounds;

    public MediaConfig() {
    }

    public ScreenConfig getScreens() {
        return screens;
    }

    public void setScreens(ScreenConfig screens) {
        this.screens = screens;
    }

    public Map<String, AnimationConfig> getAnimations() {
        return animations;
    }

    public void setAnimations(Map<String, AnimationConfig> animations) {
        this.animations = animations;
    }

    public Map<String, SoundConfig> getSounds() {
        return sounds;
    }

    public void setSounds(Map<String, SoundConfig> sounds) {
        this.sounds = sounds;
    }

}