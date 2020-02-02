package aqs;

import java.util.Iterator;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;

/**
 * @author: daiweiwei
 * @Date: 2019/8/4 16:09
 * @Description:
 */
//实现独享锁
public class DwwLock1 implements Lock {
    //1.如何判断一个锁的状态或者说拥有者
    volatile AtomicReference<Thread> owner = new AtomicReference<>();
    //保存正在等待的线程
    volatile LinkedBlockingQueue<Thread> waiters = new LinkedBlockingQueue<>();
    @Override
    public void lock() {
        boolean addQ = true;
        while (!tryLock()){
            if(addQ){
                //没拿到锁,假如等待集合
                waiters.offer(Thread.currentThread());
                addQ = false;

            }else {

                //阻塞 挂起当前的线程 不要继续往下跑了
                LockSupport.park();//伪唤醒，就是非park唤醒
            }
        }
        waiters.remove(Thread.currentThread());

    }

    @Override
    public void lockInterruptibly() throws InterruptedException {

    }

    @Override
    public boolean tryLock() {
        return owner.compareAndSet(null,Thread.currentThread());
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }

    @Override
    public void unlock() {
        if(owner.compareAndSet(Thread.currentThread(),null)){
            Iterator<Thread> iterator = waiters.iterator();
            while (iterator.hasNext()){
                Thread next = iterator.next();
                LockSupport.unpark(next);
            }
        }
    }

    @Override
    public Condition newCondition() {
        return null;
    }
}
