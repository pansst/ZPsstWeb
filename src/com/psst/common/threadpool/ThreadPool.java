package com.psst.common.threadpool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPool extends ScheduledThreadPoolExecutor {

    public ThreadPool(int arg0) {
        super(arg0);
    }

    @Override
    protected void beforeExecute(Thread arg0, Runnable arg1) {
        super.beforeExecute(arg0, arg1);
        System.out.println("beforeExecute");
    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        super.afterExecute(r, t);
        System.out.println("afterExecute");
    }
    
    public static void test01() {
        ExecutorService threadPool1 = Executors.newFixedThreadPool(3);
        test(threadPool1);
    }
    public static void test02() {
        ExecutorService threadPool2 = Executors.newCachedThreadPool();
        test(threadPool2);
    }
    public static void test03() {
        ExecutorService threadPool3 = Executors.newSingleThreadExecutor();
        test(threadPool3);
    }
    public static void test04() {
        //ExecutorService threadPool4 = Executors.newSingleThreadExecutor();
        ScheduledExecutorService threadPool = Executors.newScheduledThreadPool(3);
        threadPool.schedule(new Runnable() {
            @Override
            public void run() {
                System.out.println("加油....");
                
            }
        }, 5, TimeUnit.SECONDS);
        threadPool.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                System.out.println("加油2...." + System.currentTimeMillis()/1000);
                try {
                    Thread.sleep(3000);
                } catch (Exception e) {
                }

                
            }
        }, 5, 2, TimeUnit.SECONDS);
    }
    public static void test(ExecutorService threadPool) {
        for (int i = 1; i < 5; i++) {
            final int taskID = i;
            threadPool.execute(new Runnable() {
                
                @Override
                public void run() {
                    for (int i = 1; i < 5; i++) {
                        try {
                            Thread.sleep(20);
                        } catch (Exception e) {
                        }
                        System.out.println("第" + taskID + "次任务的第" + i + "次执行");
                    }
                }
            });
        }
        threadPool.shutdown();
    }
    public static void main(String[] args) {
        //test01();
        //test02();
        //test03();
        test04();
    }

}
