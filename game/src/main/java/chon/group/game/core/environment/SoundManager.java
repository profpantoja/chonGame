package chon.group.game.core.environment;
import java.net.URL;
import java.util.HashMap;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class SoundManager {
    // Cache for files after saved
    private static final HashMap<String, Media> mediaCache = new HashMap<>();
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
            // using cache if music starts after
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
                soundPlayer.play();
            }
        } catch (Exception e) {
            System.err.println("Erro ao tocar o som: " + resourcePath);
            e.printStackTrace();
        }
    }

    public static void setMusicVolume(double volume) {
        if (backgroundMusicPlayer != null) {
            backgroundMusicPlayer.setVolume(volume); // volume entre 0.0 (mudo) e 1.0 (máximo)
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
}