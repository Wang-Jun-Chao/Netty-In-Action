package com.netty;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 由于线程池和消息队列都是有界的，因此， 无论客户端并发连接数多大，它都不会导
 * 致线程个数过于膨胀或者内存溢出，相比于传统的一连接一线程模型，是一种改良
 * Author: 王俊超
 * Date: 2017-09-03 09:21
 * All Rights Reserved !!!
 */
public class TimeServerHandlerExecutePool {
    private ExecutorService executorService;

    public TimeServerHandlerExecutePool(int maxPoolSize, int queueSize) {
        executorService = new ThreadPoolExecutor(
                Runtime.getRuntime().availableProcessors(), // 线程池数设置为处理器数目
                maxPoolSize,
                120L,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<Runnable>(queueSize));
    }

    public void execute(java.lang.Runnable task) {
        executorService.execute(task);
    }
}
