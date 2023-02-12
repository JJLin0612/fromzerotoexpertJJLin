package com.example.fromzerotoexpert.service;

import com.example.fromzerotoexpert.entity.Webaccesscount;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.fromzerotoexpert.entity.vo.WebAccessCountQuery;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author RabbitFaFa
 * @since 2023-02-12
 */
public interface WebaccesscountService extends IService<Webaccesscount> {

    List<Webaccesscount> getAccessCount(WebAccessCountQuery query);
}
