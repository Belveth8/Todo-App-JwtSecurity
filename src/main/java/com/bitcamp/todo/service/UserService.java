package com.bitcamp.todo.service;

import com.bitcamp.todo.model.UserEntity;
import com.bitcamp.todo.persistence.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // 아이디 중복검사/유효성
    public UserEntity create(final UserEntity userEntity) {
        // 1. userEntity 유효성 체크
        if (userEntity == null || userEntity.getUsername() == null) {
            throw new RuntimeException("Invalid arguments");
        }
        final String username = userEntity.getUsername();

        // 2. 중복 검사
        if (userRepository.existsByUsername(username)) {
            log.warn("Username already exist {}", username);
            throw new RuntimeException("Username Already exist");
        }
        return userRepository.save(userEntity);
        // userEntity란 ui에서 넘어 온 username
    }

    // 로그인 아이디 & 패스워드 일치하는지 확인
    public UserEntity getByCredentials(final String username, final String password, final PasswordEncoder encoder) {
        // DB에 저장된 original 유저
        final UserEntity originalUser = userRepository.findByUsername(username);

        // matches (파싱) 메서드를 이용해 패스워드가 같은지 확인
        if(originalUser != null && encoder.matches(password, originalUser.getPassword())) {
            return originalUser;
        }

        return null;
    }

}
