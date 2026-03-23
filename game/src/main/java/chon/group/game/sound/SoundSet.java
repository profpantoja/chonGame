package chon.group.game.sound;

import java.util.HashMap;
import java.util.Map;

public class SoundSet {

    private Map<SoundEvent, Sound> sounds = new HashMap<>();

    public void add(SoundEvent event, Sound sound) {
        sounds.put(event, sound);
    }

    public Sound get(SoundEvent event) {
        return sounds.get(event);
    }

}
