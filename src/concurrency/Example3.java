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
                        queue.put("data");
                        System.out.println("Added Data for " + ++times + ", times");
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }


//                    try {
//                        // try put if you wish to wait till space is available
//                        queue.add("data");
//                        System.out.println("Added Data for " + ++times + ", times");
//                    }
//                    catch (Exception e){
//                        // Queue full exception
//                       // Swallow Exception
//                        // program runs infinitely
//                    }
//
//                    if(Thread.currentThread().isInterrupted()){
//                        // throwing a runtime exception will stop the thread
////                        throw new RuntimeException("Producer thread interrupted");
//
//                    }
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
                        throw new RuntimeException(e);
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
                System.out.println("Interrupting producer");
                producer.interrupt();
            }
        });

        producer.start();
        consumer.start();
        interrupter.start();
    }

}
