package com.example.fromzerotoexpert.config.Interceptor;

import com.alibaba.fastjson.JSONObject;
import com.example.fromzerotoexpert.dto.Result;
import com.example.fromzerotoexpert.dto.ResultCode;
import com.example.fromzerotoexpert.utils.CookieUtil;
import com.example.fromzerotoexpert.utils.IpUtil;
import com.example.fromzerotoexpert.utils.JwtUtils;
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
import java.io.IOException;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

/**
 * @author RabbitFaFa
 */
@Component
public class GlobalInterceptor implements HandlerInterceptor {

    @Resource
    private RedisTemplate<String, String> redisTemplate;
    //接口防刷 白名单key 时间限制 10s 时间限制内的访问次数上限
    private static final String WHITE_LIST_KEY = "website:whiteList";
    private static final int EXPIRE = 10;
    private static final int ACCESS_LIMIT = 10;
    //IP PV UV的缓存key
    private static final String WEBSITE_IP = "website:ip";
    private static final String WEBSITE_PV = "website:pv";
    private static final String WEBSITE_UV = "website:uv";

    /***
     * 统计网站相关访问数据
     * @param request 请求
     * @param response 回应
     * @param handler 包含的调用的API全名称
     * @return true-请求放行 false-拦截请求
     * @throws Exception 异常
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //访问量统计
        countAccess(request, response);
        //前端的token登录凭证检查
//        if (loginTokenCheck(request, response)) return false;
        //接口访问限制
        if (accessLimit(request, response)) return false;

        return true;
    }

    /***
     * 登录凭证检查
     * @param request 请求
     * @param response 回应
     * @return true-发现问题  false-无问题 可登录
     */
    private boolean loginTokenCheck(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //同源检测
//        if (StringUtils.isEmpty(request.getHeader("Referer"))) return true;
//        if (!request.getHeader("Referer").startsWith("http://localhost:8086")) return true;
        //检查token 是否：过期, 不支持, 格式不正确, 签名不正确, 非法参数
        Result result;
        if (!JwtUtils.checkToken(request)) {
            result = Result.error().setCode(ResultCode.TOKEN_NULL_OR_ERROR).setMessage("token null or error");
            response.getWriter().write(JSONObject.toJSONString(result));
            return true;
        }
        //从token中获取userId
        String userId = JwtUtils.getDataIdByJwtToken(request);
        //根据userId在redis中查询
        String deviceInfoFromCache = redisTemplate.opsForValue().get(userId);
        //缓存中token过期
        if (!StringUtils.isEmpty(deviceInfoFromCache)) {
            result = Result.error().setCode(ResultCode.TOKEN_EXPIRE_ERROR).setMessage("token expired");
            response.getWriter().write(JSONObject.toJSONString(result));
            return true;
        }

        return false;
    }

    /***
     * 统计IP PV UV等指标
     * @param request 请求
     * @param response 回应
     */
    @SuppressWarnings(value = "all")
    private void countAccess(HttpServletRequest request, HttpServletResponse response) {
        //获取请求的 ip
        String ipAddr = IpUtil.getIpAddr(request);
        //更新 IP
        redisTemplate.opsForHyperLogLog().add(WEBSITE_IP, ipAddr);
        //更新 PV
        redisTemplate.opsForValue().increment(WEBSITE_PV);
        //获取 request 中的cookie
        String cookieValue = CookieUtil.getCookieValue(request, "cookie_uv");
        //第一次请求网站 设置cookie 更新 UV
        if (StringUtils.isEmpty(cookieValue)) {
            //获取当日23:59:59与当前的时间差毫秒数
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, Calendar.MONTH + 1, Calendar.DATE, 23, 59, 59);
            long timeDiff = calendar.getTimeInMillis() - System.currentTimeMillis();
            //设置cookie
            String uuid = Generators.timeBasedGenerator(EthernetAddress.fromInterface()).generate().toString();
            Cookie cookie = new Cookie("cookie_uv", uuid);
            cookie.setMaxAge((int) timeDiff);//23:59:59 过期
            response.addCookie(cookie);

            redisTemplate.opsForHyperLogLog().add(WEBSITE_UV, uuid);
        }
    }

    /***
     * 接口访问防刷
     * @param request 请求
     * @param response 回应
     * @return true-拦截   false-请求正常或在白名单内
     */
    @SuppressWarnings(value = "all")
    private boolean accessLimit(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //请求IP
        String ipAddr = IpUtil.getIpAddr(request);
        //IP在白名单内 白名单设计: k-'website:whiteList'  v-'ip'
        if (redisTemplate.opsForSet().isMember(WHITE_LIST_KEY, ipAddr)) return false;
        //接口访问次数记录 k-ip+接口  v-count  expire
        String ipAndUrl = ipAddr + request.getRequestURL();
        //此IP第一次访问
        if (!redisTemplate.hasKey(ipAndUrl)) {
            redisTemplate.opsForValue().increment(ipAndUrl);
            redisTemplate.expire(ipAndUrl, EXPIRE, TimeUnit.SECONDS);
        }else {
            int accessCount = Integer.parseInt(redisTemplate.opsForValue().get(ipAndUrl));
            //此IP访问此接口的次数未超过限制
            if (accessCount < ACCESS_LIMIT)
                redisTemplate.opsForValue().increment(ipAndUrl);
            else {
                Result result = Result.error().setCode(ResultCode.API_ACCESS_TOO_BUSY).setMessage("api access busy");
                response.getWriter().write(JSONObject.toJSONString(result));
                return true;
            }
        }
        return false;
    }
}
