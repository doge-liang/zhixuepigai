package com.zhixue.demo.constant;


/**
 * @类名 : MsgCodeConsts
 * @说明 : 消息码常量
 * @创建日期 : 2019/8/31
 * @作者 : Niaowuuu
 * @版本 : 1.0
 */
public class StatusCodeConsts {

    /**
     * 成功
     */
    public static final Integer SUCCESS = 200;
    /**
     * 参数校验失败
     */
    public static final Integer BAD_REQUEST = 400;
    /**
     * 未授权
     */
    public static final Integer UNAUTHORIZED = 401;
    /**
     * 禁止访问
     */
    public static final Integer FORBIDDEN = 403;
    /**
     * 资源不存在
     */
    public static final Integer NOT_FOUND = 404;
    /**
     * 方法禁用
     */
    public static final Integer FORBIDDEN_METHOD = 405;
    /**
     * 请求超时
     */
    public static final Integer TIME_OUT = 408;
    /**
     * 资源已经删除
     */
    public static final Integer DELETED = 410;
    /**
     * 请求实体过大
     */
    public static final Integer ENTITY_OVERLOAD = 413;
    /**
     * 不支持的媒体类型
     */
    public static final Integer NOT_SUPPORTED_MEDIA_TYPE = 415;
    /**
     * 服务器异常
     */
    public static final Integer ERROR = 500;
    /**
     * 未登录
     */
    public static final Integer AUTH = 601;

}
