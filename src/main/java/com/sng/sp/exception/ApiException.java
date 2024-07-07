package com.sng.sp.exception;

import com.sng.sp.enums.ExceptionEnum;
import lombok.Getter;

@Getter
public class ApiException extends RuntimeException{

    private ExceptionEnum error;

    public ApiException(ExceptionEnum e) {
        super(e.getMessage());
        this.error = e;
    }
}
