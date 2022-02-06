package utils;

public class Waiter {
    private WaitFor waitFor = WaitFor.NO_ONE;

    public void waitFor(WaitFor waitFor) {
        this.waitFor = waitFor;
    }

    public void stopWaiting() {
        waitFor(WaitFor.NO_ONE);
    }

    public boolean isWaitingFor(WaitFor waitFor) {
        return this.waitFor == waitFor || isWaitingForAnyOne(waitFor) ;
    }

    private boolean isWaitingForAnyOne(WaitFor waitFor) {
        return this.waitFor == WaitFor.ANYONE && (waitFor == WaitFor.CONSUMER || waitFor == WaitFor.PRODUCER);
    }
}
