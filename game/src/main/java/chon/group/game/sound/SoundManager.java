package chon.group.game.sound;

import java.net.URL;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class SoundManager {

    private static MediaPlayer currentMusicPlayer;
    private static String currentMusicPath;

    // Toca um efeito sonoro curto 
    public static void playSound(String path) {
        try {
            URL resource = SoundManager.class.getResource(path);
            if (resource != null) {
                AudioClip clip = new AudioClip(resource.toExternalForm());
                clip.play();
            } else {
                System.err.println("[ERRO DE SOM] Efeito sonoro não encontrado: " + path);
            }
        } catch (Exception e) {
            System.err.println("[ERRO CRÍTICO DE SOM] Falha ao tocar efeito sonoro: " + path);
            e.printStackTrace();
        }
    }

    // Toca uma música de fundo (ideal para .mp3)
    public static void playMusic(String path) {
        if (path.equals(currentMusicPath)) {
            return; // Não reinicia a música se ela já estiver tocando
        }

        stopMusic(); // Para a música atual antes de tocar uma nova

        try {
            URL resource = SoundManager.class.getResource(path);
            if (resource != null) {
                Media media = new Media(resource.toExternalForm());
                currentMusicPlayer = new MediaPlayer(media);
                currentMusicPlayer.setCycleCount(MediaPlayer.INDEFINITE); // Toca em loop
                currentMusicPlayer.play();
                currentMusicPath = path;
            } else {
                System.err.println("[ERRO DE SOM] Música não encontrada: " + path);
            }
        } catch (Exception e) {
            System.err.println("[ERRO CRÍTICO DE SOM] Falha ao tocar música: " + path);
            e.printStackTrace();
        }
    }

    // Para a música de fundo que está tocando
    public static void stopMusic() {
        if (currentMusicPlayer != null) {
            currentMusicPlayer.stop();
            currentMusicPlayer = null;
            currentMusicPath = null;
        }
    }
}