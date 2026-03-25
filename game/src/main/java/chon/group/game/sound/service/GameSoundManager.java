package chon.group.game.sound.service;

import chon.group.game.Game;
import chon.group.game.sound.Sound;
import chon.group.game.sound.client.JavaFxPlayer;

public class GameSoundManager implements SoundManager {

    // private Game game;
    private final JavaFxPlayer player;

    public GameSoundManager(Game game) {
        // this.game = game;
        this.player = new JavaFxPlayer();
    }

    @Override
    public void play(Sound sound) {
        player.playSound(sound.getPath());
    }

    @Override
    public void playMusic(Sound sound) {
        player.playMusic(sound.getPath());
    }

}
