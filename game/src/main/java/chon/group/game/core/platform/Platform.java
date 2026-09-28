package chon.group.game.core.platform;

import chon.group.game.drawer.service.GameDrawer;
import chon.group.game.joystick.service.GameJoystick;
import chon.group.game.sound.service.GameSoundManager;

public class Platform {

    private GameJoystick joystick;
    private GameDrawer drawer;
    private GameSoundManager soundManager;

    public GameJoystick getJoystick() {
        return joystick;
    }

    public void setJoystick(GameJoystick joystick) {
        this.joystick = joystick;
    }

    public GameDrawer getDrawer() {
        return drawer;
    }

    public void setDrawer(GameDrawer drawer) {
        this.drawer = drawer;
    }

    public GameSoundManager getSoundManager() {
        return soundManager;
    }

    public void setSoundManager(GameSoundManager soundManager) {
        this.soundManager = soundManager;
    }
}
