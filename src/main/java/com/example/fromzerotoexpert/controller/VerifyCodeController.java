package com.example.fromzerotoexpert.controller;

import com.example.fromzerotoexpert.annotation.LogRecord;
import com.example.fromzerotoexpert.entity.dto.OperateType;
import com.example.fromzerotoexpert.entity.dto.Result;
import com.example.fromzerotoexpert.utils.RandomNum;
import com.example.fromzerotoexpert.utils.SendMsm;
import com.sun.istack.NotNull;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    @ApiOperation("根据手机号码生成并发送验证码")
    @GetMapping
    @LogRecord(operateType = OperateType.OTHER, operateDesc = "发送验证码")
    public Result generateCode(
            @RequestParam(value = "mobile", defaultValue = "")
            @NotNull
            @ApiParam("手机号码") String mobile
    ) {
        //检查redis中有未过期的验证码则删除
        String oldCode = redisTemplate.opsForValue().get(mobile);
        if (!StringUtils.isEmpty(oldCode)) redisTemplate.delete(mobile);
        //生成随机验证码
        String code = RandomNum.getSixBitRandom();
        //调用阿里云短信服务 API 给用户发送验证码
        SendMsm.sendMessageCode(mobile, code);
        //验证码存入Redis缓存 设置验证码有效期 5 min
        redisTemplate.opsForValue().set(mobile, code, 5, TimeUnit.MINUTES);

        return Result.ok();
    }

}
