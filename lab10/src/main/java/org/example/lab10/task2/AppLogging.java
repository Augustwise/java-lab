package org.example.lab10.task2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class AppLogging implements AutoCloseable {
    public static final String ROOT_NAME = "org.example.lab10";
    public static final Path DEFAULT_LOG_FILE = Path.of("logs", "lab10.log");

    public static final List<Level> LEVELS = List.of(
            Level.SEVERE, Level.WARNING, Level.INFO, Level.CONFIG, Level.FINE, Level.FINER, Level.FINEST);

    public enum Route { DROPPED_BY_LOGGER, NOWHERE, CONSOLE_ONLY, FILE_ONLY, CONSOLE_AND_FILE }

    // Тримаємо посилання на логер у полі класу, щоб збирач сміття не скинув його налаштування
    private final Logger logger = Logger.getLogger(ROOT_NAME);
    private final Handler console;
    private final Handler file;

    private AppLogging(Handler console, Handler file) {
        this.console = console;
        this.file = file;
    }

    public static AppLogging create(Path logFile) {
        Logger logger = Logger.getLogger(ROOT_NAME);
        for (Handler old : logger.getHandlers()) {
            logger.removeHandler(old);
            old.close();
        }
        // Вимикаємо передачу повідомлень батьківському логеру, щоб вони не дублювалися в консолі
        logger.setUseParentHandlers(false);
        // Логер пропускає повідомлення від FINE і вище (FINER та FINEST ігноруються)
        logger.setLevel(Level.FINE);

        // У консоль виводимо лише помилки (SEVERE) та попередження (WARNING)
        Handler console = new ConsoleHandler();
        console.setLevel(Level.WARNING);
        console.setFormatter(new LogFormatter(false));
        logger.addHandler(console);

        Handler file = null;
        try {
            file = openFileHandler(logFile);
            logger.addHandler(file);
        } catch (IOException | RuntimeException e) {
            logger.log(Level.SEVERE, "Не вдалося відкрити файл журналу " + logFile
                    + ", повідомлення виводитимуться лише в консоль", e);
        }
        return new AppLogging(console, file);
    }

    private static Handler openFileHandler(Path logFile) throws IOException {
        Path parent = logFile.toAbsolutePath().getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }
        String pattern = logFile.toAbsolutePath().toString().replace("\\", "/").replace("%", "%%");
        FileHandler handler = new FileHandler(pattern, true);
        handler.setEncoding("UTF-8");
        // Файловий обробник приймає всі повідомлення, які пропустив логер
        handler.setLevel(Level.ALL);
        handler.setFormatter(new LogFormatter(true));
        return handler;
    }

    public Level getLoggerLevel() {
        return logger.getLevel();
    }

    public void setLoggerLevel(Level level) {
        logger.setLevel(level);
    }

    public Level getConsoleLevel() {
        return console.getLevel();
    }

    public void setConsoleLevel(Level level) {
        console.setLevel(level);
    }

    public boolean isFileActive() {
        return file != null;
    }

    // Перевіряє, куди потрапить повідомлення заданого рівня при поточних налаштуваннях фільтрів
    public Route route(Level level) {
        if (!logger.isLoggable(level)) {
            return Route.DROPPED_BY_LOGGER;
        }
        boolean toConsole = passes(console, level);
        boolean toFile = file != null && passes(file, level);
        if (toConsole && toFile) {
            return Route.CONSOLE_AND_FILE;
        }
        if (toConsole) {
            return Route.CONSOLE_ONLY;
        }
        return toFile ? Route.FILE_ONLY : Route.NOWHERE;
    }

    private static boolean passes(Handler handler, Level level) {
        int threshold = handler.getLevel().intValue();
        return threshold != Level.OFF.intValue() && level.intValue() >= threshold;
    }

    public void logSamples() {
        Logger demo = Logger.getLogger(ROOT_NAME + ".demo");
        for (Level level : LEVELS) {
            demo.log(level, "Тестове повідомлення рівня " + level.getName());
        }
    }

    @Override
    public void close() {
        Logger.getLogger(ROOT_NAME).removeHandler(console);
        console.close();
        if (file != null) {
            Logger.getLogger(ROOT_NAME).removeHandler(file);
            file.close();
        }
    }
}
