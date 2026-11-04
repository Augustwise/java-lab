package org.example.lab10.task3;

import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.example.lab10.task2.AppLogging;
import org.example.lab10.task2.CipherFileService;

public class Main {
    private static final Logger LOG = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try (AppLogging logging = AppLogging.create(AppLogging.DEFAULT_LOG_FILE)) {
            LOG.info("Програму запущено (інтернаціоналізований інтерфейс)");
            run(scanner, logging, new Messages(Messages.UKRAINIAN));
            LOG.info("Програму завершено");
        }
    }

    private static void run(Scanner scanner, AppLogging logging, Messages messages) {
        CipherFileService service = new CipherFileService();

        while (true) {
            printMenu(messages);
            if (!scanner.hasNextLine()) {
                LOG.warning("Потік введення закрито, робота завершується");
                return;
            }
            String choice = scanner.nextLine().trim();
            LOG.fine(() -> "Обрано пункт меню: " + choice);

            switch (choice) {
                case "1" -> cipher(scanner, service, messages, true);
                case "2" -> cipher(scanner, service, messages, false);
                case "3" -> changeLevels(scanner, logging, messages);
                case "4" -> demonstrateLogging(logging, messages);
                case "5" -> chooseLanguage(scanner, messages);
                case "0" -> {
                    return;
                }
                default -> {
                    LOG.warning("Невідомий пункт меню: " + choice);
                    System.out.println(messages.get("menu.unknown"));
                }
            }
        }
    }

    private static void printMenu(Messages messages) {
        System.out.println();
        System.out.println("=== " + messages.get("menu.title") + " ===");
        System.out.println("1 — " + messages.get("menu.encrypt"));
        System.out.println("2 — " + messages.get("menu.decrypt"));
        System.out.println("3 — " + messages.get("menu.levels"));
        System.out.println("4 — " + messages.get("menu.demo"));
        System.out.println("5 — " + messages.get("menu.language"));
        System.out.println("0 — " + messages.get("menu.exit"));
        System.out.print(messages.get("menu.prompt") + " ");
    }

    private static void chooseLanguage(Scanner scanner, Messages messages) {
        System.out.println(messages.get("language.title"));
        for (int i = 0; i < Messages.SUPPORTED.size(); i++) {
            Locale locale = Messages.SUPPORTED.get(i);
            System.out.println((i + 1) + " — " + Messages.nativeName(locale) + " (" + locale.getLanguage() + ")");
        }
        System.out.print(messages.get("language.prompt") + " ");

        String input = scanner.nextLine();
        Locale selected = Messages.findSupported(input);
        if (selected == null) {
            LOG.warning("Невідома мова інтерфейсу: " + input.trim());
            System.out.println(messages.format("language.invalid", input.trim()));
            return;
        }

        messages.setLocale(selected);
        LOG.info("Мову інтерфейсу змінено на " + selected.getLanguage());
        System.out.println(messages.format("language.changed", Messages.nativeName(selected)));
    }

    private static void cipher(Scanner scanner, CipherFileService service, Messages messages, boolean encrypt) {
        System.out.print(messages.get("key.prompt") + " ");
        String key = scanner.nextLine();
        if (key.length() != 1 || Character.isSurrogate(key.charAt(0))) {
            LOG.warning("Некоректний ключ: потрібен рівно один символ, отримано довжину " + key.length());
            System.out.println(messages.get("key.invalid"));
            return;
        }

        System.out.print(messages.get("source.prompt") + " ");
        String sourceName = scanner.nextLine().trim();
        System.out.print(messages.get("destination.prompt") + " ");
        String destinationName = scanner.nextLine().trim();
        if (sourceName.isEmpty() || destinationName.isEmpty()) {
            LOG.warning("Шлях до файлу не може бути порожнім");
            System.out.println(messages.get("path.empty"));
            return;
        }

        try {
            Path source = Path.of(sourceName);
            Path destination = Path.of(destinationName);
            if (encrypt) {
                service.encrypt(source, destination, key.charAt(0));
            } else {
                service.decrypt(source, destination, key.charAt(0));
            }
            System.out.println(messages.format("result.saved", destination));
        } catch (IOException e) {
            System.out.println(messages.format("error.file", e.getMessage()));
        } catch (InvalidPathException e) {
            LOG.log(Level.SEVERE, "Некоректний шлях до файлу: " + e.getMessage(), e);
            System.out.println(messages.format("path.invalid", e.getMessage()));
        }
    }

    private static void changeLevels(Scanner scanner, AppLogging logging, Messages messages) {
        System.out.println(messages.get("levels.available"));

        System.out.print(messages.format("levels.loggerPrompt", logging.getLoggerLevel()) + " ");
        Level loggerLevel = readLevel(scanner.nextLine(), messages);
        System.out.print(messages.format("levels.consolePrompt", logging.getConsoleLevel()) + " ");
        Level consoleLevel = readLevel(scanner.nextLine(), messages);

        if (loggerLevel != null) {
            logging.setLoggerLevel(loggerLevel);
            LOG.info("Рівень реєстратора змінено на " + loggerLevel);
        }
        if (consoleLevel != null) {
            logging.setConsoleLevel(consoleLevel);
            LOG.info("Рівень консольного обробника змінено на " + consoleLevel);
        }
        System.out.println(messages.format("levels.summary", logging.getLoggerLevel(), logging.getConsoleLevel()));
    }

    private static Level readLevel(String text, Messages messages) {
        String name = text.trim().toUpperCase(Locale.ROOT);
        if (name.isEmpty()) {
            return null;
        }
        try {
            return Level.parse(name);
        } catch (IllegalArgumentException e) {
            LOG.warning("Невідомий рівень логування: " + name);
            System.out.println(messages.format("levels.unknown", name));
            return null;
        }
    }

    private static void demonstrateLogging(AppLogging logging, Messages messages) {
        System.out.println(messages.format("levels.summary", logging.getLoggerLevel(), logging.getConsoleLevel()));
        System.out.println(messages.get("demo.intro"));
        System.out.println(messages.get("demo.expected"));
        for (Level level : AppLogging.LEVELS) {
            System.out.printf("  %-8s — %s%n", level.getName(), describe(logging.route(level), messages));
        }
        System.out.println(messages.get("demo.console"));
        System.out.flush();

        logging.logSamples();
        System.err.flush();
        System.out.println(logging.isFileActive()
                ? messages.format("demo.file", AppLogging.DEFAULT_LOG_FILE)
                : messages.get("demo.fileUnavailable"));
    }

    private static String describe(AppLogging.Route route, Messages messages) {
        return messages.get(switch (route) {
            case CONSOLE_AND_FILE -> "route.consoleAndFile";
            case CONSOLE_ONLY -> "route.consoleOnly";
            case FILE_ONLY -> "route.fileOnly";
            case NOWHERE -> "route.nowhere";
            case DROPPED_BY_LOGGER -> "route.droppedByLogger";
        });
    }
}
