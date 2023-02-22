package com.example.fromzerotoexpert.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.fromzerotoexpert.entity.RecordLog;
import com.example.fromzerotoexpert.entity.vo.LogQuery;
import com.example.fromzerotoexpert.mapper.RecordLogMapper;
import com.example.fromzerotoexpert.service.RecordLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.logging.LogRecord;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author RabbitFaFa
 * @since 2023-02-15
 */
@Service
public class RecordLogServiceImpl extends ServiceImpl<RecordLogMapper, RecordLog> implements RecordLogService {

    /** 根据分页对象及条件查询对象查询日志 */
    @Override
    public void getLogListByQuery(Page<RecordLog> page, LogQuery logQuery) {
        QueryWrapper<RecordLog> wrapper = new QueryWrapper<>();
        //查询条件为空 返回最新日志信息
        if (StringUtils.isEmpty(logQuery)) {
            wrapper.orderByDesc("create_time");
            baseMapper.selectPage(page, wrapper);
            return;
        }
        //取出查询对象的查询条件
        String opType = logQuery.getOpType();
        String opAcc = logQuery.getOpAcc();
        String status = logQuery.getStatus();
        Date beginTime = logQuery.getBeginTime();
        Date endTime = logQuery.getEndTime();

        //组合查询条件
        if (!StringUtils.isEmpty(opAcc)) {
            wrapper.eq("op_acc", opAcc);
        }
        if (!StringUtils.isEmpty(opType)) {
            wrapper.eq("op_type", opType);
        }
        if (!StringUtils.isEmpty(status)) {
            wrapper.eq("status", status);
        }
        if (!StringUtils.isEmpty(beginTime)) {
            wrapper.ge("create_time", beginTime);
        }
        if (!StringUtils.isEmpty(endTime))
            wrapper.le("create_time", endTime);
        else wrapper.le("create_time", new Date());

        //分页查询
        baseMapper.selectPage(page, wrapper);

    }
}
