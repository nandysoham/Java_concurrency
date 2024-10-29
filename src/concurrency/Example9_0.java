package concurrency;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * A simple implementation of timed run
 */
public class Example9_0 {
    private final static ScheduledExecutorService cancelExec = new ScheduledThreadPoolExecutor(10);
    public static void timedRun(Runnable r, long timeout, TimeUnit unit){
        System.out.println("timed run Thread - "+ Thread.currentThread().getName());
        final Thread taskThread = Thread.currentThread();
        cancelExec.schedule(new Runnable() {
            @Override
            public void run() {
                System.out.println("Cancellation at " + Thread.currentThread().getName());
                taskThread.interrupt();
            }
        }, timeout, unit);
        r.run();

    }

    public static void main(String[] args) {
        final Runnable r = new Runnable() {
            @Override
            public void run() {
                while(true) {
                    try {
                        System.out.println("Thread sleeping..." + Thread.currentThread().getName());
                        Thread.sleep(1000);
                        System.out.println("Thread woke up ...");

                    } catch (InterruptedException swallowed) {
                        System.out.println("thread has been interrupted... but doing nothing");
                        // try opening this and this shall throw an exception as well
                        throw new RuntimeException(swallowed);
                    }

                }
            }
        };

        try {
            timedRun(r, 5, TimeUnit.SECONDS);
            System.out.println("Printing next line");
            cancelExec.shutdownNow();
        } catch (Throwable e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
