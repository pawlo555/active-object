package agents;

import observer.UsersObserver;
import utils.Future;
import utils.Proxy;


public class Consumer extends User {

    public Consumer(Proxy proxy, UsersObserver observer, int maxRandNumber, int extraWorkTimeNanos) {
        super(proxy, observer, maxRandNumber, extraWorkTimeNanos);
    }

    @Override
    public Future doNormalTask() {
        return proxy.consume(randNumber());
    }


}