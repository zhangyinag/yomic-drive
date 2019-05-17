package com.yomic.drive.exception;

import org.springframework.validation.ObjectError;

import java.util.List;

public class ValidationException extends RuntimeException{

    public ValidationException(List<ObjectError> errors) {
        this.errors = errors;
    }

    private List<ObjectError> errors;

    public List<ObjectError> getErrors() {
        return errors;
    }

    public void setErrors(List<ObjectError> errors) {
        this.errors = errors;
    }
}
