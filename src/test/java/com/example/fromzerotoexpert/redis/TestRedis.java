package com.example.fromzerotoexpert.redis;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @author RabbitFaFa
 * @date 2022/12/3
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestRedis {

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Test
    public void test() {
        redisTemplate.opsForValue().set("name", "RabbitFaFa");
        System.out.println(redisTemplate.opsForValue().get("name"));
    }
}
