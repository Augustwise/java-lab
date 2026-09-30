package org.example.lab5.task1;

import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Введіть шлях та ім'я текстового файлу: ");
        String fileName = scanner.nextLine().trim();

        if (fileName.isEmpty()) {
            System.out.println("Шлях до файлу не може бути порожнім.");
            return;
        }

        try {
            String line = new LineFileReader().findLineWithMostWords(Path.of(fileName));
            System.out.println(line == null ? "Файл порожній." : "Рядок з найбільшою кількістю слів: " + line);
        } catch (IOException | InvalidPathException e) {
            System.out.println("Не вдалося прочитати файл: " + e.getMessage());
        }
    }
}
