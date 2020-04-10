package com.test;

import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.Callable;
import java.util.concurrent.CyclicBarrier;
import java.util.function.Function;

public class BarrierTest {

    @Test
    public void testBarrier() throws BrokenBarrierException, InterruptedException {
        final int a = 2;
        final int b = 3;
        CyclicBarrier barrier = new CyclicBarrier(3);
        int numParties = barrier.getParties();
        System.out.println("The number of parties that need to trip the barrier:" + numParties);
        Compute comp1 = new Compute(barrier, new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return Integer.valueOf(a * b);
            }
        });
        Compute comp2 = new Compute(barrier, new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return Integer.valueOf(a + b);
            }
        });
        new Thread(comp1).start();
        new Thread(comp2).start();
        //Waits until all parties have invoked await on this barrier.
        barrier.await();
        int combinedResult = comp1.getResult() + comp2.getResult();
        Assert.assertEquals(11, combinedResult);
        barrier.reset();
        System.out.println("Barrier successfully reset.");
    }
}

class Compute implements Runnable {
    private CyclicBarrier barrier;
    private Callable<Integer> func;

    public Compute(CyclicBarrier barrier, Callable<Integer> func) {
        this.barrier = barrier;
        this.func = func;
    }

    private int result;

    public void run() {
        String threadName = Thread.currentThread().getName();
        try {
            this.result = func.call();
            int arrivalIndex = this.barrier.await();
            System.out.println(threadName + " barrier arrival index:" + arrivalIndex);
            // number of parties waiting at the barrier
            System.out.println(threadName + " Number of parties waiting at the barrier " +
                    "at this point = " + barrier.getNumberWaiting());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getResult() {
        return this.result;
    }
}
