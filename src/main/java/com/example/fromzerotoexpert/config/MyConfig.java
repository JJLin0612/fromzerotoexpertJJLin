package com.example.fromzerotoexpert.config;

import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import com.baomidou.mybatisplus.extension.injector.LogicSqlInjector;
import com.example.fromzerotoexpert.utils.RSAUtils;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author RabbitFaFa
 * @date 2022/12/3
 */
@Configuration
@MapperScan("com.example.fromzerotoexpert.mapper")
public class MyConfig {

    /**
     * 逻辑删除插件
     * @return
     */
    @Bean
    public ISqlInjector iSqlInjector() {
        return new LogicSqlInjector();
    }

    @Bean
    public RSAUtils rsaUtils() {
        return new RSAUtils();
    }
}
