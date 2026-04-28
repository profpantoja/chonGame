package chon.group.game.loader.factory;

import java.util.HashMap;
import java.util.Map;

import chon.group.game.loader.config.sound.SoundConfig;
import chon.group.game.sound.Sound;
import chon.group.game.sound.SoundType;

public class SoundFactory {

    public Map<String, Sound> buildAll(Map<String, SoundConfig> configs) {
        Map<String, Sound> sounds = new HashMap<>();

        for (var entry : configs.entrySet()) {
            String id = entry.getKey();
            SoundConfig config = entry.getValue();

            sounds.put(id, build(config));
        }

        return sounds;
    }

    public Sound build(SoundConfig config) {
        SoundType type = SoundType.valueOf(config.getType());

        return new Sound(
                config.getPath(),
                type,
                config.isLoop());
    }
}