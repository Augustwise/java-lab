package org.example.lab5.task3;

import java.io.FilterReader;
import java.io.IOException;
import java.io.Reader;

public class ShiftReader extends FilterReader {
    private final int shift;
    private int pendingCharacter = -1;

    public ShiftReader(Reader reader, int shift) {
        super(reader);
        this.shift = shift;
    }

    @Override
    public int read() throws IOException {
        if (pendingCharacter != -1) {
            int character = pendingCharacter;
            pendingCharacter = -1;
            return character;
        }

        int character = super.read();
        if (character == -1) {
            return -1;
        }
        if (Character.isHighSurrogate((char) character)) {
            int second = super.read();
            if (second == -1 || !Character.isLowSurrogate((char) second)) {
                throw new IOException("Некоректний символ у вхідному файлі.");
            }
            character = Character.toCodePoint((char) character, (char) second);
        } else if (Character.isLowSurrogate((char) character)) {
            throw new IOException("Некоректний символ у вхідному файлі.");
        }

        int shifted = shift(character);
        if (shifted > Character.MAX_VALUE) {
            pendingCharacter = Character.lowSurrogate(shifted);
            return Character.highSurrogate(shifted);
        }
        return shifted;
    }

    @Override
    public int read(char[] buffer, int offset, int length) throws IOException {
        if (offset < 0 || length < 0 || offset > buffer.length - length) {
            throw new IndexOutOfBoundsException();
        }
        if (length == 0) {
            return 0;
        }

        int count = 0;
        int character;
        while (count < length && (character = read()) != -1) {
            buffer[offset + count++] = (char) character;
        }
        return count == 0 ? -1 : count;
    }

    private int shift(int character) {
        int position = character < 0xD800 ? character : character - 0x800;
        int shifted = Math.floorMod(position + shift, 0x10F800);
        return shifted < 0xD800 ? shifted : shifted + 0x800;
    }
}
