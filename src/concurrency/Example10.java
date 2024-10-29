package concurrency;

import java.util.List;
import java.util.concurrent.*;

/**
 * Executors.methods vs new ThreadPoolExecutor()
 *      --> the latter gives far more customized options
 *      --> note you can cast the Executor methods to ThreadPoolExecutors as well
 *
 * Note there is no predefined saturation policy to make execute block when the work queue is full
 *  --> you can implement one using semaphores
 */
public class Example10 {
    final private static int QUEUE_CAPACITY = 2;
    final private static int POOL_SIZE = 5;
    public static void main(String[] args) {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(POOL_SIZE, POOL_SIZE, 0L,
                TimeUnit.MILLISECONDS, new LinkedBlockingQueue <Runnable>(QUEUE_CAPACITY));
        // Queue will be full --> if larger number of tasks get queue up while thread size is low

        BoundedExecutor boundedExecutor = new BoundedExecutor(Executors.newFixedThreadPool(POOL_SIZE), QUEUE_CAPACITY);
        // if there are too many requests compared to the queue size, the executor thread also handles the request
        // notice the main thread too takes up some work, so in an iteration --> there will be 5 + 1 threads
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // executionHandler comes into play when queue is full
        // switch to AbortPolicy() to see --> only 5 running and 2 waiting in the queue to be run

        List <Integer> list = List.of(1,2,3,4,5,6,7,8,9,10);

        list.forEach(integer -> {
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    System.out.println("Current Thread for " + integer + " " +Thread.currentThread().getName());
                    try {
                        Thread.sleep(5000L);
                        System.out.println("printing " + integer);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            };

            executor.execute(r);
//            try {
//                boundedExecutor.submitTask(r);
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
        });
        executor.shutdown();
    }


}

/**
 * Block execution --> if queue size is full
 * Effectively #parallelprocesses = min(no_of_threads, Queuesize)
 */
class BoundedExecutor{
    private final Executor exec;
    private final Semaphore semaphore;

    public BoundedExecutor(Executor exec, int bound) {
        this.exec = exec;
        this.semaphore = new Semaphore(bound);
    }

    public void submitTask(final Runnable r) throws InterruptedException {
        semaphore.acquire();
        try{
            exec.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        r.run();
                    }
                    finally {
                        semaphore.release();
                    }
                }
            });
        }catch (RejectedExecutionException e){
            semaphore.release();
        }

    }
}