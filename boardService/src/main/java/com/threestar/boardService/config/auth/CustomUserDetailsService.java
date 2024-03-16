package com.threestar.boardService.config.auth;

import com.threestar.boardService.entity.User;
;
import com.threestar.boardService.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Object> userOptional = userRepository.findByName(username);

        Object user = userOptional.orElseThrow(() -> new UsernameNotFoundException("해당하는 아이디의 회원 없음"));

        return new CustomUserDetails((User) user);
    }

}

