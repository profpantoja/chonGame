package chon.group.game.loader;

import java.io.InputStream;

import com.fasterxml.jackson.databind.ObjectMapper;

public class GameLoader {

    private final ObjectMapper objectMapper;

    public GameLoader() {
        this.objectMapper = new ObjectMapper();
    }

    public GameConfig load(String resourcePath) {
        try (InputStream inputStream = getClass().getResourceAsStream(resourcePath)) {

            if (inputStream == null) {
                throw new IllegalArgumentException(
                        "JSON config file not found: " + resourcePath);
            }

            return objectMapper.readValue(inputStream, GameConfig.class);

        } catch (Exception e) {
            throw new RuntimeException(
                    "Could not load game config from: " + resourcePath, e);
        }
    }
}
