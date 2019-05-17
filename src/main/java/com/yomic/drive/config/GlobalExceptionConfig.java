package com.yomic.drive.config;

import com.yomic.drive.constant.HttpJsonStatus;
import com.yomic.drive.exception.ValidationException;
import com.yomic.drive.helper.JsonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@ControllerAdvice
public class GlobalExceptionConfig {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public JsonResult<Object> exceptionHandler(HttpServletRequest request, Exception exception) throws Exception {
        log.error(exception.getMessage(), exception);
        if (exception instanceof ValidationException) {
            return new JsonResult<>(HttpJsonStatus.VALID_FAIL,null, ((ValidationException) exception).getErrors());
        }
        return new JsonResult<>(HttpJsonStatus.EXCEPTION,null, exception.getMessage());
    }
}
