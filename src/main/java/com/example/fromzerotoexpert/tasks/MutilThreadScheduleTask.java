package com.example.fromzerotoexpert.tasks;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


/**
 * 多线程定时任务
 * @author RabbitFaFa
 */
@EnableScheduling
@Component
@EnableAsync
public class MutilThreadScheduleTask {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /***
     * 定时任务: 清理离线的用户
     */
    @Async
    @Scheduled(cron = "0 */2 * * * ?")
    public void cleanOfflineUsersCache() {
        // 2 分钟前的毫秒值
        long time = System.currentTimeMillis() - 1000 * 60 * 2;
        //清理超时2min未操作用户 即为离线状态
        redisTemplate.opsForZSet().removeRangeByScore("user:online", 0, time);
    }

    @Async
    @Scheduled(cron = "59 59 23 * * ?")
    public void cleanIpUvPvData() {
        redisTemplate.opsForHyperLogLog().delete("website:ip");
        redisTemplate.opsForHyperLogLog().delete("website:uv");
        redisTemplate.delete("website:pv");
    }

}
