package chon.group.game.domain.environment;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class SoundManager {

    // Cache for files after saved
    private static final HashMap<String, Media> mediaCache = new HashMap<>();
    private static final ArrayList<MediaPlayer> activeSoundEffects = new ArrayList<>();

    // Player to control the mediaPlayer (stop, pause).
    private static MediaPlayer backgroundMusicPlayer;
    

    /**
     * Play a sound effect
     *
     * @param resourcePath path sound effect  
     * (ex: "/sounds/background_music.wav").
     */
    public static void playSound(String resourcePath) {
        try {
            Media media = mediaCache.computeIfAbsent(resourcePath, path -> {
                URL resourceUrl = SoundManager.class.getResource(path);
                if (resourceUrl == null) {
                    System.err.println("Arquivo de som não encontrado: " + path);
                    return null;
                }
                return new Media(resourceUrl.toString());
            });

            if (media != null) {
                MediaPlayer soundPlayer = new MediaPlayer(media);
                //  Add to active sound effects list
                // This allows multiple sound effects to play simultaneously
                activeSoundEffects.add(soundPlayer);

                // When it ends, remove from  the active list
                soundPlayer.setOnEndOfMedia(() -> activeSoundEffects.remove(soundPlayer));
                soundPlayer.play();
            }
        } catch (Exception e) {
            System.err.println("Erro ao tocar o som: " + resourcePath);
            e.printStackTrace();
        }
    }


    /**
     * Put Game Music in loop, and stop, whem another music appears
     *
     * @param resourcePath path music  
     * (ex: "/sounds/background_music.wav").
     */
    public static void playMusic(String resourcePath) {
        // Stop current music
        if (backgroundMusicPlayer != null) {
            backgroundMusicPlayer.stop();
        }
        
        try {
            Media media = mediaCache.computeIfAbsent(resourcePath, path -> {
                URL resourceUrl = SoundManager.class.getResource(path);
                if (resourceUrl == null) {
                    System.err.println("Arquivo de música não encontrado: " + path);
                    return null;
                }
                return new Media(resourceUrl.toString());
            });
            
            if (media != null) {
                backgroundMusicPlayer = new MediaPlayer(media);
                // loop music
                backgroundMusicPlayer.setCycleCount(MediaPlayer.INDEFINITE);
                backgroundMusicPlayer.play();
            }
        } catch (Exception e) {
            System.err.println("Erro ao tocar a música: " + resourcePath);
            e.printStackTrace();
        }
    }

    /**
     * Stop Music
     */
    public static void stopMusic() {
        if (backgroundMusicPlayer != null) {
            backgroundMusicPlayer.stop();
            backgroundMusicPlayer = null;
        }
    }

    /**
     * Pause Music
     */
    public static void pauseMusic() {
        if (backgroundMusicPlayer != null) {
            backgroundMusicPlayer.pause();
        }
    }
        
    /**
     * Resume Music
     */
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
}