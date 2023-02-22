package com.example.fromzerotoexpert.entity.dto;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 统一返回结果对象
 * @author JJLin
 * @date 2022/11/13
 */
@Data
public class Result {
    private Boolean isSuccess;
    private Integer code;
    private String message;
    private Map<String, Object> data = new HashMap<>();

    public Result() {}

    public static Result ok() {
        Result result = new Result();
        result.setIsSuccess(true);
        result.setCode(ResultCode.SUCCESS);
        result.setMessage("成功");
        return result;
    }

    public static Result error() {
        Result result = new Result();
        result.setIsSuccess(false);
        result.setCode(ResultCode.ERROR);
        result.setMessage("失败");
        return result;
    }

    public Result setMessage(String message) {
        this.message = message;
        return this;
    }

    public Result setCode(Integer code) {
        this.code = code;
        return this;
    }

    public Result data(Map<String, Object> data) {
        this.setData(data);
        return this;
    }

    public Result data(String key, Object value) {
        this.data.put(key, value);
        return this;
    }
}
