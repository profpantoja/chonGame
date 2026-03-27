package chon.group.game.sound.client;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class JavaFxPlayer extends SoundPlayer {

    private final Map<String, Media> mediaCache = new HashMap<>();
    private final List<MediaPlayer> activeSoundEffects = new ArrayList<>();

    private MediaPlayer musicPlayer;
    private MediaPlayer ambientPlayer;

    public JavaFxPlayer() {
    }

    public void playSound(String resourcePath) {
        try {
            Media media = getOrLoadMedia(resourcePath);
            if (media == null) {
                return;
            }

            MediaPlayer soundPlayer = new MediaPlayer(media);
            soundPlayer.setVolume(getSfxVolume());

            soundPlayer.setOnEndOfMedia(() -> {
                soundPlayer.stop();
                soundPlayer.dispose();
                activeSoundEffects.remove(soundPlayer);
            });

            soundPlayer.setOnError(() -> {
                System.err.println("Erro no player de efeito sonoro: " + resourcePath);
                if (soundPlayer.getError() != null) {
                    soundPlayer.getError().printStackTrace();
                }
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
            musicPlayer = startLoopingPlayer(resourcePath, musicPlayer, getMusicVolume(), "música");
        } catch (Exception e) {
            System.err.println("Erro ao tocar a música: " + resourcePath);
            e.printStackTrace();
        }
    }

    public void playAmbient(String resourcePath) {
        try {
            ambientPlayer = startLoopingPlayer(resourcePath, ambientPlayer, getAmbientVolume(), "ambiente");
        } catch (Exception e) {
            System.err.println("Erro ao tocar o ambiente: " + resourcePath);
            e.printStackTrace();
        }
    }

    public boolean isCurrentMusic(String resourcePath) {
        return isCurrentMedia(resourcePath, musicPlayer);
    }

    public boolean isCurrentAmbient(String resourcePath) {
        return isCurrentMedia(resourcePath, ambientPlayer);
    }

    public void stopMusic() {
        musicPlayer = stopAndDisposePlayer(musicPlayer);
    }

    public void stopAmbient() {
        ambientPlayer = stopAndDisposePlayer(ambientPlayer);
    }

    public void pauseMusic() {
        if (musicPlayer != null) {
            musicPlayer.pause();
        }
    }

    public void pauseAmbient() {
        if (ambientPlayer != null) {
            ambientPlayer.pause();
        }
    }

    public void resumeMusic() {
        if (musicPlayer != null) {
            musicPlayer.play();
        }
    }

    public void resumeAmbient() {
        if (ambientPlayer != null) {
            ambientPlayer.play();
        }
    }

    public void pauseAllSoundEffects() {
        for (MediaPlayer mp : new ArrayList<>(activeSoundEffects)) {
            mp.pause();
        }
    }

    public void resumeAllSoundEffects() {
        for (MediaPlayer mp : new ArrayList<>(activeSoundEffects)) {
            mp.play();
        }
    }

    public void stopAllSoundEffects() {
        for (MediaPlayer mp : new ArrayList<>(activeSoundEffects)) {
            try {
                mp.stop();
                mp.dispose();
            } catch (Exception e) {
                System.err.println("Erro ao encerrar efeito sonoro.");
                e.printStackTrace();
            }
        }
        activeSoundEffects.clear();
    }

    public void pauseAll() {
        pauseMusic();
        pauseAmbient();
        pauseAllSoundEffects();
    }

    public void resumeAll() {
        resumeMusic();
        resumeAmbient();
        resumeAllSoundEffects();
    }

    public void stopAll() {
        stopMusic();
        stopAmbient();
        stopAllSoundEffects();
    }

    @Override
    public void setMusicVolume(double volume) {
        super.setMusicVolume(clamp(volume));
        if (musicPlayer != null) {
            musicPlayer.setVolume(getMusicVolume());
        }
    }

    public void setAmbientVolume(double volume) {
        this.setAmbientVolume(clamp(volume));
        if (ambientPlayer != null) {
            ambientPlayer.setVolume(this.getAmbientVolume());
        }
    }

    @Override
    public void setSfxVolume(double volume) {
        super.setSfxVolume(clamp(volume));
        for (MediaPlayer mp : new ArrayList<>(activeSoundEffects)) {
            mp.setVolume(getSfxVolume());
        }
    }

    public void update() {
        List<MediaPlayer> finished = new ArrayList<>();

        for (MediaPlayer mp : activeSoundEffects) {
            MediaPlayer.Status status = mp.getStatus();
            if (status == MediaPlayer.Status.STOPPED
                    || status == MediaPlayer.Status.DISPOSED
                    || status == MediaPlayer.Status.HALTED) {
                try {
                    mp.dispose();
                } catch (Exception e) {
                    System.err.println("Erro ao fazer dispose de efeito sonoro.");
                    e.printStackTrace();
                }
                finished.add(mp);
            }
        }

        activeSoundEffects.removeAll(finished);
    }

    private MediaPlayer startLoopingPlayer(String resourcePath,
            MediaPlayer currentPlayer,
            double volume,
            String channelName) {
        Media media = getOrLoadMedia(resourcePath);
        if (media == null) {
            return currentPlayer;
        }

        if (isSameMedia(currentPlayer, media)) {
            return currentPlayer;
        }

        currentPlayer = stopAndDisposePlayer(currentPlayer);

        MediaPlayer newPlayer = new MediaPlayer(media);
        newPlayer.setVolume(clamp(volume));
        newPlayer.setCycleCount(MediaPlayer.INDEFINITE);

        newPlayer.setOnError(() -> {
            System.err.println("Erro no player de " + channelName + ": " + resourcePath);
            if (newPlayer.getError() != null) {
                newPlayer.getError().printStackTrace();
            }
        });

        newPlayer.play();
        return newPlayer;
    }

    private boolean isCurrentMedia(String resourcePath, MediaPlayer player) {
        if (player == null) {
            return false;
        }

        Media currentMedia = player.getMedia();
        if (currentMedia == null) {
            return false;
        }

        URL resourceUrl = getClass().getResource(resourcePath);
        if (resourceUrl == null) {
            return false;
        }

        return currentMedia.getSource().equals(resourceUrl.toString());
    }

    private boolean isSameMedia(MediaPlayer player, Media media) {
        if (player == null || media == null) {
            return false;
        }

        Media currentMedia = player.getMedia();
        return currentMedia != null && currentMedia.getSource().equals(media.getSource());
    }

    private MediaPlayer stopAndDisposePlayer(MediaPlayer player) {
        if (player != null) {
            try {
                player.stop();
            } catch (Exception e) {
                System.err.println("Erro ao parar player.");
                e.printStackTrace();
            }

            try {
                player.dispose();
            } catch (Exception e) {
                System.err.println("Erro ao liberar player.");
                e.printStackTrace();
            }
        }
        return null;
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