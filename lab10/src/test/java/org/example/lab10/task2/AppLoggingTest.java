package org.example.lab10.task2;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class AppLoggingTest {
    @TempDir
    Path dir;

    @Test
    void defaultLevelsRouteMessagesToDifferentDestinations() {
        try (AppLogging logging = AppLogging.create(dir.resolve("logs").resolve("test.log"))) {
            assertEquals(AppLogging.Route.CONSOLE_AND_FILE, logging.route(Level.SEVERE));
            assertEquals(AppLogging.Route.CONSOLE_AND_FILE, logging.route(Level.WARNING));
            assertEquals(AppLogging.Route.FILE_ONLY, logging.route(Level.INFO));
            assertEquals(AppLogging.Route.FILE_ONLY, logging.route(Level.FINE));
            assertEquals(AppLogging.Route.DROPPED_BY_LOGGER, logging.route(Level.FINER));
            assertEquals(AppLogging.Route.DROPPED_BY_LOGGER, logging.route(Level.FINEST));
        }
    }

    @Test
    void changingLevelsChangesRoutes() {
        try (AppLogging logging = AppLogging.create(dir.resolve("test.log"))) {
            logging.setLoggerLevel(Level.ALL);
            logging.setConsoleLevel(Level.INFO);
            assertEquals(AppLogging.Route.CONSOLE_AND_FILE, logging.route(Level.INFO));
            assertEquals(AppLogging.Route.FILE_ONLY, logging.route(Level.FINEST));

            logging.setConsoleLevel(Level.OFF);
            assertEquals(AppLogging.Route.FILE_ONLY, logging.route(Level.SEVERE));

            logging.setLoggerLevel(Level.OFF);
            assertEquals(AppLogging.Route.DROPPED_BY_LOGGER, logging.route(Level.SEVERE));
        }
    }

    @Test
    void fileReceivesOnlyMessagesPassedByLoggerLevel() throws IOException {
        Path logFile = dir.resolve("test.log");
        Logger logger = Logger.getLogger(CipherFileService.class.getName());

        try (AppLogging logging = AppLogging.create(logFile)) {
            logger.info("повідомлення-інфо");
            logger.fine("повідомлення-файн");
            logger.finer("повідомлення-файнер");
            logger.warning("повідомлення-варнінг");
        }

        String content = Files.readString(logFile, StandardCharsets.UTF_8);
        assertTrue(content.contains("повідомлення-інфо"));
        assertTrue(content.contains("повідомлення-файн"));
        assertTrue(content.contains("повідомлення-варнінг"));
        assertFalse(content.contains("повідомлення-файнер"));
    }

    @Test
    void serviceWritesProgressAndErrorsToLog() throws IOException {
        Path logFile = dir.resolve("service.log");
        Path source = dir.resolve("in.txt");
        Files.writeString(source, "abc", StandardCharsets.UTF_8);

        try (AppLogging logging = AppLogging.create(logFile)) {
            new CipherFileService().encrypt(source, dir.resolve("out.txt"), 'k');
            try {
                new CipherFileService().encrypt(dir.resolve("missing.txt"), dir.resolve("out2.txt"), 'k');
            } catch (IOException expected) {
                // помилку очікуємо, перевіряється її наявність у журналі
            }
        }

        String content = Files.readString(logFile, StandardCharsets.UTF_8);
        assertTrue(content.contains("Шифрування розпочато"));
        assertTrue(content.contains("Шифрування завершено: оброблено 3 символів"));
        assertTrue(content.contains("SEVERE"));
        assertTrue(content.contains("Шифрування не вдалося"));
    }

    @Test
    void unavailableLogFileFallsBackToConsoleOnly() throws IOException {
        Path blocker = dir.resolve("not-a-directory");
        Files.writeString(blocker, "x");

        try (AppLogging logging = AppLogging.create(blocker.resolve("test.log"))) {
            assertFalse(logging.isFileActive());
            assertEquals(AppLogging.Route.CONSOLE_ONLY, logging.route(Level.SEVERE));
            assertEquals(AppLogging.Route.NOWHERE, logging.route(Level.INFO));
        }
    }
}
