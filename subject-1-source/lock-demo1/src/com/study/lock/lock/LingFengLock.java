package com.study.lock.lock;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;

/**
 * 实现一把简单的独占锁
 *
 * @author 逼哥
 * @date 2020/2/2
 */
public class LingFengLock implements Lock {

    private volatile AtomicReference<Thread> owner = new AtomicReference<>();
    private volatile Queue<Thread> queue = new ConcurrentLinkedQueue<>();

    @Override
    public void lock() {
        boolean added = false;
        while (!tryLock()) {
            if (!added) {
                queue.add(Thread.currentThread());
                added = true;
            } else {
                LockSupport.park();
            }
        }
    queue.remove(Thread.currentThread());
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {

    }


    @Override
    public boolean tryLock() {
        return owner.compareAndSet(null, Thread.currentThread());
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }

    @Override
    public void unlock() {
        owner.compareAndSet(Thread.currentThread(), null);
        queue.forEach(LockSupport::unpark);
    }

    @Override
    public Condition newCondition() {
        return null;
    }
}
