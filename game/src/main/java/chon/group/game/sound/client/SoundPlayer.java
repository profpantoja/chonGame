package chon.group.game.sound.client;

public abstract class SoundPlayer {

    private double musicVolume = 1.0;
    private double sfxVolume = 0.65;

    public SoundPlayer() {
        
    }

    public double getMusicVolume() {
        return musicVolume;
    }

    public void setMusicVolume(double musicVolume) {
        this.musicVolume = musicVolume;
    }

    public double getSfxVolume() {
        return sfxVolume;
    }

    public void setSfxVolume(double sfxVolume) {
        this.sfxVolume = sfxVolume;
    }

    public abstract void playSound(String path);

    public abstract void playMusic(String path);

}
