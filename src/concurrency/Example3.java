package concurrency;


import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Producter
 */
public class Example3 {
    final private static BlockingQueue <String> queue = new ArrayBlockingQueue<>(5);

    public static void main(String[] args) {

        // produces everytime - however blocked by the blocking queue
        final Thread producer = new Thread(new Runnable() {
            @Override
            public void run() {
                int times  = 0;
                while(true){
                    try {
                        queue.add("data");
                        System.out.println("Added Data for " + ++times + ", times");
                    }
                    catch (Exception e){
                        // Queue full exception
                       // Swallow Exception
                        // program runs infinitely
                    }

                    if(Thread.currentThread().isInterrupted()){
                        try {
                            throw new Exception("Producer thread interrupted");
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        });

        // consume once in every 5s
        final Thread consumer = new Thread(new Runnable() {
            @Override
            public void run() {
                int times  = 0;
                while(true){
                    try {
                        Thread.sleep(5000);
                        queue.remove();
                        System.out.println("Removed Data for " + ++times + ", times");
                    }
                    catch (Exception e){
                        System.out.println(e.getMessage());
                        try {
                            throw e;
                        } catch (InterruptedException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }
            }
        });

        // interrupts every 7 s
        final Thread interrupter = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(7000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                producer.interrupt();
            }
        });

        producer.start();
        consumer.start();
        interrupter.start();
    }

}
