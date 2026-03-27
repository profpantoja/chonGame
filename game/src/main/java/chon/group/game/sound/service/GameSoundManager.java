package chon.group.game.sound.service;

import chon.group.game.sound.Sound;
import chon.group.game.sound.client.SoundPlayer;

public class GameSoundManager implements SoundManager {

    private final SoundPlayer player;

    public GameSoundManager(SoundPlayer player) {
        this.player = player;
    }

    @Override
    public void play(Sound sound) {
        player.playSound(sound.getPath());
    }

    @Override
    public void playMusic(Sound sound) {
        player.playMusic(sound.getPath());
    }

    @Override
    public void playAmbient(Sound sound) {
        player.playAmbient(sound.getPath());
    }

    @Override
    public void stop() {
        player.stop();
    }

}
