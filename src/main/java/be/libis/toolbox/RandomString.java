package be.libis.toolbox;

import java.util.Random;

@SuppressWarnings("unused")
public class RandomString {
    public String symbols = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+-=[]{};:<>?,./\\|";

    private final Random random = new Random();
    private final char[] buffer;

    public RandomString(int length) {
        if (length < 1) {
            throw new IllegalArgumentException("length < 1 : " + length);
        }
        buffer = new char[length];
    }

    public String nextString() {
        for (int i = 0; i < buffer.length; i++) {
            buffer[i] = symbols.charAt((random.nextInt(symbols.length())));
        }
        return new String(buffer);
    }
}
