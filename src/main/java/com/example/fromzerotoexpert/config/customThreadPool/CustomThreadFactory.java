package com.example.fromzerotoexpert.config.customThreadPool;

import java.util.concurrent.ThreadFactory;

/**
 * @author RabbitFaFa
 */
public class CustomThreadFactory implements ThreadFactory {

    private String threadName;

    public CustomThreadFactory() {}

    public CustomThreadFactory(String threadName) {
        this.threadName = threadName;
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread thread = new Thread(r);
        thread.setName(threadName);
        return thread;
    }
}
