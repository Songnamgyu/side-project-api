package com.sng.sp.controller;

import com.sng.sp.dto.MemberSignInRequestDto;
import com.sng.sp.dto.ResultDto;
import com.sng.sp.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UsersService usersService;
    @GetMapping("/signup")
    public ResultDto signIn( MemberSignInRequestDto memberSignInRequestDto) {
        usersService.signIn(memberSignInRequestDto);
        return new ResultDto<Object>();
    }
}
