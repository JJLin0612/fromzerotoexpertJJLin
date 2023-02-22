package com.example.fromzerotoexpert.entity.dto;

/**
 * @author JJLin
 * @date 2022/11/13
 */
public interface ResultCode {
    /***
     * 成功码
     */
    Integer SUCCESS = 20000;

    /***
     * 异常码 10000
     * 1、登录登出注册 10xxx
     *    a 用户未注册        10001
     *    b 手机号或密码错误   10002
     *    c 验证码错误        10003
     *    c 用户已注册        10004
     *    d 用户被禁用        10005
     *    e token 空或错误   10006
     *    f token 过期       10007
     */
    Integer ERROR = 10000;
    Integer USER_UNREGISTERED_ERROR = 10001;
    Integer INCORRECT_PHONE_OR_PWD_ERROR = 10002;
    Integer INCORRECT_VERIFY_CODE_ERROR = 10003;
    Integer USER_HAS_EXITS_ERROR = 10004;
    Integer USER_IS_DISABLED_ERROR = 10005;
    Integer TOKEN_NULL_OR_ERROR = 10006;
    Integer TOKEN_EXPIRE_ERROR = 10007;

    /***
     * 接口访问 11000
     *   a 访问频繁           11001
     */
    Integer API_ACCESS_TOO_BUSY = 11001;

    /***
     * 业务CRUD操作 11100
     *   a 新增失败           11101
     *   b 删除失败           11102
     *   c 更新失败           11103
     */
    Integer ADD_RECORD_FAILED = 11101;
    Integer DELETE_RECORD_FAILED = 11102;
    Integer UPDATE_RECORD_FAILED = 11103;


    /**
     * 静态变量
     * 1、 User表
     *     a 用户是否被删除     0：未删除  1：删除状态
     */
    Integer USER_IS_NOT_DELETED = 0;
    Integer USER_IS_DELETED = 1;


}
