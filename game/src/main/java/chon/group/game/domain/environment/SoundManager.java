package chon.group.game.domain.environment;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class SoundManager {

    private static double musicVolume = 1.0;
    private static double sfxVolume = 1.0;

    private static final HashMap<String, Media> mediaCache = new HashMap<>();
    private static final ArrayList<MediaPlayer> activeSoundEffects = new ArrayList<>();

    private static MediaPlayer backgroundMusicPlayer;

    public static void playSound(String resourcePath) {
        try {
            Media media = mediaCache.get(resourcePath);
            if (media == null) {
                URL resourceUrl = SoundManager.class.getResource(resourcePath);
                if (resourceUrl == null) {
                    System.err.println("Arquivo de som não encontrado: " + resourcePath);
                    return;
                }
                media = new Media(resourceUrl.toString());
                mediaCache.put(resourcePath, media);
            }

            if (media != null) {
                MediaPlayer soundPlayer = new MediaPlayer(media);
                soundPlayer.setVolume(sfxVolume);
                activeSoundEffects.add(soundPlayer);
                soundPlayer.play();
            }
        } catch (Exception e) {
            System.err.println("Erro ao tocar o som: " + resourcePath);
            e.printStackTrace();
        }
    }

    public static void setMusicVolume(double volume) {
        musicVolume = Math.max(0.0, Math.min(1.0, volume));
        if (backgroundMusicPlayer != null) {
            backgroundMusicPlayer.setVolume(musicVolume);
        }
    }

    public static double getMusicVolume() {
        return musicVolume;
    }

    public static void setSfxVolume(double volume) {
        sfxVolume = Math.max(0.0, Math.min(1.0, volume));
        for (MediaPlayer mp : activeSoundEffects) {
            mp.setVolume(sfxVolume);
        }
    }

    public static double getSfxVolume() {
        return sfxVolume;
    }

    public static void playMusic(String resourcePath) {
        // Não tocar novamente se já estiver tocando a mesma música
        if (backgroundMusicPlayer != null) {
            Media media = backgroundMusicPlayer.getMedia();
            if (media != null && media.getSource().equals(SoundManager.class.getResource(resourcePath).toString())) {
                // Mesma música já tocando, não faz nada
                return;
            }
            backgroundMusicPlayer.stop();
        }

        try {
            Media media = mediaCache.get(resourcePath);
            if (media == null) {
                URL resourceUrl = SoundManager.class.getResource(resourcePath);
                if (resourceUrl == null) {
                    System.err.println("Arquivo de música não encontrado: " + resourcePath);
                    return;
                }
                media = new Media(resourceUrl.toString());
                mediaCache.put(resourcePath, media);
            }

            if (media != null) {
                backgroundMusicPlayer = new MediaPlayer(media);
                backgroundMusicPlayer.setVolume(musicVolume);
                backgroundMusicPlayer.setCycleCount(1); // Toca só uma vez
                backgroundMusicPlayer.play();
            }
        } catch (Exception e) {
            System.err.println("Erro ao tocar a música: " + resourcePath);
            e.printStackTrace();
        }
    }

    public static boolean isCurrentMusic(String resourcePath) {
        if (backgroundMusicPlayer == null) return false;
        Media media = backgroundMusicPlayer.getMedia();
        if (media == null) return false;

        URL resourceUrl = SoundManager.class.getResource(resourcePath);
        if (resourceUrl == null) return false;

        return media.getSource().equals(resourceUrl.toString());
    }


    public static void stopMusic() {
        if (backgroundMusicPlayer != null) {
            backgroundMusicPlayer.stop();
            backgroundMusicPlayer = null;
        }
    }

    public static void pauseMusic() {
        if (backgroundMusicPlayer != null) {
            backgroundMusicPlayer.pause();
        }
    }

    public static void resumeMusic() {
        if (backgroundMusicPlayer != null) {
            backgroundMusicPlayer.play();
        }
    }

    public static void pauseAllSoundEffects() {
        for (MediaPlayer mp : activeSoundEffects) {
            mp.pause();
        }
    }

    public static void resumeAllSoundEffects() {
        for (MediaPlayer mp : activeSoundEffects) {
            mp.play();
        }
    }

    public static void stopAllSoundEffects() {
        for (MediaPlayer mp : new ArrayList<>(activeSoundEffects)) {
            mp.stop();
        }
        activeSoundEffects.clear();
    }

    public static void stopAll() {
        stopMusic();
        stopAllSoundEffects();
    }

    /**
     * Deve ser chamado no loop principal do jogo para limpar efeitos já encerrados.
     */
    public static void update() {
        ArrayList<MediaPlayer> finished = new ArrayList<>();
        for (MediaPlayer mp : activeSoundEffects) {
            MediaPlayer.Status status = mp.getStatus();
            if (status == MediaPlayer.Status.STOPPED || status == MediaPlayer.Status.DISPOSED) {
                finished.add(mp);
            }
        }
        activeSoundEffects.removeAll(finished);
    }
}
