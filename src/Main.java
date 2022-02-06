import agents.Consumer;
import agents.Producer;
import agents.Scheduler;
import observer.UsersObserver;
import utils.Buffer;
import utils.Monitor;
import utils.Proxy;

public class Main {
    private final static int N = 500;
    private final static int PRODUCERS = N;
    private final static int CONSUMERS = N;
    private final static int BUFFER_SIZE = 1000;
    private final static int EXTRA_WORK_TIME_NANOS = 100000;
    private final static int PERIOD_TO_SHOW_DATA = 60000;

    public static void main(String[] args) throws InterruptedException {
        Thread[] consumers = new Thread[CONSUMERS];
        Thread[] producers = new Thread[PRODUCERS];

        UsersObserver observer = new UsersObserver(PERIOD_TO_SHOW_DATA);
        Buffer buffer = new Buffer(BUFFER_SIZE);
        Monitor monitor = new Monitor(buffer);
        Proxy proxy = new Proxy(monitor);

        Thread scheduler = new Thread(new Scheduler(monitor));


        for (int i=0; i<CONSUMERS; i++) {
            consumers[i] = new Thread(new Consumer(proxy, observer,BUFFER_SIZE/2, EXTRA_WORK_TIME_NANOS));
        }
        for (int i=0; i<PRODUCERS; i++) {
            producers[i] = new Thread(new Producer(proxy, observer,BUFFER_SIZE/2, EXTRA_WORK_TIME_NANOS));
        }

        scheduler.start();
        for (int i=0; i<CONSUMERS; i++) {
            consumers[i].start();
        }
        for (int i=0; i<PRODUCERS; i++) {
            producers[i].start();
        }

        scheduler.join();
        for (int i=0; i<CONSUMERS; i++) {
            consumers[i].join();
        }
        for (int i=0; i<PRODUCERS; i++) {
            producers[i].join();
        }
    }
}
