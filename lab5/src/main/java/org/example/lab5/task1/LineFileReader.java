package org.example.lab5.task1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

public class LineFileReader {
    public String findLineWithMostWords(Path path) throws IOException {
        String longestLine = null;
        int mostWords = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(path.toFile(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                int words = line.isBlank() ? 0 : line.trim().split("\\s+").length;
                if (longestLine == null || words > mostWords) {
                    longestLine = line;
                    mostWords = words;
                }
            }
        }

        return longestLine;
    }
}
