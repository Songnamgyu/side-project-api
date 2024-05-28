package com.sng.sp.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sng.sp.domain.enums.Role;
import com.sng.sp.domain.enums.ShareStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name="users")
@NoArgsConstructor
@AllArgsConstructor
public class Users {

    @Id
    @GeneratedValue
    @Column(name="user_id")
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;
    @Column(nullable = false, unique = true)
    private String email;

    @Column
    private String phoneNumber;
    private String gender;

    @Enumerated(EnumType.STRING)
    @JsonIgnore
    private Role role;

    @Enumerated(EnumType.STRING)
    @JsonIgnore
    private ShareStatus shareStatus;

    private LocalDateTime createdAt; // 생성시간

    //jwt 토큰 필드 추가
    @Column(length = 1000)
    private String refreshToken;

    /**
     * RefreshToken 업데이트 메소드
     */
    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    /**
     * RefreshToken 삭제 메소드
     */
    public void deleteRefreshToken() {
        this.refreshToken = null;
    }

    //== 패스워드 암호화 ==//
    public void encodePassword(PasswordEncoder passwordEncoder){
        this.password = passwordEncoder.encode(password);
    }

}
