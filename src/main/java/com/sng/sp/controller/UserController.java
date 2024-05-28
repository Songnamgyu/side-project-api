package com.sng.sp.controller;

import com.sng.sp.domain.entity.Users;
import com.sng.sp.dto.MemberSignInRequestDto;
import com.sng.sp.dto.ResultDto;
import com.sng.sp.jwt.service.JwtService;
import com.sng.sp.service.UsersService;
import com.sng.sp.service.impl.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserDetailsServiceImpl usersServiceImpl;
    private final UsersService usersService;
    private final JwtService jwtService;
    @PostMapping("/signup")
    public ResultDto signIn( @RequestBody MemberSignInRequestDto memberSignInRequestDto) {
        usersService.signIn(memberSignInRequestDto);
        return new ResultDto<Object>();
    }

//    @PostMapping("/login")
    public ResultDto login(@RequestBody MemberSignInRequestDto param) {
        UserDetails users = usersServiceImpl.loadUserByUsername(param.getEmail());
        System.out.println("users = " + users);
        return new ResultDto<Object>();
    }
}
