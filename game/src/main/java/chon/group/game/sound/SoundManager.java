package chon.group.game.sound;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class SoundManager {

    private static final HashMap<String, Media> mediaCache = new HashMap<>();
    private static final ArrayList<MediaPlayer> activeSoundEffects = new ArrayList<>();

    private static MediaPlayer backgroundMusicPlayer;

    /**
     * Plays a sound effect from the specified resource path.
     * <p>
     * This method attempts to retrieve a cached {@link Media} object for the given resource path.
     * If not found, it loads the media from the resource path and caches it.
     * Then, it creates a {@link MediaPlayer} to play the sound and adds it to the list of active sound effects.
     * <p>
     * If the resource is not found or an error occurs during playback, an error message is printed to the standard error stream.
     *
     * @param resourcePath the path to the sound resource to be played
     */
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
                activeSoundEffects.add(soundPlayer);
                soundPlayer.play();
            }
        } catch (Exception e) {
            System.err.println("Erro ao tocar o som: " + resourcePath);
            e.printStackTrace();
        }
    }

 
    /**
     * Plays a music track from the specified resource path.
     * <p>
     * If the requested music is already playing, this method does nothing.
     * Otherwise, it stops any currently playing music and starts the new track.
     * The music is played only once (not looped).
     * <p>
     * The method uses a cache to avoid reloading media resources.
     * If the resource is not found, an error message is printed.
     * Any exceptions during playback are caught and printed to the error stream.
     *
     * @param resourcePath the path to the music resource within the application's resources
     */
    public static void playMusic(String resourcePath) {
        try {
            URL resourceUrl = SoundManager.class.getResource(resourcePath);
            if (resourceUrl == null) {
                System.err.println("Arquivo de música não encontrado: " + resourcePath);
                return;
            }

            String requestedSource = resourceUrl.toString();

            // Se já está tocando a mesma música, reinicia do zero
            if (backgroundMusicPlayer != null) {
                Media currentMedia = backgroundMusicPlayer.getMedia();
                if (currentMedia != null && currentMedia.getSource().equals(requestedSource)) {
                    backgroundMusicPlayer.stop();
                    backgroundMusicPlayer.seek(javafx.util.Duration.ZERO);
                    backgroundMusicPlayer.play();
                    return;
                } else {
                    backgroundMusicPlayer.stop();
                    backgroundMusicPlayer.dispose(); // Libera recursos
                    backgroundMusicPlayer = null;
                }
            }

            Media media = mediaCache.get(resourcePath);
            if (media == null) {
                media = new Media(requestedSource);
                mediaCache.put(resourcePath, media);
            }

            backgroundMusicPlayer = new MediaPlayer(media);
            backgroundMusicPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            backgroundMusicPlayer.play();

        } catch (Exception e) {
            System.err.println("Erro ao tocar a música: " + resourcePath);
            e.printStackTrace();
        }
    }

    /**
     * Checks if the currently playing background music matches the specified resource path.
     *
     * @param resourcePath the path to the music resource to compare with the currently playing music.
     * @return {@code true} if the currently playing music's source matches the given resource path,
     *         {@code false} otherwise (including if no music is playing or the resource cannot be found).
     */
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
     * Updates the list of active sound effects by removing any MediaPlayer instances
     * that have finished playing or have been disposed.
     * <p>
     * This method iterates through the {@code activeSoundEffects} collection and checks
     * the status of each {@code MediaPlayer}. If the status is {@code STOPPED} or {@code DISPOSED},
     * the {@code MediaPlayer} is removed from the active list.
     * </p>
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
