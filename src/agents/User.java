package agents;

import observer.UsersObserver;
import utils.Future;
import utils.Proxy;

import static java.lang.Thread.sleep;

public abstract class User extends Agent {
    private static final int NANOS_MAX = 1000000;

    protected final Proxy proxy;

    private final int extraWorkTimeNanosPerUnit;
    private final int maxRandNumber;

    private int extraWorkTimeNanos;
    private int extraWorkTimeMilli;
    private final UsersObserver observer;

    public User(Proxy proxy, UsersObserver observer, int maxRandNumber, int extraWorkTimeNanos) {
        super();
        this.proxy = proxy;
        this.maxRandNumber = maxRandNumber;
        this.extraWorkTimeNanosPerUnit = extraWorkTimeNanos;
        this.observer = observer;
        observer.addUser(getId());
    }

    @Override
    public void run() {
        while (true) {
            Future future = doNormalTask();
            calcTimeOfExtraWork(future);
            if (!future.isAvailable()) {
                try {
                    doExtraWork();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            try {
                future.sleep();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            observer.normalWorkDone(getId());
        }
    }

    private void calcTimeOfExtraWork(Future future) {
        int value = future.getValue();
        int totalExtraWorkTime = value*extraWorkTimeNanosPerUnit;
        extraWorkTimeNanos = totalExtraWorkTime%NANOS_MAX;
        extraWorkTimeMilli = totalExtraWorkTime/NANOS_MAX;
    }


    public void doExtraWork() throws InterruptedException {
        sleep(extraWorkTimeMilli, extraWorkTimeNanos);
    }

    public abstract Future doNormalTask();

    protected int randNumber() {
        return Math.abs(generator.nextInt()) % maxRandNumber + 1;
    }
}
