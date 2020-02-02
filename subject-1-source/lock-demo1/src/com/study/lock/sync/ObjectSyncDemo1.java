package com.study.lock.sync;

// 锁 方法(静态/非静态),代码块(对象/类)
public class ObjectSyncDemo1 {

    static Object temp = new Object();

    public void test1() {
        synchronized (this) {
            try {
                System.out.println(Thread.currentThread() + " 我开始执行");
                Thread.sleep(3000L);
                System.out.println(Thread.currentThread() + " 我执行结束");
            } catch (InterruptedException e) {
            }

        }
    }

    public static void main(String[] args) throws InterruptedException {
    ObjectSyncDemo1 objectSyncDemo1=new ObjectSyncDemo1();
        new Thread(() -> {
            objectSyncDemo1.test1();
        }).start();

        Thread.sleep(1000L); // 等1秒钟,让前一个线程启动起来
        new Thread(() -> {
            objectSyncDemo1.test1();
        }).start();
    }
}
