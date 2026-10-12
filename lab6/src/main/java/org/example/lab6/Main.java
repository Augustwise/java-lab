package org.example.lab6;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Translator translator = new Translator();
        translator.addWord("hello", "привіт");
        translator.addWord("world", "світ");
        translator.addWord("good", "добрий");
        translator.addWord("morning", "ранок");

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("Англійське слово (Enter — завершити): ");
            String english = scanner.nextLine().trim();
            if (english.isEmpty()) {
                break;
            }

            System.out.print("Український переклад: ");
            String ukrainian = scanner.nextLine().trim();
            translator.addWord(english, ukrainian);
        }

        System.out.print("Введіть фразу англійською: ");
        String phrase = scanner.nextLine();
        System.out.println("Переклад: " + translator.translatePhrase(phrase));
    }
}
