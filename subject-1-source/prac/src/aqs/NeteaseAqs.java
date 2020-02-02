package aqs;

import java.util.Iterator;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.LockSupport;

/**
 * @author: daiweiwei
 * @Date: 2019/8/4 16:31
 * @Description:
 */
//抽象队列同步器state owner waiters
public class NeteaseAqs {
    // acquire、 acquireShared ： 定义了资源争用的逻辑，如果没拿到，则等待。
    // tryAcquire、 tryAcquireShared ： 实际执行占用资源的操作，如何判定一个由使用者具体去实现。
    // release、 releaseShared ： 定义释放资源的逻辑，释放之后，通知后续节点进行争抢。
    // tryRelease、 tryReleaseShared： 实际执行资源释放的操作，具体的AQS使用者去实现。

    //1.如何判断资源的状态或者说拥有者
    public volatile AtomicReference<Thread> owner = new AtomicReference<>();
    //保存正在等待的线程
    public volatile LinkedBlockingQueue<Thread> waiters = new LinkedBlockingQueue<>();
    //资源状态
    public volatile AtomicInteger state = new AtomicInteger(0);


    //独占资源代码
    public boolean tryAcquire(){
        throw new UnsupportedOperationException();
    }

    //资源争用逻辑
    public void acquire(){
        boolean addQ = true;
        while (!tryAcquire()){
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

    public void release(){
        if(tryRelease()){
            Iterator<Thread> iterator = waiters.iterator();
            while (iterator.hasNext()){
                Thread next = iterator.next();
                LockSupport.unpark(next);
            }
        }
    }

    public boolean tryRelease() {
        throw new UnsupportedOperationException();
    }

    //共享资源占用逻辑，返回资源占用情况
    public int tryAcquireShared(){
        throw new UnsupportedOperationException();
    }

    public void acquireShared(){
        boolean addQ = true;
        while (tryAcquireShared()<0){
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

    public boolean tryReleaseShared(){
        throw new UnsupportedOperationException();
    }

    public void releaseShared(){
        if(tryReleaseShared()){
            Iterator<Thread> iterator = waiters.iterator();
            while (iterator.hasNext()){
                Thread next = iterator.next();
                LockSupport.unpark(next);
            }
        }
    }

    public AtomicInteger getState() {
        return state;
    }

    public void setState(AtomicInteger state) {
        this.state = state;
    }
}

