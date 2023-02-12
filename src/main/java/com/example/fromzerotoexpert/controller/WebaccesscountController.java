package com.example.fromzerotoexpert.controller;


import com.example.fromzerotoexpert.dto.Result;
import com.example.fromzerotoexpert.entity.Webaccesscount;
import com.example.fromzerotoexpert.entity.vo.WebAccessCountQuery;
import com.example.fromzerotoexpert.service.WebaccesscountService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author RabbitFaFa
 * @since 2023-02-12
 */
@RestController
@RequestMapping("/webaccesscount")
public class WebaccesscountController {

    @Resource
    private WebaccesscountService service;

    /***
     * 根据查询实体查询网站的IP UV PV
     * @param query 查询实体
     * @return 查询日期范围内网站每日/当前的 IP UV PV
     */
    @ApiOperation("根据查询实体查询网站的IP UV PV")
    @PostMapping
    public Result getAccessCount(@RequestBody WebAccessCountQuery query) {
         List<Webaccesscount> accessData = service.getAccessCount(query);
         return Result.ok().data("accessData", accessData);
    }

}

