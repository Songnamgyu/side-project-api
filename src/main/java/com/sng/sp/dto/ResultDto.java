package com.sng.sp.dto;

import lombok.*;

@Data
public class ResultDto<T> {

    private T data;
    private String message;


}
