package com.example.fromzerotoexpert.RSA;

import com.example.fromzerotoexpert.utils.MD5;
import com.example.fromzerotoexpert.utils.RSAUtils;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.List;

/**
 * @author RabbitFaFa
 * @date 2022/12/2
 */
public class RSATest {
    @Test
    public void testRsa() throws Exception {
        int modules = 512;
        String pwd = "1234";
        new RSAUtils();
        List<String> rsaKeyList = RSAUtils.getKeyStrList();
        //获取公钥 模拟传输公钥给前端
        String pubKey = rsaKeyList.get(0);
        //模拟前端加密密码
        RSAPublicKey publicKeyObj = RSAUtils.getPublicKey(pubKey);
        String encryptData = RSAUtils.encryptByPublicKey(pwd, publicKeyObj);
        System.out.println("加密的密码: " + encryptData);
        //模拟后端系统收到加密的密码后用私钥解密
        String priKey = rsaKeyList.get(1);
        String data = RSAUtils.decryptByPrivateKey(encryptData, RSAUtils.getPrivateKey(priKey));
        System.out.println("解密的密码: " + data);
    }
}
