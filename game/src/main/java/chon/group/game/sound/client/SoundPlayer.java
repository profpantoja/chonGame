package chon.group.game.sound.client;

public abstract class SoundPlayer {

    private double musicVolume = 1.0;
    private double ambientVolume = 0.6;
    private double sfxVolume = 0.65;

    public SoundPlayer() {

    }

    public double getMusicVolume() {
        return musicVolume;
    }

    public void setMusicVolume(double musicVolume) {
        this.musicVolume = musicVolume;
    }

    public double getAmbientVolume() {
        return ambientVolume;
    }

    public void setAmbienteVolume(double ambientVolume) {
        this.ambientVolume = ambientVolume;
    }

    public double getSfxVolume() {
        return sfxVolume;
    }

    public void setSfxVolume(double sfxVolume) {
        this.sfxVolume = sfxVolume;
    }

    public abstract void playSound(String path);

    public abstract void playMusic(String path);

    public abstract void playAmbient(String path);

}
