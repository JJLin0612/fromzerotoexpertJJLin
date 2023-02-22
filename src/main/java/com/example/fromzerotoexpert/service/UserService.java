package com.example.fromzerotoexpert.service;

import com.example.fromzerotoexpert.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.fromzerotoexpert.entity.vo.UserQuery;

import javax.servlet.http.HttpServletRequest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author RabbitFaFa
 * @since 2022-12-01
 */
public interface UserService extends IService<User> {

    String userRegister(String mobile, String pwd, String verifyCode);

    String userLogin(String mobile, String pwd, HttpServletRequest request);

    int userLogout(String id);

    List<User> getUsersByQuery(UserQuery query);
}
