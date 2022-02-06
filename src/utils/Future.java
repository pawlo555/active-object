package utils;


public class Future {
    private boolean isFinish;
    private final int value;

    public Future(int value) {
        isFinish = false;
        this.value = value;
    }

    public synchronized void finishProcessing() {
        isFinish = true;
        notify();
    }

    public synchronized boolean isAvailable() {
        return isFinish;
    }

    public synchronized int getValue() {
        return value;
    }

    public synchronized void sleep() throws InterruptedException {
        if (!isFinish) {
            wait();
        }

    }
}
