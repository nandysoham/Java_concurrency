package concurrency;

import java.util.concurrent.TimeUnit;




public class Example9_1 {
    public static boolean transferMoneyImplicit(int transactionid, Account fromAcc, Account toAcc, int amount) throws Exception {
        boolean done = false;
        while (true) {
            synchronized (fromAcc){
                synchronized (toAcc) {
                    if (fromAcc.getBalance() >= amount) {
                        fromAcc.debit(amount);
                        toAcc.credit(amount);
                        System.out.println("transaction id of " + transactionid + " done ");
                        done = true;

                    } else {
                        throw new Exception("Balance of account " + fromAcc.getBalance() + " less than " + amount);
                    }

                }
            }

            if(done) return true;
        }


    }

    public static void main(String[] args) {
        Account account1 = new Account(10000);
        Account account2 = new Account(10000);

        for (int i = 0; i < 100; i++) {
            int finalI = i;
            Thread thread = new Thread(() -> {
                if (finalI % 2 == 0) {
                    try {
                        transferMoneyImplicit(finalI, account1, account2, 1);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    try {
                        transferMoneyImplicit(finalI, account2, account1, 1);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            });

            thread.start();

        }
    }

}
