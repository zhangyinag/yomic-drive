package com.yomic.drive.helper;

import lombok.Data;

@Data
public class JsonResult<E> {

    private E data;

    private String code;

    private String message;

    public JsonResult() {
    }

    public JsonResult(E data) {
        this.data = data;
        this.code = StatusDict.SUCCESS.getCode();
        this.message = StatusDict.SUCCESS.getMessage();
    }

    public JsonResult(StatusDict status) {
        this.code = status.getCode();
        this.message = status.getMessage();
    }

    public JsonResult(StatusDict status, String message) {
        this.code = status.getCode();
        this.message = message == null ? status.getMessage() : message;
    }

    public JsonResult(StatusDict status, String message, E data) {
        this(status, message);
        this.data = data;
    }

}
