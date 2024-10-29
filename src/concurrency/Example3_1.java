package concurrency;

import java.util.concurrent.locks.Lock;

/**
 * Sleep vs Wait
 */
class Interruptible1{
    private final String lock = "lock";
    public void syncMethod1(){
        synchronized (lock){
            try {
                System.out.println("Thread 1 going to sleep");
                Thread.sleep(5000);
                System.out.println("thread1 exiting from sleep....");
            } catch (InterruptedException e) {
                System.out.println("Sleeping Thread Interrupted...");
            }
        }
    }

    public void syncMethod2(){
        // tries to acquire the lock
        System.out.println("Awaiting thread trying to acquire the lock");
        synchronized (lock){
            System.out.println("Awaiting Thread Acquired the lock");
        }
    }

    public void syncMethod3(){
        synchronized (lock){
            try{
                System.out.println("Thread 3 inside method");
                Thread.sleep(2000);
                System.out.println("Thread 3 going to wait()");
                lock.wait();                // current thread has to be the owner of this monitor
                // you can also specify a timeout on wait(long timeout) --> will wake up automatically after this timeout
//                new Example1().wait();
                System.out.println("thread 3 now back from wait");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void syncMethod4(){
        // tries to acquire the lock
        synchronized (lock){
            System.out.println("Awaiting Thread Acquired the lock");
            lock.notify();
        }
    }
}


public class Example3_1 {

    public static void main(String[] args) {
        final Interruptible1 obj = new Interruptible1();
//        final Thread thread1 = new Thread(()->{
//            obj.syncMethod1();
//        });
//
//        final Thread thread2 = new Thread(()->{
//            obj.syncMethod2();
//        });

        final Thread thread3 = new Thread(()->{
            obj.syncMethod3();
        });

        final Thread thread4 = new Thread(()->{
            obj.syncMethod4();
        });

        // sleep case
//        thread1.start();
//        thread2.start();


        // wait case
        thread3.start();
        thread4.start();


    }
}
