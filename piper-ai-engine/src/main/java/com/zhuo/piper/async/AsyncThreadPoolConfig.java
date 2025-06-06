package com.zhuo.piper.async;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
@Slf4j
public class AsyncThreadPoolConfig {
    /**
     * CPU 核数
     */
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    /**
     * IO 处理线程数
     */
    private static final int IO_MAX = Math.max(2, 2 * CPU_COUNT);
    /**
     * 空闲线程最大保活时限，单位为秒
     */
    private static final int KEEP_ALIVE_SECOND = 60;
    /**
     * 有界阻塞队列容量上限
     */
    private static final int QUEUE_SIZE = 10000;

    @Bean("AsyncExecutor")
    public Executor asyncThreadPool() {
        log.info("CPU_COUNT: {}", CPU_COUNT);
        ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(
                IO_MAX,
                IO_MAX * 2,
                KEEP_ALIVE_SECOND,
                TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(QUEUE_SIZE),
                new ThreadPoolExecutor.CallerRunsPolicy()
        );
        return poolExecutor;
    }
}