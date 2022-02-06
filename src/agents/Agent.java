package agents;

import java.util.Random;


public abstract class Agent implements Runnable {
    private static int NextId = 0;

    protected final int id;
    protected final Random generator;

    public Agent() {
        this.id = getNextId();
        generator = new Random(id);
    }

    private static int getNextId() {
        NextId = NextId + 1;
        return NextId - 1;
    }

    public int getId() {
        return id;
    }


}
