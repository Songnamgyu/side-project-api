package com.sng.sp.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class LoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {



    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); //401 인증 실패
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        Map errorMap = new HashMap<>();
        errorMap.put("message", "fail");
        errorMap.put("ERROR_CODE", HttpServletResponse.SC_UNAUTHORIZED);
        ObjectMapper objectMapper = new ObjectMapper();
        String result = objectMapper.writeValueAsString(errorMap);
        response.getWriter().write(result);
        log.info("로그인 실패");
    }
}