package com.example.fromzerotoexpert.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.fromzerotoexpert.entity.Webaccesscount;
import com.example.fromzerotoexpert.entity.vo.WebAccessCountQuery;
import com.example.fromzerotoexpert.mapper.WebaccesscountMapper;
import com.example.fromzerotoexpert.service.WebaccesscountService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author RabbitFaFa
 * @since 2023-02-12
 */
@Service
public class WebaccesscountServiceImpl extends ServiceImpl<WebaccesscountMapper, Webaccesscount> implements WebaccesscountService {

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    /***
     * 获取网站 IP UV PV
     * @param query 查询实体
     * @return 今日或日期范围内每日的 IP UV PV 数据
     */
    @Override
    public List<Webaccesscount> getAccessCount(WebAccessCountQuery query) {
        //取出查询条件
        String beginDate = query.getBegin();
        String endDate = query.getEnd();

        //查询 DB 中日期范围 >= begin 的数据
        QueryWrapper<Webaccesscount> wrapper = new QueryWrapper<>();
        wrapper.ge("gmt_create", beginDate);
        List<Webaccesscount> dataList = baseMapper.selectList(wrapper);

        //截止日期为空 Redis中查询今日当前 IP UV PV 数据 加上今日的数据
        Long ipSize = redisTemplate.opsForHyperLogLog().size("website:ip");
        Long uvSize = redisTemplate.opsForHyperLogLog().size("website:uv");
        String pvSize = redisTemplate.opsForValue().get("website:pv");
        Webaccesscount currData = new Webaccesscount();
        currData.setIp(ipSize.intValue());
        currData.setUv(uvSize.intValue());
        currData.setPv(Integer.parseInt(pvSize));
        dataList.add(currData);

        return dataList;
    }
}
