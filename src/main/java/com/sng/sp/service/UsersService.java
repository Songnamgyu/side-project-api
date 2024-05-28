package com.sng.sp.service;

import com.sng.sp.domain.entity.Users;
import com.sng.sp.domain.enums.Role;
import com.sng.sp.dto.MemberSignInRequestDto;
import com.sng.sp.repository.UsersRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.sng.sp.config.SecurityConfig.passwordEncoder;

@Service
@RequiredArgsConstructor
@Transactional
public class UsersService  {

    private final EntityManager em;
    private final UsersRepository usersRepository;
    public void signIn(MemberSignInRequestDto memberSignInRequestDto) {
        Users users = new Users();
        users.setEmail(memberSignInRequestDto.getEmail());

//        users.setPassword(memberSignInRequestDto.getPassword());
        users.setPassword(passwordEncoder().encode(memberSignInRequestDto.getPassword()));
        users.setGender("M");
        users.setRole(Role.ADMIN);
        users.setUsername(memberSignInRequestDto.getUsername());

        usersRepository.save(users);

    }


    public Optional<Users> login(MemberSignInRequestDto param) {
        Optional<Users> findUsers = usersRepository.findByEmail(param.getEmail());
        if(findUsers.isEmpty()) {
            return null;
        }
        boolean matches = passwordEncoder().matches(param.getPassword(), findUsers.get().getPassword());
        if(!matches) {
            throw new IllegalArgumentException("비밀번호가 일치하지않습니다");
        }
        return findUsers;
    }


}
