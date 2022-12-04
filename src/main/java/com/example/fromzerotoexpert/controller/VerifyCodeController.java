package com.example.fromzerotoexpert.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.fromzerotoexpert.utils.RandomNum;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.tea.*;
import com.aliyun.teaopenapi.models.Config;
import com.aliyun.teautil.Common;
import com.aliyun.teautil.models.RuntimeOptions;
import com.example.fromzerotoexpert.utils.Constants;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
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

    @GetMapping
    public String generateCode() {
        //生成随机验证码
        String code = RandomNum.getSixBitRandom();
        Map<String, String> map = new HashMap<>(1);
        map.put("code", code);
        try {
            Client client = createClient(Constants.ACCESS_KEY_ID, Constants.ACCESS_KEY_SECRET);
            SendSmsRequest sendSmsRequest = new SendSmsRequest()
                    .setSignName("在线个人项目")
                    .setTemplateCode("SMS_262435388")
                    .setPhoneNumbers("13682718873")
                    .setTemplateParam(JSONObject.toJSONString(map));
            RuntimeOptions runtime = new RuntimeOptions();

            // 复制代码运行请自行打印 API 的返回值
            client.sendSms(sendSmsRequest);
        } catch (TeaException error) {
            // 如有需要，请打印 error
            Common.assertAsString(error.message);
        } catch (Exception _error) {
            TeaException error = new TeaException(_error.getMessage(), _error);
            // 如有需要，请打印 error
            Common.assertAsString(error.message);
        }

        //验证码存入Redis缓存 设置验证码有效期 5min
        redisTemplate.opsForValue().set("verifyCode", code, 5, TimeUnit.MINUTES);
        //TODO 统一返回结果
        return "";
    }

    /**
     * 使用AK&SK初始化账号Client
     *
     * @param accessKeyId
     * @param accessKeySecret
     * @return Client
     * @throws Exception
     */
    private Client createClient(String accessKeyId, String accessKeySecret) throws Exception {
        Config config = new Config()
                // 必填，您的 AccessKey ID
                .setAccessKeyId(accessKeyId)
                // 必填，您的 AccessKey Secret
                .setAccessKeySecret(accessKeySecret);
        // 访问的域名
        config.endpoint = "dysmsapi.aliyuncs.com";
        return new Client(config);
    }
}
