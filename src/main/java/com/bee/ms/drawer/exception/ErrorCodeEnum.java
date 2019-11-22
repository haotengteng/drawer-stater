package com.bee.ms.drawer.exception;

/**
 * @author Created by haoteng on 2018/6/29.
 */
public enum ErrorCodeEnum {

    ERROR_SYS_CUSTOM(601000100, "系统自定义错误码"),

    ERROR_NOT_FOUND(601000101, "请求地址不存在"),

    ERROR_ILLEGAL_PARAMETER(601000102, "缺少参数或参数不合法"),

    ERROR_SERVER_MAINTENANCE(601000103, "服务器正在维护。如有问题请联系客服"),

    ERROR_INTERNAL_ERROR(601000104, "您的操作有误或服务器繁忙，请稍后重试。如有问题请联系客服"),

    ERROR_ATTACK_BLOCKED(601000105, "客户端禁用，攻击机器"),

    ERROR_INJECT_REQUEST(601000106, "请求不合法，注入攻击请求"),

    ERROR_NO_DATA(1000007, "没有数据"),

    ERROR_NO_LOGIN(601000107, "请重新登录"),

    ERROR_NO_AUTHORITY(601000108, "您的账号暂无权限"),

    ERROR_REPEAT_REQUEST(601000109, "请求重复");


    private int code;

    private String message;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    ErrorCodeEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public ErrorCodeEnum customCode(int code) {
        this.code = code;
        return this;
    }

    public ErrorCodeEnum customMassage(String message) {
        this.message = message;
        return this;
    }

    public ErrorCodeEnum customCodeAndMassage(int code, String message) {
        this.code = code;
        this.message = message;
        return this;
    }

    @Override
    public String toString() {
        StringBuffer jsonBuilder = new StringBuffer();
        jsonBuilder.append("{\"error\":").append("{").append("\"code\"").append(":").append(this.code).append(",\"message\"").append(":").append("\"" + this.message + "\"").append("}}");
        return jsonBuilder.toString();
    }
}
