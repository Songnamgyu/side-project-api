package com.sng.sp.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sng.sp.filter.JsonUsernamePasswordAuthenticationFilter;
import com.sng.sp.handler.LoginFailureHandler;
import com.sng.sp.handler.LoginSuccessJWTProvideHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configurers.userdetails.DaoAuthenticationConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserDetailsServiceImpl userDetailsService;
    private final ObjectMapper objectMapper;

    /**
     * h2 db 접근 허용
     *
     * @return
     */
    @Bean
    public WebSecurityCustomizer configure() {
        return (web ->
                web.ignoring()
                        .requestMatchers(toH2Console()).requestMatchers("/h2-console/**"))
    }

    /**
     * 특정 HTTP 요청에 대한 웹 기반 보안 구성
     */

    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .addFilterAfter(jsonUsernamePasswordLoginFilter(), LogoutFilter.class)
                .authorizeHttpRequests(
                        (authorize) -> authorize.requestMatchers("/signup", "/", "/login").permitAll()
                                .anyRequest().authenticated()
                )
                .logout((logout) -> logout
                        .logoutSuccessUrl("/login")
                        .invalidateHttpSession(true))
                .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );
        return http.build();
    }

    /**
     * 인증 관리자 관련 설
     */
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() throws Exception {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();

        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());

        return daoAuthenticationProvider();
    }

    /**
     * PASSWORD_ENCODER
     * StandardPasswordEncoder : SHA-256을 이용해 암호를 해시한다. (강도가 약한 해싱 알고리즘이기 때문에 지금은 많이 사용되지 않는다.)
     * Pbkdf2PasswordEncoder : PBKDF2를 이용한다.
     * BCryptPasswordEncoder : bcrypt 강력 해싱 함수로 암호를 인코딩한다
     * NoOpPasswordEncoder : 암호를 인코딩하지 않고 일반 텍스트로 유지(테스트 용도로만 사용한다.)
     * SCryptPasswordEncoder : scrypt 해싱 함수로 암호를 인코딩한다.
     * 현재 사용되는 알고리즘에서 취약성이 발견되어 다른 인코딩 알고리즘으로 변경하고자 할 때 대응하기 좋은 방법은 DelegatingPasswordEncoder을 사용하는 것입니다.
     * by Peak
     */
    @Bean
    public static PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    /**
     * AuthenticationManager 등록
     * @return
     * @throws Exception
     */
    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        DaoAuthenticationProvider provider = daoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(provider);
    }

    @Bean
    public LoginSuccessJWTProvideHandler loginSuccessJWTProvideHandler(){
        return new LoginSuccessJWTProvideHandler();
    }

    @Bean
    public LoginFailureHandler loginFailureHandler(){
        return new LoginFailureHandler();
    }
    @Bean
    public JsonUsernamePasswordAuthenticationFilter jsonUsernamePasswordLoginFilter() throws Exception {
        JsonUsernamePasswordAuthenticationFilter jsonUsernamePasswordLoginFilter =
                new JsonUsernamePasswordAuthenticationFilter(objectMapper);
        jsonUsernamePasswordLoginFilter.setAuthenticationManager(authenticationManager());
        jsonUsernamePasswordLoginFilter.setAuthenticationSuccessHandler(loginSuccessJWTProvideHandler());
        jsonUsernamePasswordLoginFilter.setAuthenticationFailureHandler(loginFailureHandler());
        return jsonUsernamePasswordLoginFilter;

    }

}