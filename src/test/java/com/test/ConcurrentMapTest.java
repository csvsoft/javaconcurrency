package com.test;

import org.junit.Test;

import java.util.Map;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;

public class ConcurrentMapTest {

    @Test(expected = NullPointerException.class)
    public void testConcurrentHashMap() {
        Map<String, String> mapA = new ConcurrentHashMap<>();
        mapA.putIfAbsent("key1", "vaue1");
        synchronized (mapA) {
            mapA.put("key2", "value2");
        }
        String value1 = mapA.get("key1");

        // null no allowed , different from hashmap
        mapA.put("key3", null);
    }

    @Test
    public void testBlockingQueue() {
        class Producer implements Runnable {
            private BlockingDeque<String> blockingDeque;

            public Producer(BlockingDeque<String> blockingDeque) {
                this.blockingDeque = blockingDeque;
            }

            public void run() {
                try {
                    for (int i = 0; i < 10; i++) {
                        this.blockingDeque.put("msg" + i);
                    }
                    this.blockingDeque.put("stop");
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }

            }
        }
        class Consumer implements Runnable {
            private BlockingDeque<String> blockingDeque;

            public Consumer(BlockingDeque<String> blockingDeque) {
                this.blockingDeque = blockingDeque;
            }

            public void run() {
                String msg = "";
                while (!msg.startsWith("stop")) {
                    try {
                        msg = this.blockingDeque.take();
                        System.out.println(msg);
                        Thread.sleep(500);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }

            }
        }

        BlockingDeque<String> queue = new LinkedBlockingDeque<>(2);
        Producer p = new Producer(queue);
        Consumer c = new Consumer(queue);
        new Thread(p).start();
        new Thread(c).start();
        try {
            Thread.sleep(10000);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }
}
