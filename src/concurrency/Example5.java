package concurrency;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

/**
 * Runnable --> run() --> return type void, no exceptions handled  -- executor.execute()
 * Callable --> returns a result and returns checked exceptions as well!    -- executor.submit()
 *
 */
public class Example5 {
    public static void main(String[] args) {
        final Callable <String> task = ()->{
            Thread.sleep(5000);
//            if(true) {
//                throw new Exception("random exceptions");
//            }
            return "completed";
        };

        final long endTime = System.nanoTime() + 10L * 1000000000L;
        final FutureTask <String> futureTask = new FutureTask<>(task);            // note: returned as futures
        // can also give (Runnable, V Result);
        final Thread thread = new Thread(futureTask);
        thread.start();
        try{
            System.out.println("Thread sleeping...");
            Thread.sleep(4000);
            System.out.println("Thread out of sleep");
//            String result = futureTask.get();               // can also be an exception
            long timeLeft = endTime - System.nanoTime();
            String result = futureTask.get(timeLeft, TimeUnit.NANOSECONDS);
            System.out.println(result);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
