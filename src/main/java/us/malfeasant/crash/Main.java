package us.malfeasant.crash;

import java.util.ArrayList;

public class Main {
    private static void looper(int count) {
        var tid = Thread.currentThread().threadId();
        if (count > 500) {
            return;
        }
        try {
            Thread.sleep(10);
            if (count % 100 == 0) {
                System.out.println("Thread id: " + tid + " : " + count);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        looper(count + 1);
    }

    public static Thread makeThread(Runnable r) {
        // Didn't actually crash...
        //return new Thread(r);
        return Thread.ofVirtual().unstarted(r);
    }

    public static void main(String[] args) {
        var threads = new ArrayList<Thread>();
        for (int i = 0; i < 20_000; i++) {
            var t = makeThread(() -> looper(1));
            t.start();
            threads.add(t);
            if (i % 1_000 == 0) {
                System.out.println(i + " thread started");
            }
        }
        // Join all the threads
        threads.forEach(t -> {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }
}