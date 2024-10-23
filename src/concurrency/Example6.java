package concurrency;

/**
 * Simple example of using threadLocal as a map
 */
public class Example6 {
    private static ThreadLocal <Integer> threadLocal = new ThreadLocal<>();
    public static void main(String[] args) throws InterruptedException {
        final Thread thread1 = new Thread(()->{
           threadLocal.set(10);
            System.out.println(threadLocal.get());          // 10
        });

        final Thread thread2 = new Thread(()->{
            threadLocal.set(20);
            System.out.println(threadLocal.get());          // 20
        });

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

        System.out.println(threadLocal.get());              // null
    }
}
