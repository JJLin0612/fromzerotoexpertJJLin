package com.example.fromzerotoexpert.utils;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.tea.*;
import com.aliyun.teaopenapi.models.Config;
import com.aliyun.teautil.Common;

import java.util.HashMap;
import java.util.Map;

/**
 * 调用阿里云SMS短信服务接口
 * @author RabbitFaFa
 * @date 2022/12/4
 */
public class SendMsm {

    /**
     * 使用AK&SK初始化账号Client
     *
     * @param accessKeyId 阿里云accessKeyId
     * @param accessKeySecret 阿里云accessKeySecret
     * @return Client 发送请求的客户端
     * @throws Exception 抛出异常
     */
    private static Client createClient(String accessKeyId, String accessKeySecret) throws Exception {
        Config config = new Config()
                // 必填，您的 AccessKey ID
                .setAccessKeyId(accessKeyId)
                // 必填，您的 AccessKey Secret
                .setAccessKeySecret(accessKeySecret);
        // 访问的域名
        config.endpoint = Constants.MSM_endPoint;
        return new Client(config);
    }

    /**
     * 调用阿里云短信服务接口发送Code
     * @param code 需要发送的验证码
     */
    public static void sendMessageCode(String mobile, String code) {

        Map<String, String> map = new HashMap<>(1);
        map.put("code", code);
        try {
            Client client = createClient(Constants.ACCESS_KEY_ID, Constants.ACCESS_KEY_SECRET);
            SendSmsRequest sendSmsRequest = new SendSmsRequest()
                    .setSignName(Constants.MSM_signName)
                    .setTemplateCode(Constants.MSM_templateCode)
                    .setPhoneNumbers(mobile)
                    .setTemplateParam(JSONObject.toJSONString(map));
            //发送验证码 复制代码运行请自行打印 API 的返回值
            client.sendSms(sendSmsRequest);
        } catch (TeaException error) {
            // 如有需要，请打印 error
            Common.assertAsString(error.message);
        } catch (Exception _error) {
            TeaException error = new TeaException(_error.getMessage(), _error);
            // 如有需要，请打印 error
            Common.assertAsString(error.message);
        }
    }
}
