package concurrency;

public class Example1_1 {
    private static int a = 0;
    public static void main(String[] args) {
        for(int i =0; i < 100000; i++){
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    a = a + 1;
                }
            });
            thread.start();
        }
        System.out.println("Value of a " + a);
    }
}
