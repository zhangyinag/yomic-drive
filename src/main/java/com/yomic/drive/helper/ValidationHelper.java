package com.yomic.drive.helper;

import com.yomic.drive.exception.ValidationException;
import org.springframework.validation.BindingResult;

public class ValidationHelper {
    public static void validate(BindingResult result) throws ValidationException {
        if(result.hasErrors()){
            throw new ValidationException(result.getAllErrors());
        }
    }
}
