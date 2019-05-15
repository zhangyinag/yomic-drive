package com.yomic.drive.helper;

public enum StatusDict {
    ERROR("-1", "操作失败"), // 失败
    SUCCESS("000000", "操作成功"); // 成功

    private final String code;
    private final String message;

    private StatusDict(String code, String message) {
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
