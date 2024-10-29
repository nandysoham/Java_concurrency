package concurrency;

import java.util.concurrent.*;

/**
 * TimedRun -- you expect a function to return in atmost x time
 * However the runnable r shall continue running --> if its not susceptible to interruption
 * However if runnable r --> can respond to interruption -- it is catched at the catch block of run()
 */
public class Example9 {
    final private static  ScheduledExecutorService cancelExec = new ScheduledThreadPoolExecutor(10);
    final private static  ExecutorService taskExecute = Executors.newFixedThreadPool(10);

    public static void timedRun(Runnable r, long timeout, TimeUnit unit) throws Throwable {

        // Note, since this method has no idea of how r would respond to interruption -- it should have a wrapper
        class RethrowableTask implements Runnable{
            private volatile Throwable t;
            // volatile --> to publish safe from the task thread of r to the timedRunThread
            @Override
            public void run() {
                try{
                    r.run();
                }
                catch (Throwable t){
                    // exceptions encountered while running r
                    System.out.println("Exception occured at the r");
                    this.t = t;
                }
            }

            void rethrow() throws Throwable {
                if(t != null){
                    throw t;
                }
            }
        }


        final RethrowableTask task = new RethrowableTask();
        final Thread taskThread = new Thread(task);
        taskThread.start();
        cancelExec.schedule(new Runnable() {
            @Override
            public void run() {
                taskThread.interrupt();
            }
        }, timeout, unit);

        // incase the runnable doesn't respond to interruption
        // join with the main thread --> after twice the time
        taskThread.join(unit.toMillis(2*timeout));
        task.rethrow();

    }

    public static void timedRunUsingFutures(Runnable r, long timeout, TimeUnit unit){
        final Future <?> task = taskExecute.submit(r);
        try{
            task.get(timeout, unit);            // will get null if successfully returned
        } catch (TimeoutException e){
            // cancel the task
        } catch (ExecutionException | InterruptedException e) {
            // execution exception
        } finally {
            // if completed --> no problem
            // else will interrupt
            task.cancel(true);
        }



    }

    public static void main(String[] args) {
        final Runnable r = new Runnable() {
            @Override
            public void run() {
                while(true) {
                    try {
                        System.out.println("Thread sleeping...");
                        Thread.sleep(10000);
                        System.out.println("Thread woke up ...");
                    } catch (InterruptedException swallowed) {
                        System.out.println("thread has been interrupted... but doing nothing");
                        // try opening this and this shall throw an exception as well
//                        try {
//                            throw swallowed;
//                        } catch (InterruptedException e) {
//                            throw new RuntimeException(e);
//                        }
                    }
                }
            }
        };

        try {
            timedRun(r, 5, TimeUnit.SECONDS);
            System.out.println("Printing next line");
        } catch (Throwable e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }

//        System.out.println("running with futures");
//        try {
//            timedRunUsingFutures(r, 5, TimeUnit.SECONDS);
//            System.out.println("Printing next line after done with futures");
//        } catch (Throwable e) {
//            System.out.println(e.getMessage());
//            throw new RuntimeException(e);
//        }
    }
}
