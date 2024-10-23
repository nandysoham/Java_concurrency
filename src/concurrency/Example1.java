package concurrency;

import java.util.ArrayList;

public class Example1 {

    final private static Example1 example1Obj = new Example1();
    public int a = 1;               // not well guarded
    public static Example1 getExample1Obj() {
//        Example1 example1Obj = new Example1();
        return example1Obj;
    }

    public void syncMethod1() throws InterruptedException {
        synchronized (this){
            System.out.println("Thread starting at method 1");
            System.out.println("Value of a while entering "+ a);
            Thread.sleep(4000);
            System.out.println("Thread leaving from method 1");
            System.out.println("Value of a while leaving "+ a);
        }
    }

    public void syncMethod2() throws InterruptedException {
        synchronized (this){
            // replace this with any thing else say
            // new ArrayList<>()
            System.out.println("Thread starting at method 2");
            Thread.sleep(3000);
            System.out.println("Thread leaving from method 2");
        }
    }

    public void nonSyncMethod1() throws InterruptedException {
        System.out.println("Thread starting at method 3 - Non Sync");
        Thread.sleep(1000);
        a = 10;
        System.out.println("Thread leaving from method 3 - Non sync");

    }

    public static void main(String[] args) {

        final Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Example1.getExample1Obj().syncMethod1();
                }
                catch (Exception e){
                    System.out.println("Error calling sync Method 1");
                    e.printStackTrace();
                }
            }
        });


        final Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Example1.getExample1Obj().syncMethod2();
                }
                catch (Exception e){
                    System.out.println("Error calling sync Method 2");
                    e.printStackTrace();
                }
            }
        });

        final Thread t3 = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Example1.getExample1Obj().nonSyncMethod1();
//                    t1.interrupt();
                }
                catch (Exception e){
                    System.out.println("Error calling non - sync Method 1");
                    e.printStackTrace();
                }
            }
        });
        t1.start();
        t2.start();
        t3.start();

    }
}
