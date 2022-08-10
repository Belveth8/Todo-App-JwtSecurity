package com.bitcamp.todo.persistence;

import com.bitcamp.todo.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {

    UserEntity findByUsername(String username);

    // 이 유저가 table에 존재 하는가
    Boolean existsByUsername(String username);
    // 사용자 이름& 패스워드가 같은 지
    UserEntity findByUsernameAndPassword (String username, String password);
}
// userentity 테이블과 연결되어있다고 생각하면 됨