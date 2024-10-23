package concurrency;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/***
 * Executors:
 */
public class Example7 {
    private static final int NTHREADS = 100;
    private static final ExecutorService exec = Executors.newFixedThreadPool(NTHREADS);
    private static int counter = 0;

    public static void main(String[] args) {
        for(int i =0; i < 10; i++){
            final Runnable task = new Runnable() {
                @Override
                public void run() {
                    try {
                        System.out.println("Thread Name " + Thread.currentThread().getName());
                        Thread.sleep(5000);
                        System.out.println("Printing " + counter++);
                        if(counter == 5){
                            throw new Exception("Custom exception which is never handled");
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            };

            exec.execute(task);

        }


        exec.shutdown();            // until you explicitly shut it down --> jvm will not stop
    }
}
