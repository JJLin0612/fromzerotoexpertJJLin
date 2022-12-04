package com.example.fromzerotoexpert.aliyunMsm;

import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.tea.*;
import com.aliyun.teaopenapi.models.Config;
import com.aliyun.teautil.Common;
import com.aliyun.teautil.models.RuntimeOptions;
import com.example.fromzerotoexpert.utils.Constants;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


/**
 * @author RabbitFaFa
 * @date 2022/12/3
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class MessageTest {

    /**
     * 使用AK&SK初始化账号Client
     *
     * @param accessKeyId
     * @param accessKeySecret
     * @return Client
     * @throws Exception
     */
    public static Client createClient(String accessKeyId, String accessKeySecret) throws Exception {
        Config config = new Config()
                // 必填，您的 AccessKey ID
                .setAccessKeyId(accessKeyId)
                // 必填，您的 AccessKey Secret
                .setAccessKeySecret(accessKeySecret);
        // 访问的域名
        config.endpoint = "dysmsapi.aliyuncs.com";
        return new Client(config);
    }

    @Test
    public void test() throws Exception {
        Client client = MessageTest.createClient(Constants.ACCESS_KEY_ID,Constants.ACCESS_KEY_SECRET);
        SendSmsRequest sendSmsRequest = new SendSmsRequest()
                .setSignName("在线个人项目")
                .setTemplateCode("SMS_262435388")
                .setPhoneNumbers("13682718873")
                .setTemplateParam("{\"code\":\"1234\"}");
        RuntimeOptions runtime = new RuntimeOptions();
        try {
            // 复制代码运行请自行打印 API 的返回值
            SendSmsResponse smsResponse = client.sendSms(sendSmsRequest);
            System.out.println(smsResponse.getBody());
            SendSmsResponse response = client.sendSmsWithOptions(sendSmsRequest, runtime);
            System.out.println("======" + response.getBody());
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
