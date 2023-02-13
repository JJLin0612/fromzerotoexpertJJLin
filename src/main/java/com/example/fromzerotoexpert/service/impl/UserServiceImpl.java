package com.example.fromzerotoexpert.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.fromzerotoexpert.dto.ResultCode;
import com.example.fromzerotoexpert.entity.User;
import com.example.fromzerotoexpert.exception.CustomException;
import com.example.fromzerotoexpert.mapper.UserMapper;
import com.example.fromzerotoexpert.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.fromzerotoexpert.utils.ClientInfo;
import com.example.fromzerotoexpert.utils.JwtUtils;
import com.example.fromzerotoexpert.utils.MD5;
import com.example.fromzerotoexpert.utils.RSAUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.security.interfaces.RSAPrivateKey;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author RabbitFaFa
 * @since 2022-12-01
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    /***
     * 用户注册具体业务实现逻辑
     * @param mobile 未解密的手机号码
     * @param pwd 未解密的登录密码
     * @param verifyCode 验证码
     * @return 插入成功的用户的数量 0(插入失败)、1(插入成功)
     */
    @Override
    public int userRegister(String mobile, String pwd, String verifyCode) {
        //解密手机号
        mobile = decryptData(mobile);
        //从Redis中获取验证码并验证
        String code = redisTemplate.opsForValue().get(mobile);
        if (!StringUtils.isEmpty(code) && !code.equals(verifyCode))
            throw new CustomException(ResultCode.INCORRECT_VERIFY_CODE_ERROR, "incorrect verifCode");
        //缓存中删除验证码
        redisTemplate.delete(mobile);

        //解密密码
        pwd = decryptData(pwd);

        //根据手机号码查询是否存在该手机号 且 该用户是否被删除
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("mobile", mobile);
        Integer countByPhone = baseMapper.selectCount(wrapper);
        wrapper.eq("is_deleted", ResultCode.USER_IS_NOT_DELETED);
        Integer countByPhoAndIsDele = baseMapper.selectCount(wrapper);
        //手机号已存在(存在且未被删除 或 存在但删除字段为 1)
        if (countByPhone == 1) {
            if (countByPhoAndIsDele == 1) {
                //该手机号用户存在且未被删除
                log.error("号码已注册");
                throw new CustomException(ResultCode.USER_HAS_EXITS_ERROR, "user has registered");
            }

            else {
                //改手机号用户存在但已为删除状态 将记录删除
                wrapper = new QueryWrapper<>();
                wrapper.eq("mobile", mobile);
                baseMapper.delete(wrapper);
            }
        }

        //生成账号acc
        Calendar calendar = Calendar.getInstance();
        String newAccNumber = "d" + calendar.get(Calendar.DAY_OF_YEAR) + "_" + Long.toHexString(System.currentTimeMillis());

        //MD5加密密码 新增user
        User user = new User();
        user.setAcc(newAccNumber);
        user.setMobile(mobile);
        user.setPwd(MD5.encrypt(pwd));
        return baseMapper.insert(user);
    }

    /**
     * 用户登录业务具体实现逻辑
     * @param mobile 未解密的手机号
     * @param pwd 未解密的登录密码
     * @param request HTTP 请求的实例对象
     * @return token
     */
    @Override
    public String userLogin(String mobile, String pwd, HttpServletRequest request) {
        //解密
        log.warn(mobile);
        mobile = decryptData(mobile);
        pwd = decryptData(pwd);

        // 用户未注册则抛异常
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("mobile", mobile);
        Integer count = baseMapper.selectCount(wrapper);
        if (count == 0)
            throw new CustomException(ResultCode.USER_UNREGISTERED_ERROR, "unregistered");

        //根据手机号获取user
        User user = baseMapper.selectOne(wrapper);

        // 比较手机号 密码是否正确
        wrapper.eq("pwd", MD5.encrypt(pwd));
        Integer res = baseMapper.selectCount(wrapper);
        if (res == 0)
            throw new CustomException(ResultCode.INCORRECT_PHONE_OR_PWD_ERROR, "incorrect phone or pwd");

        //用户被禁用
        if (user.getIsDisabled())
            throw new CustomException(ResultCode.USER_IS_DISABLED_ERROR, "user has been disabled");

        //获取用户id
        String id = user.getId();
        //redis缓存(旧设备)中是否有 该 userId
        String oldToken = redisTemplate.opsForValue().get(id);
        //账号在新设备登录 旧设备下线
        if (!StringUtils.isEmpty(oldToken)) redisTemplate.delete(id);
        //当前请求的设备(新设备)的信息
        String deviceInfo = ClientInfo.getDeviceInfo(request);
        //生成token
        String newtoken = JwtUtils.getJwtToken(id, deviceInfo);
        //将token缓存
        redisTemplate.opsForValue().set(id, newtoken, 24 * 7, TimeUnit.HOURS);

        return newtoken;
    }

    /***
     * 用户登出实现逻辑
     * @param id 用户id
     * @return 操作结果数
     */
    @Override
    public int userLogout(String id) {
        int res = 0;
        if (!StringUtils.isEmpty(id)) {
            redisTemplate.delete(id);
            res = 1;
        }
        return res;
    }

    /***
     * RSA 解密
     * @param encryptedData 待解密的内容
     * @return 已解密的内容
     */
    private static String decryptData(String encryptedData) {
        //获取私钥
        List<Key> keys = RSAUtils.getKeyObjList();
        RSAPrivateKey privateKey = (RSAPrivateKey) keys.get(1);

        //解密 encryptedData
        String dataDecrypted = "";
        try {
            dataDecrypted = RSAUtils.decryptByPrivateKey(encryptedData, privateKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataDecrypted;
    }
}
