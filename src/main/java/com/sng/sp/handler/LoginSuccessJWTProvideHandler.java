package com.sng.sp.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sng.sp.controller.UserController;
import com.sng.sp.dto.ResultDto;
import com.sng.sp.jwt.service.JwtService;
import com.sng.sp.repository.UsersRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.security.SecurityUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class LoginSuccessJWTProvideHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final UsersRepository usersRepository;

    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String email = extractEmail(authentication);

        String accessToken = jwtService.createAccessToken(email);
        String refreshToken = jwtService.createRefreshToken();

        jwtService.sendAccessAndRefreshToken(response, accessToken, refreshToken);
        usersRepository.findByEmail(email).ifPresent(
                users -> users.updateRefreshToken(refreshToken)
        );



        log.info( "로그인에 성공합니다. email: {}" , email);
        log.info( "AccessToken 을 발급합니다. AccessToken: {}" ,accessToken);
        log.info( "RefreshToken 을 발급합니다. RefreshToken: {}" ,refreshToken);

        Map resultMap = new HashMap();
        resultMap.put("email", email);
        resultMap.put("token", accessToken);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        String result = objectMapper.writeValueAsString(resultMap);
        response.getWriter().write(result);


    }

    private String extractEmail(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userDetails.getUsername();
    }

    private ResultDto response(Map resultMap) {
        return ResultDto.builder().data(resultMap).build();
    }

}
