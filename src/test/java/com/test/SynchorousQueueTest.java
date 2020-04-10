package com.test;

import org.junit.Test;

import java.util.concurrent.*;

public class SynchorousQueueTest {

    @Test
    /**
     * Synchronous queue acts as an exchange point between threads.
     */
    public void test() throws InterruptedException {
        SynchronousQueue<Integer> q = new SynchronousQueue<>();
        Runnable p =  () ->{
            Integer n = ThreadLocalRandom.current().nextInt();
            try {
                q.put(n);
            }catch(InterruptedException ex){
                ex.printStackTrace();
            }
        };
        Runnable c =  () ->{
            try {
                Integer n = q.take();
                System.out.println("Consumed "+n);
            }catch(InterruptedException ex){
                ex.printStackTrace();
            }
        };

        ExecutorService executor = Executors.newFixedThreadPool(2);
        executor.execute(p);

        executor.execute(c);

        executor.awaitTermination(5,TimeUnit.SECONDS);
        executor.shutdown();


    }
}
