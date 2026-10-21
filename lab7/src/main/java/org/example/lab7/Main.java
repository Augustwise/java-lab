package org.example.lab7;

import java.util.Arrays;
import java.util.Scanner;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class Main {
    public static String[] findWords(String line) {
        if (line.isBlank()) {
            return new String[0];
        }

        Predicate<String> equalLetters = word -> {
            long vowels = word.chars()
                    .filter(letter -> "aeiouAEIOU".indexOf(letter) >= 0)
                    .count();
            return vowels * 2 == word.length();
        };

        return Stream.of(line.trim().split("\\s+"))
                .filter(word -> word.matches("[A-Za-z]+"))
                .filter(equalLetters)
                .toArray(String[]::new);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        System.out.println(Arrays.toString(findWords(line)));
    }
}
