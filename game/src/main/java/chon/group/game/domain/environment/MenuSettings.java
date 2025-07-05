package chon.group.game.domain.environment;

import chon.group.game.domain.types.SettingsEnum;
import chon.group.game.drawer.JavaFxDrawer;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;

public class MenuSettings {

    private int selectedOptionIndex = 0;
    private final String[] options = {"Música", "Efeitos", "Voltar"};
    private final JavaFxDrawer drawer;
    private final Image backgroundImage;

    public MenuSettings(JavaFxDrawer drawer, Image backgroundImage) {
        this.drawer = drawer;
        this.backgroundImage = backgroundImage;
    }

    public void draw() {
        double musicVolume = SoundManager.getMusicVolume();
        double sfxVolume = SoundManager.getSfxVolume();
        drawer.drawMenuSettings(backgroundImage, "Settings", musicVolume, sfxVolume, options, selectedOptionIndex);
    }

    public SettingsEnum.Settings handleInput(KeyCode code) {
        switch (code) {
            case UP:
                selectedOptionIndex = (selectedOptionIndex - 1 + options.length) % options.length;
                break;
            case DOWN:
                selectedOptionIndex = (selectedOptionIndex + 1) % options.length;
                break;
            case LEFT:
                adjustVolume(-0.05);
                break;
            case RIGHT:
                adjustVolume(0.05);
                break;
            case ENTER:
                if (options[selectedOptionIndex].equals("Voltar")) {
                    return SettingsEnum.Settings.BACK;
                }
                break;
            case ESCAPE:
                return SettingsEnum.Settings.BACK;
            default:
                break;
        }
        return null;
    }

    private void adjustVolume(double delta) {
        if (options[selectedOptionIndex].equals("Música")) {
            double newVolume = clamp(SoundManager.getMusicVolume() + delta);
            SoundManager.setMusicVolume(newVolume);
        } else if (options[selectedOptionIndex].equals("Efeitos")) {
            double newVolume = clamp(SoundManager.getSfxVolume() + delta);
            SoundManager.setSfxVolume(newVolume);
        }
    }

    private double clamp(double value) {
        return Math.max(0.0, Math.min(1.0, value));
    }

    public void reset() {
        selectedOptionIndex = 0;
    }
}
