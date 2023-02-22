package com.example.fromzerotoexpert.controller;


import com.example.fromzerotoexpert.annotation.LogRecord;
import com.example.fromzerotoexpert.entity.dto.OperateType;
import com.example.fromzerotoexpert.entity.dto.Result;
import com.example.fromzerotoexpert.entity.Webaccesscount;
import com.example.fromzerotoexpert.entity.vo.WebAccessCountQuery;
import com.example.fromzerotoexpert.service.WebaccesscountService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

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
    @LogRecord(operateType = OperateType.READ, operateDesc = "查询IP UV PV")
    public Result getAccessCount(@RequestBody WebAccessCountQuery query) {
         List<Webaccesscount> accessData = service.getAccessCount(query);
         return Result.ok().data("accessData", accessData);
    }

    @ApiOperation("新增用户IP到白名单中")
    @GetMapping("addWhiteList")
    @LogRecord(operateType = OperateType.ADD, operateDesc = "新增用户IP白名单")
    public Result addUserWhiteList(HttpServletRequest request) {
        boolean res = service.addUserWhiteList(request);
        return res ? Result.ok() : Result.error();
    }

    @ApiOperation("从白名单中删除此IP")
    @DeleteMapping("deleteWhiteIP")
    @LogRecord(operateType = OperateType.DELETE, operateDesc = "移除白名单指定IP")
    public Result deleteWhiteIP(HttpServletRequest request) {
        boolean res = service.deleteUserWhiteList(request);
        return res ? Result.ok() : Result.error();
    }

}

