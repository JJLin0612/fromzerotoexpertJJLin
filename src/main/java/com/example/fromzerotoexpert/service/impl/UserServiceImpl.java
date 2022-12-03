package com.example.fromzerotoexpert.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.fromzerotoexpert.entity.User;
import com.example.fromzerotoexpert.mapper.UserMapper;
import com.example.fromzerotoexpert.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.fromzerotoexpert.utils.MD5;
import com.example.fromzerotoexpert.utils.RSAUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Override
    public int userRegister(String mobile, String pwd, String verifyCode) {
        //TODO 验证验证码

        //获取服务器的私钥
        List<Key> keys = RSAUtils.getKeyObjList();
        RSAPrivateKey privateKey = (RSAPrivateKey) keys.get(1);
        String pwdDecrypt = "";
        //私钥解密
        try {
            pwdDecrypt = RSAUtils.decryptByPrivateKey(pwd, privateKey);
        }catch (Exception e) {
            e.printStackTrace();
        }
        //根据手机号码查询是否存在该手机号 且 该用户是否被删除
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("mobile", mobile)
                .eq("is_deleted", 0);
        Integer count = baseMapper.selectCount(wrapper);
        if (count != 0) return 0;//TODO 此处待抛出异常

        //生成acc
        Calendar calendar = Calendar.getInstance();
        String newAccNumber = "d" + calendar.get(Calendar.DAY_OF_YEAR) + "_" + Long.toHexString(System.currentTimeMillis());
        //MD5加密密码 新增user
        User user = new User();
        user.setAcc(newAccNumber);
        user.setPwd(MD5.encrypt(pwdDecrypt));
        int insert = baseMapper.insert(user);

        return insert;
    }
}
