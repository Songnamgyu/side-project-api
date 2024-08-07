package com.sng.sp.filter;

import com.sng.sp.controller.UserController;
import com.sng.sp.domain.UserDetailsImpl;
import com.sng.sp.domain.entity.Users;
import com.sng.sp.jwt.service.JwtService;
import com.sng.sp.repository.UsersRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationProcessingFilter  extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UsersRepository usersRepository;

    private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();//5

    private final String NO_CHECK_URL = "/login";//1

    /**
     * 1. 리프레시 토큰이 오는 경우 -> 유효하면 AccessToken 재발급후, 필터 진행 X, 바로 튕기기
     *
     * 2. 리프레시 토큰은 없고 AccessToken만 있는 경우 -> 유저정보 저장후 필터 계속 진행
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(request.getRequestURI().equals(NO_CHECK_URL)) {
            filterChain.doFilter(request, response);

            return;//안해주면 아래로 내려가서 계속 필터를 진행해버림
        }

        String refreshToken = jwtService
                .extractRefreshToken(request)
                .filter(jwtService::isTokenValid)
                .orElse(null); //2


        if(refreshToken != null){
            checkRefreshTokenAndReIssueAccessToken(response, refreshToken);//3
            return;
        }

        checkAccessTokenAndAuthentication(request, response, filterChain);//4
    }

    private void checkAccessTokenAndAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        jwtService.extractAccessToken(request).filter(jwtService::isTokenValid).ifPresent(

                accessToken -> jwtService.extractEmail(accessToken).ifPresent(

                        email -> usersRepository.findByEmail(email).ifPresent(

                                users -> saveAuthentication(users)
                        )
                )
        );

        filterChain.doFilter(request,response);
    }



    private void saveAuthentication(Users users) {
        UserDetailsImpl userDetails = new UserDetailsImpl(users);

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null,authoritiesMapper.mapAuthorities(userDetails.getAuthorities()));


        SecurityContext context = SecurityContextHolder.createEmptyContext();//5
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }

    private void checkRefreshTokenAndReIssueAccessToken(HttpServletResponse response, String refreshToken) {
        usersRepository.findByRefreshToken(refreshToken).ifPresent(
                users -> jwtService.sendAccessToken(response, jwtService.createAccessToken(users.getEmail()))
        );


    }
}
