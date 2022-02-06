package utils;

import java.util.LinkedList;
import java.util.Objects;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Monitor {
    private final LinkedList<Future> consumers = new LinkedList<>();
    private final LinkedList<Future> producers = new LinkedList<>();

    ReentrantLock lock = new ReentrantLock();
    Condition schedulerCondition = lock.newCondition();
    private final Waiter waiter = new Waiter(); // class to manage for what scheduler is waiting

    private final Buffer buffer;

    public Monitor(Buffer buffer) {
        this.buffer = buffer;
    }

    // Add consumers Future object to list, if needed it will signal scheduler
    public void addConsumer(Future future) {
        lock.lock();
        consumers.addLast(future);
        if(waiter.isWaitingFor(WaitFor.CONSUMER)) {
            schedulerCondition.signal();
        }
        lock.unlock();
    }

    // as addConsumer
    public void addProducer(Future future) {
        lock.lock();
        producers.addLast(future);
        if(waiter.isWaitingFor(WaitFor.PRODUCER)) {
            schedulerCondition.signal();
        }
        lock.unlock();
    }

    public void schedule() throws InterruptedException {
        lock.lock();
        while (isAllListsEmpty()) { // if all lists all empty wait for any client
            waiter.waitFor(WaitFor.ANYONE);
            schedulerCondition.await();
        }
        // case when we have producer but he can't produce so we need to wait for consumer
        while (waitForConsumer()) {
            waiter.waitFor(WaitFor.CONSUMER);
            schedulerCondition.await();
        }
        // case as below but we wait for producer
        while (waitForProducer()) {
            waiter.waitFor(WaitFor.PRODUCER);
            schedulerCondition.await();
        }
        // now we need to choose between doing production or consumption
        if (canScheduleProduction()) {
            scheduleProduction();
        } else {
            scheduleConsumption();
        }
        waiter.stopWaiting();
        lock.unlock();
    }

    private boolean waitForProducer() {
        return getProduce() == null && buffer.toLittleResources(Objects.requireNonNull(getConsume()).getValue());
    }

    private boolean waitForConsumer() {
        return getConsume() == null && buffer.toLittleSpace(Objects.requireNonNull(getProduce()).getValue());
    }

    private boolean canScheduleProduction() {
        return getProduce() != null && !buffer.toLittleSpace(getProduce().getValue());
    }

    private void scheduleProduction() {
        Future futureProduce = producers.removeFirst();

        buffer.increment(futureProduce.getValue());
        futureProduce.finishProcessing();
    }

    private void scheduleConsumption() {
        Future futureConsume = consumers.removeFirst();

        buffer.decrement(futureConsume.getValue());
        futureConsume.finishProcessing();
    }

    private Future getConsume() {
        return consumers.isEmpty() ? null : consumers.getFirst();
    }

    public Future getProduce() {
        return producers.isEmpty() ? null : producers.getFirst();
    }

    private boolean isAllListsEmpty() {
        return consumers.isEmpty() && producers.isEmpty();
    }
}
