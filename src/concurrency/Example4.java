package concurrency;

import java.util.ArrayList;

import java.util.Collections;
import java.util.List;

public class Example4 {
    public static void main(String []args) throws InterruptedException
    {
        final List<String> list = new ArrayList<>();
        final List<String> synList = Collections.synchronizedList(new ArrayList<>());

        final List <String> ones = new ArrayList<>();
        final List <String> twos = new ArrayList<>();
        for(int i =0; i < 5000000; i++){
            ones.add("one");
            twos.add("two");
        }
        for(int i =0; i < 10; i++) {
            final Thread t1 = new Thread(new Runnable() {
                @Override
                public void run() {
                    list.addAll(ones);
                    synList.addAll(ones);
                }
            });

            final Thread t2 = new Thread(new Runnable() {
                @Override
                public void run() {
                    list.addAll(twos);
                    synList.addAll(twos);
                }
            });

            t1.start();
            t2.start();

            t1.join();
            t2.join();

            System.out.println("First index of two for normal list = "+ list.indexOf("two"));
            System.out.println("First index of two for sync list = " +synList.indexOf("two"));
            System.out.println("---------------------------------------------------------------");
            list.clear();
            synList.clear();
        }
    }
}
