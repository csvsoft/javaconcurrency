package com.test;


import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Semaphore;

public class SemaphoreTest {
    class BoundedHashSet<T> {
        private Set<T> set;
        private Semaphore sem;

        public BoundedHashSet(int n) {
            this.set = Collections.synchronizedSet(new HashSet<T>(n));
            this.sem = new Semaphore(n);
        }

        public boolean add(T t) throws InterruptedException {
            this.sem.acquire(1);
            boolean wasAdded = false;
            try {
                wasAdded = this.set.add(t);
                return wasAdded;
            } finally {
                if (!wasAdded) {
                    this.sem.release();
                }
            }
        }

        public boolean remove(T e) {
            boolean wasRemoved = this.set.remove(e);
            if(wasRemoved) {
                this.sem.release(1);
            }
            return wasRemoved;
        }
    }
}
