package org.seckill;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.interceptor.CacheAspectSupport;
import org.springframework.cache.interceptor.CacheInterceptor;
import org.springframework.cache.support.AbstractCacheManager;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.core.RedisTemplate;

import java.lang.reflect.Array;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;

public class Test {

//    Lock lock =new ReentrantLock();
//    Condition condition = lock.newCondition();

    public static void main(String[] args) throws Exception {
//        RedisCacheManager
//                CacheInterceptor
//
//        CacheAspectSupport
//        AbstractQueuedSynchronizer
//        BlockingQueue
        //ConcurrentLinkedQueue
        if( args instanceof Object){

        }

        int n = 2;  // 10
        System.out.println(n >>> 1);
        n |= n >>> 1; // 01 按位 或运算

        System.out.println("==== " + n);

        ReentrantLock lock = new ReentrantLock();
        Condition condition = lock.newCondition();
        List list = new ArrayList<>(2);
        int size = 2;

        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {


                for (int i = 0; i < 6; i++) {

                    lock.lock();

                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if (list.size() >= size) {
                        try {
                            condition.await();
                            System.out.println("thread1 await ...");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    list.add(i);
                    System.out.println("list add i=" + i);
                    lock.unlock();
                }
                System.out.println("unlock");
            }

        });

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                int count = 0;
                while (list.size() <= size && count < 7) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    lock.lock();
                    if (!list.isEmpty()) {
                        list.remove(0);
                        condition.signal();
                        System.out.println(" remove list and signal ");
                    }
                    lock.unlock();
                    count++;
                }

            }
        });
        thread1.start();
        thread.start();

//        ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(2,6,20, TimeUnit.MILLISECONDS,new ArrayBlockingQueue<>(3));
//        ExecutorService executorService = Executors.newFixedThreadPool(8);
//        Semaphore semaphore = new Semaphore(2);
//        Exchanger<String> exchanger = new Exchanger<>();
//
//        for (int i = 0; i < 8; i++) {
//            executorService.execute(new Runnable() {
//                @Override
//                public void run() {
//
//                    try {
//                        semaphore.acquire();
//                        System.out.println(Thread.currentThread().getName() + "获得");
//                        TimeUnit.SECONDS.sleep(4);
//                        semaphore.release();
//                        System.out.println(Thread.currentThread().getName() + "release");
//
//
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//
//                }
//            });
//        }
//        executorService.shutdown();


//        int water = 0;
//        long time = System.currentTimeMillis();
//        int size = 10;
//        int rat = 3;
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        long now = System.currentTimeMillis();
//        int out = (int)(now-time)/700*rat;
//        System.out.println(out);
//
//
//        Thread thread = new Thread(() -> {
//            System.out.println(Thread.currentThread().getName() + "被park");
//            LockSupport.park();
//            System.out.println(Thread.currentThread().getName() + "被唤醒");
//        });
//        thread.start();
//        try {
//            Thread.sleep(3000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        LockSupport.unpark(thread);


//        for (int i = 0; i < 10; i++) {
//
//            Thread thread = new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    Lock lock =new ReentrantLock();
//                    Condition condition = lock.newCondition();
//
//                    lock.lock();
//                    try {
//                        condition.await();
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }finally {
//                        lock.unlock();
//                    }
//
//                }
//
//            });
//            thread.start();
//        }


    }


}
