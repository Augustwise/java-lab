package org.example.lab10.task1;

import java.util.NoSuchElementException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try {
            literalDemo();
            System.out.println();
            keyboardDemo(scanner);
        } catch (NoSuchElementException e) {
            System.out.println("Введення завершено раніше, ніж очікувалося.");
        } catch (IllegalStateException e) {
            System.out.println("Помилка: " + e.getMessage());
        }
    }

    private static void literalDemo() {
        System.out.println("=== 1. Рядок-літерал, значення для заміни задано програмно ===");
        String text = "Hello, Java!";
        String sameLiteral = "Hello, Java!";
        String replacement = "Рядок змінено рефлексією";

        System.out.println("Рядок до зміни:   " + text);
        System.out.println("Внутрішній масив: " + StringMutator.describeInternals(text));

        StringMutator.replaceContent(text, replacement);

        System.out.println("Рядок після зміни: " + text);
        System.out.println("Внутрішній масив:  " + StringMutator.describeInternals(text));
        System.out.println("Інша змінна з тим самим літералом: " + sameLiteral);
        // Виводимо окремим викликом, щоб компілятор не склеїв два рядки в один ще під час компіляції
        System.out.print("Сам літерал \"Hello, Java!\" тепер дає: ");
        System.out.println("Hello, Java!");
        System.out.println("Літерали з однаковим текстом — це один об'єкт у пулі рядків,"
                + " тому змінилися всі посилання на нього.");
    }

    private static void keyboardDemo(Scanner scanner) {
        System.out.println("=== 2. Рядок і значення для заміни введені з клавіатури ===");
        String text = readNonEmpty(scanner, "Введіть рядок: ");
        String replacement = readNonEmpty(scanner, "Введіть нове значення: ");

        System.out.println("Рядок до зміни:   " + text);
        System.out.println("Внутрішній масив: " + StringMutator.describeInternals(text));

        StringMutator.replaceContent(text, replacement);

        System.out.println("Рядок після зміни: " + text);
        System.out.println("Внутрішній масив:  " + StringMutator.describeInternals(text));
        System.out.println("Довжина рядка: " + text.length());
    }

    private static String readNonEmpty(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine();
            if (!line.isEmpty()) {
                return line;
            }
            System.out.println("Рядок не може бути порожнім.");
        }
    }
}
