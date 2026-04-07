package chon.group.game.sound.client;

public abstract class SoundPlayer {

    private double musicVolume = 0.3;
    private double ambientVolume = 0.6;
    private double sfxVolume = 0.6;

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

    public void increaseMusicVolume() {
        this.musicVolume += 0.05;
        if (this.musicVolume > 1)
            this.musicVolume = 1;
    }

    public void decreaseMusicVolume() {
        this.musicVolume -= 0.05;
        if (this.musicVolume < 0)
            this.musicVolume = 0;
    }

    public void increaseSfxVolume() {
        this.sfxVolume += 0.05;
        if (this.sfxVolume > 1)
            this.sfxVolume = 1;
    }

    public void decreaseSfxVolume() {
        this.sfxVolume -= 0.05;
        if (this.sfxVolume < 0)
            this.sfxVolume = 0;
    }

    public void increaseAmbientVolume() {
        this.ambientVolume += 0.05;
        if (this.ambientVolume > 1)
            this.ambientVolume = 1;
    }

    public void decreaseAmbientVolume() {
        this.ambientVolume -= 0.05;
        if (this.ambientVolume < 0)
            this.ambientVolume = 0;
    }

    public abstract void playSound(String path);

    public abstract void playMusic(String path);

    public abstract void playAmbient(String path);

    public abstract void stop();

}
