package chon.group.game.sound.service;

import chon.group.game.sound.Sound;

public interface SoundManager {

    /** Plays sounds. */
    void play(Sound sound);

    /** Plays the background music. */
    void playMusic(Sound sound);

}
