package org.example.lab5.task3;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

public class CipherFileService {
    public void encrypt(Path source, Path destination, char key) throws IOException {
        process(source, destination, key);
    }

    public void decrypt(Path source, Path destination, char key) throws IOException {
        process(source, destination, -key);
    }

    private void process(Path source, Path destination, int shift) throws IOException {
        if (source.toAbsolutePath().normalize().equals(destination.toAbsolutePath().normalize())) {
            throw new IOException("Вхідний і вихідний файли повинні бути різними.");
        }

        try (ShiftReader reader = new ShiftReader(
                new BufferedReader(new FileReader(source.toFile(), StandardCharsets.UTF_8)), shift);
             BufferedWriter writer = new BufferedWriter(
                     new FileWriter(destination.toFile(), StandardCharsets.UTF_8))) {
            char[] buffer = new char[4096];
            int count;
            while ((count = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, count);
            }
        }
    }
}
