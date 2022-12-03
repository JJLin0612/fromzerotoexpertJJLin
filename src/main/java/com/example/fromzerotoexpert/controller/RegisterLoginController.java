package com.example.fromzerotoexpert.controller;

import com.example.fromzerotoexpert.service.UserService;
import com.example.fromzerotoexpert.utils.RSAUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.security.interfaces.RSAPublicKey;
import java.util.List;

/**
 * @author JJLin
 * @date 2022/11/29
 */
@RestController
@RequestMapping("/Register")
public class RegisterLoginController {

    @Resource
    private UserService userService;

    /**
     * 执行用户注册的 API
     * @param mobile 手机号码
     * @param pwd 密码
     * @param verifyCode 验证码
     * @return 重定向至 登录页面
     */
    @PostMapping
    public String doRegister(
            @RequestParam(value = "mobile", defaultValue = "") String mobile,
            @RequestParam(value = "pwd", defaultValue = "") String pwd,
            @RequestParam(value = "verifyCode", defaultValue = "") String verifyCode
    ) {
        userService.userRegister(mobile, pwd, verifyCode);
        //待处理 成功则重定向到登录页 否则抛出异常重新登录
        return "redirect:/Login";
    }

    /**
     * 模拟前端加密用户的密码
     * @param pwd 待加密的密码
     * @return 加密后的密码
     */
    @GetMapping("rsa")
    public String rsa(@RequestParam(value = "pwd") String pwd) {

        //生成公钥私钥
        List<String> rsaKeyList = RSAUtils.getKeyStrList();
        //获取公钥 模拟传输公钥给前端
        String pubKey = rsaKeyList.get(0);
        //模拟前端加密密码
        String encryptData = "";
        try {
            RSAPublicKey publicKeyObj = RSAUtils.getPublicKey(pubKey);
            encryptData = RSAUtils.encryptByPublicKey(pwd, publicKeyObj);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return encryptData;
    }
}
