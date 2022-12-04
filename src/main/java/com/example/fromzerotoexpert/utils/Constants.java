package com.example.fromzerotoexpert.utils;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author RabbitFaFa
 * @date 2022/12/4
 */
@Component
public class Constants implements InitializingBean {
    @Value("${aliyun.RAM.keyid}")
    private String keyId;

    @Value("${aliyun.RAM.keysecret}")
    private String keysecret;

    @Value("${aliyun.endpoint}")
    private String endpoint;

    @Value("${aliyun.signname}")
    private String signName;

    @Value("${aliyun.templatecode}")
    private String templateCode;

    @Value("${aliyun.phonenumbers}")
    private String phoneNumbers;

    //公开静态常量
    public static String ACCESS_KEY_ID;
    public static String ACCESS_KEY_SECRET;
    public static String MSM_endPoint;
    public static String MSM_signName;
    public static String MSM_templateCode;
    public static String MSM_phoneNumbers;

    @Override
    public void afterPropertiesSet() throws Exception{
        ACCESS_KEY_ID = keyId;
        ACCESS_KEY_SECRET = keysecret;
        MSM_endPoint = endpoint;
        MSM_signName = signName;
        MSM_templateCode = templateCode;
        MSM_phoneNumbers = phoneNumbers;
    }
}
