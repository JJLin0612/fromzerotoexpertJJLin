package com.example.fromzerotoexpert.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.fromzerotoexpert.entity.RecordLog;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.fromzerotoexpert.entity.vo.LogQuery;

import java.util.List;
import java.util.logging.LogRecord;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author RabbitFaFa
 * @since 2023-02-15
 */
public interface RecordLogService extends IService<RecordLog> {

    void getLogListByQuery(Page<RecordLog> page, LogQuery logQuery);
}
