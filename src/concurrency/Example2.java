package concurrency;

public class Example2 {
    public void syncMethod1() throws Exception {
        synchronized (this){
            while(true){
                if(Thread.interrupted()){
                    System.out.println("Thread is interrupted");
                    System.out.println("You might not like to stop the process");
//                    throw new Exception("Interrupted ... Exiting");
                }
            }
        }
    }


    public void syncMethod2() throws Exception {
        synchronized (this){
            Thread.sleep(5000);
        }
    }
    public static void main(String[] args) {
        final Example2 example2Obj = new Example2();
        final Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    example2Obj.syncMethod1();
//                    example2Obj.syncMethod2();
                }
                catch (Exception e){
                    System.out.println("Exception occured");
                    e.printStackTrace();
                }
            }
        });

        t1.start();
        t1.interrupt();
    }
}
