package com.example.fromzerotoexpert.controller;

import com.example.fromzerotoexpert.utils.RandomNum;
import com.example.fromzerotoexpert.utils.SendMsm;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * 验证码Controller
 *
 * @author RabbitFaFa
 * @date 2022/12/3
 */
@RestController
@RequestMapping("/VerifyCode")
public class VerifyCodeController {

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 生成 发送验证码至用户手机号码
     * 验证码存入Redis 设置有效期
     * @return 统一返沪结果
     */
    @GetMapping
    public String generateCode() {
        //生成随机验证码
        String code = RandomNum.getSixBitRandom();
        //调用阿里云短信服务 API 给用户发送验证码
        SendMsm.sendMessageCode(code);
        //验证码存入Redis缓存 设置验证码有效期 5min
        redisTemplate.opsForValue().set("verifyCode", code, 5, TimeUnit.MINUTES);
        //TODO 统一返回结果
        return "";
    }

}
