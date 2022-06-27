package com.company;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public class Config {
    private static final String CONFIG_NAME = "rev-the-game.cfg";
    private static final int DEFAULT_FPS = 25;

    private static int fps;

    public static int getFps() {
        return fps;
    }

    public static boolean readConfig() {
        Path path = Path.of(CONFIG_NAME);

        if (Files.notExists(path)) {
            try {
                createConfig(path);
            } catch (IOException e) {
                return false;
            }

            fps = DEFAULT_FPS;
        } else {
            Properties config = new Properties();

            try (FileInputStream fileInputStream = new FileInputStream(CONFIG_NAME)) {
                config.load(fileInputStream);
            } catch (IOException e) {
                return false;
            }

            try {
                fps = Integer.parseInt(config.getProperty("fps"));
            } catch (RuntimeException e) {
                return false;
            }
        }
        return true;
    }

    private static void createConfig(Path path) throws IOException {
        Files.write(path, ("fps=" + DEFAULT_FPS).getBytes());
    }
}
