package chon.group.game.sound.client;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class JavaFxPlayer {

    private double musicVolume = 1.0;
    private double sfxVolume = 1.0;

    private final Map<String, Media> mediaCache = new HashMap<>();
    private final List<MediaPlayer> activeSoundEffects = new ArrayList<>();

    private MediaPlayer backgroundMusicPlayer;

    public void playSound(String resourcePath) {
        try {
            Media media = getOrLoadMedia(resourcePath);
            if (media == null) {
                return;
            }

            MediaPlayer soundPlayer = new MediaPlayer(media);
            soundPlayer.setVolume(sfxVolume);

            soundPlayer.setOnEndOfMedia(() -> {
                soundPlayer.stop();
                soundPlayer.dispose();
                activeSoundEffects.remove(soundPlayer);
            });

            activeSoundEffects.add(soundPlayer);
            soundPlayer.play();

        } catch (Exception e) {
            System.err.println("Erro ao tocar o som: " + resourcePath);
            e.printStackTrace();
        }
    }

    public void playMusic(String resourcePath) {
        try {
            Media media = getOrLoadMedia(resourcePath);
            if (media == null) {
                return;
            }

            if (backgroundMusicPlayer != null) {
                Media currentMedia = backgroundMusicPlayer.getMedia();
                if (currentMedia != null && currentMedia.getSource().equals(media.getSource())) {
                    return;
                }
                stopMusic();
            }

            backgroundMusicPlayer = new MediaPlayer(media);
            backgroundMusicPlayer.setVolume(musicVolume);

            // Use INDEFINITE if you want stage music looping by default
            backgroundMusicPlayer.setCycleCount(1);

            backgroundMusicPlayer.play();

        } catch (Exception e) {
            System.err.println("Erro ao tocar a música: " + resourcePath);
            e.printStackTrace();
        }
    }

    public boolean isCurrentMusic(String resourcePath) {
        if (backgroundMusicPlayer == null) {
            return false;
        }

        Media currentMedia = backgroundMusicPlayer.getMedia();
        if (currentMedia == null) {
            return false;
        }

        URL resourceUrl = getClass().getResource(resourcePath);
        if (resourceUrl == null) {
            return false;
        }

        return currentMedia.getSource().equals(resourceUrl.toString());
    }

    public void stopMusic() {
        if (backgroundMusicPlayer != null) {
            backgroundMusicPlayer.stop();
            backgroundMusicPlayer.dispose();
            backgroundMusicPlayer = null;
        }
    }

    public void pauseMusic() {
        if (backgroundMusicPlayer != null) {
            backgroundMusicPlayer.pause();
        }
    }

    public void resumeMusic() {
        if (backgroundMusicPlayer != null) {
            backgroundMusicPlayer.play();
        }
    }

    public void pauseAllSoundEffects() {
        for (MediaPlayer mp : activeSoundEffects) {
            mp.pause();
        }
    }

    public void resumeAllSoundEffects() {
        for (MediaPlayer mp : activeSoundEffects) {
            mp.play();
        }
    }

    public void stopAllSoundEffects() {
        for (MediaPlayer mp : new ArrayList<>(activeSoundEffects)) {
            mp.stop();
            mp.dispose();
        }
        activeSoundEffects.clear();
    }

    public void stopAll() {
        stopMusic();
        stopAllSoundEffects();
    }

    public void setMusicVolume(double volume) {
        musicVolume = clamp(volume);
        if (backgroundMusicPlayer != null) {
            backgroundMusicPlayer.setVolume(musicVolume);
        }
    }

    public double getMusicVolume() {
        return musicVolume;
    }

    public void setSfxVolume(double volume) {
        sfxVolume = clamp(volume);
        for (MediaPlayer mp : activeSoundEffects) {
            mp.setVolume(sfxVolume);
        }
    }

    public double getSfxVolume() {
        return sfxVolume;
    }

    /**
     * Optional maintenance update.
     * Kept in case you still want polling-based cleanup.
     */
    public void update() {
        List<MediaPlayer> finished = new ArrayList<>();

        for (MediaPlayer mp : activeSoundEffects) {
            MediaPlayer.Status status = mp.getStatus();
            if (status == MediaPlayer.Status.STOPPED || status == MediaPlayer.Status.DISPOSED) {
                mp.dispose();
                finished.add(mp);
            }
        }

        activeSoundEffects.removeAll(finished);
    }

    private Media getOrLoadMedia(String resourcePath) {
        Media media = mediaCache.get(resourcePath);
        if (media != null) {
            return media;
        }

        URL resourceUrl = getClass().getResource(resourcePath);
        if (resourceUrl == null) {
            System.err.println("Arquivo de áudio não encontrado: " + resourcePath);
            return null;
        }

        media = new Media(resourceUrl.toString());
        mediaCache.put(resourcePath, media);
        return media;
    }

    private double clamp(double value) {
        return Math.max(0.0, Math.min(1.0, value));
    }
}