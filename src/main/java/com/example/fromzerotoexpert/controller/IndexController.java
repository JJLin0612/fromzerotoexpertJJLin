package com.example.fromzerotoexpert.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author JJLin
 * @date 2022/11/28
 */
@RequestMapping("/")
@RestController
public class IndexController {
    /**index页*/
    @Value("${project.nickname}")
    private String nickname;
    /**cookie的名称*/
    @Value("${project.index.cookie.name}")
    private String cookieName;
    /**cookie的值*/
    @Value("${project.index.cookie.value}")
    private String cookieValue;
    /**再次访问index页重定向路径*/
    @Value("${project.index.redirect.path}")
    private String redirectPath;

    @GetMapping("FromZerotoExpert")
    public String accessFirstly(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        Cookie[] cookies = request.getCookies();
        //请求携带cookie
        if(cookies.length == 0 || cookies == null) {
            for(Cookie cookie : cookies) {
                if(!cookie.getName().equals(cookieName)) continue;
                //请求带cookie且cookie信息正确 重定向到再次登录index页
                if(cookie.getValue().equals(cookieValue)) {
                    response.sendRedirect(redirectPath);
                }
            }
        }
        //请求不携带cookie 发送cookie
        Cookie cookie = new Cookie(cookieName, cookieValue);
        response.addCookie(cookie);
        return "嗨，欢迎您来到 " + nickname;
    }

    @GetMapping("accessIndexAgain")
    public String accessAgain() {
        return "嗨，欢迎您再次来到 " + nickname;
    }

}
