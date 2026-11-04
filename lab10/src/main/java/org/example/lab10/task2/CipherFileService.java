package org.example.lab10.task2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CipherFileService {
    private static final Logger LOG = Logger.getLogger(CipherFileService.class.getName());

    public void encrypt(Path source, Path destination, char key) throws IOException {
        process("Шифрування", source, destination, key, key);
    }

    public void decrypt(Path source, Path destination, char key) throws IOException {
        process("Розшифрування", source, destination, key, -key);
    }

    private void process(String operation, Path source, Path destination, char key, int shift)
            throws IOException {
        LOG.info(() -> operation + " розпочато: " + source + " -> " + destination);
        LOG.fine(() -> "Ключовий символ '" + key + "' має код " + (int) key + ", зсув = " + shift);

        try {
            if (source.toAbsolutePath().normalize().equals(destination.toAbsolutePath().normalize())) {
                throw new IOException("Вхідний і вихідний файли повинні бути різними.");
            }

            long total = 0;
            int blocks = 0;
            try (ShiftReader reader = new ShiftReader(
                    new BufferedReader(new FileReader(source.toFile(), StandardCharsets.UTF_8)), shift);
                 BufferedWriter writer = new BufferedWriter(
                         new FileWriter(destination.toFile(), StandardCharsets.UTF_8))) {
                LOG.fine("Вхідний і вихідний файли відкрито");
                char[] buffer = new char[4096];
                int count;
                while ((count = reader.read(buffer)) != -1) {
                    writer.write(buffer, 0, count);
                    total += count;
                    blocks++;
                    int blockNumber = blocks;
                    int blockSize = count;
                    LOG.finer(() -> "Блок " + blockNumber + ": оброблено " + blockSize + " символів");
                }
            }

            long processed = total;
            LOG.info(() -> operation + " завершено: оброблено " + processed + " символів, результат у " + destination);
        } catch (IOException e) {
            LOG.log(Level.SEVERE, operation + " не вдалося: " + e.getMessage(), e);
            throw e;
        }
    }
}
