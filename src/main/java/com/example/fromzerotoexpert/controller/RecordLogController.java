package com.example.fromzerotoexpert.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.fromzerotoexpert.entity.RecordLog;
import com.example.fromzerotoexpert.entity.dto.OperateType;
import com.example.fromzerotoexpert.entity.dto.Result;
import com.example.fromzerotoexpert.entity.vo.LogQuery;
import com.example.fromzerotoexpert.service.RecordLogService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.logging.LogRecord;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * 日志记录接口
 * @author RabbitFaFa
 * @since 2023-02-15
 */
@RestController
@RequestMapping("/record-log")
public class RecordLogController {

    @Resource
    private RecordLogService logService;

    @PostMapping("getLogByQuery/{pageCurr}/{pageSize}")
    public Result getLog(
            @RequestBody LogQuery logQuery,
            @PathVariable(value = "pageCurr") Long pageCurr,
            @PathVariable(value = "pageSize") Long pageSize
            ) {
        Page<RecordLog> page = new Page<>(pageCurr, pageSize);
        logService.getLogListByQuery(page, logQuery);
        List<RecordLog> logListPage = page.getRecords();
        long total = page.getTotal();
        return Result.ok().data("logListPage", logListPage).data("total", total);
    }

//    @GetMapping("addLog")
//    public Result addLog() {
//        RecordLog log = new RecordLog();
//        log.setStatus("success");
//        log.setCostMs("16889");
//        log.setOpIp("192.168.1.146");
//        log.setOpDesc("测试添加日志功能");
//        log.setOpAcc("JJLin");
//        log.setOpType(OperateType.ADD);
//        logService.save(log);
//        return Result.ok();
//    }
}

