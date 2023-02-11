package com.example.fromzerotoexpert.controller;

import com.example.fromzerotoexpert.dto.Result;
import com.example.fromzerotoexpert.service.DataStatisticsService;
import com.sun.istack.NotNull;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author RabbitFaFa
 */
@RestController
@RequestMapping("/dataStatistics")
public class DataStatisticsController {

    @Resource
    private DataStatisticsService service;

    /***
     * 处理前端发送的心跳
     * @param id 用户id
     * @return 无
     */
    @ApiOperation("前端心跳处理")
    @GetMapping("heartbeat")
    public Result handleHeartbeat(
            @RequestParam(value = "id", defaultValue = "")
            @NotNull
            @ApiParam("用户id") String id
    ) {
        service.updateOnlineUsers(id);
        return Result.ok();
    }

    @ApiOperation("获取当前在线人数API")
    @GetMapping("showOnlineUsers")
    public Result showOnlineUsers() {
        long count = service.onlineUsers();
        return Result.ok().data("onlineUsers", count);
    }

}
