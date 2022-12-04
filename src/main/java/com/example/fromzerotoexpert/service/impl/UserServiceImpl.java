package com.example.fromzerotoexpert.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.fromzerotoexpert.entity.User;
import com.example.fromzerotoexpert.mapper.UserMapper;
import com.example.fromzerotoexpert.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.fromzerotoexpert.utils.MD5;
import com.example.fromzerotoexpert.utils.RSAUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.util.Calendar;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author RabbitFaFa
 * @since 2022-12-01
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public int userRegister(String mobile, String pwd, String verifyCode) {
        //从Redis中获取验证码
        String code = redisTemplate.opsForValue().get("verifyCode");
        if (code.isEmpty() || !code.equals(verifyCode)) {
            //TODO 抛出验证码错误异常
        }

        //获取服务器的私钥
        List<Key> keys = RSAUtils.getKeyObjList();
        RSAPrivateKey privateKey = (RSAPrivateKey) keys.get(1);
        String mobileDecrypt = "";
        String pwdDecrypt = "";
        //私钥解密
        try {
            mobileDecrypt = RSAUtils.decryptByPrivateKey(mobile, privateKey);
            pwdDecrypt = RSAUtils.decryptByPrivateKey(pwd, privateKey);
        }catch (Exception e) {
            e.printStackTrace();
        }
        //根据手机号码查询是否存在该手机号 且 该用户是否被删除
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("mobile", mobileDecrypt)
                .eq("is_deleted", 0);
        Integer count = baseMapper.selectCount(wrapper);
        if (count != 0) return 0;//TODO 抛出手机号已注册异常

        //生成acc
        Calendar calendar = Calendar.getInstance();
        String newAccNumber = "d" + calendar.get(Calendar.DAY_OF_YEAR) + "_" + Long.toHexString(System.currentTimeMillis());
        //MD5加密密码 新增user
        User user = new User();
        user.setAcc(newAccNumber);
        user.setMobile(mobileDecrypt);
        user.setPwd(MD5.encrypt(pwdDecrypt));
        int insert = baseMapper.insert(user);

        return insert;
    }
}
