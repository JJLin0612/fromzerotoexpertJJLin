package com.example.fromzerotoexpert.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.fromzerotoexpert.entity.User;
import com.example.fromzerotoexpert.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author RabbitFaFa
 * @since 2022-12-01
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 根据phoneNumbers查询user
     * @param mobile 电话号码
     * @return 查询到的user
     */
    @GetMapping("/{mobile}")
    public String userList(@PathVariable("mobile") String mobile) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("mobile", mobile);
        userService.getOne(wrapper);
        return "";
    }

}

