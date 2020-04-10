package com.test;

import org.junit.Test;

class ProducerConsumer{
    private int p;
    private void prt(Object o){
        System.out.println(o);
    }
    public void produce(){
        synchronized (this){
            p = Math.round(10);
            prt("Producing:" + p);

            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            prt("Producing resumed.");
        }
    }
    public void consume()  {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        synchronized (this){
            prt("Consuming:"+p);
            notify();
            prt("Consuming resumed");
        }
    }
}
public class ObjectWaitNotifiyTest {

    @Test
    public void  testWaitNotify(){
        final ProducerConsumer pc = new ProducerConsumer();
        Thread producerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                pc.produce();
            }
        });
        Thread consumerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                pc.consume();
            }
        });
        producerThread.start();
        consumerThread.start();
        try {
            producerThread.join();
            consumerThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
