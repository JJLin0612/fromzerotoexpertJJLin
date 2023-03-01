package com.example.fromzerotoexpert.tasks;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.fromzerotoexpert.entity.Webaccesscount;
import com.example.fromzerotoexpert.service.WebaccesscountService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * 多线程定时任务
 * @author RabbitFaFa
 */
@EnableScheduling
@Component
public class MutilThreadScheduleTask {

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Resource
    private WebaccesscountService webaccesscountService;

    /***
     * 定时任务: 清理离线的用户
     */
    @Async
    @Scheduled(cron = "0 */2 * * * ?")
    public void cleanOfflineUsersCache() {
        // 2 分钟前的毫秒值
        long time = System.currentTimeMillis() - 1000 * 60 * 2;
        //清理超时2min未操作用户 即为离线状态
        redisTemplate.opsForZSet().removeRangeByScore("user:online", 0, time);
    }

    /**
     * 每天23:59:59 保存网站IP UV PV到数据库
     * 再清除Redis中的IP UV PV
     */
    @Async
    @Scheduled(cron = "59 59 23 * * ?")
    public void cleanIpUvPvData() {
        redisTemplate.opsForHyperLogLog().delete("website:ip");
        redisTemplate.opsForHyperLogLog().delete("website:uv");
        redisTemplate.delete("website:pv");
    }

    /***
     * 每个月清除一个月前的IP UV PV数据
     */
    @Async
    @Scheduled(cron = "* * * */1 * ?")
    public void clearOneMonthBeforeData() {
        //获取上个月此时的日期时间
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        String lastMonth = dateFormat.format(calendar.getTime());
        //删除上个月的统计数据
        QueryWrapper<Webaccesscount> wrapper = new QueryWrapper<>();
        wrapper.lt("gmt_create", lastMonth);
        webaccesscountService.remove(wrapper);
    }

}
