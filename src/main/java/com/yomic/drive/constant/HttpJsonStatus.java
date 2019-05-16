package com.yomic.drive.constant;

public enum HttpJsonStatus {
    EXCEPTION("20000", "服务器异常"),
    ACCESS_DENIED("110000", "没有访问权限"),
    AUTH_REQUIRED("100000", "未登录"),
    ERROR("-1", "访问异常"), // 失败
    SUCCESS("000000", "操作成功"); // 成功

    private final String code;
    private final String message;

    private HttpJsonStatus(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
