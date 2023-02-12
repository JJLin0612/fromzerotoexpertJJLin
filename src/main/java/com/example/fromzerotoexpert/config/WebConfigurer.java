package com.example.fromzerotoexpert.config;

import com.example.fromzerotoexpert.config.Interceptor.GlobalInterceptor;
import com.example.fromzerotoexpert.config.Interceptor.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author RabbitFaFa
 */
@Configuration
public class WebConfigurer implements WebMvcConfigurer {

    @Autowired
    private GlobalInterceptor globalInterceptor;
    @Autowired
    private LoginInterceptor loginInterceptor;

    /***
     * 用于注册拦截器
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(globalInterceptor)
                .addPathPatterns("/**");
        //注册登录拦截器
//        registry.addInterceptor(loginInterceptor)
//                .addPathPatterns("/**").excludePathPatterns("/swagger-ui.html");
    }

    /***
     * 配置静态资源
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

    }
}
