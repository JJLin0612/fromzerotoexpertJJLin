package com.example.fromzerotoexpert.dto;

/**
 * @author JJLin
 * @date 2022/11/13
 */
public interface ResultCode {
    /***
     * 成功码
     */
    public static Integer SUCCESS = 20000;

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
    public static Integer ERROR = 10000;
    public static Integer USER_UNREGISTERED_ERROR = 10001;
    public static Integer INCORRECT_PHONE_OR_PWD_ERROR = 10002;
    public static Integer INCORRECT_VERIFY_CODE_ERROR = 10003;
    public static Integer USER_HAS_EXITS_ERROR = 10004;
    public static Integer USER_IS_DISABLED_ERROR = 10005;
    public static Integer TOKEN_NULL_OR_ERROR = 10006;
    public static Integer TOKEN_EXPIRE_ERROR = 10007;

    /***
     * 接口访问 11000
     *   a 访问频繁           11001
     */
    public static Integer API_ACCESS_TOO_BUSY = 11001;


    /**
     * 静态变量
     * 1、 User表
     *     a 用户是否被删除     0：未删除  1：删除状态
     */
    public static Integer USER_IS_NOT_DELETED = 0;
    public static Integer USER_IS_DELETED = 1;

}
