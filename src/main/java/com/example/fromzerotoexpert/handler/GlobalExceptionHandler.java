package com.example.fromzerotoexpert.handler;

import com.example.fromzerotoexpert.entity.dto.Result;
import com.example.fromzerotoexpert.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * 统一异常处理
 * @author RabbitFaFa
 * @date 2023/2/7
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /***
     * 处理全局出现的非自定义异常
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result exceptionAppear(Exception e) {
        e.printStackTrace();
        return Result.error().setMessage(e.getMessage());
    }

    /***
     * 处理自定义异常
     * @param customException
     * @return
     */
    @ExceptionHandler(CustomException.class)
    @ResponseBody
    public Result customExceptionHandle(CustomException customException) {
        customException.printStackTrace();
        log.error(customException.getErrorMsg());
        return Result.error()
                .setMessage(customException.getMessage())
                .setCode(customException.getErrorCode());
    }
}
