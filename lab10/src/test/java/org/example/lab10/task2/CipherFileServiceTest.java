package org.example.lab10.task2;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class CipherFileServiceTest {
    private final CipherFileService service = new CipherFileService();

    @TempDir
    Path dir;

    @Test
    void decryptRestoresOriginal() throws IOException {
        String text = "Hello, світе!\nДругий рядок 😀\n";
        Path source = dir.resolve("source.txt");
        Path encrypted = dir.resolve("encrypted.txt");
        Path decrypted = dir.resolve("decrypted.txt");
        Files.writeString(source, text, StandardCharsets.UTF_8);

        service.encrypt(source, encrypted, 'k');
        service.decrypt(encrypted, decrypted, 'k');

        assertNotEquals(text, Files.readString(encrypted, StandardCharsets.UTF_8));
        assertEquals(text, Files.readString(decrypted, StandardCharsets.UTF_8));
    }

    @Test
    void shiftsByKeyCode() throws IOException {
        Path source = dir.resolve("in.txt");
        Path encrypted = dir.resolve("out.txt");
        Files.writeString(source, "abc", StandardCharsets.UTF_8);

        service.encrypt(source, encrypted, '\u0001');

        assertEquals("bcd", Files.readString(encrypted, StandardCharsets.UTF_8));
    }

    @Test
    void rejectsSameInputAndOutput() throws IOException {
        Path file = dir.resolve("same.txt");
        Files.writeString(file, "data", StandardCharsets.UTF_8);

        assertThrows(IOException.class, () -> service.encrypt(file, file, 'k'));
        assertEquals("data", Files.readString(file, StandardCharsets.UTF_8));
    }

    @Test
    void missingSourceDoesNotCreateOutput() {
        Path output = dir.resolve("out.txt");

        assertThrows(IOException.class, () -> service.encrypt(dir.resolve("missing.txt"), output, 'k'));
        assertFalse(Files.exists(output));
    }
}
