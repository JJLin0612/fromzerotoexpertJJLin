package com.example.fromzerotoexpert.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @author RabbitFaFa
 */
@Aspect
@Component
public class AopEnhance {

    //切入点
    @Pointcut("execution(public * com.example.fromzerotoexpert.controller.*.*(..))")
    public void antiBrushing() {
        System.out.println("antiBrushing aspect");
    }

    @Before("antiBrushing()")
    public void dobefore() {

    }
}
