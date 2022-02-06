package utils;

public class Buffer {
    private final int maxBuffer;
    private int buffer = 0;

    public Buffer(int maxBuffer) {
        this.maxBuffer = maxBuffer;
    }

    public boolean toLittleSpace(int n) {
        return buffer + n > maxBuffer;
    }

    public boolean toLittleResources(int n) {
        return buffer - n < 0;
    }

    public void increment(int value) {
        if (toLittleSpace(value)) {
            throw new IllegalStateException("Too much elements in buffer:" + (buffer + value));
        }
        else {
            buffer = buffer + value;
        }
    }

    public void decrement(int value) {
        if (toLittleResources(value)) {
            throw new IllegalStateException("Not enough elements in buffer:" + (buffer - value));
        }
        else {
            buffer = buffer - value;
        }
    }
}
