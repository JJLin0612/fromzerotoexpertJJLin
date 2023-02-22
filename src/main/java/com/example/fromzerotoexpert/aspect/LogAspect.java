package com.example.fromzerotoexpert.aspect;

import com.example.fromzerotoexpert.annotation.LogRecord;
import com.example.fromzerotoexpert.entity.RecordLog;
import com.example.fromzerotoexpert.entity.User;
import com.example.fromzerotoexpert.entity.dto.OperateType;
import com.example.fromzerotoexpert.entity.dto.Result;
import com.example.fromzerotoexpert.service.RecordLogService;
import com.example.fromzerotoexpert.service.UserService;
import com.example.fromzerotoexpert.utils.IpUtil;
import com.example.fromzerotoexpert.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author RabbitFaFa
 */
@Slf4j
@Aspect
@Component
public class LogAspect {

    @Resource
    private UserService userService;

    @Resource
    private RecordLogService logService;

    @Autowired
    private ThreadPoolExecutor executor;

    /**
     * 切入点
     */
    @Pointcut("@annotation(com.example.fromzerotoexpert.annotation.LogRecord)")
    public void logRecord() {
    }

    /***
     * 增强(通知)
     * @param joinPoint 连接点
     * @return 原被增强方法的执行结果
     */
    @Around("logRecord()")
    public Result aroundEnhance(ProceedingJoinPoint joinPoint) {
        //获取request
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();

        //获取方法上的LogRecord注解
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        LogRecord logRecordAnnotation = method.getAnnotation(LogRecord.class);

        //获取LogRecord注解的属性
        String operateType = logRecordAnnotation.operateType();
        String operateDesc = logRecordAnnotation.operateDesc();
        String contentId = logRecordAnnotation.contentId();

        //获取ip 根据token获取用户id再查询DB得用户账号
        String opIp = IpUtil.getIpAddr(request);
        String opAcc = "";
        if (!operateType.equals(OperateType.LOGIN) && !operateType.equals(OperateType.REGISTER)) {
            String userId = JwtUtils.getDataIdByJwtToken(request);//操作者(用户)id
            User user = userService.getById(userId);
            opAcc = user.getAcc();
        }

        //旧纪录

        //TODO 用户进行更新操作 从DB根据id读取旧纪录
//        if (operateType.equals(OperateType.MODIFY)) {
//
//        }

        Result result = null;

        //操作结果 错误信息 耗时
        String status = "";
        String errorMsg = "";
        long timeCost = 0;

        try {
            long startTime = System.currentTimeMillis();
            //执行目标方法
            result = (Result) joinPoint.proceed();
            //获取操作耗时
            timeCost = System.currentTimeMillis() - startTime;
            //用户注册操作 获取注册后的acc更新opAcc
            if (operateType.equals(OperateType.REGISTER)) {
                Map<String, Object> data = result.getData();
                opAcc = (String) data.get("acc");
            }
            if (operateType.equals(OperateType.LOGIN)) {
                Map<String, Object> data = result.getData();
                String token = (String)data.get("token");
                String id = JwtUtils.getDataIdByTokenStr(token);
                User user = userService.getById(id);
                opAcc = user.getAcc();
            }
            //操作状态
            status = result.getIsSuccess() ? "success" : "failed";
        } catch (Throwable throwable) {
            status = "failed";
            //记录错误信息
            errorMsg = throwable.getMessage();
            log.error("目标方法执行异常");
        }

        //创建日志对象
        RecordLog recordLog = new RecordLog();
        recordLog.setOpType(operateType);
        recordLog.setOpAcc(opAcc);
        recordLog.setOpDesc(operateDesc);
        recordLog.setOpIp(opIp);
        recordLog.setStatus(status);
        recordLog.setErrMsg(errorMsg);
        recordLog.setCostMs(String.valueOf(timeCost));

        //将日志记录 异步 刷盘
        executor.submit(() -> {
            logService.save(recordLog);
        });

        return result;
    }

}
