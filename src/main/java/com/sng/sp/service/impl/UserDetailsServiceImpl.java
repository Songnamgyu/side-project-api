package com.sng.sp.service.impl;

import com.sng.sp.domain.UserDetailsImpl;
import com.sng.sp.domain.entity.Users;
import com.sng.sp.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class UserDetailsServiceImpl implements UserDetailsService {

    private final UsersRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Users users = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException(email));
        return new UserDetailsImpl(users);
    }
}
