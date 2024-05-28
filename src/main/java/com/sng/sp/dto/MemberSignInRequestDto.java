package com.sng.sp.dto;

import lombok.Data;

@Data
public class MemberSignInRequestDto {

    private String username;
    private String email;
    private String password;

}
