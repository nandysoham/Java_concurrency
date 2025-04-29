package concurrency;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class CheckBlocking {
    public static void main(String[] args) {
        BlockingQueue <Integer> queue = new ArrayBlockingQueue<>(5);
        for(int i =0; i < 10; i++){
            try {
                queue.put(i);
                System.out.println("put in " + i);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        System.out.println("After putting everything !");
    }
}
