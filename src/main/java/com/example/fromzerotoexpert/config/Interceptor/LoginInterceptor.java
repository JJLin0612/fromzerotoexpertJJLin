package com.example.fromzerotoexpert.config.Interceptor;

import com.example.fromzerotoexpert.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登录拦截器
 * @author RabbitFaFa
 * @date 2022/12/28
 */
@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /***
     * 目标控制器方法执行之前
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //同源检测
        if (StringUtils.isEmpty(request.getHeader("Referer"))) return false;
        if (!request.getHeader("Referer").startsWith("http://localhost:8086")) {
            return false;
        }
        //检查token 是否：过期, 不支持, 格式不正确, 签名不正确, 非法参数
        if (!JwtUtils.checkToken(request)) return false;
        //从token中获取userId
        String userId = JwtUtils.getDataIdByJwtToken(request);
        //根据userId在redis中查询
        String deviceInfoFromCache = redisTemplate.opsForValue().get(userId);
        //缓存中token过期
        if (!StringUtils.isEmpty(deviceInfoFromCache)) return false;

        return true;
    }

}
