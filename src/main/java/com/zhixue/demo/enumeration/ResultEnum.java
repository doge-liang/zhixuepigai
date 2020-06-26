package com.zhixue.demo.enumeration;

import com.zhixue.demo.constant.StatusCodeConsts;

/**
 * @ClassName ResultEnum
 * @Description TODO
 * @Author Tori
 * @Date 2020/6/27 1:11
 * @Version 1.0
 **/
public enum ResultEnum {

    /**
     * 常见错误
     */
    SUCCESS(StatusCodeConsts.SUCCESS, "成功"),
    NOT_FOUND(StatusCodeConsts.NOT_FOUND, "资源不存在或者已删除"),
    USER_NOT_FOUND(StatusCodeConsts.NOT_FOUND, "用户不存在 "),
    INVALID_APP_SORT(StatusCodeConsts.BAD_REQUEST, "无效的应用排序"),
    DEPT_NAME_NOT_BLANK(StatusCodeConsts.BAD_REQUEST, "部门名称不能为空"),
    GROUP_NAME_NOT_BLANK(StatusCodeConsts.BAD_REQUEST, "权限组名称不能为空"),
    GROUP_NAME_EXIST(StatusCodeConsts.BAD_REQUEST, "权限组名称已存在"),
    GROUP_NOT_FOUND(StatusCodeConsts.BAD_REQUEST, "权限组不存在"),
    DEFAULT_DATA(StatusCodeConsts.FORBIDDEN, "系统默认数据，无法編輯和删除"),
    INVALID_SORT_INFO(StatusCodeConsts.BAD_REQUEST, "无效的排序信息"),
    ERROR(StatusCodeConsts.ERROR, "系统异常"),
    REDIS_CONNECTION_FAIL(StatusCodeConsts.BAD_REQUEST, "缓存服务器连接失败，请联系维护人员"),
    ENCRYPT_FAIL(StatusCodeConsts.FORBIDDEN, "请求数据解密失败"),
    ACCOUNT_HAS_BOUND(StatusCodeConsts.FORBIDDEN, "该账号已被绑定"),
    HAS_NOT_AUTH(StatusCodeConsts.FORBIDDEN, "请先完成认证"),
    FORBIDDEN_ACCESS(StatusCodeConsts.FORBIDDEN, "非法操作，禁止访问"),
    REQUIRE_LOGIN(StatusCodeConsts.AUTH, "请先登录"),
    INVALID_DISTRIBUTION(StatusCodeConsts.FORBIDDEN, "权限管理栏目不能与其它栏目同时分配"),

    BAD_REQUEST(StatusCodeConsts.BAD_REQUEST, "参数有误"),
    UNAUTHORIZED(StatusCodeConsts.FORBIDDEN, "无权限"),
    REQUEST_TIME_OUT(StatusCodeConsts.TIME_OUT, "请求超时"),
    REQUEST_LOGIN(StatusCodeConsts.FORBIDDEN, "请登录"),


    /**
     * 基本操作
     */
    INSERT_SUCCESS(StatusCodeConsts.SUCCESS, "新建成功"),
    UPDATE_SUCCESS(StatusCodeConsts.SUCCESS, "更新成功"),
    DELETE_SUCCESS(StatusCodeConsts.SUCCESS, "删除成功"),

    INSERT_FAILURE(StatusCodeConsts.BAD_REQUEST, "新建失败"),
    UPDATE_FAILURE(StatusCodeConsts.BAD_REQUEST, "更新失败"),
    DELETE_FAILURE(StatusCodeConsts.BAD_REQUEST, "删除失败");

    private Integer code;

    private String msg;

    ResultEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
