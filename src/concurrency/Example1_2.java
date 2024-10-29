package concurrency;

import java.util.ArrayList;
import java.util.List;

class EscapeClass{
    private List <Integer> list;

    EscapeClass(){
        Thread thread = new Thread(()->{
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            list = new ArrayList<>(1);
            list.add(1);
        });
        thread.start();
    }

    public List<Integer> getList() {
        return list;
    }
}
public class Example1_2 {
    public static void main(String[] args) {
        EscapeClass obj = new EscapeClass();
        System.out.println(obj.getList().get(0));
    }

}
