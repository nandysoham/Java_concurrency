package concurrency;

import java.util.concurrent.CountDownLatch;

/**
 * Using Latches to start 2 threads at the exact same time
 *      --> Create a latch with a countdown of 1
 *      --> once the latch finishes countdown --> start the thread
 */

class WorkerThread extends Thread{
    // rare case in which we create our own thread

    private CountDownLatch latch;

    public WorkerThread(CountDownLatch latch) {
        this.latch = latch;
    }

    @Override
    public void run() {
        try {
            System.out.println("Thread " + Thread.currentThread().getName() + "Created at " + System.nanoTime());
            // say some heavy preprocessing here
            latch.await();
            Long start = System.nanoTime();
            System.out.println("Thread " + Thread.currentThread().getName() + "Starts at " + start);

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}

public class Example2_1 {
    public static void main(String[] args) throws Exception{
        CountDownLatch countDownLatch = new CountDownLatch(1);
        WorkerThread thread1 = new WorkerThread(countDownLatch);
        WorkerThread thread2 = new WorkerThread(countDownLatch);

        thread1.start();
        thread2.start();

        Thread.sleep(2000);     // main thread sleeps
        countDownLatch.countDown();     // latch falls to 0

    }

}
