package concurrency;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


public class Example1_3 {
    private final AtomicInteger size = new AtomicInteger(0);
    private final List<Object> items = Collections.synchronizedList(new ArrayList<>());

    public void add(Object item) {
        items.add(item);
        size.incrementAndGet();
    }

    public Object remove() {
        if (size.get() == 0) {
            return null;
        }
        Object item = items.remove(0);
        size.decrementAndGet();
        return item;
    }

    public int size() {
        return size.get();
    }

    public static void main(String[] args) {
        Example1_3 obj = new Example1_3();
        for(int i =0; i < 10000; i++){
            final int ii = i;
            Thread thread = new Thread(()->{
               if(ii % 5 == 0 ) obj.add(1);
               else {
                   Object o = obj.remove();
               }
            });
            thread.start();
        }
    }
}