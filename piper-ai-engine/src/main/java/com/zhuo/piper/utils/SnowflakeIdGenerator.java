package com.zhuo.piper.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * 雪花算法ID生成器
 * 优化版本：支持时钟回拨处理
 */
@Slf4j
public class SnowflakeIdGenerator {

    private static volatile SnowflakeIdGenerator instance;
    // 开始时间截 (2023-01-01)
    private final long twepoch = 1672531200000L;
    // 机器id所占的位数
    private final long workerIdBits = 5L;
    // 数据标识id所占的位数
    private final long datacenterIdBits = 5L;
    // 支持的最大机器id，结果是31
    private final long maxWorkerId = -1L ^ (-1L << workerIdBits);
    // 支持的最大数据标识id，结果是31
    private final long maxDatacenterId = -1L ^ (-1L << datacenterIdBits);
    // 序列在id中占的位数
    private final long sequenceBits = 12L;
    // 机器ID向左移12位
    private final long workerIdShift = sequenceBits;
    // 数据标识id向左移17位(12+5)
    private final long datacenterIdShift = sequenceBits + workerIdBits;
    // 时间截向左移22位(5+5+12)
    private final long timestampLeftShift = sequenceBits + workerIdBits + datacenterIdBits;
    // 生成序列的掩码，这里为4095 (0b111111111111=0xfff=4095)
    private final long sequenceMask = -1L ^ (-1L << sequenceBits);
    // 工作机器ID(0~31)
    private long workerId;
    // 数据中心ID(0~31)
    private long datacenterId;
    // 毫秒内序列(0~4095)
    private long sequence = 0L;
    // 上次生成ID的时间截
    private long lastTimestamp = -1L;
    // 时钟回拨容忍度（毫秒）
    private final long maxBackwardMillis = 1000L;
    // 时钟回拨等待时间（毫秒）
    private final long waitMillis = 1L;

    /**
     * 构造函数
     *
     * @param workerId     工作ID (0~31)
     * @param datacenterId 数据中心ID (0~31)
     */
    public SnowflakeIdGenerator(long workerId, long datacenterId) {
        if (workerId > maxWorkerId || workerId < 0) {
            throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0", maxWorkerId));
        }
        if (datacenterId > maxDatacenterId || datacenterId < 0) {
            throw new IllegalArgumentException(String.format("datacenter Id can't be greater than %d or less than 0", maxDatacenterId));
        }
        this.workerId = workerId;
        this.datacenterId = datacenterId;
    }

    /**
     * 获取单例
     */
    public static SnowflakeIdGenerator getInstance() {
        if (instance == null) {
            synchronized (SnowflakeIdGenerator.class) {
                if (instance == null) {
                    // 默认使用0作为机器ID和数据中心ID
                    instance = new SnowflakeIdGenerator(0, 0);
                }
            }
        }
        return instance;
    }

    /**
     * 获得下一个ID (该方法是线程安全的)
     *
     * @return SnowflakeId
     */
    public synchronized long nextId() {
        long timestamp = timeGen();

        // 如果当前时间小于上一次ID生成的时间戳，说明系统时钟回退过
        if (timestamp < lastTimestamp) {
            long offset = lastTimestamp - timestamp;
            // 如果时钟回拨在容忍范围内，则等待
            if (offset <= maxBackwardMillis) {
                try {
                    // 等待时钟追上
                    TimeUnit.MILLISECONDS.sleep(waitMillis);
                    timestamp = timeGen();
                    // 如果还是小于，则继续等待
                    while (timestamp < lastTimestamp) {
                        TimeUnit.MILLISECONDS.sleep(waitMillis);
                        timestamp = timeGen();
                    }
                } catch (InterruptedException e) {
                    log.error("Clock moved backwards. Waiting interrupted", e);
                    throw new RuntimeException("Clock moved backwards. Waiting interrupted", e);
                }
            } else {
                // 如果时钟回拨超过容忍范围，则抛出异常
                log.error("Clock moved backwards. Refusing to generate id for {} milliseconds", offset);
                throw new RuntimeException(String.format("Clock moved backwards. Refusing to generate id for %d milliseconds", offset));
            }
        }

        // 如果是同一时间生成的，则进行毫秒内序列
        if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) & sequenceMask;
            // 毫秒内序列溢出
            if (sequence == 0) {
                // 阻塞到下一个毫秒,获得新的时间戳
                timestamp = tilNextMillis(lastTimestamp);
            }
        }
        // 时间戳改变，毫秒内序列重置
        else {
            sequence = 0L;
        }

        // 上次生成ID的时间截
        lastTimestamp = timestamp;

        // 移位并通过或运算拼到一起组成64位的ID
        return ((timestamp - twepoch) << timestampLeftShift)
                | (datacenterId << datacenterIdShift)
                | (workerId << workerIdShift)
                | sequence;
    }

    /**
     * 获取下一个ID字符串形式
     *
     * @return ID字符串
     */
    public String nextIdStr() {
        return String.valueOf(nextId());
    }

    /**
     * 阻塞到下一个毫秒，直到获得新的时间戳
     *
     * @param lastTimestamp 上次生成ID的时间截
     * @return 当前时间戳
     */
    protected long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    /**
     * 返回以毫秒为单位的当前时间
     *
     * @return 当前时间(毫秒)
     */
    protected long timeGen() {
        return System.currentTimeMillis();
    }
}