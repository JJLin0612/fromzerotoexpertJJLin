package com.example.fromzerotoexpert.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.fromzerotoexpert.annotation.LogRecord;
import com.example.fromzerotoexpert.entity.User;
import com.example.fromzerotoexpert.entity.dto.OperateType;
import com.example.fromzerotoexpert.entity.dto.Result;
import com.example.fromzerotoexpert.entity.vo.UserQuery;
import com.example.fromzerotoexpert.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 前端控制器
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


    @GetMapping
    @LogRecord(operateType = OperateType.READ, operateDesc = "多条件组合查询用户信息")
    public Result userList(@RequestBody UserQuery query) {
        List<User> list = userService.getUsersByQuery(query);
        return Result.ok().data("userList", list);
    }

    @PostMapping
    @LogRecord(operateType = OperateType.MODIFY,
            operateDesc = "更新用户信息",
            contentId = "{{#user.id}}")
    public Result modifyUser(@RequestBody User user) {
        userService.updateById(user);
        return Result.ok();
    }

}

