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

    /***
     * @return 返回操作成功的Result对象
     */
    public static Result ok() {
        Result result = new Result();
        result.setIsSuccess(true);
        result.setCode(ResultCode.SUCCESS);
        result.setMessage("成功");
        return result;
    }

    /***
     * @return 返回操作失败的Result对象
     */
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

    /***
     * 将Result对象中的默认map替换为新的data
     * @param data 新的data
     * @return Result对象实例
     */
    public Result data(Map<String, Object> data) {
        this.setData(data);
        return this;
    }

    /***
     * 向结果对象中以 k-v 的方式加入数据
     * @param key
     * @param value
     * @return
     */
    public Result data(String key, Object value) {
        this.data.put(key, value);
        return this;
    }
}
