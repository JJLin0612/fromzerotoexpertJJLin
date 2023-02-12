package com.example.fromzerotoexpert.config.Interceptor;

import com.example.fromzerotoexpert.utils.CookieUtil;
import com.example.fromzerotoexpert.utils.IpUtil;
import com.fasterxml.uuid.EthernetAddress;
import com.fasterxml.uuid.Generators;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Calendar;

/**
 * @author RabbitFaFa
 */
@Component
public class GlobalInterceptor implements HandlerInterceptor {

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    /***
     * 统计网站相关访问数据
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //获取请求的 ip
        String ipAddr = IpUtil.getIpAddr(request);
        //更新 IP
        redisTemplate.opsForHyperLogLog().add("website:ip", ipAddr);
        //更新 PV
        redisTemplate.opsForValue().increment("website:pv");
        //获取 request 中的cookie
        String cookieValue = CookieUtil.getCookieValue(request, "cookie_uv");
        //第一次请求网站 设置cookie 更新 UV
        if (StringUtils.isEmpty(cookieValue)) {
            //获取当日23:59:59与当前的时间差毫秒数
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, Calendar.MONTH + 1, Calendar.DATE, 23, 59, 59);
            Long timeDiff = calendar.getTimeInMillis() - System.currentTimeMillis();
            //设置cookie
            String uuid = Generators.timeBasedGenerator(EthernetAddress.fromInterface()).generate().toString();
            Cookie cookie = new Cookie("cookie_uv", uuid);
            cookie.setMaxAge(timeDiff.intValue());//23:59:59 过期
            response.addCookie(cookie);

            redisTemplate.opsForHyperLogLog().add("website:uv", uuid);

        }

        return true;
    }
}
