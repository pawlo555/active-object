package agents;

import utils.Monitor;

public class Scheduler implements Runnable {
    private final Monitor monitor;

    public Scheduler(Monitor monitor) {
        this.monitor = monitor;
    }

    @Override
    public void run() {
        while (true) {
            try {
                monitor.schedule();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}