package observer;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class UsersObserver {

    private final Map<Integer, Integer> extraWorkMap = new ConcurrentHashMap<>();
    private final Map<Integer, Integer> normalWorkMap = new ConcurrentHashMap<>();

    public UsersObserver(int period) {
        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                showStatistics();
            }
        }, period, period);
    }

    public void addUser(int userId) {
        extraWorkMap.put(userId, 0);
        normalWorkMap.put(userId, 0);
    }

    public void extraWorkDone(int userId) {
        int prevValue = extraWorkMap.get(userId);
        extraWorkMap.put(userId, prevValue+1);
    }

    public void normalWorkDone(int userId) {
        int prevValue = normalWorkMap.get(userId);
        normalWorkMap.put(userId, prevValue+1);
    }

    private void showStatistics() {
        Collection<Integer> values = normalWorkMap.values();
        int min = Collections.min(values);
        int max = Collections.max(values);
        int average = getAverageExtraWork(values);
        System.out.println("Average: " + average);
        System.out.println("Min: " + min);
        System.out.println("Max: " + max);
        System.out.println(normalWorkMap);
        System.out.println();
    }

    private int getAverageExtraWork(Collection<Integer> values) {
        int total = 0;
        for (Integer i: values) {
            total += i;
        }
        return total/values.size();
    }
}