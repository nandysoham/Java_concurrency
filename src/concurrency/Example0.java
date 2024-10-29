package concurrency;


class MyThread extends Thread{
    @Override
    public void run() {
        System.out.println("Hello from thread" + Thread.currentThread().getName());
    }
}
/**
 * You cannot use thread.start() more than once
 * Once a thread starts , it will tear down after completion
 *
 *
 * Note: the start() method is actually a native method
 *
 */
public class Example0 {

    public static void main(String[] args) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("Hi from thread");
            }
        });

        thread.start();

        System.out.println("in b/w");
//        thread.start();               // cannot do so


        thread = new Thread(()->{
            System.out.println("From new thread");
        });

        thread.start();

        MyThread myThread = new MyThread();
        myThread.start();
    }
}
