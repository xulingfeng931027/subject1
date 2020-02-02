package aqs;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author: daiweiwei
 * @Date: 2019/8/4 17:34
 * @Description:
 */

//自定义信号量实现
public class NeteaseSemaphore {
    NeteaseAqs aqs = new NeteaseAqs(){
        @Override
        public int tryAcquireShared() {
            for(;;){

                int count = getState().get();
                int n = count - 1;
                if(count <= 0 || n < 0){
                    return -1;
                }
                if(getState().compareAndSet(count,n)){
                    return 1;
                }
            }
        }

        @Override
        public boolean tryReleaseShared() {
            return getState().incrementAndGet() >= 0;
        }
    };
    public NeteaseSemaphore(int count){
        aqs.getState().set(count);
    }

    public void acquire(){
        aqs.acquireShared();
    }

    public void release(){
        aqs.releaseShared();
    }


}
