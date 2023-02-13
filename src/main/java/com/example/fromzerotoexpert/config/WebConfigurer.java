package com.example.fromzerotoexpert.config;

import com.example.fromzerotoexpert.config.Interceptor.GlobalInterceptor;
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

    /***
     * 用于注册拦截器
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(globalInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/VerifyCode/**")
                .excludePathPatterns("/RSA-Encryption/**")
                .excludePathPatterns("/RegisterAndLogin/**")
                .excludePathPatterns("/swagger-ui.html/**")
                .excludePathPatterns("/webjars/springfox-swagger-ui/**")
                .excludePathPatterns("/swagger-resources/**");
    }

    /***
     * 配置静态资源
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

    }
}
