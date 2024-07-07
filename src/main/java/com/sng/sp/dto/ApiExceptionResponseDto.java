package com.sng.sp.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ApiExceptionResponseDto {

    private String errorCode;
    private String errorMessage;

    @Builder
    public ApiExceptionResponseDto(String errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
