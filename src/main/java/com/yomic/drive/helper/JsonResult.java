package com.yomic.drive.helper;

import com.yomic.drive.constant.HttpJsonStatus;
import lombok.Data;

@Data
public class JsonResult<E> {

    private E data;

    private String code;

    private String message;

    public JsonResult() {
        this(HttpJsonStatus.SUCCESS);
    }

    public JsonResult(E data) {
        this.data = data;
        this.code = HttpJsonStatus.SUCCESS.getCode();
        this.message = HttpJsonStatus.SUCCESS.getMessage();
    }

    public JsonResult(HttpJsonStatus status) {
        this.code = status.getCode();
        this.message = status.getMessage();
    }

    public JsonResult(HttpJsonStatus status, String message) {
        this.code = status.getCode();
        this.message = message == null ? status.getMessage() : message;
    }

    public JsonResult(HttpJsonStatus status, String message, E data) {
        this(status, message);
        this.data = data;
    }

}
