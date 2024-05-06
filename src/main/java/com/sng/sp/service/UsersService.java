package com.sng.sp.service;

import com.sng.sp.domain.entity.Users;
import com.sng.sp.dto.MemberSignInRequestDto;
import com.sng.sp.repository.UsersRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsersService {

    private final EntityManager em;
    public void signIn(MemberSignInRequestDto memberSignInRequestDto) {
        Users users = new Users();
        users.setEmail("aiden.song@aa.com");
        users.setPassword("1122");
        users.setGender("M");
        users.setUsername("aiden");

        em.persist(users);
    }




}
