package concurrency;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * If a thread is in a synchronized block the following will happen
 *      1. Interrupted Status will change
 *      2. However the thread will continue to hold the lock
 *
 * However if a thread is sleeping, an interruption occurs and it you rethrow the exception
 *      the thread will release the lock
 *      THROWING AN EXCEPTION BY A THREAD WHILE HOLDING A LOCK --> WILL ALWAYS RELEASE THE LOCK
 *
 * However if you swallow the interrupted exception --> nothing will happen
 *
 * ------------------------------------------------------------------------------------------------
 * For Reentrant Locks, you can chose interrupted locking,
 *      ==> however the thread has to be interrupted before lock.lockInterruptly() is called
 *
 *
 *
 *          SLEEP VS WAIT
 * Sleep doesn't release the lock, however wait release the lock
 * a thread waiting shall wake up only when it is notified
 */

class Interruptible{
    private ReentrantLock lock;
    public void syncMethod1(){
        Thread.currentThread().interrupt();
        synchronized (this){

            long endTime = System.nanoTime() + TimeUnit.SECONDS.toNanos(10);
            boolean notifiedInterruptedOnce = false;
            while(true){
                if(System.nanoTime() > endTime) {
                    break;
                }
                if(!notifiedInterruptedOnce && Thread.currentThread().isInterrupted()){
                    System.out.println("Implicit SyncMethod: Thread has been interrupted ... however it never leaves the lock");
                    notifiedInterruptedOnce = true;
                }
            }
            System.out.println("Implicit SyncMethod: now leaving the lock...");
        }
    }

    public void syncMethod2() {
        lock = new ReentrantLock();
        Thread.currentThread().interrupt();
        try {
            lock.lockInterruptibly();
//            lock.lock();                  <-- this will have the same behaviour as implicit locks
            try {
                long endTime = System.nanoTime() + TimeUnit.SECONDS.toNanos(30);
                boolean notifiedInterruptedOnce = false;
                while (true) {
                    if (System.nanoTime() > endTime) {
                        break;
                    }
                    if (!notifiedInterruptedOnce && Thread.currentThread().isInterrupted()) {
                        System.out.println("Interruptible SyncMethod: Thread has been interrupted ... however it leaves the lock");

                        notifiedInterruptedOnce = true;
                    }
                }
                System.out.println("Interruptible SyncMethod: now leaving the lock...");
            } finally {
                lock.unlock();
            }
        } catch (InterruptedException e) {
            System.out.println("Interruptible SyncMethod: lock interrupted");
        }
    }
}

public class Example12 {
    public static void main(String[] args) throws Exception{
        Interruptible obj = new Interruptible();
        Thread thread1 = new Thread(()->{
           obj.syncMethod1();
        });
        thread1.start();
        Thread.sleep(1000);


        Thread.sleep(1000);
        Thread thread2 = new Thread(()->{
            obj.syncMethod2();

        });
        thread2.start();
        Thread.sleep(1000);
    }

}
