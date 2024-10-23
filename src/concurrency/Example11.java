package concurrency;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * trylock allows probablistic deadlock avoidance
 * try with implicit lock - to find a deadlock
 */

class Account {
    final public Lock lock;
    private int balance;

    public Account(int balance) {
        lock = new ReentrantLock();
        this.balance = balance;
    }

    public int getBalance() {
        return balance;
    }

    public void debit(int x) {
        balance -= x;
    }

    public void credit(int x) {
        balance += x;
    }
}

public class Example11 {
    // timed method for transfer of money;
    public static boolean transferMoneyRetrant(int transactionid, Account fromAcc, Account toAcc, int amount, long timeout, TimeUnit unit) throws Exception {
        long endTime = System.nanoTime() + unit.toNanos(timeout);
        boolean done = false;
        while (true) {
            if (fromAcc.lock.tryLock()) {
                try {
                    if (toAcc.lock.tryLock()) {
                        try {
                            if (fromAcc.getBalance() >= amount) {
                                fromAcc.debit(amount);
                                toAcc.credit(amount);
//                                Thread.sleep(1000);

                                System.out.println("transaction id of " + transactionid + " done ");
                                done = true;

                            } else {
                                throw new Exception("Balance of account " + fromAcc.getBalance() + " less than " + amount);
                            }

                        } finally {
                            toAcc.lock.unlock();
                        }
                    }
                } finally {
                    fromAcc.lock.unlock();
                }
            }
            if (System.nanoTime() > endTime) {
                System.out.println("Aborting transaction " + transactionid + "due to timeout");
                return false;
            } else {
                // if you return true --> the ids that are not present --> they couldn't get the lock

//                 return true;
                if (done) return true;
            }

        }


    }

    public static boolean transferMoneyImplicit(int transactionid, Account fromAcc, Account toAcc, int amount, long timeout, TimeUnit unit) throws Exception {
        long endTime = System.nanoTime() + unit.toNanos(timeout);
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

            if(System.nanoTime() > endTime){
                System.out.println("Aborting transaction " + transactionid + "due to time out");
                return false;
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
//                        transferMoneyRetrant(finalI, account1, account2, 1, 2, TimeUnit.SECONDS);
                        transferMoneyImplicit(finalI, account1, account2, 1, 2, TimeUnit.SECONDS);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    try {
//                        transferMoneyRetrant(finalI, account2, account1, 1, 2, TimeUnit.SECONDS);
                        transferMoneyImplicit(finalI, account2, account1, 1, 2, TimeUnit.SECONDS);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            });

            thread.start();

        }
    }
}



