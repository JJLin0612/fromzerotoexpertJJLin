package com.example.fromzerotoexpert.controller;

import com.sun.xml.internal.bind.v2.TODO;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 验证码Controller
 *
 * @author RabbitFaFa
 * @date 2022/12/3
 */
@RestController
@RequestMapping("/VerifyCode")
public class VerifyCodeController {

    //TODO 生成验证码API
    public String generateCode() {
        //生成验证码

        //验证码存入Redis缓存 设置验证码有效期60s

        return "";
    }
}
