package com.sng.sp.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResultDto<T> {

    private Integer status;
    private T data;
    private String serviceCode;

    private String errorCode;
    private String message;

}
