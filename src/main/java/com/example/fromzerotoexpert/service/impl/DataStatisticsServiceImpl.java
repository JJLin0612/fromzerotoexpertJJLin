package com.example.fromzerotoexpert.service.impl;

import com.example.fromzerotoexpert.service.DataStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author RabbitFaFa
 */
@Service
public class DataStatisticsServiceImpl implements DataStatisticsService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /***
     * 更新/新增 在线用户记录
     * @param id 用户id
     */
    @Override
    public void updateOnlineUsers(String id) {
        //获取系统当前毫秒时间
        long timeMillis = System.currentTimeMillis();
        //更新Redis中 member = id 的记录 或 添加记录
        redisTemplate.opsForZSet().add("user:online", id, timeMillis);
    }

    /***
     * 获取当前在线用户数量
     * @return 在线用户数
     */
    @Override
    public long onlineUsers() {
        return redisTemplate.opsForZSet().zCard("user:online");
    }

}
