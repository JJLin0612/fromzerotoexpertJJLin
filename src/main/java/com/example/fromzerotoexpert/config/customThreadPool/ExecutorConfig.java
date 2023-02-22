package com.example.fromzerotoexpert.config.customThreadPool;

import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author RabbitFaFa
 */
@Data
@Configuration
public class ExecutorConfig {

    //核心线程数
    private static int corePoolSize = 10;
    //最大线程数
    private static int maximumPoolSize = 10;
    //存活时间
    private static long keepAliveTime = 5;
    //单位
    private static final TimeUnit unit =TimeUnit.MINUTES;
    //队列长度
    private static final int queueSize = 10;

    //获取自定义线程池
    @Bean("LogThreadPool")
    public ThreadPoolExecutor buildThreadPoolExcutor() {
        return new ThreadPoolExecutor(corePoolSize,
                maximumPoolSize,
                keepAliveTime,
                unit,
                new ResizableLinkedBlockingQueue<>(10),
                new CustomThreadFactory("LogThreadPool"));
    }
}
