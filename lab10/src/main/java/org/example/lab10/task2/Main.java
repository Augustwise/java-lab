package org.example.lab10.task2;

import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    private static final Logger LOG = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try (AppLogging logging = AppLogging.create(AppLogging.DEFAULT_LOG_FILE)) {
            LOG.info("Програму запущено");
            run(scanner, logging);
            LOG.info("Програму завершено");
        }
    }

    private static void run(Scanner scanner, AppLogging logging) {
        CipherFileService service = new CipherFileService();

        while (true) {
            printMenu();
            if (!scanner.hasNextLine()) {
                LOG.warning("Потік введення закрито, робота завершується");
                return;
            }
            String choice = scanner.nextLine().trim();
            LOG.fine(() -> "Обрано пункт меню: " + choice);

            switch (choice) {
                case "1" -> cipher(scanner, service, true);
                case "2" -> cipher(scanner, service, false);
                case "3" -> changeLevels(scanner, logging);
                case "4" -> demonstrateLogging(logging);
                case "0" -> {
                    return;
                }
                default -> {
                    LOG.warning("Невідомий пункт меню: " + choice);
                    System.out.println("Невідомий пункт меню.");
                }
            }
        }
    }

    private static void printMenu() {
        System.out.println();
        System.out.println("=== Шифрування файлів ===");
        System.out.println("1 — Зашифрувати файл");
        System.out.println("2 — Розшифрувати файл");
        System.out.println("3 — Змінити рівні логування");
        System.out.println("4 — Демонстрація логування");
        System.out.println("0 — Вихід");
        System.out.print("Ваш вибір: ");
    }

    private static void cipher(Scanner scanner, CipherFileService service, boolean encrypt) {
        System.out.print("Введіть ключовий символ: ");
        String key = scanner.nextLine();
        if (key.length() != 1 || Character.isSurrogate(key.charAt(0))) {
            LOG.warning("Некоректний ключ: потрібен рівно один символ, отримано довжину " + key.length());
            System.out.println("Ключ має складатися з одного символу.");
            return;
        }

        System.out.print("Введіть шлях та ім'я вхідного файлу: ");
        String sourceName = scanner.nextLine().trim();
        System.out.print("Введіть шлях та ім'я вихідного файлу: ");
        String destinationName = scanner.nextLine().trim();
        if (sourceName.isEmpty() || destinationName.isEmpty()) {
            LOG.warning("Шлях до файлу не може бути порожнім");
            System.out.println("Шлях до файлу не може бути порожнім.");
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
            System.out.println("Результат записано у файл: " + destination);
        } catch (IOException e) {
            System.out.println("Помилка роботи з файлом: " + e.getMessage());
        } catch (InvalidPathException e) {
            LOG.log(Level.SEVERE, "Некоректний шлях до файлу: " + e.getMessage(), e);
            System.out.println("Некоректний шлях до файлу: " + e.getMessage());
        }
    }

    private static void changeLevels(Scanner scanner, AppLogging logging) {
        System.out.println("Доступні рівні: SEVERE, WARNING, INFO, CONFIG, FINE, FINER, FINEST, ALL, OFF");

        System.out.print("Рівень реєстратора [" + logging.getLoggerLevel() + "], Enter — без змін: ");
        Level loggerLevel = readLevel(scanner.nextLine());
        System.out.print("Рівень консольного обробника [" + logging.getConsoleLevel() + "], Enter — без змін: ");
        Level consoleLevel = readLevel(scanner.nextLine());

        if (loggerLevel != null) {
            logging.setLoggerLevel(loggerLevel);
            LOG.info("Рівень реєстратора змінено на " + loggerLevel);
        }
        if (consoleLevel != null) {
            logging.setConsoleLevel(consoleLevel);
            LOG.info("Рівень консольного обробника змінено на " + consoleLevel);
        }
        System.out.println("Реєстратор: " + logging.getLoggerLevel()
                + ", консоль: " + logging.getConsoleLevel() + ", файл: ALL");
    }

    private static Level readLevel(String text) {
        String name = text.trim().toUpperCase();
        if (name.isEmpty()) {
            return null;
        }
        try {
            return Level.parse(name);
        } catch (IllegalArgumentException e) {
            LOG.warning("Невідомий рівень логування: " + name);
            System.out.println("Невідомий рівень «" + name + "», значення не змінено.");
            return null;
        }
    }

    private static void demonstrateLogging(AppLogging logging) {
        System.out.println("Реєстратор: " + logging.getLoggerLevel()
                + ", консоль: " + logging.getConsoleLevel() + ", файл: ALL");
        System.out.println("Зараз буде записано по одному повідомленню кожного рівня.");
        System.out.println("Очікуваний результат:");
        for (Level level : AppLogging.LEVELS) {
            System.out.printf("  %-8s — %s%n", level.getName(), describe(logging.route(level)));
        }
        System.out.println("Повідомлення в консолі (червоні рядки в IntelliJ IDEA):");
        System.out.flush();

        logging.logSamples();
        System.err.flush();
        System.out.println("Повний журнал дивіться у файлі " + AppLogging.DEFAULT_LOG_FILE
                + (logging.isFileActive() ? "." : " (файл недоступний)."));
    }

    private static String describe(AppLogging.Route route) {
        return switch (route) {
            case CONSOLE_AND_FILE -> "консоль і файл";
            case CONSOLE_ONLY -> "тільки консоль";
            case FILE_ONLY -> "тільки файл (консольний обробник його відсікає)";
            case NOWHERE -> "нікуди (відсікають обробники)";
            case DROPPED_BY_LOGGER -> "нікуди (відсікає рівень реєстратора)";
        };
    }
}
