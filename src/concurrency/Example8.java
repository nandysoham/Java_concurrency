package concurrency;

import java.util.List;
import java.util.concurrent.*;

/**
 * CompletionService: ExecutorService + Blocking Queue
 * 1. You can submit both callable and runnable tasks
 * 2. **Both Callables & Runnables return with a future**
 *      For Runnable --> the future.get() --> will return the value passed as 2nd argument
 * 3. For runnable a seperate result has to be passed in completionService.submit(Runnable, result) -->
 *      which will be passed back as future upon completion
 */
public class Example8 {
    final private static  ExecutorService executor = Executors.newFixedThreadPool(10);
    // reduce capacity to 2 --> blocking queue will throw an exeption
    final private static  BlockingQueue <Future <Integer> > customQueue = new ArrayBlockingQueue<>(2);
    final static CompletionService <Integer> completionService = new ExecutorCompletionService <Integer>(executor, customQueue);

    public static void main(String[] args) {
        final List <Integer> list = List.of(1,2,3,4,5);
        for(Integer value : list){
            completionService.submit(new Callable<Integer>() {
                @Override
                public Integer call() throws Exception {
                    try {
                        System.out.println(Thread.currentThread().getName() + " starting to sleep");
                        Thread.sleep(5000);
                        if (value == 1) {
                            throw new Exception("Custom Exception at 1");           // you can even handle this exception in future
                        }
                        return value*value;
                    } catch (RuntimeException e) {
                        // Queue full
                        System.out.println(e.getMessage());
                        throw e;                // you can swallow the "Queue Full" exception here as well!
                    }

                }
            });
        }

        for(int i =0; i < 5; i++){
            try {
                // if before I remove, the queue is full --> Queue Full Exception!
                // THROWS EXCEPTION - QUEUE FULL
                final Future<Integer> f = completionService.take();
                System.out.println("Returned "+ f.get());
            }
            catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
        executor.shutdown();
    }
}
