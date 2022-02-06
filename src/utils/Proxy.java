package utils;

public class Proxy {
    private final Monitor monitor;

    public Proxy(Monitor monitor) {
        this.monitor = monitor;
    }

    public Future consume(int value) {
        Future future = new Future(value);
        monitor.addConsumer(future);
        return future;
    }

    public Future produce(int value) {
        Future future = new Future(value);
        monitor.addProducer(future);
        return future;
    }

}
