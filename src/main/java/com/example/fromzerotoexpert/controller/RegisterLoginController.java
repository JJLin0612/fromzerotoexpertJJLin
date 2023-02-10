package com.example.fromzerotoexpert.controller;

import com.example.fromzerotoexpert.dto.Result;
import com.example.fromzerotoexpert.service.UserService;
import com.example.fromzerotoexpert.utils.RSAUtils;
import com.sun.istack.internal.NotNull;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.security.interfaces.RSAPublicKey;
import java.util.List;

/**
 * @author JJLin
 * @date 2022/11/29
 */
@CrossOrigin
@RestController
@RequestMapping("/RegisterAndLogin")
public class RegisterLoginController {

    @Resource
    private UserService userService;

    /**
     * 执行用户注册的 API
     *
     * @param mobile     手机号码
     * @param pwd        密码
     * @param verifyCode 验证码
     * @return 重定向至 登录页面
     */
    @ApiOperation("用户注册")
    @PostMapping("Regist")
    public Result doRegister(
            @RequestParam(value = "mobile", defaultValue = "") @NotNull @ApiParam("手机号码")
                    String mobile,
            @RequestParam(value = "pwd", defaultValue = "") @NotNull @ApiParam("登录密码")
                    String pwd,
            @RequestParam(value = "verifyCode", defaultValue = "") @NotNull @ApiParam("手机验证码")
                    String verifyCode
    ) {
        int res = userService.userRegister(mobile, pwd, verifyCode);
        return res == 1 ? Result.ok() : Result.error();
    }

    /***
     * 处理用户登录逻辑
     * @param mobile 加密了的用户电话号码
     * @param pwd 加密了的密码
     * @return token
     */
    @ApiOperation("用户登录")
    @PostMapping("Login")
    public Result doLogin(
            @RequestParam(value = "mobile", defaultValue = "") @NotNull @ApiParam("手机号码")
                    String mobile,
            @RequestParam(value = "pwd", defaultValue = "") @NotNull @ApiParam("登录密码")
                    String pwd,
            HttpServletRequest request
    ) {
        String token = userService.userLogin(mobile, pwd, request);
        return Result.ok().data("token", token);
    }

    /***
     * 用户登出API
     * @param id 用户id
     * @return 操作结果
     */
    @ApiOperation("用户退出登录")
    @GetMapping("logout")
    public Result doLogout(
            @RequestParam(value = "id", defaultValue = "")
            @NotNull
            @ApiParam("用户id") String id
    ) {
        int res = userService.userLogout(id);
        return res == 1 ? Result.ok() : Result.error();
    }

    /**
     * 模拟前端加密用户的数据
     *
     * @param data 待加密的数据
     * @return 加密后的密码
     */
    @GetMapping("RSA-Encryption")
    public String rsa(@RequestParam(value = "data") String data) {

        //生成公钥私钥
        List<String> rsaKeyList = RSAUtils.getKeyStrList();
        //获取公钥 模拟传输公钥给前端
        String pubKey = rsaKeyList.get(0);
        //模拟前端加密密码
        String encryptData = "";
        try {
            RSAPublicKey publicKeyObj = RSAUtils.getPublicKey(pubKey);
            encryptData = RSAUtils.encryptByPublicKey(data, publicKeyObj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encryptData;
    }
}
